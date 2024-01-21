/*
 * KeysPerSecond: An open source input statistics displayer.
 * Copyright (C) 2017  Roan Hofland (roan@roanh.dev).  All rights reserved.
 * GitHub Repository: https://github.com/RoanH/KeysPerSecond
 *
 * KeysPerSecond is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KeysPerSecond is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dev.roanh.kps;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.github.kwhat.jnativehook.NativeHookException;

import dev.roanh.kps.config.ConfigLoader;
import dev.roanh.kps.config.Configuration;
import dev.roanh.kps.config.ThemeColor;
import dev.roanh.kps.config.UpdateRate;
import dev.roanh.kps.config.Version;
import dev.roanh.kps.config.group.CommandSettings;
import dev.roanh.kps.config.group.GraphPanelSettings;
import dev.roanh.kps.config.group.KeyPanelSettings;
import dev.roanh.kps.config.group.SpecialPanelSettings;
import dev.roanh.kps.event.EventManager;
import dev.roanh.kps.event.source.NativeHookInputSource;
import dev.roanh.kps.layout.GridPanel;
import dev.roanh.kps.layout.Layout;
import dev.roanh.kps.panels.DataPanel;
import dev.roanh.kps.panels.GraphPanel;
import dev.roanh.kps.ui.dialog.MainDialog;
import dev.roanh.kps.ui.listener.MainWindowListener;
import dev.roanh.util.Dialog;
import dev.roanh.util.ExclamationMarkPath;
import dev.roanh.util.Util;

/**
 * This program can be used to display information about how many times
 * certain keys are pressed and what certain derivative statistics like
 * the average, maximum and current number of keys pressed per second are.
 * In addition various graphs can also be shown and the entire program can
 * be customised. Custom configurations for the program can also be saved.
 * @author Roan Hofland (<a href="mailto:roan@roanh.dev">roan@roanh.dev</a>)
 * @see <a href="https://github.com/RoanH/KeysPerSecond">GitHub Repository</a>
 */
public class Main{
	/**
	 * String holding the version of the program.
	 */
	public static final Version VERSION = Version.readVersion();
	/**
	 * The number of seconds the average has
	 * been calculated for
	 */
	protected static long n = 0;
	/**
	 * The number of keys pressed in the
	 * ongoing second
	 */
	protected static final AtomicInteger tmp = new AtomicInteger(0);
	/**
	 * The average keys per second
	 */
	public static double avg;
	/**
	 * The maximum keys per second value reached so far
	 */
	public static int max;
	/**
	 * Total number of hits
	 */
	public static int hits;
	/**
	 * The keys per second of the previous second
	 * used for displaying the current keys per second value
	 */
	public static int prev;
	/**
	 * Nanosecond time of the last tracked input.
	 */
	public static long lastHitTime = -1;
	/**
	 * Last known cursor location.
	 */
	public static final Point mouseLoc = new Point();
	/**
	 * HashMap containing all the tracked keys and their virtual
	 * codes. Used to increment the count for the keys.
	 */
	public static final Map<Integer, Key> keys = new HashMap<Integer, Key>();
	/**
	 * Main panel used for showing all the sub panels that
	 * display all the information
	 */
	public static GridPanel content;
	/**
	 * Graph panel.
	 */
	private static final List<GraphPanel> graphs = new ArrayList<GraphPanel>();
	/**
	 * Linked list containing all the past key counts per time frame
	 */
	private static final LinkedList<Integer> timepoints = new LinkedList<Integer>();
	/**
	 * The program's main frame
	 */
	public static JFrame frame;
	/**
	 * Whether or not the counter is paused
	 */
	protected static volatile boolean suspended = false;
	/**
	 * The configuration
	 */
	public static Configuration config = new Configuration();
	/**
	 * The loop timer
	 */
	private static final ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
	/**
	 * The loop timer task
	 */
	private static volatile ScheduledFuture<?> future = null;
	/**
	 * The layout for the main panel of the program
	 */
	private static Layout layout;
	/**
	 * Small icon for the program
	 */
	public static final Image iconSmall;
	/**
	 * Icon for the program
	 */
	public static final Image icon;
	/**
	 * Dummy key for getOrDefault operations
	 */
	private static final Key DUMMY_KEY;
	/**
	 * Best text rendering hints.
	 */
	public static Map<?, ?> desktopHints;
	/**
	 * Event manager responsible for forwarding input events.
	 */
	public static final EventManager eventManager = new EventManager();
	
