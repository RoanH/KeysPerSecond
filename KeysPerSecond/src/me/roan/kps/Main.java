package me.roan.kps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

import me.roan.kps.CommandKeys.CMD;
import me.roan.kps.layout.GridPanel;
import me.roan.kps.layout.Layout;
import me.roan.kps.layout.LayoutValidator;
import me.roan.kps.layout.Positionable;
import me.roan.kps.panels.AvgPanel;
import me.roan.kps.panels.BasePanel;
import me.roan.kps.panels.GraphPanel;
import me.roan.kps.panels.KeyPanel;
import me.roan.kps.panels.MaxPanel;
import me.roan.kps.panels.NowPanel;
import me.roan.kps.panels.TotPanel;
import me.roan.kps.ui.model.EndNumberModel;
import me.roan.kps.ui.model.MaxNumberModel;
import me.roan.kps.ui.model.DynamicInteger;
import me.roan.util.ClickableLink;
import me.roan.util.Dialog;
import me.roan.util.ExclamationMarkPath;
import me.roan.util.Util;

/**
 * This program can be used to display
 * information about how many times
 * certain keys are pressed and what the
 * average, maximum and current
 * amount of keys pressed per second is.
 * <p>
 * Besides the tracking of the assigned keys
 * this program responds to 6 key events these are:
 * <ol><li><b>Ctrl + P</b>: Causes the program to reset the average and maximum value
 * And to print the statistics to standard output
 * </li><li><b>Ctrl + O</b>: Terminates the program
 * </li><li><b>Ctrl + I</b>: Causes the program to reset the amount of times a key is pressed
 * And to print the statistics to standard output
 * </li><li><b>Ctrl + Y</b>: Hides/shows the GUI
 * </li><li><b>Ctrl + T</b>: Pauses/resumes the counter
 * </li><li><b>Ctrl + R</b>: Reloads the configuration</li></ol>
 * The program also constantly prints the current keys per second to
 * the standard output.<br>
 * A key is only counted as being pressed if the key has been released before
 * this deals with the issue of holding a key firing multiple key press events<br>
 * This program also has support for saving and loading configurations
 * @author Roan
 */
public class Main{
	/**
	 * The number of seconds the average has
	 * been calculated for
	 */
	protected static long n = 0;
	/**
	 * The number of keys pressed in the
	 * ongoing second
	 */
	protected static AtomicInteger tmp = new AtomicInteger(0);
	/**
	 * The average keys per second
	 */
	public static double avg;
	/**
	 * The maximum keys per second value reached so far
	 */
	public static int max;
	/**
	 * The keys per second of the previous second
	 * used for displaying the current keys per second value
	 */
	public static int prev;
	/**
	 * HashMap containing all the tracked keys and their
	 * virtual codes<br>Used to increment the count for the
	 * keys
	 */
	protected static Map<Integer, Key> keys = new HashMap<Integer, Key>();
	/**
	 * The most recent key event, only
	 * used during the initial setup
	 */
	protected static NativeKeyEvent lastevent;
	/**
	 * Main panel used for showing all the sub panels that
	 * display all the information
	 */
	private static final GridPanel content = new GridPanel();
	/**
	 * Graph panel
	 */
	protected static GraphPanel graph = new GraphPanel();
	/**
	 * Linked list containing all the past key counts per time frame
	 */
	private static LinkedList<Integer> timepoints = new LinkedList<Integer>();
	/**
	 * The program's main frame
	 */
	protected static final JFrame frame = new JFrame("KeysPerSecond");
	/**
	 * Whether or not the counter is paused
	 */
	protected static boolean suspended = false;
	/**
	 * The configuration
	 */
	public static Configuration config = new Configuration(null);
	/**
	 * The loop timer
	 */
	protected static ScheduledExecutorService timer = null;
	/**
	 * The loop timer task
	 */
	protected static ScheduledFuture<?> future = null;
	/**
	 * Frame for the graph
	 */
	protected static JFrame graphFrame = new JFrame("KeysPerSecond");
	/**
	 * The layout for the main panel of the program
	 */
	protected static final Layout layout = new Layout(content);
	/**
	 * Small icon for the program
	 */
	private static final Image iconSmall;
	/**
	 * Icon for the program
	 */
	private static final Image icon;
	/**
	 * Called when a frame is closed
	 */
	private static final WindowListener onClose;
	/**
	 * Dummy key for getOrDefault operations
	 */
	private static final Key DUMMY_KEY;
	
