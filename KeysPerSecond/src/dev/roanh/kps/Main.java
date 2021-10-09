package dev.roanh.kps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.NativeInputEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;

import dev.roanh.kps.CommandKeys.CMD;
import dev.roanh.kps.layout.GridPanel;
import dev.roanh.kps.layout.Layout;
import dev.roanh.kps.panels.AvgPanel;
import dev.roanh.kps.panels.GraphPanel;
import dev.roanh.kps.panels.MaxPanel;
import dev.roanh.kps.panels.NowPanel;
import dev.roanh.kps.panels.TotPanel;
import dev.roanh.kps.ui.dialog.KeysDialog;
import dev.roanh.kps.ui.dialog.LayoutDialog;
import dev.roanh.util.ClickableLink;
import dev.roanh.util.Dialog;
import dev.roanh.util.ExclamationMarkPath;
import dev.roanh.util.Util;

/**
 * This program can be used to display
 * information about how many times
 * certain keys are pressed and what the
 * average, maximum and current
 * number of keys pressed per second is.
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
	 * String holding the version of the program.
	 */
	public static final String VERSION = "v8.4";//XXX the version number  - don't forget build.gradle
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
	public static Map<Integer, Key> keys = new HashMap<Integer, Key>();
	/**
	 * The most recent key event, only
	 * used during the initial setup
	 */
	public static NativeKeyEvent lastevent;
	/**
	 * Main panel used for showing all the sub panels that
	 * display all the information
	 */
	public static final GridPanel content = new GridPanel();
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
	protected static final Key DUMMY_KEY;
	/**
	 * Best text rendering hints.
	 */
	public static Map<?, ?> desktopHints;
	
	/**
	 * Main method
	 * @param args - configuration file path
	 */
	public static void main(String[] args){
		//Work around for a JDK bug
		ExclamationMarkPath.check(args);
		
		//Basic setup and info
		String config = null;
		if(args.length >= 1 && !args[0].equalsIgnoreCase("-relaunch")){
			config = args[0];
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
			if(files != null && files.length > 0 && files[0].exists()){
				toLoad.loadConfig(files[0]);
				Main.config = toLoad;
				System.out.println("Loaded config file: " + files[0].getName());
			}else{
				System.out.println("Provided config file does not exist.");
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
					avg = (avg * n + totaltmp) / (n + 1.0D);
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
		ctop.addActionListener((e)->{
			config.overlay = ctop.isSelected();
		});
		callKeys.addActionListener((e)->{
			config.trackAllKeys = callKeys.isSelected();
		});
		callButtons.addActionListener((e)->{
			config.trackAllButtons = callButtons.isSelected();
		});
		cmax.addActionListener((e)->{
			config.showMax = cmax.isSelected();
		});
		cavg.addActionListener((e)->{
			config.showAvg = cavg.isSelected();
		});
		ccur.addActionListener((e)->{
			config.showCur = ccur.isSelected();
		});
		cgra.addActionListener((e)->{
			config.showGraph = cgra.isSelected();
		});
		ccol.addActionListener((e)->{
			config.customColors = ccol.isSelected();
		});
		ckey.addActionListener((e)->{
			config.showKeys = ckey.isSelected();
		});
		ctot.addActionListener((e)->{
			config.showTotal = ctot.isSelected();
		});
		cmod.addActionListener((e)->{
			config.enableModifiers = cmod.isSelected();
		});
		JPanel options = new JPanel();
		labels.setPreferredSize(new Dimension((int)labels.getPreferredSize().getWidth(), (int)boxes.getPreferredSize().getHeight()));
		options.add(labels);
		options.add(boxes);
		JPanel buttons = new JPanel(new GridLayout(10, 1));
		JButton save = new JButton("Save config");
		JButton addkey = new JButton("Add key");
		JButton load = new JButton("Load config");
		JButton updaterate = new JButton("Update rate");
		JButton cmdkeys = new JButton("Commands");
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
			LayoutDialog.configureLayout(false);
		});
		cmdkeys.addActionListener((e)->{
			configureCommandKeys();
		});
		precision.addActionListener((e)->{
			configurePrecision();
		});
		graph.addActionListener((e)->{
			configureGraph();
		});
		addkey.addActionListener((e)->{
			KeysDialog.configureKeys();
		});
		color.addActionListener((e)->{
			configureColors();
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
		});
		updaterate.addActionListener((e)->{
			configureUpdateRate();
		});
		autoSave.addActionListener((e)->{
			Statistics.configureAutoSave(false);
		});
		JPanel info = new JPanel(new GridLayout(2, 1, 0, 2));
		info.add(Util.getVersionLabel("KeysPerSecond", VERSION));
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
	}
	
	/**
	 * Shows a dialog to configure the graph.
	 */
	private static final void configureGraph(){
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
		}
	}
	
	/**
	 * Shows a dialog to configure the precision.
	 */
	private static final void configurePrecision(){
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
		}
	}
	
	/**
	 * Shows a dialog to configure the update rate.
	 */
	private static final void configureUpdateRate(){
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
		}
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
	public static final void reconfigure(){
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
			frame.setAlwaysOnTop(config.overlay);
			graphFrame.setAlwaysOnTop(config.overlay);
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
			
			//Start stats saving
			Statistics.cancelScheduledTask();
			if(Main.config.autoSaveStats){
				Statistics.saveStatsTask();
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
		Statistics.saveStatsOnExit();
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

			@Override
			public void keyPressed(){
			}

			@Override
			public void keyReleased(){
			}
		};
		
		//Request best text anti-aliasing settings
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		desktopHints = (Map<?, ?>)Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints");
		if(desktopHints == null){
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			desktopHints = map;
		}else{
			toolkit.addPropertyChangeListener("awt.font.desktophints", event->desktopHints = (Map<?, ?>)event.getNewValue());
		}
	}
}