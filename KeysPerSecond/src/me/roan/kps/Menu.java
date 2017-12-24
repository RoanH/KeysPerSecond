package me.roan.kps;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.plaf.basic.BasicMenuUI;

import me.roan.kps.Main.Key;
import me.roan.kps.Main.KeyInformation;

import sun.swing.SwingUtilities2;

/**
 * This class handles everything related to
 * the popup menus
 * @author Roan
 */
public class Menu {
	/**
	 * The right click menu
	 */
	protected static final JPopupMenu menu = new JPopupMenu();
	/**
	 * The configuration menu
	 */
	private static final JMenu configure = new JMenu("Configure");
	/**
	 * The general menu
	 */
	private static final JMenu general = new JMenu("General");
	/**
	 * The graph menu
	 */
	private static final JMenu mgraph = new JMenu("Graph");
	/**
	 * Precision menu
	 */
	private static final JMenu precision = new JMenu("Precision");
	/**
	 * Custom colors menu
	 */
	private static final JMenu configcolors = new JMenu("Colours");
	/**
	 * Update rate menu
	 */
	private static final JMenu rate = new JMenu("Update rate");
	/**
	 * Reset menu
	 */
	private static final JMenu reset = new JMenu("Reset");
	/**
	 * Load/Save menu
	 */
	private static final JMenu saveLoad = new JMenu("Save / Load");
	/**
	 * The pause menu item
	 */
	protected static final JCheckBoxMenuItem pause = new JCheckBoxMenuItem("Pause");
	
	/**
	 * Repaints the component border
	 */
	protected static final void repaint(){
		Border border = BorderFactory.createLineBorder(Main.config.customColors ? Main.config.foreground : Color.CYAN);
		menu.setBorder(border);
		configure.getPopupMenu().setBorder(border);
		general.getPopupMenu().setBorder(border);
		precision.getPopupMenu().setBorder(border);
		configcolors.getPopupMenu().setBorder(border);
		mgraph.getPopupMenu().setBorder(border);
		rate.getPopupMenu().setBorder(border);
		reset.getPopupMenu().setBorder(border);
		saveLoad.getPopupMenu().setBorder(border);
	}

