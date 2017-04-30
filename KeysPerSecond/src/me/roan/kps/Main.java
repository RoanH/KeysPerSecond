package me.roan.kps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

import me.roan.kps.CommandKeys.CMD;

/**
 * This program can be used to display
 * information about how many times
 * certain keys are pressed and what
 * average, maximum and current
 * amount of keys pressed per second is
 * <pre>
 * Besides the tracking of the assigned keys
 * this program responds to 5 key events these are:
 * <ol><li><b>Ctrl + P</b>: Causes the program to reset the average and maximum value
 * And to print the statistics to standard output
 * </li><li><b>Ctrl + O</b>: Terminates the program
 * </li><li><b>Ctrl + I</b>: Causes the program to reset the amount of times a key is pressed
 * And to print the statistics to standard output
 * </li><li><b>Ctrl + Y</b>: Hides/shows the GUI
 * </li><li><b>Ctrl + T</b>: Pauses/resumes the counter
 * </li><li><b>Ctrl + R</b>: Reloads the configuration</li></ol></pre>
 * The program also constantly prints the current keys per second to
 * the standard output.<br>
 * And key is only counted as being pressed if the key has been released before
 * this deals with the issue of holding a key firing multiple key press events<br>
 * This program also has support for saving and loading configurations
 * @author Roan
 */
public class Main {
	/**
	 * The number of seconds the average has
	 * been calculated for
	 */
	protected static long n = 0;
	/**
	 * The number of keys pressed in the
	 * ongoing second
	 */
	private static int tmp = 0;
	/**
	 * The average keys per second
	 */
	protected static double avg;
	/**
	 * The maximum keys per second value reached so far
	 */
	protected static int max;
	/**
	 * The keys per second of the previous second
	 * used for displaying the current keys per second value
	 */
	protected static int prev;
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
	private static JPanel content = new JPanel(new GridLayout(1, 0, 2, 0));
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
	protected static final JFrame frame = new JFrame("Keys per second");
	/**
	 * Whether or not the counter is paused
	 */
	protected static boolean suspended = false;
	/**
	 * The configuration
	 */
	protected static Configuration config = new Configuration(null);
	/**
	 * The loop timer
	 */
	protected static ScheduledExecutorService timer = null;
	/**
	 * The loop timer task
	 */
	protected static ScheduledFuture<?> future = null;

	/**
	 * Main method
	 * @param args - configuration file path
	 */
	public static void main(String[] args) {
		String config = null;
		if(args.length >= 1){
			config = args[0];
			for(int i = 1; i < args.length; i++){
				config += " " + args[i];
			}
			System.out.println("Attempting to load config: " + config);
		}
		relaunchFromTemp(config);
		System.out.println("Control keys:");
		System.out.println("Ctrl + P: Causes the program to reset and print the average and maximum value");
		System.out.println("Ctrl + U: Terminates the program");
		System.out.println("Ctrl + I: Causes the program to reset and print the key press statistics");
		System.out.println("Ctrl + Y: Hides/shows the GUI");
		System.out.println("Ctrl + T: Pauses/resumes the counter");
		System.out.println("Ctrl + R: Reloads the configuration");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
		}