	/**
	 * Main method
	 * @param args - configuration file path
	 */
	public static void main(String[] args){
		//Work around for a JDK bug
		boolean relaunch = args.length != 0 && args[args.length - 1].equalsIgnoreCase("-relaunch");
		if(relaunch){
			String[] tmp = new String[args.length - 1];
			System.arraycopy(args, 0, tmp, 0, tmp.length);
			args = tmp;
		}
		ExclamationMarkPath.check(relaunch, args);
		
		//Basic setup and info
		String config = null;
		if(args.length >= 1){
			config = args[0];
			for(int i = 1; i < args.length; i++){
				config += " " + args[i];
			}
			System.out.println("Attempting to load config: " + config);
		}
		System.out.println("Control keys:");
		System.out.println("Ctrl + P: Causes the program to reset and print the average and maximum value");
		System.out.println("Ctrl + U: Terminates the program");
		System.out.println("Ctrl + I: Causes the program to reset and print the key press statistics");
		System.out.println("Ctrl + Y: Hides/shows the GUI");
		System.out.println("Ctrl + T: Pauses/resumes the counter");
		System.out.println("Ctrl + R: Reloads the configuration");
		Util.installUI();
		
		//Set dialog defaults
		Dialog.setDialogIcon(iconSmall);
		Dialog.setParentFrame(frame);
		Dialog.setDialogTitle("Keys per second");

		//Make sure the native hook is always unregistered
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){
				try{
					GlobalScreen.unregisterNativeHook();
				}catch(NativeHookException e1){
					e1.printStackTrace();
				}
			}
		});

		//Initialise native library and register event handlers
		setupNativeHook();

		//Set configuration for the keys
		if(config != null){
			Configuration toLoad = new Configuration(new File(config));
			int index = config.lastIndexOf(File.separatorChar);
			File dir = new File(config.substring(0, index));
			final String name = config.substring(index + 1);
			File[] files = null;
			if(dir.exists()){
				files = dir.listFiles((FilenameFilter)(f, n)->{
					for(int i = 0; i < name.length(); i++){
						char ch = name.charAt(i);
						if(ch == '?'){
							continue;
						}
						if(i >= n.length() || ch != n.charAt(i)){
							return false;
						}
					}
					return true;
				});
			}
			if(files != null && files.length > 0){
				toLoad.loadConfig(files[0]);
				Main.config = toLoad;
				System.out.println("Loaded config file: " + files[0].getName());
			}
		}else{
			try{
				configure();
			}catch(NullPointerException e){
				e.printStackTrace();
				try{
					Dialog.showErrorDialog("Failed to load the configuration menu, however you can use the live menu instead");
				}catch(Throwable t){
					t.printStackTrace();
				}
				System.err.println("Failed to load the configuration menu, however you can use the live menu instead");
			}
		}

		//Build GUI
		try{
			buildGUI();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		//Start stats saving
		if(Main.config.autoSaveStats){
			Statistics.saveStatsTask();
		}

		//Enter the main loop
		mainLoop();
	}

	/**
	 * Main loop of the program
	 * this loop updates the
	 * average, current and 
	 * maximum keys per second
	 */
	protected static final void mainLoop(){
		if(timer == null){
			timer = Executors.newSingleThreadScheduledExecutor();
		}else{
			future.cancel(false);
		}
		future = timer.scheduleAtFixedRate(()->{
			if(!suspended){
				int currentTmp = tmp.getAndSet(0);
				int totaltmp = currentTmp;
				for(int i : timepoints){
					totaltmp += i;
				}
				if(totaltmp > max){
					max = totaltmp;
				}
				if(totaltmp != 0){
					avg = (avg * (double)n + (double)totaltmp) / ((double)n + 1.0D);
					n++;
					TotPanel.hits += currentTmp;
					System.out.println("Current keys per second: " + totaltmp + " time frame: " + tmp);
				}
				graph.addPoint(totaltmp);
				graph.repaint();
				content.repaint();
				prev = totaltmp;
				timepoints.addFirst(currentTmp);
				if(timepoints.size() >= 1000 / config.updateRate){
					timepoints.removeLast();
				}
			}
		}, 0, config.updateRate, TimeUnit.MILLISECONDS);
	}

	/**
	 * Registers the native libraries and
	 * registers event handlers for key
	 * press events
	 */
	private static final void setupNativeHook(){
		try{
			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			logger.setLevel(Level.WARNING);
			logger.setUseParentHandlers(false);
			GlobalScreen.registerNativeHook();
		}catch(NativeHookException ex){
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			Dialog.showErrorDialog("There was a problem registering the native hook: " + ex.getMessage());
			try{
				GlobalScreen.unregisterNativeHook();
			}catch(NativeHookException e1){
				e1.printStackTrace();
			}
			System.exit(1);
		}
		GlobalScreen.addNativeKeyListener(new NativeKeyListener(){

			@Override
			public void nativeKeyPressed(NativeKeyEvent event){
				pressEvent(event);
			}

			@Override
			public void nativeKeyReleased(NativeKeyEvent event){
				releaseEvent(event);
			}

			@Override
			public void nativeKeyTyped(NativeKeyEvent event){
			}
		});
		GlobalScreen.addNativeMouseListener(new NativeMouseListener(){

			@Override
			public void nativeMouseClicked(NativeMouseEvent event){
			}

			@Override
			public void nativeMousePressed(NativeMouseEvent event){
				pressEvent(event);
			}

			@Override
			public void nativeMouseReleased(NativeMouseEvent event){
				releaseEvent(event);
			}
		});
	}

	/**
	 * Called when a key is released
	 * @param event The event that occurred
	 */
	private static final void releaseEvent(NativeInputEvent event){
		int code = getExtendedKeyCode(event);
		if(code == CommandKeys.ALT){
			CommandKeys.isAltDown = false;
		}else if(code == CommandKeys.CTRL){
			CommandKeys.isCtrlDown = false;
		}else if(CommandKeys.isShift(code)){
			CommandKeys.isShiftDown = false;
		}
		if(config.enableModifiers){
			if(code == CommandKeys.ALT){
				for(Key k : keys.values()){
					if(k.alt){
						k.keyReleased();
					}
				}
			}else if(code == CommandKeys.CTRL){
				for(Key k : keys.values()){
					if(k.ctrl){
						k.keyReleased();
					}
				}
			}else if(CommandKeys.isShift(code)){
				for(Key k : keys.values()){
					if(k.shift){
						k.keyReleased();
					}
				}
			}
			for(Entry<Integer, Key> k : keys.entrySet()){
				if(getBaseKeyCode(code) == getBaseKeyCode(k.getKey())){
					k.getValue().keyReleased();
				}
			}
		}else{
			keys.getOrDefault(code, DUMMY_KEY).keyReleased();
		}
	}

	/**
	 * Called when a key is pressed
	 * @param nevent The event that occurred
	 */
	private static final void pressEvent(NativeInputEvent nevent){
		Integer code = getExtendedKeyCode(nevent);
		if(!keys.containsKey(code)){
			if(config.trackAllKeys && nevent instanceof NativeKeyEvent){
				keys.put(code, new Key(KeyInformation.getKeyName(NativeKeyEvent.getKeyText(((NativeKeyEvent)nevent).getKeyCode()), code)));
			}else if(config.trackAllButtons && nevent instanceof NativeMouseEvent){
				keys.put(code, new Key("M" + ((NativeMouseEvent)nevent).getButton()));
			}
		}
		if(!suspended && keys.containsKey(code)){
			Key key = keys.get(code);
			key.keyPressed();
			if(config.enableModifiers){
				if(key.alt){
					keys.getOrDefault(CommandKeys.ALT, DUMMY_KEY).keyReleased();
				}
				if(key.ctrl){
					keys.getOrDefault(CommandKeys.CTRL, DUMMY_KEY).keyReleased();
				}
				if(key.shift){
					keys.getOrDefault(CommandKeys.RSHIFT, DUMMY_KEY).keyReleased();
					keys.getOrDefault(CommandKeys.LSHIFT, DUMMY_KEY).keyReleased();
				}
			}
		}
		if(nevent instanceof NativeKeyEvent){
			NativeKeyEvent event = (NativeKeyEvent)nevent;
			if(!CommandKeys.isAltDown){
				CommandKeys.isAltDown = code == CommandKeys.ALT;
			}
			if(!CommandKeys.isCtrlDown){
				CommandKeys.isCtrlDown = code == CommandKeys.CTRL;
			}
			if(!CommandKeys.isShiftDown){
				CommandKeys.isShiftDown = CommandKeys.isShift(code);
			}
			lastevent = event;
			if(config.CP.matches(event.getKeyCode())){
				resetStats();
			}else if(config.CU.matches(event.getKeyCode())){
				exit();
			}else if(config.CI.matches(event.getKeyCode())){
				resetTotals();
			}else if(config.CY.matches(event.getKeyCode())){
				if(frame.getContentPane().getComponentCount() != 0){
					frame.setVisible(!frame.isVisible());
				}
			}else if(config.CT.matches(event.getKeyCode())){
				suspended = !suspended;
				Menu.pause.setSelected(suspended);
			}else if(config.CR.matches(event.getKeyCode())){
				config.reloadConfig();
				Menu.resetData();
			}
		}
	}

	/**
	 * Gets the extended key code for this event, this key code
	 * includes modifiers
	 * @param event The event that occurred
	 * @return The extended key code for this event
	 */
	private static final int getExtendedKeyCode(NativeInputEvent event){
		if(event instanceof NativeKeyEvent){
			NativeKeyEvent key = (NativeKeyEvent)event;
			if(!config.enableModifiers){
				return CommandKeys.getExtendedKeyCode(key.getKeyCode(), false, false, false);
			}else{
				return CommandKeys.getExtendedKeyCode(key.getKeyCode());
			}
		}else{
			return -((NativeMouseEvent)event).getButton();
		}
	}

	/**
	 * Gets the base key code for the extended key code,
	 * this is the key code without modifiers
	 * @param code The extended key code
	 * @return The base key code
	 */
	private static final int getBaseKeyCode(int code){
		return code & CommandKeys.KEYCODE_MASK;
	}

	/**
	 * Asks the user for a configuration
	 * though a series of dialogs
	 * These dialogs also provide the
	 * option of saving or loading an
	 * existing configuration
	 */
	private static final void configure(){
		JPanel form = new JPanel(new BorderLayout());
		JPanel boxes = new JPanel(new GridLayout(11, 0));
		JPanel labels = new JPanel(new GridLayout(11, 0));
		JCheckBox cmax = new JCheckBox();
		JCheckBox cavg = new JCheckBox();
		JCheckBox ccur = new JCheckBox();
		JCheckBox ckey = new JCheckBox();
		JCheckBox cgra = new JCheckBox();
		JCheckBox ctop = new JCheckBox();
		JCheckBox ccol = new JCheckBox();
		JCheckBox callKeys = new JCheckBox();
		JCheckBox callButtons = new JCheckBox();
		JCheckBox ctot = new JCheckBox();
		JCheckBox cmod = new JCheckBox();
		cmax.setSelected(true);
		cavg.setSelected(true);
		ccur.setSelected(true);
		ckey.setSelected(true);
		JLabel lmax = new JLabel("Show maximum: ");
		JLabel lavg = new JLabel("Show average: ");
		JLabel lcur = new JLabel("Show current: ");
		JLabel lkey = new JLabel("Show keys");
		JLabel lgra = new JLabel("Show graph: ");
		JLabel ltop = new JLabel("Overlay mode: ");
		JLabel lcol = new JLabel("Custom colours: ");
		JLabel lallKeys = new JLabel("Track all keys");
		JLabel lallButtons = new JLabel("Track all buttons");
		JLabel ltot = new JLabel("Show total");
		JLabel lmod = new JLabel("Key-modifier tracking");
		ltop.setToolTipText("Requires you to run osu! out of full screen mode, known to not (always) work with the wine version of osu!");
		boxes.add(cmax);
		boxes.add(cavg);
		boxes.add(ccur);
		boxes.add(ctot);
		boxes.add(ckey);
		boxes.add(cgra);
		boxes.add(ctop);
		boxes.add(ccol);
		boxes.add(callKeys);
		boxes.add(callButtons);
		boxes.add(cmod);
		labels.add(lmax);
		labels.add(lavg);
		labels.add(lcur);
		labels.add(ltot);
		labels.add(lkey);
		labels.add(lgra);
		labels.add(ltop);
		labels.add(lcol);
		labels.add(lallKeys);
		labels.add(lallButtons);
		labels.add(lmod);
		JButton save = new JButton("Save config");
		ctop.addActionListener((e)->{
			config.overlay = ctop.isSelected();
			save.setEnabled(true);
		});
		callKeys.addActionListener((e)->{
			config.trackAllKeys = callKeys.isSelected();
			save.setEnabled(true);
		});
		callButtons.addActionListener((e)->{
			config.trackAllButtons = callButtons.isSelected();
			save.setEnabled(true);
		});
		cmax.addActionListener((e)->{
			config.showMax = cmax.isSelected();
			save.setEnabled(true);
		});
		cavg.addActionListener((e)->{
			config.showAvg = cavg.isSelected();
			save.setEnabled(true);
		});
		ccur.addActionListener((e)->{
			config.showCur = ccur.isSelected();
			save.setEnabled(true);
		});
		cgra.addActionListener((e)->{
			config.showGraph = cgra.isSelected();
			save.setEnabled(true);
		});
		ccol.addActionListener((e)->{
			config.customColors = ccol.isSelected();
			save.setEnabled(true);
		});
		ckey.addActionListener((e)->{
			config.showKeys = ckey.isSelected();
			save.setEnabled(true);
		});
		ctot.addActionListener((e)->{
			config.showTotal = ctot.isSelected();
			save.setEnabled(true);
		});
		cmod.addActionListener((e)->{
			config.enableModifiers = cmod.isSelected();
			save.setEnabled(true);
		});
		JPanel options = new JPanel();
		labels.setPreferredSize(new Dimension((int)labels.getPreferredSize().getWidth(), (int)boxes.getPreferredSize().getHeight()));
		options.add(labels);
		options.add(boxes);
		JPanel buttons = new JPanel(new GridLayout(10, 1));
		JButton addkey = new JButton("Add key");
		JButton load = new JButton("Load config");
		JButton updaterate = new JButton("Update rate");
		JButton cmdkeys = new JButton("Commands");
		save.setEnabled(false);
		JButton graph = new JButton("Graph");
		graph.setEnabled(false);
		cgra.addActionListener((e)->{
			graph.setEnabled(cgra.isSelected());
			graph.repaint();
		});
		JButton color = new JButton("Colours");
		color.setEnabled(false);
		ccol.addActionListener((e)->{
			color.setEnabled(ccol.isSelected());
			color.repaint();
		});
		JButton precision = new JButton("Precision");
		JButton layout = new JButton("Layout");
		JButton autoSave = new JButton("Stats saving");
		buttons.add(addkey);
		buttons.add(load);
		buttons.add(save);
		buttons.add(graph);
		buttons.add(updaterate);
		buttons.add(color);
		buttons.add(precision);
		buttons.add(autoSave);
		buttons.add(cmdkeys);
		buttons.add(layout);
		form.add(options, BorderLayout.CENTER);
		options.setBorder(BorderFactory.createTitledBorder("General"));
		buttons.setBorder(BorderFactory.createTitledBorder("Configuration"));
		JPanel all = new JPanel(new BorderLayout());
		all.add(options, BorderLayout.LINE_START);
		all.add(buttons, BorderLayout.LINE_END);
		form.add(all, BorderLayout.CENTER);
		layout.addActionListener((e)->{
			configureLayout(false);
			save.setEnabled(true);
		});
		cmdkeys.addActionListener((e)->{
			configureCommandKeys();
			save.setEnabled(true);
		});
		precision.addActionListener((e)->{
			JPanel pconfig = new JPanel(new BorderLayout());
			JLabel info1 = new JLabel("Specify how many digits should be displayed");
			JLabel info2 = new JLabel("beyond the decimal point for the average.");
			JPanel plabels = new JPanel(new GridLayout(2, 1, 0, 0));
			plabels.add(info1);
			plabels.add(info2);
			JComboBox<String> values = new JComboBox<String>(new String[]{"No digits beyond the decimal point", "1 digit beyond the decimal point", "2 digits beyond the decimal point", "3 digits beyond the decimal point"});
			values.setSelectedIndex(config.precision);
			JLabel vlabel = new JLabel("Precision: ");
			JPanel pvalue = new JPanel(new BorderLayout());
			pvalue.add(vlabel, BorderLayout.LINE_START);
			pvalue.add(values, BorderLayout.CENTER);
			pconfig.add(plabels, BorderLayout.CENTER);
			pconfig.add(pvalue, BorderLayout.PAGE_END);
			if(Dialog.showSaveDialog(pconfig)){
				config.precision = values.getSelectedIndex();
				save.setEnabled(true);
			}
		});
		graph.addActionListener((e)->{
			JPanel pconfig = new JPanel();
			JSpinner backlog = new JSpinner(new SpinnerNumberModel(Main.config.backlog, 1, Integer.MAX_VALUE, 1));
			JCheckBox showavg = new JCheckBox();
			showavg.setSelected(Main.config.graphAvg);
			JLabel lbacklog;
			if(config.updateRate != 1000){
				lbacklog = new JLabel("Backlog (seconds / " + (1000 / config.updateRate) + "): ");
			}else{
				lbacklog = new JLabel("Backlog (seconds): ");
			}
			JLabel lshowavg = new JLabel("Show average: ");
			JPanel glabels = new JPanel(new GridLayout(2, 1));
			JPanel gcomponents = new JPanel(new GridLayout(2, 1));
			glabels.add(lbacklog);
			glabels.add(lshowavg);
			gcomponents.add(backlog);
			gcomponents.add(showavg);
			glabels.setPreferredSize(new Dimension((int)glabels.getPreferredSize().getWidth(), (int)gcomponents.getPreferredSize().getHeight()));
			gcomponents.setPreferredSize(new Dimension(50, (int)gcomponents.getPreferredSize().getHeight()));
			pconfig.add(glabels);
			pconfig.add(gcomponents);
			if(Dialog.showSaveDialog(pconfig)){
				Main.config.graphAvg = showavg.isSelected();
				Main.config.backlog = (int)backlog.getValue();
				save.setEnabled(true);
			}
		});
		addkey.addActionListener((e)->{
			configureKeys();
			save.setEnabled(true);
		});
		color.addActionListener((e)->{
			configureColors();
			save.setEnabled(true);
		});
		save.addActionListener((e)->{
			config.saveConfig(false);
		});
		load.addActionListener((e)->{
			if(!Configuration.loadConfiguration()){
				return;
			}

			cmax.setSelected(config.showMax);
			ccur.setSelected(config.showCur);
			cavg.setSelected(config.showAvg);
			cgra.setSelected(config.showGraph);
			if(config.showGraph){
				graph.setEnabled(true);
			}
			ccol.setSelected(config.customColors);
			if(config.customColors){
				color.setEnabled(true);
			}
			callKeys.setSelected(config.trackAllKeys);
			callButtons.setSelected(config.trackAllButtons);
			ckey.setSelected(config.showKeys);
			ctop.setSelected(config.overlay);
			ctot.setSelected(config.showTotal);
			cmod.setSelected(config.enableModifiers);
			save.setEnabled(true);
		});
		updaterate.addActionListener((e)->{
			JPanel info = new JPanel(new GridLayout(2, 1, 0, 0));
			info.add(new JLabel("Here you can change the rate at which"));
			info.add(new JLabel("the graph, max, avg & cur are updated."));
			JPanel pconfig = new JPanel(new BorderLayout());
			JComboBox<String> update = new JComboBox<String>(new String[]{"1000ms", "500ms", "250ms", "200ms", "125ms", "100ms", "50ms", "25ms", "20ms", "10ms", "5ms", "1ms"});
			update.setSelectedItem(config.updateRate + "ms");
			update.setRenderer(new DefaultListCellRenderer(){
				/**
				 * Serial ID
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus){
					Component item = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					if(((String)value).length() < 5 || ((String)value).equals("100ms")){
						item.setForeground(Color.RED);
					}
					if(((String)value).length() < 4){
						item.setForeground(Color.MAGENTA);
					}
					return item;
				}
			});
			JLabel lupdate = new JLabel("Update rate: ");
			pconfig.add(info, BorderLayout.PAGE_START);
			pconfig.add(lupdate, BorderLayout.WEST);
			pconfig.add(update, BorderLayout.CENTER);
			if(Dialog.showSaveDialog(pconfig)){
				config.updateRate = Integer.parseInt(((String)update.getSelectedItem()).substring(0, ((String)update.getSelectedItem()).length() - 2));
				save.setEnabled(true);
			}
		});
		autoSave.addActionListener((e)->{
			Statistics.configureAutoSave(false);
			save.setEnabled(true);
		});
		JPanel info = new JPanel(new GridLayout(2, 1, 0, 2));
		info.add(Util.getVersionLabel("KeysPerSecond", "v8.2"));//XXX the version number  - don't forget build.gradle
		JPanel links = new JPanel(new GridLayout(1, 2, -2, 0));
		JLabel forum = new JLabel("<html><font color=blue><u>Forums</u></font> -</html>", SwingConstants.RIGHT);
		JLabel git = new JLabel("<html>- <font color=blue><u>GitHub</u></font></html>", SwingConstants.LEFT);
		links.add(forum);
		links.add(git);
		forum.addMouseListener(new ClickableLink("https://osu.ppy.sh/community/forums/topics/552405"));
		git.addMouseListener(new ClickableLink("https://github.com/RoanH/KeysPerSecond"));
		info.add(links);
		form.add(info, BorderLayout.PAGE_END);
		
		JButton ok = new JButton("OK");
		JButton exit = new JButton("Exit");
		exit.addActionListener(e->exit());
		
		CountDownLatch latch = new CountDownLatch(1);
		ok.addActionListener(e->latch.countDown());
		
		JPanel bottomButtons = new JPanel();
		bottomButtons.add(ok);
		bottomButtons.add(exit);
		
		JPanel dialog = new JPanel(new BorderLayout());
		dialog.add(form, BorderLayout.CENTER);
		dialog.add(bottomButtons, BorderLayout.PAGE_END);
		
		dialog.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JFrame conf = new JFrame("KeysPerSecond");
		conf.add(dialog);
		conf.pack();
		conf.setResizable(false);
		conf.setLocationRelativeTo(null);
		List<Image> icons = new ArrayList<Image>();
		icons.add(icon);
		icons.add(iconSmall);
		conf.setIconImages(icons);
		conf.addWindowListener(onClose);
		conf.setVisible(true);
		
		try{
			latch.await();
		}catch(InterruptedException e1){
		}
		conf.setVisible(false);
		conf.dispose();
		frame.setAlwaysOnTop(config.overlay);
		graphFrame.setAlwaysOnTop(config.overlay);
	}

	/**
	 * Shows the color configuration dialog
	 */
	protected static final void configureColors(){
		JPanel cfg = new JPanel();
		JPanel cbg = new JPanel();
		MouseListener clistener = new MouseListener(){
			/**
			 * Whether or not the color chooser is open
			 */
			private boolean open = false;
			/**
			 * Color chooser instance
			 */
			private final JColorChooser chooser = new JColorChooser();

			@Override
			public void mouseClicked(MouseEvent e){
				if(!open){
					open = true;
					chooser.setColor(e.getComponent().getBackground());
					if(Dialog.showSaveDialog(chooser)){
						e.getComponent().setBackground(chooser.getColor());
					}
					open = false;
				}
			}

			@Override
			public void mousePressed(MouseEvent e){
			}

			@Override
			public void mouseReleased(MouseEvent e){
			}

			@Override
			public void mouseEntered(MouseEvent e){
			}

			@Override
			public void mouseExited(MouseEvent e){
			}
		};
		cbg.addMouseListener(clistener);
		cfg.addMouseListener(clistener);
		cfg.setBackground(Main.config.foreground);
		cbg.setBackground(Main.config.background);
		Color prevfg = cfg.getForeground();
		Color prevbg = cbg.getForeground();
		JPanel cform = new JPanel(new GridLayout(2, 3, 4, 2));
		JLabel lfg = new JLabel("Foreground colour: ");
		JLabel lbg = new JLabel("Background colour: ");
		JSpinner sbg = new JSpinner(new SpinnerNumberModel((int)(config.opacitybg * 100), 0, 100, 5));
		JSpinner sfg = new JSpinner(new SpinnerNumberModel((int)(config.opacityfg * 100), 0, 100, 5));
		sbg.setPreferredSize(new Dimension(sbg.getPreferredSize().width + 15, sbg.getPreferredSize().height));
		sfg.setPreferredSize(new Dimension(sfg.getPreferredSize().width + 15, sbg.getPreferredSize().height));
		JPanel spanelfg = new JPanel(new BorderLayout());
		JPanel spanelbg = new JPanel(new BorderLayout());
		spanelfg.add(new JLabel("Opacity (%): "), BorderLayout.LINE_START);
		spanelbg.add(new JLabel("Opacity (%): "), BorderLayout.LINE_START);
		spanelfg.add(sfg, BorderLayout.CENTER);
		spanelbg.add(sbg, BorderLayout.CENTER);
		cform.add(lfg);
		cform.add(cfg);
		cform.add(spanelfg);
		cform.add(lbg);
		cform.add(cbg);
		cform.add(spanelbg);
		if(Dialog.showSaveDialog(cform, false)){
			config.foreground = cfg.getBackground();
			config.background = cbg.getBackground();
			config.opacitybg = (float)((int)sbg.getValue() / 100.0D);
			config.opacityfg = (float)((int)sfg.getValue() / 100.0D);
		}else{
			cfg.setForeground(prevfg);
			cbg.setForeground(prevbg);
		}
		frame.repaint();
	}

	/**
	 * Show the command key configuration dialog
	 */
	protected static final void configureCommandKeys(){
		JPanel content = new JPanel(new GridLayout(6, 2, 10, 2));

		JLabel lcp = new JLabel("Reset stats:");
		JLabel lcu = new JLabel("Exit the program:");
		JLabel lci = new JLabel("Reset totals:");
		JLabel lcy = new JLabel("Show/hide GUI:");
		JLabel lct = new JLabel("Pause/Resume:");
		JLabel lcr = new JLabel("Reload config:");

		JButton bcp = new JButton(config.CP.toString());
		JButton bcu = new JButton(config.CU.toString());
		JButton bci = new JButton(config.CI.toString());
		JButton bcy = new JButton(config.CY.toString());
		JButton bct = new JButton(config.CT.toString());
		JButton bcr = new JButton(config.CR.toString());

		content.add(lcp);
		content.add(bcp);

		content.add(lcu);
		content.add(bcu);

		content.add(lci);
		content.add(bci);

		content.add(lcy);
		content.add(bcy);

		content.add(lct);
		content.add(bct);

		content.add(lcr);
		content.add(bcr);

		bcp.addActionListener((e)->{
			CMD cmd = CommandKeys.askForNewKey();
			if(cmd != null){
				config.CP = cmd;
				bcp.setText(cmd.toString());
			}
		});
		bci.addActionListener((e)->{
			CMD cmd = CommandKeys.askForNewKey();
			if(cmd != null){
				config.CI = cmd;
				bci.setText(cmd.toString());
			}
		});
		bcu.addActionListener((e)->{
			CMD cmd = CommandKeys.askForNewKey();
			if(cmd != null){
				config.CU = cmd;
				bcu.setText(cmd.toString());
			}
		});
		bcy.addActionListener((e)->{
			CMD cmd = CommandKeys.askForNewKey();
			if(cmd != null){
				config.CY = cmd;
				bcy.setText(cmd.toString());
			}
		});
		bct.addActionListener((e)->{
			CMD cmd = CommandKeys.askForNewKey();
			if(cmd != null){
				config.CT = cmd;
				bct.setText(cmd.toString());
			}
		});
		bcr.addActionListener((e)->{
			CMD cmd = CommandKeys.askForNewKey();
			if(cmd != null){
				config.CR = cmd;
				bcr.setText(cmd.toString());
			}
		});

		Dialog.showMessageDialog(content);
	}

	/**
	 * Changes the update rate
	 * @param newRate The new update rate
	 */
	protected static final void changeUpdateRate(int newRate){
		n *= (double)config.updateRate / (double)newRate;
		tmp.set(0);
		timepoints.clear();
		config.updateRate = newRate;
		mainLoop();
	}

	/**
	 * Shows the layout configuration dialog
	 * @param live Whether or not changes should be
	 *        applied in real time
	 */
	protected static final void configureLayout(boolean live){
		content.showGrid();
		JPanel form = new JPanel(new BorderLayout());

		JPanel fields = new JPanel(new GridLayout(0, 5, 2, 2));
		JPanel modes = new JPanel(new GridLayout(0, 1, 0, 2));

		fields.add(new JLabel("Key", SwingConstants.CENTER));
		fields.add(new JLabel("X", SwingConstants.CENTER));
		fields.add(new JLabel("Y", SwingConstants.CENTER));
		fields.add(new JLabel("Width", SwingConstants.CENTER));
		fields.add(new JLabel("Height", SwingConstants.CENTER));
		modes.add(new JLabel("Mode", SwingConstants.CENTER));

		for(KeyInformation i : config.keyinfo){
			createListItem(i, fields, modes, live);
		}
		if(config.showAvg){
			createListItem(new Positionable(){

				@Override
				public void setX(int x){
					config.avg_x = x;
				}

				@Override
				public void setY(int y){
					config.avg_y = y;
				}

				@Override
				public void setWidth(int w){
					config.avg_w = w;
				}

				@Override
				public void setHeight(int h){
					config.avg_h = h;
				}

				@Override
				public String getName(){
					return "AVG";
				}

				@Override
				public int getX(){
					return config.avg_x;
				}

				@Override
				public int getY(){
					return config.avg_y;
				}

				@Override
				public int getWidth(){
					return config.avg_w;
				}

				@Override
				public int getHeight(){
					return config.avg_h;
				}

				@Override
				public RenderingMode getRenderingMode(){
					return config.avg_mode;
				}

				@Override
				public void setRenderingMode(RenderingMode mode){
					config.avg_mode = mode;
				}
			}, fields, modes, live);
		}
		if(config.showMax){
			createListItem(new Positionable(){

				@Override
				public void setX(int x){
					config.max_x = x;
				}

				@Override
				public void setY(int y){
					config.max_y = y;
				}

				@Override
				public void setWidth(int w){
					config.max_w = w;
				}

				@Override
				public void setHeight(int h){
					config.max_h = h;
				}

				@Override
				public String getName(){
					return "MAX";
				}

				@Override
				public int getX(){
					return config.max_x;
				}

				@Override
				public int getY(){
					return config.max_y;
				}

				@Override
				public int getWidth(){
					return config.max_w;
				}

				@Override
				public int getHeight(){
					return config.max_h;
				}

				@Override
				public RenderingMode getRenderingMode(){
					return config.max_mode;
				}

				@Override
				public void setRenderingMode(RenderingMode mode){
					config.max_mode = mode;
				}
			}, fields, modes, live);
		}
		if(config.showCur){
			createListItem(new Positionable(){

				@Override
				public void setX(int x){
					config.cur_x = x;
				}

				@Override
				public void setY(int y){
					config.cur_y = y;
				}

				@Override
				public void setWidth(int w){
					config.cur_w = w;
				}

				@Override
				public void setHeight(int h){
					config.cur_h = h;
				}

				@Override
				public String getName(){
					return "CUR";
				}

				@Override
				public int getX(){
					return config.cur_x;
				}

				@Override
				public int getY(){
					return config.cur_y;
				}

				@Override
				public int getWidth(){
					return config.cur_w;
				}

				@Override
				public int getHeight(){
					return config.cur_h;
				}

				@Override
				public RenderingMode getRenderingMode(){
					return config.cur_mode;
				}

				@Override
				public void setRenderingMode(RenderingMode mode){
					config.cur_mode = mode;
				}
			}, fields, modes, live);
		}
		if(config.showTotal){
			createListItem(new Positionable(){

				@Override
				public void setX(int x){
					config.tot_x = x;
				}

				@Override
				public void setY(int y){
					config.tot_y = y;
				}

				@Override
				public void setWidth(int w){
					config.tot_w = w;
				}

				@Override
				public void setHeight(int h){
					config.tot_h = h;
				}

				@Override
				public String getName(){
					return "TOT";
				}

				@Override
				public int getX(){
					return config.tot_x;
				}

				@Override
				public int getY(){
					return config.tot_y;
				}

				@Override
				public int getWidth(){
					return config.tot_w;
				}

				@Override
				public int getHeight(){
					return config.tot_h;
				}

				@Override
				public RenderingMode getRenderingMode(){
					return config.tot_mode;
				}

				@Override
				public void setRenderingMode(RenderingMode mode){
					config.tot_mode = mode;
				}
			}, fields, modes, live);
		}

		JPanel keys = new JPanel(new BorderLayout());
		keys.add(fields, BorderLayout.CENTER);
		keys.add(modes, BorderLayout.LINE_END);

		JPanel view = new JPanel(new BorderLayout());
		view.add(keys, BorderLayout.PAGE_START);
		view.add(new JPanel(), BorderLayout.CENTER);
		
		JPanel gridSize = new JPanel(new GridLayout(2, 2, 0, 5));
		gridSize.setBorder(BorderFactory.createTitledBorder("Size"));
		gridSize.add(new JLabel("Cell size: "));
		JSpinner gridSpinner = new JSpinner(new SpinnerNumberModel(config.cellSize, BasePanel.imageSize, Integer.MAX_VALUE, 1));
		gridSize.add(gridSpinner);
		gridSize.add(new JLabel("Panel border offset: "));
		JSpinner gapSpinner = new JSpinner(new SpinnerNumberModel(config.borderOffset, 0, new DynamicInteger(()->(config.cellSize - BasePanel.imageSize)), 1));
		gapSpinner.addChangeListener((e)->{
			config.borderOffset = (int)gapSpinner.getValue();
			if(live){
				reconfigure();
			}
		});
		gridSize.add(gapSpinner);
		gridSpinner.addChangeListener((e)->{
			config.cellSize = (int)gridSpinner.getValue();
			if(config.borderOffset > config.cellSize - BasePanel.imageSize){
				config.borderOffset = config.cellSize - BasePanel.imageSize;
				gapSpinner.setValue(config.borderOffset);
			}
			if(live){
				reconfigure();
			}
		});
		
		form.add(gridSize, BorderLayout.PAGE_START);
		
		JScrollPane pane = new JScrollPane(view);
		pane.setBorder(BorderFactory.createTitledBorder("Panels"));
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		pane.setPreferredSize(new Dimension(450, 200));

		form.add(pane, BorderLayout.CENTER);

		JPanel graphLayout = new JPanel(new GridLayout(5, 2, 0, 5));
		graphLayout.setBorder(BorderFactory.createTitledBorder("Graph"));
		graphLayout.add(new JLabel("Graph mode: "));
		JComboBox<Object> graphMode = new JComboBox<Object>(GraphMode.values());
		graphMode.setSelectedItem(Main.config.graphMode);
		graphLayout.add(graphMode);

		LayoutValidator validator = new LayoutValidator();

		graphLayout.add(new JLabel("Graph x position: "));
		JSpinner x = new JSpinner(new EndNumberModel(Main.config.graph_x, validator.getXField(), (val)->{
			Main.config.graph_x = val;
			if(live){
				reconfigure();
			}
		}));
		x.setEnabled(Main.config.graphMode == GraphMode.INLINE);
		graphLayout.add(x);

		graphLayout.add(new JLabel("Graph y position: "));
		JSpinner y = new JSpinner(new EndNumberModel(Main.config.graph_y, validator.getYField(), (val)->{
			Main.config.graph_y = val;
			if(live){
				reconfigure();
			}
		}));
		y.setEnabled(Main.config.graphMode == GraphMode.INLINE);
		graphLayout.add(y);

		graphLayout.add(new JLabel("Graph width: "));
		JSpinner w = new JSpinner(new MaxNumberModel(Main.config.graph_w, validator.getWidthField(), (val)->{
			Main.config.graph_w = val;
			if(live){
				reconfigure();
			}
		}));
		graphLayout.add(w);

		graphLayout.add(new JLabel("Graph height: "));
		JSpinner h = new JSpinner(new MaxNumberModel(Main.config.graph_h, validator.getHeightField(), (val)->{
			Main.config.graph_h = val;
			if(live){
				reconfigure();
			}
		}));
		graphLayout.add(h);

		graphMode.addActionListener((e)->{
			Main.config.graphMode = (GraphMode)graphMode.getSelectedItem();
			if(graphMode.getSelectedItem() == GraphMode.INLINE){
				x.setEnabled(true);
				y.setEnabled(true);
			}else{
				x.setEnabled(false);
				y.setEnabled(false);
			}
			if(live){
				reconfigure();
			}
		});

		form.add(graphLayout, BorderLayout.PAGE_END);

		Dialog.showMessageDialog(form, true);
		content.hideGrid();
	}

	/**
	 * Creates a editable list item for the
	 * layout configuration dialog
	 * @param info The positionable that links the 
	 *        editor to the underlying data
	 * @param fields The GUI panel that holds all the fields
	 * @param modes The GUI panel that holds all the modes
	 * @param live Whether or not edits should be displayed in real time
	 */
	private static final void createListItem(Positionable info, JPanel fields, JPanel modes, boolean live){
		fields.add(new JLabel(info.getName(), SwingConstants.CENTER));

		LayoutValidator validator = new LayoutValidator();

		JSpinner x = new JSpinner(new EndNumberModel(info.getX(), validator.getXField(), (val)->{
			info.setX(val);
			if(live){
				reconfigure();
			}
		}));
		fields.add(x);

		JSpinner y = new JSpinner(new EndNumberModel(info.getY(), validator.getYField(), (val)->{
			info.setY(val);
			if(live){
				reconfigure();
			}
		}));
		fields.add(y);

		JSpinner w = new JSpinner(new MaxNumberModel(info.getWidth(), validator.getWidthField(), (val)->{
			info.setWidth(val);
			if(live){
				reconfigure();
			}
		}));
		fields.add(w);

		JSpinner h = new JSpinner(new MaxNumberModel(info.getHeight(), validator.getHeightField(), (val)->{
			info.setHeight(val);
			if(live){
				reconfigure();
			}
		}));
		fields.add(h);

		JComboBox<RenderingMode> mode = new JComboBox<RenderingMode>(RenderingMode.values());
		mode.setSelectedItem(info.getRenderingMode());
		mode.addActionListener((e)->{
			info.setRenderingMode((RenderingMode)mode.getSelectedItem());
			if(live){
				reconfigure();
			}
		});
		modes.add(mode);
	}
	
	/**
	 * Shows the key configuration dialog
	 */
	protected static final void configureKeys(){
		List<KeyInformation> copy = new ArrayList<KeyInformation>(config.keyinfo);
		boolean[] visibleState = new boolean[copy.size()];
		String[] nameState = new String[copy.size()];
		for(int i = 0; i < copy.size(); i++){
			visibleState[i] = copy.get(i).visible;
			nameState[i] = copy.get(i).name;
		}
		
		JPanel keyform = new JPanel(new BorderLayout());
		keyform.add(new JLabel("Currently added keys (you can edit these fields):"), BorderLayout.PAGE_START);
		JTable keys = new JTable();
		DefaultTableModel model = new DefaultTableModel(){
			/**
			 * Serial ID
			 */
			private static final long serialVersionUID = -5510962859479828507L;

			@Override
			public int getRowCount(){
				return config.keyinfo.size();
			}

			@Override
			public int getColumnCount(){
				return 3;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex){
				switch(columnIndex){
				case 0:
					return config.keyinfo.get(rowIndex).name;
				case 1:
					return config.keyinfo.get(rowIndex).visible;
				case 2:
					return false;
				default:
					return null;
				}
			}

			@Override
			public String getColumnName(int col){
				switch(col){
				case 0:
					return "Key";
				case 1:
					return "Visible";
				case 2:
					return "Remove";
				default:
					return null;
				}
			}

			@Override
			public Class<?> getColumnClass(int columnIndex){
				if(columnIndex == 1 || columnIndex == 2){
					return Boolean.class;
				}
				return super.getColumnClass(columnIndex);
			}

			@Override
			public boolean isCellEditable(int row, int col){
				return true;
			}

			@Override
			public void setValueAt(Object value, int row, int col){
				switch(col){
				case 0:
					config.keyinfo.get(row).setName((String)value);
					break;
				case 1:
					config.keyinfo.get(row).visible = (boolean)value;
					break;
				case 2:
					if((boolean)value == true){
						Main.keys.remove(config.keyinfo.get(row).keycode);
						config.keyinfo.remove(row);
						keys.repaint();
					}
					break;
				}
			}
		};
		keys.setModel(model);
		keys.setDragEnabled(false);
		JScrollPane pane = new JScrollPane(keys);
		pane.setPreferredSize(new Dimension((int)keyform.getPreferredSize().getWidth() + 50, 120));
		keyform.add(pane, BorderLayout.CENTER);
		JButton newkey = new JButton("Add Key");
		newkey.addActionListener((evt)->{
			JPanel form = new JPanel(new GridLayout(config.enableModifiers ? 4 : 1, 1));
			JLabel txt = new JLabel("Press a key and click 'OK' to add it.");
			form.add(txt);
			JCheckBox ctrl = new JCheckBox();
			JCheckBox alt = new JCheckBox();
			JCheckBox shift = new JCheckBox();
			if(config.enableModifiers){
				JPanel a = new JPanel(new BorderLayout());
				JPanel c = new JPanel(new BorderLayout());
				JPanel s = new JPanel(new BorderLayout());
				c.add(ctrl, BorderLayout.LINE_START);
				c.add(new JLabel("Ctrl"), BorderLayout.CENTER);
				a.add(alt, BorderLayout.LINE_START);
				a.add(new JLabel("Alt"), BorderLayout.CENTER);
				s.add(shift, BorderLayout.LINE_START);
				s.add(new JLabel("Shift"), BorderLayout.CENTER);
				form.add(c);
				form.add(a);
				form.add(s);
			}
			if(Dialog.showSaveDialog(form)){
				if(lastevent == null){
					Dialog.showMessageDialog("No key pressed!");
					return;
				}
				KeyInformation info = new KeyInformation(NativeKeyEvent.getKeyText(lastevent.getKeyCode()), lastevent.getKeyCode(), (alt.isSelected() || CommandKeys.isAltDown) && config.enableModifiers, (ctrl.isSelected() || CommandKeys.isCtrlDown) && config.enableModifiers, (shift.isSelected() || CommandKeys.isShiftDown) && config.enableModifiers, false);
				int n = (CommandKeys.hasAlt(info.keycode) ? 1 : 0) + (CommandKeys.hasCtrl(info.keycode) ? 1 : 0) + (CommandKeys.hasShift(info.keycode) ? 1 : 0);
				if(Dialog.showConfirmDialog("Add the " + info.getModifierString() + info.name.substring(n) + " key?")){
					if(config.keyinfo.contains(info)){
						KeyInformation.autoIndex -= 2;
						Dialog.showMessageDialog("That key was already added before.\nIt was not added again.");
					}else{
						config.keyinfo.add(info);
					}
				}
				model.fireTableDataChanged();
			}
		});
		JButton newmouse = new JButton("Add Mouse Button");
		newmouse.addActionListener((e)->{
			JPanel addform = new JPanel(new BorderLayout());
			addform.add(new JLabel("Select the mouse buttons to add:"), BorderLayout.PAGE_START);

			JPanel buttons = new JPanel(new GridLayout(5, 1, 2, 0));
			String[] names = new String[]{"M1", "M2", "M3", "M4", "M5"};
			JCheckBox[] boxes = new JCheckBox[]{
				new JCheckBox(names[0] + " (left click)"), 
			    new JCheckBox(names[1] + " (right click)"), 
			    new JCheckBox(names[2] + " (mouse wheel)"), 
			    new JCheckBox(names[3]), 
			    new JCheckBox(names[4])
			};

			for(JCheckBox box : boxes){
				buttons.add(box);
			}

			addform.add(buttons, BorderLayout.CENTER);

			if(Dialog.showSaveDialog(addform)){
				for(int i = 0; i < boxes.length; i++){
					if(boxes[i].isSelected()){
						KeyInformation key = new KeyInformation(names[i], -(i + 1), false, false, false, true);
						if(config.keyinfo.contains(key)){
							KeyInformation.autoIndex -= 2;
							Dialog.showMessageDialog("The " + names[i] + " button was already added before.\nIt was not added again.");
						}else{
							config.keyinfo.add(key);
						}
					}
				}
				model.fireTableDataChanged();
			}
		});
		JPanel nbuttons = new JPanel(new GridLayout(1, 2, 2, 0));
		nbuttons.add(newkey, BorderLayout.LINE_START);
		nbuttons.add(newmouse, BorderLayout.LINE_END);
		keyform.add(nbuttons, BorderLayout.PAGE_END);
		
		if(!Dialog.showSaveDialog(keyform, true)){
			for(int i = 0; i < copy.size(); i++){
				copy.get(i).visible = visibleState[i];
				copy.get(i).setName(nameState[i]);
			}
			config.keyinfo = copy;
		}
	}

	/**
	 * Builds the main GUI of the program
	 * @throws IOException When an IO Exception occurs, this can be thrown
	 *         when the program fails to load its resources
	 */
	protected static final void buildGUI() throws IOException{
		Menu.createMenu();
		frame.setResizable(false);
		frame.setIconImage(icon);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		new Listener(frame);
		graphFrame.setResizable(false);
		graphFrame.setIconImage(icon);
		graphFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		graphFrame.setUndecorated(true);
		frame.addWindowListener(onClose);
		graphFrame.addWindowListener(onClose);
		new Listener(graphFrame);
		reconfigure();
	}

	/**
	 * Reconfigures the layout of the program
	 */
	protected static final void reconfigure(){
		SwingUtilities.invokeLater(()->{
			frame.getContentPane().removeAll();
			layout.removeAll();
			try{
				ColorManager.prepareImages(config.customColors);
			}catch(IOException e){
				e.printStackTrace();
			}
			Key k;
			int panels = 0;
			for(KeyInformation i : config.keyinfo){
				if(!keys.containsKey(i.keycode)){
					keys.put(i.keycode, k = new Key(i.name));
					k.alt = CommandKeys.hasAlt(i.keycode);
					k.ctrl = CommandKeys.hasCtrl(i.keycode);
					k.shift = CommandKeys.hasShift(i.keycode);
				}else{
					k = keys.get(i.keycode);
				}
				if(config.showKeys && i.visible){
					content.add(k.getPanel(i));
					k.getPanel(i).sizeChanged();
					panels++;
				}
			}
			if(config.showMax){
				content.add(MaxPanel.INSTANCE);
				MaxPanel.INSTANCE.sizeChanged();
				panels++;
			}
			if(config.showAvg){
				content.add(AvgPanel.INSTANCE);
				AvgPanel.INSTANCE.sizeChanged();
				panels++;
			}
			if(config.showCur){
				content.add(NowPanel.INSTANCE);
				NowPanel.INSTANCE.sizeChanged();
				panels++;
			}
			if(config.showTotal){
				content.add(TotPanel.INSTANCE);
				TotPanel.INSTANCE.sizeChanged();
				panels++;
			}
			if(panels == 0 && !config.showGraph){
				frame.setVisible(false);
				return;//don't create a GUI if there's nothing to display
			}

			Menu.repaint();

			JPanel all = new JPanel(new BorderLayout());
			all.add(content, BorderLayout.CENTER);
			all.setOpaque(config.getBackgroundOpacity() != 1.0F ? !ColorManager.transparency : true);
			if(config.showGraph){
				if(config.graphMode == GraphMode.INLINE){
					content.add(graph);
					graphFrame.setVisible(false);
				}else{
					graph.setOpaque(config.getBackgroundOpacity() != 1.0F ? !ColorManager.transparency : true);
					graphFrame.add(graph);
					graphFrame.setSize(config.graph_w * config.cellSize, config.graph_h * config.cellSize);
					graphFrame.setVisible(true);
				}
			}else{
				graphFrame.setVisible(false);
			}
			frame.setSize(layout.getWidth(), layout.getHeight());
			if(config.getBackgroundOpacity() != 1.0F){
				frame.setBackground(ColorManager.transparent);
				content.setOpaque(false);
				content.setBackground(ColorManager.transparent);
			}else{
				content.setOpaque(true);
				content.setBackground(config.background);
			}
			frame.add(all);
			if(panels > 0){
				frame.setVisible(true);
			}else{
				frame.setVisible(false);
			}
		});
	}

	/**
	 * Shuts down the program
	 */
	protected static final void exit(){
		try{
			GlobalScreen.unregisterNativeHook();
		}catch(NativeHookException e1){
			e1.printStackTrace();
		}
		System.exit(0);
	}

	/**
	 * Resets avg, max &amp; cur
	 */
	protected static final void resetStats(){
		System.out.println("Reset max & avg | max: " + max + " avg: " + avg);
		n = 0;
		avg = 0;
		max = 0;
		tmp.set(0);
		graph.reset();
		frame.repaint();
		graphFrame.repaint();
	}

	/**
	 * Resets key count totals
	 */
	protected static final void resetTotals(){
		System.out.print("Reset key counts | ");
		for(Key k : keys.values()){
			System.out.print(k.name + ":" + k.count + " ");
			k.count = 0;
		}
		System.out.println();
		frame.repaint();
	}
	
	//=================================================================================================
	//================== NESTED CLASSES ===============================================================
	//=================================================================================================

	/**
	 * This class is used to keep track
	 * of how many times a specific key
	 * is pressed
	 * @author Roan
	 */
	public static class Key implements Serializable{
		/**
		 * Serial ID
		 */
		private static final long serialVersionUID = 1263090697516120354L;
		/**
		 * Whether or not this key is currently pressed
		 */
		public transient boolean down = false;
		/**
		 * The total number of times this key has been pressed
		 */
		public int count = 0;
		/**
		 * The key in string form<br>
		 * For example: X
		 */
		public String name;
		/**
		 * The graphical display for this key
		 */
		private transient KeyPanel panel = null;
		/**
		 * Whether or not alt has to be down
		 */
		protected boolean alt;
		/**
		 * Whether or not ctrl has to be down
		 */
		protected boolean ctrl;
		/**
		 * Whether or not shift has to be down
		 */
		protected boolean shift;

		/**
		 * Constructs a new Key object
		 * for the key with the given
		 * name
		 * @param name The name of the key
		 * @see #name
		 */
		private Key(String name){
			this.name = name;
		}

		/**
		 * Creates a new KeyPanel with this
		 * objects as its data source
		 * @param i The information object for this key
		 * @return A new KeyPanel
		 */
		private KeyPanel getPanel(KeyInformation i){
			return panel != null ? panel : (panel = new KeyPanel(this, i));
		}

		/**
		 * Called when a key is pressed
		 */
		protected void keyPressed(){
			if(!down){
				count++;
				down = true;
				tmp.incrementAndGet();
				if(panel != null){
					panel.repaint();
				}
			}
		}

		/**
		 * Called when a key is released
		 */
		protected void keyReleased(){
			if(down){
				down = false;
				if(panel != null){
					panel.repaint();
				}
			}
		}
	}

	/**
	 * Simple class that holds all
	 * the essential information 
	 * about a key.
	 * @author Roan
	 */
	public static final class KeyInformation implements Serializable, Positionable{
		/**
		 * Serial ID
		 */
		private static final long serialVersionUID = -8669938978334898443L;
		/**
		 * The name of this key
		 * @see Key#name
		 */
		private String name;
		/**
		 * The virtual key code of this key<br>
		 * This code represents the key
		 */
		protected int keycode;
		/**
		 * Whether or not this key is displayed
		 */
		protected boolean visible = true;
		/**
		 * Auto-increment for #x
		 */
		protected static transient volatile int autoIndex = -2;
		/**
		 * The x position of this panel in the layout
		 */
		protected int x = autoIndex += 2;
		/**
		 * The y postion of this panel in the layout
		 */
		protected int y = 0;
		/**
		 * The width of this panel in the layout
		 */
		protected int width = 2;
		/**
		 * The height of this panel in the layout
		 */
		protected int height = 3;
		/**
		 * The text rendering mode for this panel
		 */
		protected RenderingMode mode = RenderingMode.VERTICAL;

		/**
		 * Constructs a new KeyInformation
		 * object with the given information
		 * @param name The name of the key
		 * @param code The virtual key code of the key
		 * @param alt Whether or not alt is down
		 * @param ctrl Whether or not ctrl is down
		 * @param shift Whether or not shift is down
		 * @param mouse Whether or not this is a mouse button
		 * @see #name
		 * @see #keycode 
		 */
		private KeyInformation(String name, int code, boolean alt, boolean ctrl, boolean shift, boolean mouse){
			this.keycode = mouse ? code : CommandKeys.getExtendedKeyCode(code, shift, ctrl, alt);
			this.name = mouse ? name : getKeyName(name, keycode);
		}

		/**
		 * Constructs the key name from the key
		 * and modifiers
		 * @param name The name of the key
		 * @param code The virtual key code of the key
		 * @return The full name of this given key
		 */
		private static final String getKeyName(String name, int code){
			return ((CommandKeys.hasAlt(code) ? "a" : "") + (CommandKeys.hasCtrl(code) ? "c" : "") + (CommandKeys.hasShift(code) ? "s" : "")) + (name.length() == 1 ? name.toUpperCase(Locale.ROOT) : getKeyText(code & CommandKeys.KEYCODE_MASK));
		}

		/**
		 * Constructs a new KeyInformation
		 * object with the given information
		 * @param name The name of the key
		 * @param code The virtual key code of the key
		 * @param visible Whether or not the key is visible
		 * @see #name
		 * @see #keycode
		 */
		protected KeyInformation(String name, int code, boolean visible){
			this.name = name;
			this.keycode = code;
			this.visible = visible;
		}
		
		/**
		 * Changes the display name of this
		 * key to the given string. If {@link Key}
		 * panels are active with the same key code
		 * as this {@link KeyInformation} object
		 * then their display name is also updated.
		 * @param name The new display name
		 */
		public void setName(String name){
			this.name = name;
			keys.getOrDefault(keycode, DUMMY_KEY).name = name;
		}

		/**
		 * Gets a string containing all
		 * the modifiers for this key
		 * @return The modifier string
		 */
		public String getModifierString(){
			return (CommandKeys.hasCtrl(keycode) ? "Ctrl + " : "") + (CommandKeys.hasAlt(keycode) ? "Alt + " : "") + (CommandKeys.hasShift(keycode) ? "Shift + " : "");
		}

		@Override
		public String toString(){
			return "[keycode=" + keycode + ",x=" + x + ",y=" + y + ",width=" + width + ",height=" + height + ",mode=" + mode.name() + ",visible=" + visible + ",name=\"" + name + "\"]";
		}

		@Override
		public int hashCode(){
			return keycode;
		}

		@Override
		public boolean equals(Object other){
			return other instanceof KeyInformation && keycode == ((KeyInformation)other).keycode;
		}

		/**
		 * Legacy object initialisation
		 * @see ObjectInputStream#defaultReadObject()
		 * @param stream The object input stream
		 * @throws IOException When an IOException occurs
		 * @throws ClassNotFoundException When this class cannot be found
		 */
		private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
			stream.defaultReadObject();
			x = -1;
			y = 0;
			width = 2;
			height = 3;
			mode = RenderingMode.VERTICAL;
			keycode = CommandKeys.getExtendedKeyCode(keycode, false, false, false);
		}

		/**
		 * Gets the key name for a key code
		 * @param keyCode The key code
		 * @return The key name
		 */
		public static String getKeyText(int keyCode){
			switch(keyCode){
			case NativeKeyEvent.VC_ESCAPE:
				return "Esc";
			// Begin Function Keys
			case NativeKeyEvent.VC_F1:
				return "F1";
			case NativeKeyEvent.VC_F2:
				return "F2";
			case NativeKeyEvent.VC_F3:
				return "F3";
			case NativeKeyEvent.VC_F4:
				return "F4";
			case NativeKeyEvent.VC_F5:
				return "F5";
			case NativeKeyEvent.VC_F6:
				return "F6";
			case NativeKeyEvent.VC_F7:
				return "F7";
			case NativeKeyEvent.VC_F8:
				return "F8";
			case NativeKeyEvent.VC_F9:
				return "F9";
			case NativeKeyEvent.VC_F10:
				return "F10";
			case NativeKeyEvent.VC_F11:
				return "F11";
			case NativeKeyEvent.VC_F12:
				return "F12";
			case NativeKeyEvent.VC_F13:
				return "F13";
			case NativeKeyEvent.VC_F14:
				return "F14";
			case NativeKeyEvent.VC_F15:
				return "F15";
			case NativeKeyEvent.VC_F16:
				return "F16";
			case NativeKeyEvent.VC_F17:
				return "F17";
			case NativeKeyEvent.VC_F18:
				return "F18";
			case NativeKeyEvent.VC_F19:
				return "F19";
			case NativeKeyEvent.VC_F20:
				return "F20";
			case NativeKeyEvent.VC_F21:
				return "F21";
			case NativeKeyEvent.VC_F22:
				return "F22";
			case NativeKeyEvent.VC_F23:
				return "F23";
			case NativeKeyEvent.VC_F24:
				return "F24";
			// Begin Alphanumeric Zone
			case NativeKeyEvent.VC_BACKQUOTE:
				return "'";
			case NativeKeyEvent.VC_MINUS:
				return "-";
			case NativeKeyEvent.VC_EQUALS:
				return "=";
			case NativeKeyEvent.VC_BACKSPACE:
				return "\u2190";
			case NativeKeyEvent.VC_TAB:
				return "Tab";
			case NativeKeyEvent.VC_CAPS_LOCK:
				return "Cap";
			case NativeKeyEvent.VC_OPEN_BRACKET:
				return "(";
			case NativeKeyEvent.VC_CLOSE_BRACKET:
				return ")";
			case NativeKeyEvent.VC_BACK_SLASH:
				return "\\";
			case NativeKeyEvent.VC_SEMICOLON:
				return ";";
			case NativeKeyEvent.VC_QUOTE:
				return "\"";
			case NativeKeyEvent.VC_ENTER:
				return "\u21B5";
			case NativeKeyEvent.VC_COMMA:
				return ",";
			case NativeKeyEvent.VC_PERIOD:
				return ".";
			case NativeKeyEvent.VC_SLASH:
				return "/";
			case NativeKeyEvent.VC_SPACE:
				return " ";
			// Begin Edit Key Zone
			case NativeKeyEvent.VC_INSERT:
				return "Ins";
			case NativeKeyEvent.VC_DELETE:
				return "Del";
			case NativeKeyEvent.VC_HOME:
				return "\u2302";
			case NativeKeyEvent.VC_END:
				return "End";
			case NativeKeyEvent.VC_PAGE_UP:
				return "\u2191";
			case NativeKeyEvent.VC_PAGE_DOWN:
				return "\u2193";
			// Begin Cursor Key Zone
			case NativeKeyEvent.VC_UP:
				return "\u25B4";
			case NativeKeyEvent.VC_LEFT:
				return "\u25C2";
			case NativeKeyEvent.VC_CLEAR:
				return "Clr";
			case NativeKeyEvent.VC_RIGHT:
				return "\u25B8";
			case NativeKeyEvent.VC_DOWN:
				return "\u25BE";
			// Begin Modifier and Control Keys
			case NativeKeyEvent.VC_SHIFT:
			case CommandKeys.VC_RSHIFT:
				return "\u21D1";
			case NativeKeyEvent.VC_CONTROL:
				return "Ctl";
			case NativeKeyEvent.VC_ALT:
				return "Alt";
			case NativeKeyEvent.VC_META:
				return "\u2318";
			default:
				return NativeKeyEvent.getKeyText(keyCode);
			}
		}

		@Override
		public void setX(int x){
			this.x = x;
		}

		@Override
		public void setY(int y){
			this.y = y;
		}

		@Override
		public void setWidth(int w){
			width = w;
		}

		@Override
		public void setHeight(int h){
			height = h;
		}

		@Override
		public String getName(){
			return name;
		}

		@Override
		public int getX(){
			return x;
		}

		@Override
		public int getY(){
			return y;
		}

		@Override
		public int getWidth(){
			return width;
		}

		@Override
		public int getHeight(){
			return height;
		}

		@Override
		public RenderingMode getRenderingMode(){
			return mode;
		}

		@Override
		public void setRenderingMode(RenderingMode mode){
			this.mode = mode;
		}
	}
	
	static{
		Image img;
		try{
			img = ImageIO.read(ClassLoader.getSystemResource("kps_small.png"));
		}catch(IOException e){
			img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
		}
		iconSmall = img;
		try{
			img = ImageIO.read(ClassLoader.getSystemResource("kps.png"));
		}catch(IOException e){
			img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
		}
		icon = img;
		onClose = new WindowListener(){

			@Override
			public void windowOpened(WindowEvent e){
			}

			@Override
			public void windowClosing(WindowEvent e){
				exit();
			}

			@Override
			public void windowClosed(WindowEvent e){
			}

			@Override
			public void windowIconified(WindowEvent e){
			}

			@Override
			public void windowDeiconified(WindowEvent e){
			}

			@Override
			public void windowActivated(WindowEvent e){
			}

			@Override
			public void windowDeactivated(WindowEvent e){
			}
		};
		DUMMY_KEY = new Key(null){
			/**
			 * Serial ID
			 */
			private static final long serialVersionUID = 5918421427659740215L;

			@Override
			public void keyPressed(){
			}
			
			@Override
			public void keyReleased(){
			}
		};
	}
}