	/**
	 * Creates the popup menu
	 */
	protected static final void createMenu(){
		List<JMenuItem> components = new ArrayList<JMenuItem>();
		JMenuItem size = new JMenuItem("Size");
		JMenuItem snap = new JMenuItem("Snap to edges");
		JMenuItem exit = new JMenuItem("Exit");
		JMenuItem sreset = new JMenuItem("Reset statistics");
		JMenuItem sresetmax = new JMenuItem("Reset maximum");
		JMenuItem sresetavg = new JMenuItem("Reset average");
		JMenuItem sresettot = new JMenuItem("Reset total");
		JMenuItem sresetgraph = new JMenuItem("Reset graph");
		JMenuItem treset = new JMenuItem("Reset key totals");
		JMenuItem configkeys = new JMenuItem("Keys");
		JMenuItem colorcustom = new JMenuItem("Configure colours");
		JMenuItem backlog = new JMenuItem("Backlog");
		JMenuItem commandkeys = new JMenuItem("Commands");
		JMenuItem layout = new JMenuItem("Layout");
		JCheckBoxMenuItem colorenable = new JCheckBoxMenuItem("Enable custom colours");
		JCheckBoxMenuItem tAll = new JCheckBoxMenuItem("Track all keys");
		JCheckBoxMenuItem overlay = new JCheckBoxMenuItem("Overlay mode");
		JCheckBoxMenuItem p0 = new JCheckBoxMenuItem("No digits beyond the decimal point");
		JCheckBoxMenuItem p1 = new JCheckBoxMenuItem("1 digit beyond the decimal point");
		JCheckBoxMenuItem p2 = new JCheckBoxMenuItem("2 digits beyond the decimal point");
		JCheckBoxMenuItem p3 = new JCheckBoxMenuItem("3 digits beyond the decimal point");
		JCheckBoxMenuItem max = new JCheckBoxMenuItem("Show max");
		JCheckBoxMenuItem avg = new JCheckBoxMenuItem("Show average");
		JCheckBoxMenuItem cur = new JCheckBoxMenuItem("Show current");
		JCheckBoxMenuItem tot = new JCheckBoxMenuItem("Show total");
		JCheckBoxMenuItem graph = new JCheckBoxMenuItem("Enable graph");
		JCheckBoxMenuItem keys = new JCheckBoxMenuItem("Show keys");
		JCheckBoxMenuItem graphavg = new JCheckBoxMenuItem("Show average");
		JCheckBoxMenuItem[] rates = new JCheckBoxMenuItem[12];
		JCheckBoxMenuItem modifiers = new JCheckBoxMenuItem("Key-modifier tracking");
		JMenuItem save = new JMenuItem("Save config");
		JMenuItem load = new JMenuItem("Load config");
		JMenuItem saveStats = new JMenuItem("Save stats");
		JMenuItem loadStats = new JMenuItem("Load stats");
		components.add(saveStats);
		components.add(loadStats);
		components.add(load);
		components.add(layout);
		components.add(save);
		components.add(size);
		components.add(snap);
		components.add(exit);
		components.add(pause);
		components.add(sreset);
		components.add(sresetmax);
		components.add(sresetavg);
		components.add(sresettot);
		components.add(sresetgraph);
		components.add(treset);
		components.add(configkeys);
		components.add(colorcustom);
		components.add(backlog);
		components.add(colorenable);
		components.add(tAll);
		components.add(overlay);
		components.add(commandkeys);
		components.add(p0);
		components.add(p1);
		components.add(p2);
		components.add(p3);
		components.add(max);
		components.add(avg);
		components.add(cur);
		components.add(tot);
		components.add(graph);
		components.add(keys);
		components.add(graphavg);
		components.add(modifiers);
		configure.setUI(new MenuUI());
		general.setUI(new MenuUI());
		precision.setUI(new MenuUI());
		configcolors.setUI(new MenuUI());
		mgraph.setUI(new MenuUI());
		rate.setUI(new MenuUI());
		reset.setUI(new MenuUI());
		saveLoad.setUI(new MenuUI());
		for(JMenuItem e : components){
			e.setUI(new MenuItemUI());
		}
		snap.addActionListener((e)->{
			JFrame frame = (JFrame) KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
			Point loc = frame.getLocationOnScreen();
			Rectangle bounds = frame.getGraphicsConfiguration().getBounds();	
			frame.setLocation(Math.abs(loc.x - bounds.x) < 100 ? bounds.x : 
				Math.abs((loc.x + frame.getWidth()) - (bounds.x + bounds.width)) < 100 ? bounds.x + bounds.width - frame.getWidth() : loc.x, 
						Math.abs(loc.y - bounds.y) < 100 ? bounds.y : 
							Math.abs((loc.y + frame.getHeight()) - (bounds.y + bounds.height)) < 100 ? bounds.y + bounds.height - frame.getHeight() : loc.y);
		});
		exit.addActionListener((e)->{
			Main.exit();
		});
		pause.setSelected(Main.suspended);
		pause.addActionListener((e)->{
			Main.suspended = pause.isSelected();
		});
		sreset.addActionListener((e)->{
			Main.resetStats();
		});
		sresetmax.addActionListener((e)->{
			Main.max = 0;
		});
		sresetavg.addActionListener((e)->{
			Main.n = 0;
			Main.avg = 0;
			Main.tmp.set(0);
		});
		sresettot.addActionListener((e)->{
			TotPanel.hits = 0;
		});
		sresetgraph.addActionListener((e)->{
			Main.graph.reset();
		});
		commandkeys.addActionListener((e)->{
			Main.configureCommandKeys();
		});
		treset.addActionListener((e)->{
			Main.resetTotals();
		});
		tAll.setSelected(Main.config.trackAll);
		tAll.addActionListener((e)->{
			Main.config.trackAll = tAll.isSelected();
			Iterator<Entry<Integer, Key>> iter = Main.keys.entrySet().iterator();
			while(iter.hasNext()){
				Entry<Integer, Key> key = iter.next();
				boolean remove = true;
				for(KeyInformation info : Main.config.keyinfo){
					if(info.keycode == key.getKey()){
						remove = false;
					}
				}
				if(remove){
					iter.remove();
				}
			}
		});
		overlay.setSelected(Main.config.overlay);
		overlay.addActionListener((e)->{
			Main.config.overlay = overlay.isSelected();
			Main.frame.setAlwaysOnTop(Main.config.overlay);
			Main.graphFrame.setAlwaysOnTop(Main.config.overlay);
		});
		precision.add(p0);
		precision.add(p1);
		precision.add(p2);
		precision.add(p3);
		p0.addActionListener((e)->{
			Main.config.precision = 0;
			p0.setSelected(true);
			p1.setSelected(false);
			p2.setSelected(false);
			p3.setSelected(false);
		});
		p1.addActionListener((e)->{
			Main.config.precision = 1;
			p0.setSelected(false);
			p1.setSelected(true);
			p2.setSelected(false);
			p3.setSelected(false);
		});
		p2.addActionListener((e)->{
			Main.config.precision = 2;
			p0.setSelected(false);
			p1.setSelected(false);
			p2.setSelected(true);
			p3.setSelected(false);
		});
		p3.addActionListener((e)->{
			Main.config.precision = 3;
			p0.setSelected(false);
			p1.setSelected(false);
			p2.setSelected(false);
			p3.setSelected(true);
		});
		switch(Main.config.precision){
		case 0:
			p0.setSelected(true);
			break;
		case 1:
			p1.setSelected(true);
			break;
		case 2:
			p2.setSelected(true);
			break;
		case 3:
			p3.setSelected(true);
			break;
		}
		max.setSelected(Main.config.showMax);
		max.addActionListener((e)->{
			Main.config.showMax = max.isSelected();
			Main.reconfigure();
		});
		avg.setSelected(Main.config.showAvg);
		avg.addActionListener((e)->{
			Main.config.showAvg = avg.isSelected();
			Main.reconfigure();
		});
		cur.setSelected(Main.config.showCur);
		cur.addActionListener((e)->{
			Main.config.showCur = cur.isSelected();
			Main.reconfigure();
		});
		tot.setSelected(Main.config.showTotal);
		tot.addActionListener((e)->{
			Main.config.showTotal = tot.isSelected();
			Main.reconfigure();
		});
		keys.setSelected(Main.config.showKeys);
		keys.addActionListener((e)->{
			Main.config.showKeys = keys.isSelected();
			Main.reconfigure();
		});
		graph.setSelected(Main.config.showGraph);
		graph.addActionListener((e)->{
			Main.config.showGraph = graph.isSelected();
			Main.reconfigure();
		});
		configkeys.addActionListener((e)->{
			Main.configureKeys();
			Main.reconfigure();
		});
		colorcustom.addActionListener((e)->{
			SwingUtilities.invokeLater(()->{
				Main.configureColors();
				Main.reconfigure();
			});
		});
		colorenable.setSelected(Main.config.customColors);
		colorenable.addActionListener((e)->{
			SwingUtilities.invokeLater(()->{
				Main.config.customColors = colorenable.isSelected();
				Main.reconfigure();
			});
		});
		modifiers.setSelected(Main.config.enableModifiers);
		modifiers.addActionListener((e)->{
			Main.config.enableModifiers = modifiers.isSelected();
		});
		configcolors.add(colorenable);
		configcolors.add(colorcustom);
		graphavg.setSelected(Main.config.graphAvg);
		graphavg.addActionListener((e)->{
			Main.config.graphAvg = graphavg.isSelected();
		});
		backlog.addActionListener((e)->{
			JPanel pconfig = new JPanel();
			JSpinner sbacklog = new JSpinner(new SpinnerNumberModel(Main.config.backlog, 1, Integer.MAX_VALUE, 1));
			JLabel lbacklog;
			if(Main.config.updateRate != 1000){
				lbacklog = new JLabel("Backlog (seconds / " + (1000 / Main.config.updateRate) + "): ");
			}else{
				lbacklog = new JLabel("Backlog (seconds): ");
			}
			pconfig.add(lbacklog);
			pconfig.add(sbacklog);
			JOptionPane.showMessageDialog(null, pconfig, "Keys per second", JOptionPane.QUESTION_MESSAGE, null);
			Main.config.backlog = (int)sbacklog.getValue();
		});
		size.addActionListener((e)->{
			double old = Main.config.size;
			Main.configureSize();
			SizeManager.scale(Main.config.size / old);
			Main.reconfigure();
		});
		layout.addActionListener((e)->{
			RenderingMode m = Main.config.mode;
			Main.configureLayout();
			SizeManager.setLayoutMode(m, Main.config.mode);
			Main.reconfigure();
		});
		rates[0] = new JCheckBoxMenuItem("1000ms", Main.config.updateRate == 1000);
		rates[0].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.changeUpdateRate(1000);
			rates[0].setSelected(true);
		});
		rate.add(rates[0]);
		rates[1] = new JCheckBoxMenuItem("500ms", Main.config.updateRate == 500);
		rates[1].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.changeUpdateRate(500);
			rates[1].setSelected(true);
		});
		rate.add(rates[1]);
		rates[2] = new JCheckBoxMenuItem("250ms", Main.config.updateRate == 250);
		rates[2].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.changeUpdateRate(250);
			rates[2].setSelected(true);
		});
		rate.add(rates[2]);
		rates[3] = new JCheckBoxMenuItem("200ms", Main.config.updateRate == 200);
		rates[3].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.changeUpdateRate(200);
			rates[3].setSelected(true);
		});
		rate.add(rates[3]);
		rates[4] = new JCheckBoxMenuItem("125ms", Main.config.updateRate == 125);
		rates[4].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.changeUpdateRate(125);
			rates[4].setSelected(true);
		});
		rate.add(rates[4]);
		rates[5] = new JCheckBoxMenuItem("100ms", Main.config.updateRate == 100);
		rates[5].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.changeUpdateRate(100);
			rates[5].setSelected(true);
		});
		rate.add(rates[5]);
		rates[6] = new JCheckBoxMenuItem("50ms", Main.config.updateRate == 50);
		rates[6].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.changeUpdateRate(50);
			rates[6].setSelected(true);
		});
		rate.add(rates[6]);
		rates[7] = new JCheckBoxMenuItem("25ms", Main.config.updateRate == 25);
		rates[7].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.changeUpdateRate(25);
			rates[7].setSelected(true);
		});
		rate.add(rates[7]);
		rates[8] = new JCheckBoxMenuItem("20ms", Main.config.updateRate == 20);
		rates[8].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.changeUpdateRate(20);
			rates[8].setSelected(true);
		});
		rate.add(rates[8]);
		rates[9] = new JCheckBoxMenuItem("10ms", Main.config.updateRate == 10);
		rates[9].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.changeUpdateRate(10);
			rates[9].setSelected(true);
		});
		rate.add(rates[9]);
		rates[10] = new JCheckBoxMenuItem("5ms", Main.config.updateRate == 5);
		rates[10].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.changeUpdateRate(5);
			rates[10].setSelected(true);
		});
		rate.add(rates[10]);
		rates[11] = new JCheckBoxMenuItem("1ms", Main.config.updateRate == 1);
		rates[11].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.changeUpdateRate(1);
			rates[11].setSelected(true);
		});
		rate.add(rates[11]);
		for(JMenuItem e : rates){
			components.add(e);
			e.setUI(new MenuItemUI());
		}
		save.addActionListener((e)->{
			Main.config.saveConfig(true);
		});
		load.addActionListener((e)->{
			double oldScale = Main.config.size;
			if(Configuration.loadConfiguration()){
				resetData(oldScale);
			}
		});
		saveStats.addActionListener((e)->{
			Main.saveStats();
		});
		loadStats.addActionListener((e)->{
			Main.loadStats();
		});
		
		reset.add(treset);
		reset.add(sreset);
		reset.add(sresetmax);
		reset.add(sresetavg);
		reset.add(sresettot);
		reset.add(sresetgraph);

		mgraph.add(graph);
		mgraph.add(graphavg);
		mgraph.add(backlog);

		general.add(max);
		general.add(avg);
		general.add(cur);
		general.add(tot);
		general.add(keys);
		general.add(overlay);
		general.add(tAll);
		general.add(modifiers);

		configure.add(general);
		configure.add(configkeys);
		configure.add(mgraph);
		configure.add(rate);
		configure.add(configcolors);
		configure.add(precision);
		configure.add(size);
		configure.add(commandkeys);
		configure.add(layout);
		
		saveLoad.add(load);
		saveLoad.add(save);
		saveLoad.add(loadStats);
		saveLoad.add(saveStats);

		menu.add(configure);
		menu.add(snap);
		menu.add(reset);
		menu.add(pause);
		menu.add(saveLoad);
		menu.add(exit);
	}

	/**
	 * Applies a new configuration to the program
	 */
	protected static final void resetData(double oldScale){
		menu.removeAll();
		configure.removeAll();
		general.removeAll();
		precision.removeAll();
		configcolors.removeAll();
		mgraph.removeAll();
		rate.removeAll();
		reset.removeAll();
		saveLoad.removeAll();
		createMenu();
		SizeManager.scale(Main.config.size / oldScale);
		Main.keys.clear();
		Main.resetStats();
		Main.reconfigure();
		Main.mainLoop();
		Main.graphFrame.setAlwaysOnTop(Main.config.overlay);
		Main.frame.setAlwaysOnTop(Main.config.overlay);
	}

	/**
	 * UI for JMenus
	 * @author Roan
	 */
	private static final class MenuUI extends BasicMenuUI implements MouseListener, PopupMenuListener{
		/**
		 * Whether or not the component currently has focus
		 */
		private boolean hasCursor = false;

		@Override
		public void installUI(JComponent c) {
			super.installUI(c);
			this.menuItem.addMouseListener(this);
			menu.addPopupMenuListener(this);
		}

		@Override
		public Dimension getPreferredMenuItemSize(JComponent c, Icon checkIcon, Icon arrowIcon, int defaultTextIconGap){
			Dimension calc = super.getPreferredMenuItemSize(c, checkIcon, arrowIcon, defaultTextIconGap);
			return new Dimension(calc.width, calc.height < 22 ? 22 : calc.height);
		}

		@Override
		public void paintMenuItem(Graphics g, JComponent c, Icon checkIcon, Icon arrowIcon, Color bg, Color fg, int defaultTextIconGap){
			paintMenuItem((Graphics2D) g, menuItem, hasCursor, defaultTextIconGap);
		}
		
		/**
		 * Paints a menu item
		 * @param g The graphics to use
		 * @param menuItem The menu item to paint
		 * @param hasCursor Whether or not the cursor is over the component
		 * @param defaultTextIconGap The gap between the text and the icon
		 */
		private static final void paintMenuItem(Graphics2D g, JMenuItem menuItem, boolean hasCursor, int defaultTextIconGap){
			g.setColor(Main.config.customColors ? Main.config.background : Color.BLACK);
			g.fillRect(0, 0, menuItem.getWidth(), menuItem.getHeight());
			if(menuItem instanceof JCheckBoxMenuItem && menuItem.isSelected()){
				g.drawImage(ColorManager.checkmark, 0, 0, 22, 22, 0, 0, 100, 100, menuItem);
			}else if(menuItem instanceof JMenu){
				g.drawImage(ColorManager.arrow, menuItem.getWidth() - 12, 5, menuItem.getWidth(), 17, 0, 0, 128, 128, menuItem);
			}
			g.setColor(Main.config.customColors ? Main.config.foreground : Color.CYAN);
			if(hasCursor){
				g.drawLine(0, 0, menuItem.getWidth(), 0);
				g.drawLine(0, menuItem.getHeight() - 1, menuItem.getWidth(), menuItem.getHeight() - 1);
				Composite prev = g.getComposite();
				g.setComposite(MenuItemUI.mode);
				g.fillRect(0, 0, menuItem.getWidth(), menuItem.getHeight());
				g.setComposite(prev);
			}
			FontMetrics fm = SwingUtilities2.getFontMetrics(menuItem, g);
			int mnemIndex = menuItem.getDisplayedMnemonicIndex();
			int y = (22 - fm.getHeight()) / 2;
			SwingUtilities2.drawStringUnderlineCharAt(menuItem, g, menuItem.getText(), mnemIndex, 22 + defaultTextIconGap, y + fm.getAscent());
		}

		@Override
		public void mouseClicked(MouseEvent e) {			
		}

		@Override
		public void mousePressed(MouseEvent e) {			
		}

		@Override
		public void mouseReleased(MouseEvent e) {			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			hasCursor = true;	
		}

		@Override
		public void mouseExited(MouseEvent e) {
			hasCursor = false;	
		}

		@Override
		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		}

		@Override
		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			hasCursor = false;
		}

		@Override
		public void popupMenuCanceled(PopupMenuEvent e) {
			hasCursor = false;
		}
	}

	/**
	 * UI for JMenuItems
	 * @author Roan
	 */
	private static final class MenuItemUI extends BasicMenuItemUI implements MouseListener, PopupMenuListener{
		/**
		 * Whether or not the component currently has a mouse over it
		 */
		private boolean hasCursor = false;
		/**
		 * Opaque selection painting color1
		 */
		private static final AlphaComposite mode = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1F);

		@Override
		public void installUI(JComponent c) {
			super.installUI(c);
			this.menuItem.addMouseListener(this);
			menu.addPopupMenuListener(this);
		}

		@Override
		public Dimension getPreferredMenuItemSize(JComponent c, Icon checkIcon, Icon arrowIcon, int defaultTextIconGap){
			Dimension calc = super.getPreferredMenuItemSize(c, checkIcon, arrowIcon, defaultTextIconGap);
			return new Dimension(calc.width, calc.height < 22 ? 22 : calc.height);
		}

		@Override
		public void paintMenuItem(Graphics g, JComponent c, Icon checkIcon, Icon arrowIcon, Color bg, Color fg, int defaultTextIconGap){
			MenuUI.paintMenuItem((Graphics2D) g, menuItem, hasCursor, defaultTextIconGap);
		}

		@Override
		public void mouseClicked(MouseEvent e) {			
		}

		@Override
		public void mousePressed(MouseEvent e) {			
		}

		@Override
		public void mouseReleased(MouseEvent e) {			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			hasCursor = true;			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			hasCursor = false;		
		}
		
		@Override
		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		}

		@Override
		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			hasCursor = false;
		}

		@Override
		public void popupMenuCanceled(PopupMenuEvent e) {
			hasCursor = false;
		}
	}
}