		//Make sure the native hook is always unregistered
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){
				try {
					GlobalScreen.unregisterNativeHook();
				} catch (NativeHookException e1) {
					e1.printStackTrace();
				}
			}
		});

		//Initialise native library and register event handlers
		setupNativeHook();

		//Set configuration for the keys
		if(config != null){
			Configuration toLoad = new Configuration(config == null ? null : new File(config));
			int index = config.lastIndexOf(File.separatorChar);
			File dir = new File(config.substring(0, index));
			final String name = config.substring(index + 1);
			File[] files = null;
			if(dir.exists()){
				files = dir.listFiles((FilenameFilter) (f, n)->{
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
					JOptionPane.showMessageDialog(null, "Failed to load the configuration menu, however you can use the live menu instead", "Keys per second", JOptionPane.ERROR_MESSAGE);
				}catch(Throwable t){
					t.printStackTrace();
				}
				System.err.println("Failed to load the configuration menu, however you can use the live menu instead");
			}
		}
		
		//Build GUI
		try {
			buildGUI();
		} catch (IOException e) {
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
		future = timer.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if(!suspended){
					int totaltmp = tmp;
					for(int i : timepoints){
						totaltmp += i;
					}
					if(totaltmp > max){
						max = totaltmp;
					}
					if(totaltmp != 0){
						avg = (avg * (double)n + (double)totaltmp) / ((double)n + 1.0D);
						n++;
						TotPanel.hits += tmp;
						System.out.println("Current keys per second: " + totaltmp + " time frame: " + tmp);
					}
					graph.addPoint(totaltmp);
					graph.repaint();
					content.repaint();
					prev = totaltmp;
					timepoints.addFirst(tmp);
					if(timepoints.size() >= 1000 / config.updateRate){
						timepoints.removeLast();
					}
					tmp = 0;
				}else{
					tmp = 0;
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
		try {
			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			logger.setLevel(Level.WARNING);
			logger.setUseParentHandlers(false);
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			JOptionPane.showMessageDialog(null, "There was a problem registering the native hook: " + ex.getMessage(), "Keys per second", JOptionPane.ERROR_MESSAGE);
			try {
				GlobalScreen.unregisterNativeHook();
			} catch (NativeHookException e1) {
				e1.printStackTrace();
			}
			System.exit(1);
		}
		GlobalScreen.addNativeKeyListener(new NativeKeyListener(){

			@Override
			public void nativeKeyPressed(NativeKeyEvent event) {
				pressEvent(event);
			}

			@Override
			public void nativeKeyReleased(NativeKeyEvent event) {
				releaseEvent(event);
			}

			@Override
			public void nativeKeyTyped(NativeKeyEvent event) {
			}
		});
		GlobalScreen.addNativeMouseListener(new NativeMouseListener(){

			@Override
			public void nativeMouseClicked(NativeMouseEvent event) {				
			}

			@Override
			public void nativeMousePressed(NativeMouseEvent event) {
				pressEvent(event);
			}

			@Override
			public void nativeMouseReleased(NativeMouseEvent event) {
				releaseEvent(event);
			}
		});
	}
	
	/**
	 * Called when a key is released
	 * @param event The event that occurred
	 */
	private static final void releaseEvent(NativeInputEvent event){
		int code = event instanceof NativeKeyEvent ? ((NativeKeyEvent)event).getKeyCode() : -((NativeMouseEvent)event).getButton();
		if(keys.containsKey(code)){
			keys.get(code).keyReleased();
		}
	}
	
	/**
	 * Called when a key is pressed
	 * @param event The event that occurred
	 */
	private static final void pressEvent(NativeInputEvent nevent){
		int code = nevent instanceof NativeKeyEvent ? ((NativeKeyEvent)nevent).getKeyCode() : -((NativeMouseEvent)nevent).getButton();
		if(config.trackAll && !keys.containsKey(code)){
			if(nevent instanceof NativeKeyEvent){
				keys.put(code, new Key(NativeKeyEvent.getKeyText(((NativeKeyEvent)nevent).getKeyCode())));
			}else{
				keys.put(code, new Key("M" + ((NativeMouseEvent)nevent).getButton()));
			}
		}
		if(keys.containsKey(code) && !suspended){
			keys.get(code).keyPressed();	
		}
		if(nevent instanceof NativeKeyEvent){
			NativeKeyEvent event = (NativeKeyEvent)nevent;
			boolean ctrl = (!frame.isFocusOwner()) ? ((event.getModifiers() & NativeKeyEvent.CTRL_MASK) != 0) : (((event.getModifiers() & (NativeKeyEvent.CTRL_MASK | NativeKeyEvent.CTRL_L_MASK | NativeKeyEvent.CTRL_R_MASK)) != 0) && (lastevent == null ? false : ((lastevent.getModifiers() & (NativeKeyEvent.CTRL_MASK | NativeKeyEvent.CTRL_L_MASK | NativeKeyEvent.CTRL_R_MASK)) != 0)));
			boolean alt = (event.getModifiers() & NativeKeyEvent.ALT_MASK) != 0;
			lastevent = event;
			if(config.CP.matches(event.getKeyCode(), alt, ctrl)){
				resetStats();
			}else if(config.CU.matches(event.getKeyCode(), alt, ctrl)){
				exit();
			}else if(config.CI.matches(event.getKeyCode(), alt, ctrl)){
				resetTotals();
			}else if(config.CY.matches(event.getKeyCode(), alt, ctrl)){
				if(frame.getContentPane().getComponentCount() != 0){
					frame.setVisible(!frame.isVisible());
				}
			}else if(config.CT.matches(event.getKeyCode(), alt, ctrl)){
				suspended = !suspended;
				Menu.pause.setSelected(suspended);
			}else if(config.CR.matches(event.getKeyCode(), alt, ctrl)){
				double oldScale = config.size;
				config.reloadConfig();
				Menu.resetData(oldScale);
			}
		}
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
		JPanel boxes = new JPanel(new GridLayout(9, 0));
		JPanel labels = new JPanel(new GridLayout(9, 0));
		JCheckBox cmax = new JCheckBox();
		JCheckBox cavg = new JCheckBox();
		JCheckBox ccur = new JCheckBox();
		JCheckBox ckey = new JCheckBox();
		JCheckBox cgra = new JCheckBox();
		JCheckBox ctop = new JCheckBox();
		JCheckBox ccol = new JCheckBox();
		JCheckBox call = new JCheckBox();
		JCheckBox ctot = new JCheckBox();
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
		JLabel lall = new JLabel("Track all keys");
		JLabel ltot = new JLabel("Show total");
		ltop.setToolTipText("Requires you to run osu! out of full screen mode, known to not (always) work with the wine version of osu!");
		boxes.add(cmax);
		boxes.add(cavg);
		boxes.add(ccur);
		boxes.add(ctot);
		boxes.add(ckey);
		boxes.add(cgra);
		boxes.add(ctop);
		boxes.add(ccol);
		boxes.add(call);
		labels.add(lmax);
		labels.add(lavg);
		labels.add(lcur);
		labels.add(ltot);
		labels.add(lkey);
		labels.add(lgra);
		labels.add(ltop);
		labels.add(lcol);
		labels.add(lall);
		JButton save = new JButton("Save config");
		ctop.addActionListener((e)->{
			config.overlay = ctop.isSelected();
			save.setEnabled(true);
		});
		call.addActionListener((e)->{
			config.trackAll = call.isSelected();
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
		JPanel options = new JPanel();
		labels.setPreferredSize(new Dimension((int)labels.getPreferredSize().getWidth(), (int)boxes.getPreferredSize().getHeight()));
		options.add(labels);
		options.add(boxes);
		JPanel buttons = new JPanel(new GridLayout(9, 0));
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
		JButton size = new JButton("Size");
		buttons.add(addkey);
		buttons.add(load);
		buttons.add(save);
		buttons.add(graph);
		buttons.add(updaterate);
		buttons.add(color);
		buttons.add(precision);
		buttons.add(size);
		buttons.add(cmdkeys);
		form.add(options, BorderLayout.CENTER);
		options.setBorder(BorderFactory.createTitledBorder("General"));
		buttons.setBorder(BorderFactory.createTitledBorder("Configuration"));
		JPanel all = new JPanel(new BorderLayout());
		all.add(options, BorderLayout.LINE_START);
		all.add(buttons, BorderLayout.LINE_END);
		form.add(all, BorderLayout.CENTER);
		size.addActionListener((e)->{
			configureSize();
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
			if(0 == JOptionPane.showOptionDialog(null, pconfig, "Keys per second", 0, JOptionPane.QUESTION_MESSAGE, null, new String[]{"OK", "Cancel"}, 0)){
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
			if(0 == JOptionPane.showOptionDialog(null, pconfig, "Keys per second", 0, JOptionPane.QUESTION_MESSAGE, null, new String[]{"OK", "Cancel"}, 0)){
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
			call.setSelected(config.trackAll);
			ckey.setSelected(config.showKeys);
			ctop.setSelected(config.overlay);
			ctot.setSelected(config.showTotal);
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
				public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
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
			if(0 == JOptionPane.showOptionDialog(null, pconfig, "Keys per second", 0, JOptionPane.QUESTION_MESSAGE, null, new String[]{"OK", "Cancel"}, 0)){
				config.updateRate = Integer.parseInt(((String)update.getSelectedItem()).substring(0, ((String)update.getSelectedItem()).length() - 2));
				save.setEnabled(true);
			}
		});
		String version = checkVersion();//XXX the version number 
		JLabel ver = new JLabel("<html><center><i>Version: v6.1, latest version: " + (version == null ? "unknown :(" : version) + "<br>"
				+ "<u><font color=blue>https://osu.ppy.sh/forum/t/552405</font></u></i></center></html>", SwingConstants.CENTER);
		ver.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if(Desktop.isDesktopSupported()){
					try {
						Desktop.getDesktop().browse(new URL("https://osu.ppy.sh/forum/t/552405").toURI());
					} catch (IOException | URISyntaxException e1) {
						//pity
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});
		form.add(ver, BorderLayout.PAGE_END);
		int option = JOptionPane.showOptionDialog(null, form, "Keys per second", 0, JOptionPane.QUESTION_MESSAGE, null, new String[]{"OK", "Exit"}, 0);
		if(1 == option || option == JOptionPane.CLOSED_OPTION){
			try {
				GlobalScreen.unregisterNativeHook();
			} catch (NativeHookException e1) {
				e1.printStackTrace();
			}
			System.exit(0);
		}
		frame.setAlwaysOnTop(config.overlay);
	}
	
	/**
	 * Shows the size configuration dialog
	 */
	protected static final void configureSize(){
		JPanel pconfig = new JPanel(new BorderLayout());
		JSpinner s = new JSpinner(new SpinnerNumberModel(config.size * 100, 50, Integer.MAX_VALUE, 1));
		JLabel info = new JLabel("<html>Change how big the displayed window is.<br>"
				+ "The precentage specifies how big the window is in<br>"
				+ "comparison to the default size of the window.<html>");
		pconfig.add(info, BorderLayout.PAGE_START);
		pconfig.add(new JSeparator(), BorderLayout.CENTER);
		JPanel line = new JPanel();
		line.add(new JLabel("Size: "));
		line.add(s);
		line.add(new JLabel("%"));
		pconfig.add(line, BorderLayout.PAGE_END);
		if(0 == JOptionPane.showOptionDialog(null, pconfig, "Keys per second", 0, JOptionPane.QUESTION_MESSAGE, null, new String[]{"OK", "Cancel"}, 0)){
			config.size = ((double)s.getValue()) / 100.0D;
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
			public void mouseClicked(MouseEvent e) {
				if(!open){
					open = true;
					chooser.setColor(e.getComponent().getBackground());
					if(0 == JOptionPane.showOptionDialog(null, chooser, "Keys per second", 0, JOptionPane.QUESTION_MESSAGE, null, new String[]{"OK", "Cancel"}, 0)){
						e.getComponent().setBackground(chooser.getColor());
					}
					open = false;
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {				
			}

			@Override
			public void mouseReleased(MouseEvent e) {				
			}

			@Override
			public void mouseEntered(MouseEvent e) {				
			}

			@Override
			public void mouseExited(MouseEvent e) {				
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
		if(1 == JOptionPane.showOptionDialog(frame.isVisible() ? frame : null, cform, "Keys per second", 0, JOptionPane.QUESTION_MESSAGE, null, new String[]{"OK", "Cancel"}, 0)){
			cfg.setForeground(prevfg);
			cbg.setForeground(prevbg);
		}else{
			config.foreground = cfg.getBackground();
			config.background = cbg.getBackground();
			config.opacitybg = (float)(double)((int)sbg.getValue() / 100.0D);
			config.opacityfg = (float)(double)((int)sfg.getValue() / 100.0D);
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
		
		JOptionPane.showOptionDialog(frame.isVisible() ? frame : null, content, "Keys per second", 0, JOptionPane.QUESTION_MESSAGE, null, new String[]{"OK"}, 0);
	}
	
	/**
	 * Shows the key configuration dialog
	 */
	protected static final void configureKeys(){
		List<KeyInformation> copy = new ArrayList<KeyInformation>(config.keyinfo);
		JPanel keyform = new JPanel(new BorderLayout());
		keyform.add(new JLabel("Currently added keys (you can edit the position & visible or remove it):"), BorderLayout.PAGE_START);
		JTable keys = new JTable();
		DefaultTableModel model = new DefaultTableModel(){
			/**
			 * Serial ID
			 */
			private static final long serialVersionUID = -5510962859479828507L;				

			@Override
			public int getRowCount() {
				return config.keyinfo.size();
			}

			@Override
			public int getColumnCount() {
				return 4;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch(columnIndex){
				case 0:
					return config.keyinfo.get(rowIndex).index;
				case 1:
					return config.keyinfo.get(rowIndex).name;
				case 2:
					return config.keyinfo.get(rowIndex).visible;
				case 3:
					return false;
				}
				return null;
			}

			@Override
			public String getColumnName(int col) {
				switch(col){
				case 0:
					return "Position";
				case 1:
					return "Key";
				case 2:
					return "Visible";
				case 3:
					return "Remove";
				}
				return null;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == 2 || columnIndex ==3){
					return Boolean.class;
				}
				return super.getColumnClass(columnIndex);
			}

			@Override
			public boolean isCellEditable(int row, int col){
				return col != 1;
			}

			@Override
			public void setValueAt(Object value, int row, int col){
				if(col == 0){
					try{
						config.keyinfo.get(row).index = Integer.parseInt((String)value);
					}catch(NumberFormatException | NullPointerException e){
						JOptionPane.showMessageDialog(null, "Entered position not a (whole) number!", "Keys per second", JOptionPane.ERROR_MESSAGE);
					}
				}else if(col == 2){
					config.keyinfo.get(row).visible = (boolean)value;
				}else{
					if((boolean)value == true){
						Main.keys.remove(config.keyinfo.get(row).keycode);
						config.keyinfo.remove(row);
						keys.repaint();
					}
				}
			}
		};
		keys.setModel(model);
		keys.setDragEnabled(false);
		JScrollPane pane = new JScrollPane(keys);
		pane.setPreferredSize(new Dimension((int)keys.getPreferredSize().getWidth(), 120));
		keyform.add(pane, BorderLayout.CENTER);
		JButton newkey = new JButton("Add Key");
		newkey.addActionListener((evt)->{
			if(JOptionPane.showOptionDialog(frame.isVisible() ? frame : null, "Press a key and press 'OK' to add it.", "Keys per second", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"OK", "Cancel"}, 0) == 0){ 	
				if(lastevent == null){ 				
					JOptionPane.showMessageDialog(frame.isVisible() ? frame : null, "No key pressed!", "Keys per second", JOptionPane.ERROR_MESSAGE); 	
					return; 	
				} 			
				KeyInformation info = new KeyInformation(NativeKeyEvent.getKeyText(lastevent.getKeyCode()), lastevent.getKeyCode()); 	
				if(JOptionPane.showConfirmDialog(frame.isVisible() ? frame : null, "Add the " + info.name + " key?", "Keys per second", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){ 		
					config.keyinfo.add(info); 			
				} 			
				model.fireTableDataChanged(); 		
			}
		});
		JButton newmouse = new JButton("Add Mouse Button");
		newmouse.addActionListener((e)->{
			JPanel addform = new JPanel(new BorderLayout());
			addform.add(new JLabel("Select the mouse buttons to add:"), BorderLayout.PAGE_START);
			
			JPanel buttons = new JPanel(new GridLayout(5, 1, 2, 0));
			
			JPanel m1 = new JPanel(new BorderLayout());
			JCheckBox cm1 = new JCheckBox();
			m1.add(cm1, BorderLayout.LINE_START);
			m1.add(new JLabel("M1 (left click)"), BorderLayout.CENTER);
			
			JPanel m2 = new JPanel(new BorderLayout());
			JCheckBox cm2 = new JCheckBox();
			m2.add(cm2, BorderLayout.LINE_START);
			m2.add(new JLabel("M2 (right click)"), BorderLayout.CENTER);
			
			JPanel m3 = new JPanel(new BorderLayout());
			JCheckBox cm3 = new JCheckBox();
			m3.add(cm3, BorderLayout.LINE_START);
			m3.add(new JLabel("M3 (mouse wheel)"), BorderLayout.CENTER);
			
			JPanel m4 = new JPanel(new BorderLayout());
			JCheckBox cm4 = new JCheckBox();
			m4.add(cm4, BorderLayout.LINE_START);
			m4.add(new JLabel("M4"), BorderLayout.CENTER);
			
			JPanel m5 = new JPanel(new BorderLayout());
			JCheckBox cm5 = new JCheckBox();
			m5.add(cm5, BorderLayout.LINE_START);
			m5.add(new JLabel("M5"), BorderLayout.CENTER);
			
			buttons.add(m1);
			buttons.add(m2);
			buttons.add(m3);
			buttons.add(m4);
			buttons.add(m5);
			
			addform.add(buttons, BorderLayout.CENTER);
			
			if(JOptionPane.showOptionDialog(frame.isVisible() ? frame : null, addform, "Keys per second", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"OK", "Cancel"}, 0) == 0){
				if(cm1.isSelected()){
					config.keyinfo.add(new KeyInformation("M1", -NativeMouseEvent.BUTTON1));
				}
				if(cm2.isSelected()){
					config.keyinfo.add(new KeyInformation("M2", -NativeMouseEvent.BUTTON2));
				}
				if(cm3.isSelected()){
					config.keyinfo.add(new KeyInformation("M3", -NativeMouseEvent.BUTTON3));
				}
				if(cm4.isSelected()){
					config.keyinfo.add(new KeyInformation("M4", -NativeMouseEvent.BUTTON4));
				}
				if(cm5.isSelected()){
					config.keyinfo.add(new KeyInformation("M5", -NativeMouseEvent.BUTTON5));
				}
				model.fireTableDataChanged();
			}
		});
		JPanel nbuttons = new JPanel(new GridLayout(1, 2, 2, 0));
		nbuttons.add(newkey, BorderLayout.LINE_START);
		nbuttons.add(newmouse, BorderLayout.LINE_END);
		keyform.add(nbuttons, BorderLayout.PAGE_END);
		if(JOptionPane.showOptionDialog(frame.isVisible() ? frame : null, keyform, "Keys per second", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Save", "Cancel"}, 0) == 1){
			config.keyinfo = copy;
		}
	}

	/**
	 * Builds the main GUI of the program
	 * @throws IOException When an IO Exception occurs, this can be thrown
	 *         when the program fails the load its resources
	 */
	protected static final void buildGUI() throws IOException {
		Menu.createMenu();
		frame.setResizable(false);
		frame.setIconImage(ImageIO.read(ClassLoader.getSystemResource("kps.png")));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.addWindowListener(new WindowListener(){

			@Override
			public void windowOpened(WindowEvent e) {				
			}

			@Override
			public void windowClosing(WindowEvent e) {				
			}

			@Override
			public void windowClosed(WindowEvent e) {
				try {
					GlobalScreen.unregisterNativeHook();
				} catch (NativeHookException e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void windowIconified(WindowEvent e) {				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {				
			}

			@Override
			public void windowActivated(WindowEvent e) {				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {				
			}
		});
		frame.addMouseMotionListener(Listener.INSTANCE);
		frame.addMouseListener(Listener.INSTANCE);
		SizeManager.scale(config.size);
		reconfigure();
	}

	/**
	 * Reconfigures the layout of the program
	 */
	protected static final void reconfigure(){
		SwingUtilities.invokeLater(()->{
			frame.getContentPane().removeAll();
			content = new JPanel(new GridLayout(1, 0, 0, 0));
			try {
				ColorManager.prepareImages(config.showGraph, config.customColors);
			} catch (IOException e) {
				e.printStackTrace();
			}
			config.keyinfo.sort((KeyInformation left, KeyInformation right) -> (left.index > right.index ? 1 : -1));
			Key k;
			int panels = 0;
			for(KeyInformation i : config.keyinfo){
				if(!keys.containsKey(i.keycode)){
					keys.put(i.keycode, k = new Key(i.name));
				}else{
					k = keys.get(i.keycode);
				}
				if(config.showKeys && i.visible){
					content.add(k.getPanel());
					panels++;
				}
			}
			if(config.showMax){
				content.add(new MaxPanel());
				panels++;
			}
			if(config.showAvg){
				content.add(new AvgPanel());
				panels++;
			}
			if(config.showCur){
				content.add(new NowPanel());
				panels++;
			}
			if(config.showTotal){
				content.add(new TotPanel());
				panels++;
			}
			if(panels == 0 && !config.showGraph){
				frame.setVisible(false);
				return;//don't create a GUI if there's nothing to display
			}

			Menu.repaint();
			
			JPanel allcontent = new JPanel(new GridLayout((config.showGraph ? 1 : 0) + (panels > 0 ? 1 : 0), 1, 0, 0));
			allcontent.setOpaque(config.getBackgroundOpacity() != 1.0F ? !ColorManager.transparency : true);
			if(panels > 0){
				allcontent.add(content);
			}
			if(config.showGraph){
				allcontent.add(graph);
				GraphPanel.frames = panels > 0 ? panels : 5;
			}
			frame.setSize((panels == 0 && config.showGraph) ? SizeManager.defaultGraphWidth : (panels * SizeManager.keyPanelWidth), (panels > 0 ? SizeManager.subComponentHeight : 0) + (config.showGraph ? SizeManager.subComponentHeight : 0));
			if(ColorManager.transparency){
				frame.setBackground(ColorManager.transparent);
			}
			frame.add(allcontent);
			frame.setVisible(true);
		});
	}

	/**
	 * Check the KeysPerSecond version to see
	 * if we are running the latest version
	 * @return The latest version
	 */
	private static final String checkVersion(){
		try{ 			
			HttpURLConnection con = (HttpURLConnection) new URL("https://api.github.com/repos/RoanH/KeysPerSecond/tags").openConnection(); 			
			con.setRequestMethod("GET"); 		
			con.setConnectTimeout(10000); 					   
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream())); 	
			String line = reader.readLine(); 		
			reader.close(); 	
			String[] versions = line.split("\"name\":\"v");
			int max_main = 3;
			int max_sub = 0;
			String[] tmp;
			for(int i = 1; i < versions.length; i++){
				tmp = versions[i].split("\",\"")[0].split("\\.");
				if(Integer.parseInt(tmp[0]) > max_main){
					max_main = Integer.parseInt(tmp[0]);
					max_sub = Integer.parseInt(tmp[1]);
				}else if(Integer.parseInt(tmp[0]) < max_main){
					continue;
				}else{
					if(Integer.parseInt(tmp[1]) > max_sub){
						max_sub = Integer.parseInt(tmp[1]);
					}
				}
			}
			return "v" + max_main + "." + max_sub;
		}catch(Exception e){ 	
			return null;
			//No Internet access or something else is wrong,
			//No problem though since this isn't a critical function
		}
	}

	/**
	 * Shuts down the program
	 */
	protected static final void exit(){
		try {
			GlobalScreen.unregisterNativeHook();
		} catch (NativeHookException e1) {
			e1.printStackTrace();
		}
		System.exit(0);
	}

	/**
	 * Reset avg, max & cur
	 */
	protected static final void resetStats(){
		System.out.println("Reset max & avg | max: " + max + " avg: " + avg);
		n = 0;
		avg = 0;
		max = 0;
		tmp = 0;
		graph.reset();
	}

	/**
	 * Rest key count totals
	 */
	protected static final void resetTotals(){
		System.out.print("Reset key counts | ");
		for(Key k : keys.values()){
			System.out.print(k.name + ":" + k.count + " ");
			k.count = 0;
		}
		System.out.println();
	}

	/**
	 * Re-launches the program from the temp directory
	 * if the program path contains a ! this fixes a
	 * bug in the native library loading
	 * @param args
	 */
	private static final void relaunchFromTemp(String args){
		URL url = Main.class.getProtectionDomain().getCodeSource().getLocation(); 	
		File exe; 	
		try { 		 
			exe = new File(url.toURI()); 	
		} catch(URISyntaxException e) { 	
			exe = new File(url.getPath()); 	
		} 	
		if(!exe.getAbsolutePath().contains("!")){
			return;
		}
		File jvm = new File(System.getProperty("java.home") +  File.separator + "bin" + File.separator + "java.exe");
		if(!jvm.exists() || !exe.exists()){
			System.out.println("JVM exists: " + jvm.exists() + " Executable exists: " + exe.exists());
			JOptionPane.showMessageDialog(null, "An error occured whilst trying to launch the program >.<");
			System.exit(0);
		}
		File tmp = null;
		try {
			tmp = File.createTempFile("kps", null);
			if(tmp.getAbsolutePath().contains("!")){
				JOptionPane.showMessageDialog(null, "An error occured whilst trying to launch the program >.<");
				System.exit(0);
			}
			Files.copy(exe.toPath(), tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error occured whilst trying to launch the program >.<");
			tmp.deleteOnExit();
			tmp.delete();
			System.exit(0);
		}
		ProcessBuilder builder = new ProcessBuilder();
		if(args != null){
			builder.command(jvm.getAbsolutePath(), "-jar", tmp.getAbsolutePath(), args);
		}else{
			builder.command(jvm.getAbsolutePath(), "-jar", tmp.getAbsolutePath());
		}
		Process proc = null;
		try {
			proc = builder.start();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error occured whilst trying to launch the program >.<");
			tmp.deleteOnExit();
			tmp.delete();
			System.exit(0);
		}		
		BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		String line;
		try {
			while(proc.isAlive()){
				while((line = in.readLine()) != null){
					System.out.println(line);
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
		} catch (IOException e) {
			System.err.print("Output stream chrashed :/");
		}
		tmp.deleteOnExit();
		tmp.delete();
		System.exit(0);
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
	protected static final class Key {
		/**
		 * Whether or not this key is currently pressed
		 */
		protected boolean down = false;
		/**
		 * The total number of times this key has been pressed
		 */
		protected int count = 0;
		/**
		 * The key in string form<br>
		 * For example: X
		 */
		protected final String name;
		/**
		 * The graphical display for this key
		 */
		private KeyPanel panel = null;

		/**
		 * Constructs a new Key object
		 * for the key with the given
		 * name
		 * @param name The name of the key
		 * @see #name
		 */
		private Key(String name) {
			this.name = name;
		}

		/**
		 * Creates a new KeyPanel with this
		 * objects as its data source
		 * @return A new KeyPanel
		 */
		private KeyPanel getPanel() {
			return panel != null ? panel : (panel = new KeyPanel(this));
		}

		/**
		 * Called when a key is pressed
		 * @return Whether or not this was a key press
		 *         to register
		 */
		private void keyPressed() {
			if (!down) {
				count++;
				down = true;
				tmp++;
				if(panel != null){
					panel.repaint();
				}
			}
		}

		/**
		 * Called when a key is released
		 */
		private void keyReleased() {
			down = false;
			if(panel != null){
				panel.repaint();
			}
		}
	}

	/**
	 * Simple class that holds all
	 * the essential information 
	 * about a key<br>This class
	 * is mainly used for serialisation
	 * which allows for easy saving and
	 * loading of configurations
	 * @author Roan
	 */
	protected static final class KeyInformation implements Serializable{
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
		 * Index of the key
		 */
		protected int index = autoIndex++;
		/**
		 * Auto-increment for #index
		 */
		protected static transient int autoIndex = 0; 
		/**
		 * Whether or not this key is displayed
		 */
		protected boolean visible = true;

		/**
		 * Constructs a new KeyInformation
		 * object with the given information
		 * @param name The name of the key
		 * @param code The virtual key code of the key
		 * @see #name
		 * @see #keycode
		 */
		private KeyInformation(String name, int code){
			this.name = name.length() == 1 ? name.toUpperCase() : parseKeyString(name);
			this.keycode = code;
		}

		/**
		 * Constructs a new KeyInformation
		 * object with the given information
		 * @param name The name of the key
		 * @param code The virtual key code of the key
		 * @param visible Whether or not the key is visible
		 * @param index The key index
		 * @see #name
		 * @see #keycode
		 */
		protected KeyInformation(String name, int code, boolean visible, int index){
			this.name = name;
			this.keycode = code;
			this.visible = visible;
			this.index = index;
		}

		@Override
		public String toString(){
			return "[keycode=" + keycode + ",index=" + index + ",visible=" + visible + ",name=\"" + name + "\"]";
		}

		/**
		 * Converts a long key name to
		 * something a bit shorter
		 * @param key The key to convert
		 * @return The shorter key name
		 */
		private static final String parseKeyString(String key){
			switch(key){
			case "Back Quote":
				return "'";
			case "Minus":
				return "-";
			case "Equals":
				return "=";
			case "Backspace":
				return "\u2190";
			case "Caps Lock":
				return "Cap";
			case "Open Bracket":
				return "(";
			case "Close Bracket":
				return ")";
			case "Back Slash":
				return "\\";
			case "Semicolon":
				return ";";
			case "Quote":
				return "\"";
			case "Enter":
				return "\u21B5";
			case "Comma":
				return ",";
			case "Period":
				return ".";
			case "Slash":
				return "/";
			case "Space":
				return " ";
			case "Insert":
				return "Ins";
			case "Delete":
				return "Del";
			case "Home":
				return "\u2302";
			case "Page Up":
				return "\u2191";
			case "Page Down":
				return "\u2193";
			case "Up":
				return "\u25B2";
			case "Left":
				return "\u25B0";
			case "Right":
				return "\u25B6";
			case "Down":
				return "\u25BC";
			case "Shift":
				return "\u21D1";
			default:
				return key;
			}
		}
	}
}
