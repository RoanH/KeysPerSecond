package me.roan.kps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
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
import java.util.List;

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

public class Main {

	private static long last = System.currentTimeMillis();
	private static long n = 0;
	private static int tmp = 0;
	private static double avg;
	private static int max;
	private static int prev;
	private static Image pressed;
	private static Image unpressed;
	private static ArrayList<Key> keys = new ArrayList<Key>();
	private static GlobalKeyEvent lastevent;
	private static String addedkeys = "";
	private static List<KeyInformation> keyinfo = new ArrayList<KeyInformation>();

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
		}
		
		GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();
		keyboardHook.addKeyListener(new GlobalKeyAdapter() {
			@Override 
			public void keyPressed(GlobalKeyEvent event) {
				lastevent = event;
				for(Key k : keys){
					if(k.keyPressed(event)){
						tmp++;
					}
				}
				if(event.getVirtualKeyCode() == GlobalKeyEvent.VK_P && event.isControlPressed()){
					System.out.println("max: " + max + " avg: " + avg);
					n = 0;
					avg = 0;
					max = 0;
					tmp = 0;
				}else if(event.getVirtualKeyCode() == GlobalKeyEvent.VK_O && event.isControlPressed()){
					System.exit(0);
				}
			}
			@Override
			public void keyReleased(GlobalKeyEvent event){
				for(Key k : keys){
					k.keyReleased(event);
				}
			}
		});
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
		
		for(KeyInformation i : keyinfo){
			keys.add(new Key(i.keycode, i.name));
		}
		
		try {
			buildGUI(cmax.isSelected(), cavg.isSelected(), ccur.isSelected());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true){
			if(System.currentTimeMillis() - last >= 1000){
				last = System.currentTimeMillis();
				if(tmp > max){
					max = tmp;
				}
				if(tmp != 0){
					avg = (avg * (double)n + (double)tmp) / ((double)n + 1.0D);
					n++;
					System.out.println(tmp);
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

	private static JPanel content = new JPanel(new GridLayout(1, 0, 2, 0));

	private static final void buildGUI(boolean max, boolean avg, boolean cur) throws IOException {
		pressed = ImageIO.read(ClassLoader.getSystemResource("hit.png"));
		unpressed = ImageIO.read(ClassLoader.getSystemResource("key.png"));
		JFrame frame = new JFrame();

		content.setBackground(Color.BLACK);
		for (Key k : keys) {
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

	private static final class Key {

		private int key;
		private boolean down = false;
		private int count = 0;
		private KeyPanel kp;
		private String name;

		private Key(int key, String name) {
			this.key = key;
			this.name = name;
		}

		private KeyPanel getPanel() {
			return kp = new KeyPanel(this);
		}

		public boolean keyPressed(GlobalKeyEvent event) {
			if (key == event.getVirtualKeyCode() && !down) {
				count++;
				down = true;
				if (count >= 1000) {
					kp.font2 = new Font("Dialog", Font.PLAIN, 14);
				}
				return true;
			}
			return false;
		}

		public void keyReleased(GlobalKeyEvent event) {
			if (key == event.getVirtualKeyCode()) {
				down = false;
			}
		}

		private static final class KeyPanel extends JPanel {
			/**
			 * 
			 */
			private static final long serialVersionUID = 8816524158873355997L;
			private Key key;
			private Font font1 = new Font("Dialog", Font.BOLD, 24);
			private Font font2 = new Font("Dialog", Font.PLAIN, 18);

			private KeyPanel(Key key) {
				this.key = key;
			}

			@Override
			public void paintComponent(Graphics g1) {
				Graphics2D g = (Graphics2D) g1;
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				g.drawImage(unpressed, 2, 2, null);
				if (key.down) {
					g.drawImage(pressed, 2, 2, null);
				}
				g.setColor(Color.CYAN);
				g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				g.setFont(font1);
				g.drawString(key.name, (this.getWidth() - g.getFontMetrics().stringWidth(key.name)) / 2, 30);
				g.setFont(font2);
				String str = String.valueOf(key.count);
				g.drawString(str, (this.getWidth() - g.getFontMetrics().stringWidth(str)) / 2, 55);
			}
		}
	}

	private static final class MaxPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8816524158873355997L;
		private Font font1 = new Font("Dialog", Font.BOLD, 15);
		private Font font2 = new Font("Dialog", Font.PLAIN, 18);

		@Override
		public void paintComponent(Graphics g1) {
			Graphics2D g = (Graphics2D) g1;
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.drawImage(unpressed, 2, 2, null);
			g.setColor(Color.CYAN);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setFont(font1);
			g.drawString("MAX", (this.getWidth() - g.getFontMetrics().stringWidth("MAX")) / 2, 30);
			g.setFont(font2);
			String str = String.valueOf(max);
			g.drawString(str, (this.getWidth() - g.getFontMetrics().stringWidth(str)) / 2, 55);
		}
	}

	private static final class AvgPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8816524158873355997L;
		private Font font1 = new Font("Dialog", Font.BOLD, 15);
		private Font font2 = new Font("Dialog", Font.PLAIN, 18);

		@Override
		public void paintComponent(Graphics g1) {
			Graphics2D g = (Graphics2D) g1;
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.drawImage(unpressed, 2, 2, null);
			g.setColor(Color.CYAN);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setFont(font1);
			g.drawString("AVG", (this.getWidth() - g.getFontMetrics().stringWidth("AVG")) / 2, 30);
			g.setFont(font2);
			String str = String.valueOf(((int) (avg * 10)) / 10);
			g.drawString(str, (this.getWidth() - g.getFontMetrics().stringWidth(str)) / 2, 55);
		}
	}

	private static final class NowPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8816524158873355997L;
		private Font font1 = new Font("Dialog", Font.BOLD, 15);
		private Font font2 = new Font("Dialog", Font.PLAIN, 18);

		@Override
		public void paintComponent(Graphics g1) {
			Graphics2D g = (Graphics2D) g1;
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.drawImage(unpressed, 2, 2, null);
			g.setColor(Color.CYAN);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setFont(font1);
			g.drawString("CUR", (this.getWidth() - g.getFontMetrics().stringWidth("CUR")) / 2, 30);
			g.setFont(font2);
			String str = String.valueOf(prev);
			g.drawString(str, (this.getWidth() - g.getFontMetrics().stringWidth(str)) / 2, 55);
		}
	}
	
	private static final class KeyInformation implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -3752409253121094171L;
		private String name;
		private int keycode;
		
		private KeyInformation(char name, int code){
			this.name = String.valueOf(name).toUpperCase();
			this.keycode = code;
		}
	}
}