	/**
	 * Main method.
	 * @param args The configuration file path.
	 */
	public static void main(String[] args){
		//work around for a JDK bug
		ExclamationMarkPath.check(args);

		//simple hello print
		System.out.println("KeysPerSecond " + VERSION);
		
		//check for a passed config
		String configPath = null;
		if(args.length >= 1 && !args[0].equalsIgnoreCase("-relaunch")){
			configPath = args[0];
			System.out.println("Attempting to load config: " + configPath);
		}
		
		//set UI components
		Util.installUI();
		content = new GridPanel();
		frame = new JFrame("KeysPerSecond");
		layout = new Layout(content);
		content.setLayout(layout);
		
		//set dialog defaults
		Dialog.setDialogIcon(iconSmall);
		Dialog.setParentFrame(frame);
		Dialog.setDialogTitle("KeysPerSecond");
		
		//register input sources
		try{
			eventManager.registerInputSource(new NativeHookInputSource(eventManager));
		}catch(NativeHookException ex){
			System.err.println("There was a problem registering the native hook.");
			ex.printStackTrace();
			Dialog.showErrorDialog("There was a problem registering the native hook: " + ex.getMessage());
			System.exit(1);
		}
		
		//register command handlers
		CommandKeys listener = new CommandKeys();
		eventManager.registerKeyPressListener(listener);
		eventManager.registerKeyReleaseListener(listener);
		
		//load any given configurations
		Configuration quickLoad = ConfigLoader.quickLoadConfiguration(configPath);
		if(quickLoad == null){
			//if no (valid) ones were found let the user create a new config
			applyConfig(MainDialog.configure(), false);
		}else{
			applyConfig(quickLoad, false);
		}

		//build GUI
		buildGUI();
		
		//register default event handlers
		eventManager.registerButtonPressListener(Main::pressEventButton);
		eventManager.registerButtonReleaseListener(Main::releaseEventButton);
		eventManager.registerKeyPressListener(Main::pressEventKey);
		eventManager.registerKeyReleaseListener(Main::releaseEventKey);
		eventManager.registerKeyPressListener(Main::triggerCommandKeys);
		eventManager.registerMouseMoveListener(Main::moveEventMouse);
		
		//enter the main loop
		mainLoop();
	}
	
	/**
	 * Applies a new configuration to the program.
	 * @param config The new configuration to load.
	 * @param live True if the main GUI is already visible.
	 */
	public static final void applyConfig(Configuration config, boolean live){
		Main.config = config;
		
		//rebuild the menu
		Menu.createMenu();
		
		//reset stats and keys
		resetData();
		
		//apply a frame position if set
		if(config.getFramePosition().hasPosition()){
			frame.setLocation(config.getFramePosition().getLocation());
		}

		//load initial stats
		if(config.getStatsSavingSettings().isLoadOnLaunchEnabled()){
			try{
				Statistics.loadStats(Paths.get(config.getStatsSavingSettings().getSaveFile()));
			}catch(IOException | UnsupportedOperationException | IllegalArgumentException e){
				e.printStackTrace();
				Dialog.showMessageDialog("Failed to load statistics on launch.\nCause: " + e.getMessage());
			}
		}
		
		//print loaded config
		if(config.getPath() != null){
			System.out.println("Loaded config file: " + config.getPath().toString());
		}
		
		//update the running state
		if(live){
			reconfigure();
			mainLoop();
		}
	}

	/**
	 * Main loop of the program
	 * this loop updates the
	 * average, current and 
	 * maximum keys per second
	 */
	protected static final void mainLoop(){
		if(future != null){
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
				
				prev = totaltmp;
				if(totaltmp != 0){
					avg = (avg * n + totaltmp) / (n + 1.0D);
					n++;
					hits += currentTmp;
				}
				
				for(GraphPanel graph : graphs){
					graph.update();
				}
				
				content.repaint();
				timepoints.addFirst(currentTmp);
				if(timepoints.size() >= 1000 / config.getUpdateRateMs()){
					timepoints.removeLast();
				}
			}
		}, 0, config.getUpdateRateMs(), TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Handles cursor movement.
	 * @param x The new cursor x coordinate.
	 * @param y The new cursor y coordinate.
	 */
	public static final void moveEventMouse(int x, int y){
		mouseLoc.move(x, y);
	}

	/**
	 * Handles a mouse button release event.
	 * @param button The ID of the button that was released.
	 */
	private static final void releaseEventButton(int button){
		keys.getOrDefault(getExtendedButtonCode(button), DUMMY_KEY).keyReleased();
	}

