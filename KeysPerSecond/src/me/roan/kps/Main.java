package me.roan.kps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

/**
 * This program can be used to display
 * information about how many times
 * certain keys are pressed and what
 * average, maximum and current
 * amount of keys pressed per second is
 * <pre>
 * Besides the tracking of the assigned keys
 * this program responds to 3 key events these are:
 * <ol><li><b>Ctrl + P</b>: Causes the program to reset the average and maximum value
 * And to print the statistics to standard output
 * </li><li><b>Ctrl + O</b>: Terminates the program
 * </li><li><b>Ctrl + I</b>: Causes the program to reset the amount of times a key is pressed
 * And to print the statistics to standard output</li></ol></pre>
 * The program also constantly prints the current keys per second to
 * the standard output.<br>
 * And key is only counted as being pressed if the key has been released before
 * this deals with the issue of holding a key firing multiple key press events<br>
 * This program also has support for saving and loading configurations
 * @author Roan
 */
public class Main {
	/**
	 * The directory this jar is in
	 */
	public static String dir = "";
	/**
	 * Last main loop update
	 */
	private static long last = System.currentTimeMillis();
	/**
	 * The number of seconds the average has
	 * been calculated for
	 */
	private static long n = 0;
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
	 * Image for a pressed key
	 * Image taken from osu!lazer
	 * https://cloud.githubusercontent.com/assets/191335/16511435/17acd2f2-3f8b-11e6-8b50-5fccba819ce5.png
	 */
	protected static Image pressed;
	/**
	 * Image for an unpressed key<br>
	 * Image taken from osu!lazer
	 * https://cloud.githubusercontent.com/assets/191335/16511432/17ac5232-3f8b-11e6-95b7-33f9a4df0b7c.png
	 */
	protected static Image unpressed;
	/**
	 * HashMap containing all the tracked keys and their
	 * virtual codes<br>Used to increment the count for the
	 * keys
	 */
	private static Map<Integer, Key> keys = new HashMap<Integer, Key>();
	/**
	 * The most recent key event, only
	 * used during the initial setup
	 */
	private static GlobalKeyEvent lastevent;
	/**
	 * String containing all the tracked keys
	 * only used during the initial setup
	 */
	private static String addedkeys = "";
	/**
	 * Key configuration data, can be serialised
	 */
	private static List<KeyInformation> keyinfo = new ArrayList<KeyInformation>();
	/**
	 * Main panel used for showing all the sub panels that
	 * display all the information
	 */
	private static JPanel content = new JPanel(new GridLayout(1, 0, 2, 0));

	/**
	 * Main method
	 * @param args - No valid command line arguments for this program
	 */
	public static void main(String[] args) {		
		if(args.length == 1){
			dir = args[0];
			Runtime.getRuntime().load(args[0]);
		}
		System.out.println("Control keys:");
		System.out.println("Ctrl + P: Causes the program to reset and print the average and maximum value");
		System.out.println("Ctrl + O: Terminates the program");
		System.out.println("Ctrl + I: Causes the program to reset and print the key press statistics");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
		}
		
		//Initialise native library and register event handlers
		setupKeyboardHook();
		
		//Get a configuration for the keys
		boolean[] fields = configure();
		
		//Register the keys to monitor
		for(KeyInformation i : keyinfo){
			keys.put(i.keycode, new Key(i.name));
		}
		