	/**
	 * Handles a key release event.
	 * @param rawCode The key code of the key that was released.
	 */
	private static final void releaseEventKey(int rawCode){
		int code = getExtendedKeyCode(rawCode);
		
		if(config.isKeyModifierTrackingEnabled()){
			if(code == CommandKeys.ALT){
				for(Key k : keys.values()){
					if(k.hasAlt()){
						k.keyReleased();
					}
				}
			}else if(code == CommandKeys.CTRL){
				for(Key k : keys.values()){
					if(k.hasCtrl()){
						k.keyReleased();
					}
				}
			}else if(CommandKeys.isShift(code)){
				for(Key k : keys.values()){
					if(k.hasShift()){
						k.keyReleased();
					}
				}
			}
			
			for(Entry<Integer, Key> k : keys.entrySet()){
				if(CommandKeys.getBaseKeyCode(code) == CommandKeys.getBaseKeyCode(k.getKey())){
					k.getValue().keyReleased();
				}
			}
		}else{
			keys.getOrDefault(code, DUMMY_KEY).keyReleased();
		}
	}
	
	/**
	 * Handles a button press event.
	 * @param button The ID of the button that was pressed.
	 */
	private static final void pressEventButton(int button){
		int code = getExtendedButtonCode(button);
		Key key = keys.get(code);
		
		if(config.isTrackAllButtons() && key == null){
			key = new Key();
			keys.put(code, key);
		}
		
		if(!suspended && key != null){
			key.keyPressed();
		}
	}

	/**
	 * Handles a key press event.
	 * @param rawCode The key code of the key that was pressed.
	 */
	private static final void pressEventKey(int rawCode){
		int code = getExtendedKeyCode(rawCode);
		Key key = keys.get(code);
		
		if(config.isTrackAllKeys() && key == null){
			key = new Key(code);
			keys.put(code, key);
		}
		
		if(!suspended && key != null){
			key.keyPressed();
			if(config.isKeyModifierTrackingEnabled()){
				if(key.hasAlt()){
					keys.getOrDefault(CommandKeys.ALT, DUMMY_KEY).keyReleased();
				}
				
				if(key.hasCtrl()){
					keys.getOrDefault(CommandKeys.CTRL, DUMMY_KEY).keyReleased();
				}
				
				if(key.hasShift()){
					keys.getOrDefault(CommandKeys.RSHIFT, DUMMY_KEY).keyReleased();
					keys.getOrDefault(CommandKeys.LSHIFT, DUMMY_KEY).keyReleased();
				}
			}
		}
	}
	
	/**
	 * Handles a received key press and triggers command keys.
	 * @param code The received key press key code.
	 */
	private static void triggerCommandKeys(int code){
		CommandSettings commands = Main.config.getCommands();
		if(commands.getCommandResetStats().matches(code)){
			resetStats();
		}else if(commands.getCommandExit().matches(code)){
			exit();
		}else if(commands.getCommandResetTotals().matches(code)){
			resetTotals();
		}else if(commands.getCommandHide().matches(code)){
			if(frame.isVisible()){
				Menu.minimizeToSystemTray();
			}else{
				Menu.restoreFromSystemTray();
			}
		}else if(commands.getCommandPause().matches(code)){
			suspended = !suspended;
			Menu.pause.setSelected(suspended);
		}else if(commands.getCommandReload().matches(code)){
			ConfigLoader.reloadConfig();
		}
	}

	/**
	 * Gets the extended key code for this event, this key code includes modifiers.
	 * @param rawCode The received key code for the key that was pressed.
	 * @return The extended key code for this event.
	 * @see #getExtendedButtonCode(int)
	 */
	public static final int getExtendedKeyCode(int rawCode){
		if(config.isKeyModifierTrackingEnabled()){
			return CommandKeys.getExtendedKeyCode(rawCode);
		}else{
			return CommandKeys.getExtendedKeyCode(rawCode, false, false, false);
		}
	}
	
	/**
	 * Gets the extended button code for this event.
	 * @param button The button that was pressed.
	 * @return The extended key code for this event.
	 * @see #getExtendedKeyCode(int)
	 */
	public static final int getExtendedButtonCode(int button){
		return -button;
	}

	/**
	 * Changes the update rate
	 * @param newRate The new update rate
	 */
	protected static final void changeUpdateRate(UpdateRate newRate){
		n *= (double)config.getUpdateRateMs() / (double)newRate.getRate();
		tmp.set(0);
		timepoints.clear();
		resetGraphs();
		config.setUpdateRate(newRate);
		mainLoop();
	}

	/**
	 * Builds the main GUI of the program.
	 */
	private static final void buildGUI(){
		frame.setResizable(false);
		frame.setIconImage(icon);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Listener.configureListener(frame);
		frame.addWindowListener(new MainWindowListener());
		reconfigure();
	}
	