		//Build GUI
		try {
			buildGUI(fields[0], fields[1], fields[2]);
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
	private static final void mainLoop(){
		while(true){
			if(System.currentTimeMillis() - last >= 1000){
				last = System.currentTimeMillis();
				if(tmp > max){
					max = tmp;
				}
				if(tmp != 0){
					avg = (avg * (double)n + (double)tmp) / ((double)n + 1.0D);
					n++;
					System.out.println("Current keys per second: " + tmp);
				}
				prev = tmp;
				tmp = 0;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			content.repaint();
		}
	}
	
	/**
	 * Registers the native libraries and
	 * registers event handlers for key
	 * press events
	 */
	private static final void setupKeyboardHook(){
		GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();
		keyboardHook.addKeyListener(new GlobalKeyAdapter() {
			
			@Override 
			public void keyPressed(GlobalKeyEvent event) {
				lastevent = event;
				if(keys.containsKey(event.getVirtualKeyCode())){
					keys.get(event.getVirtualKeyCode()).keyPressed();	
				}
				if(event.getVirtualKeyCode() == GlobalKeyEvent.VK_P && event.isControlPressed()){
					System.out.println("Reset max & avg | max: " + max + " avg: " + avg);
					n = 0;
					avg = 0;
					max = 0;
					tmp = 0;
				}else if(event.getVirtualKeyCode() == GlobalKeyEvent.VK_O && event.isControlPressed()){
					System.exit(0);
				}else if(event.getVirtualKeyCode() == GlobalKeyEvent.VK_I && event.isControlPressed()){
					System.out.print("Reset key counts | ");
					for(Key k : keys.values()){
						System.out.print(k.name + ":" + k.count + " ");
						k.count = 0;
					}
					System.out.println();
				}
			}
			
			@Override
			public void keyReleased(GlobalKeyEvent event){
				if(keys.containsKey(event.getVirtualKeyCode())){
					keys.get(event.getVirtualKeyCode()).keyReleased();
				}
			}
		});
	}

	/**
	 * Asks the user for a configuration
	 * though a series of dialogs
	 * These dialogs also provide the
	 * option of saving or loading an
	 * existing configuration
	 * @return An array consisting of 3 boolean
	 *         values. The values represent (in
	 *         the following order) whether or not
	 *         to display: the maximum keys per second,
	 *         the average keys per second, the current
	 *         keys per second
	 */
	@SuppressWarnings("unchecked")
	private static final boolean[] configure(){
		JPanel form = new JPanel(new BorderLayout());
		JPanel boxes = new JPanel(new GridLayout(3, 0));
		JPanel labels = new JPanel(new GridLayout(3, 0));
		JCheckBox cmax = new JCheckBox();
		JCheckBox cavg = new JCheckBox();
		JCheckBox ccur = new JCheckBox();
		cmax.setSelected(true);
		cavg.setSelected(true);
		ccur.setSelected(true);
		JLabel lmax = new JLabel("Show maximum: ");
		JLabel lavg = new JLabel("Show average: ");
		JLabel lcur = new JLabel("Show current: ");
		boxes.add(cmax);
		boxes.add(cavg);
		boxes.add(ccur);
		labels.add(lmax);
		labels.add(lavg);
		labels.add(lcur);
		JPanel options = new JPanel();
		labels.setPreferredSize(new Dimension((int)labels.getPreferredSize().getWidth(), (int)boxes.getPreferredSize().getHeight()));
		options.add(labels);
		options.add(boxes);
		JPanel buttons = new JPanel(new GridLayout(3, 0));
		JButton addkey = new JButton("Add key");
		JButton load = new JButton("Load config");
		JButton save = new JButton("Save config");
		buttons.add(addkey);
		buttons.add(load);
		buttons.add(save);
		form.add(options, BorderLayout.CENTER);
		options.setBorder(BorderFactory.createTitledBorder("General"));
		buttons.setBorder(BorderFactory.createTitledBorder("Config"));
		JPanel all = new JPanel(new BorderLayout());
		all.add(options, BorderLayout.LINE_START);
		all.add(buttons, BorderLayout.LINE_END);
		form.add(all, BorderLayout.CENTER);
		addkey.addActionListener((e)->{
			if(JOptionPane.showConfirmDialog(null, "Press a key and press OK to add it\nCurrently added keys: " + addedkeys, "Keys per second", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
				if(lastevent == null){
					JOptionPane.showMessageDialog(null, "No key pressed!", "Keys per second", JOptionPane.ERROR_MESSAGE);
					return;
				}
				KeyInformation info = new KeyInformation(lastevent.getKeyChar(), lastevent.getVirtualKeyCode());
				if(JOptionPane.showConfirmDialog(null, "Add the " + info.name + " key?", "Keys per second", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
					if(addedkeys.isEmpty()){
						addedkeys += info.name;
					}else{
						addedkeys += ", " + info.name;
					}
					keyinfo.add(info);
				}
			}
		});
		save.addActionListener((e)->{
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			if(chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION){
				return;
			};
			File saveloc = new File(chooser.getSelectedFile().getAbsolutePath() + ".kpsconf");
			if(!saveloc.exists() || (saveloc.exists() && JOptionPane.showConfirmDialog(null, "File already exists, overwrite?", "Keys per second", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)){
				try {
					ObjectOutputStream objout = new ObjectOutputStream(new FileOutputStream(saveloc));
					objout.writeObject(keyinfo);
					objout.flush();
					objout.close();
					JOptionPane.showMessageDialog(null, "Config succesfully saved", "Keys per second", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Failed to save the config!", "Keys per second", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		load.addActionListener((e)->{
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			if(chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION){
				return;
			}
			File saveloc = chooser.getSelectedFile();
			try {
				ObjectInputStream objin = new ObjectInputStream(new FileInputStream(saveloc));
				keyinfo = (List<KeyInformation>) objin.readObject();
				addedkeys = "";
				objin.close();
				for(KeyInformation i : keyinfo){
					if(addedkeys.isEmpty()){
						addedkeys += i.name;
					}else{
						addedkeys += ", " + i.name;
					}
				}
				JOptionPane.showMessageDialog(null, "Config succesfully loaded", "Keys per second", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null, "Failed to load the config!", "Keys per second", JOptionPane.ERROR_MESSAGE);
			}
		});
		JOptionPane.showOptionDialog(null, form, "Keys per second", 0, JOptionPane.QUESTION_MESSAGE, null, new String[]{"OK"}, 0);
		return new boolean[]{cmax.isSelected(), cavg.isSelected(), ccur.isSelected()};
	}
	
	/**
	 * Builds the main GUI of the program
	 * @param max Whether or not to display the maximum keys per second
	 * @param avg Whether or not to display the average keys per second
	 * @param cur Whether or not to display the current keys per second
	 * @throws IOException When an IO Exception occurs, this can be thrown
	 *         when the program fails the load its resources
	 */
	private static final void buildGUI(boolean max, boolean avg, boolean cur) throws IOException {
		pressed = ImageIO.read(ClassLoader.getSystemResource("hit.png"));
		unpressed = ImageIO.read(ClassLoader.getSystemResource("key.png"));
		JFrame frame = new JFrame("Keys per second");

		content.setBackground(Color.BLACK);
		for (Key k : keys.values()) {
			content.add(k.getPanel());
		}
		int extra = 0;
		if(max){
			content.add(new MaxPanel());
			extra++;
		}
		if(avg){
			content.add(new AvgPanel());
			extra++;
		}
		if(cur){
			content.add(new NowPanel());
			extra++;
		}

		frame.setSize((keys.size() + extra) * 44 + ((keys.size() + extra) - 1) * 2, 68);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(content);
		frame.setUndecorated(true);
		frame.addMouseMotionListener(new MouseMotionListener(){
			/**
			 * Previous location of the mouse on the screen
			 */
			private Point from = null;
			
			@Override
			public void mouseDragged(MouseEvent e) {
				Point to = e.getPoint();
				if(from == null){
					from = to;
					return;
				}
				JFrame dia = (JFrame)e.getSource();
				Point at = dia.getLocation();
				int x = at.x + (to.x - from.x);
				int y = at.y + (to.y - from.y);
				dia.setLocation(new Point(x, y));
			}

			@Override
			public void mouseMoved(MouseEvent e) {
			}
		});
		frame.setVisible(true);
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
		 * The {@link KeyPanel} responsible for
		 * displaying information about the tracked key
		 */
		private KeyPanel kp;
		/**
		 * The key in string form<br>
		 * For example: X
		 */
		protected final String name;

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
			return kp = new KeyPanel(this);
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
				if (count >= 1000) {
					kp.font2 = new Font("Dialog", Font.PLAIN, 14);
				}
				tmp++;
			}
		}

		/**
		 * Called when a key is released
		 */
		private void keyReleased() {
			down = false;
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
	private static final class KeyInformation implements Serializable{
		/**
		 * Serial ID
		 */
		private static final long serialVersionUID = -3752409253121094171L;
		/**
		 * The name of this key
		 * @see Key#name
		 */
		private String name;
		/**
		 * The virtual key code of this key<br>
		 * This code represents the key
		 */
		private int keycode;
		
		/**
		 * Constructs a new KeyInformation
		 * object with the given information
		 * @param name The name of the key
		 * @param code The virtual key code of the key
		 * @see #name
		 * @see #keycode
		 */
		private KeyInformation(char name, int code){
			this.name = String.valueOf(name).toUpperCase();
			this.keycode = code;
		}
	}
}