	/**
	 * Reconfigures the layout of the program
	 */
	public static final void reconfigure(){
		SwingUtilities.invokeLater(()->{
			if(config.isWindowedMode() == frame.isUndecorated()){
				frame.setVisible(false);
				frame.dispose();
				frame.setUndecorated(!Main.config.isWindowedMode());
			}
			
			frame.getContentPane().removeAll();
			layout.removeAll();
			
			//theme
			ThemeColor background = config.getTheme().getBackground();
			boolean opaque = background.getAlpha() != 1.0F ? !ColorManager.transparency : true;
			Menu.repaint();

			try{
				ColorManager.prepareImages();
			}catch(IOException e){
				e.printStackTrace();
			}
			
			//key panels
			for(KeyPanelSettings info : config.getKeys()){
				Key key = keys.computeIfAbsent(info.getKeyCode(), code->new Key(info));
				if(info.isVisible()){
					content.add(info.createPanel(key));
				}
			}
			
			//special panels
			for(SpecialPanelSettings panel : config.getPanels()){
				content.add(panel.createPanel());
			}
			
			//graph panels
			graphs.clear();
			for(GraphPanelSettings info : config.getGraphs()){
				GraphPanel graph = info.createGraph();
				content.add(graph);
				graphs.add(graph);
			}
			
			//frame configuration
			JPanel all = new JPanel(new BorderLayout());
			all.add(content, BorderLayout.CENTER);
			all.setOpaque(opaque);
			
			if(background.getAlpha() != 1.0F && frame.isUndecorated()){
				frame.setBackground(ColorManager.transparent);
				content.setOpaque(false);
				content.setBackground(ColorManager.transparent);
			}else{
				content.setOpaque(true);
				content.setBackground(background.getColor());
			}
			
			frame.add(all);
			frame.pack();
			Insets insets = frame.getInsets();
			frame.setSize(layout.getWidth() + insets.left + insets.right, layout.getHeight() + insets.top + insets.bottom);
			frame.setAlwaysOnTop(config.isOverlayMode());
			frame.setVisible(true);
		});
		
		//Start stats saving
		Statistics.cancelScheduledTask();
		if(config.getStatsSavingSettings().isAutoSaveEnabled()){
			Statistics.saveStatsTask();
		}
	}
	
	/**
	 * Signals to all panels that size related information has changed
	 * and that all rendering caches should be invalidated.
	 */
	public static final void resetPanels(){
		for(Component component : content.getComponents()){
			if(component instanceof DataPanel){
				((DataPanel)component).sizeChanged();
			}
		}
	}
	
	/**
	 * Clears the data for all active graphs.
	 */
	public static final void resetGraphs(){
		graphs.forEach(GraphPanel::reset);
	}

	/**
	 * Shuts down the program
	 */
	public static final void exit(){
		Statistics.saveStatsOnExit();
		System.exit(0);
	}
	
	/**
	 * Resets all statistics and keys.
	 */
	protected static final void resetData(){
		keys.clear();
		resetStats();
	}

	/**
	 * Resets all derived statistics.
	 */
	protected static final void resetStats(){
		System.out.println("Reset stats | max: " + max + " avg: " + avg + " tot: " + hits);
		n = 0;
		avg = 0;
		max = 0;
		hits = 0;
		tmp.set(0);
		lastHitTime = -1;
		resetGraphs();
		frame.repaint();
	}

	/**
	 * Resets key count totals.
	 */
	protected static final void resetTotals(){
		System.out.print("Reset key counts |");
		for(Entry<Integer, Key> key : keys.entrySet()){
			System.out.print(" " + CommandKeys.formatExtendedCode(key.getKey()) + ":" + key.getValue().getCount());
			key.getValue().setCount(0);
		}
		System.out.println();
		
		frame.repaint();
	}
	
	/**
	 * Removes all tracked information for the given key code.
	 * @param keycode The key code to remove.
	 */
	public static void removeKey(int keycode){
		keys.remove(keycode);
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
		DUMMY_KEY = new Key(){

			@Override
			public void keyPressed(){
			}

			@Override
			public void keyReleased(){
			}
		};
		
		//Request the best text anti-aliasing settings
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		desktopHints = (Map<?, ?>)Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints");
		Map<Object, Object> defaultHints = new HashMap<Object, Object>();
		defaultHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		if(desktopHints == null){
			desktopHints = defaultHints;
		}
		toolkit.addPropertyChangeListener("awt.font.desktophints", event->{
			desktopHints = event.getNewValue() == null ? defaultHints : (Map<?, ?>)event.getNewValue();
		});
	}
}