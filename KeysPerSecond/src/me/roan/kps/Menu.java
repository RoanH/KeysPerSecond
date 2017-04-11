package me.roan.kps;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import me.roan.kps.Main.Key;
import me.roan.kps.Main.KeyInformation;

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
	 * Menu component
	 */
	private static final List<JMenuItem> components = new ArrayList<JMenuItem>();
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
	 * Sets the foreground and background
	 * color of the popup menu
	 */
	protected static final void repaint(){
		menu.setForeground(Main.config.getForegroundColor());
		configure.setForeground(Main.config.getForegroundColor());
		general.setForeground(Main.config.getForegroundColor());
		precision.setForeground(Main.config.getForegroundColor());
		configcolors.setForeground(Main.config.getForegroundColor());
		mgraph.setForeground(Main.config.getForegroundColor());
		rate.setForeground(Main.config.getForegroundColor());
		
		menu.setBackground(Main.config.getBackgroundColor());
		configure.setBackground(Main.config.getBackgroundColor());
		general.setBackground(Main.config.getBackgroundColor());
		precision.setBackground(Main.config.getBackgroundColor());
		configcolors.setBackground(Main.config.getBackgroundColor());
		mgraph.setBackground(Main.config.getBackgroundColor());
		rate.setBackground(Main.config.getBackgroundColor());
		
		for(JMenuItem item : components){
			item.setForeground(Main.config.getForegroundColor());
			item.setBackground(Main.config.getBackgroundColor());
		}
		
		menu.setBorder(BorderFactory.createLineBorder(Main.config.getForegroundColor()));
		general.getPopupMenu().setBorder(BorderFactory.createLineBorder(Main.config.getForegroundColor()));
		precision.getPopupMenu().setBorder(BorderFactory.createLineBorder(Main.config.getForegroundColor()));
		configure.getPopupMenu().setBorder(BorderFactory.createLineBorder(Main.config.getForegroundColor()));
		configcolors.getPopupMenu().setBorder(BorderFactory.createLineBorder(Main.config.getForegroundColor()));
		mgraph.getPopupMenu().setBorder(BorderFactory.createLineBorder(Main.config.getForegroundColor()));
		rate.getPopupMenu().setBorder(BorderFactory.createLineBorder(Main.config.getForegroundColor()));
	}

	/**
	 * Creates the popup menu
	 */
	protected static final void createMenu(){
		JMenuItem size = new JMenuItem("Size");
		JMenuItem snap = new JMenuItem("Snap to edges");
		JMenuItem exit = new JMenuItem("Exit");
		JMenuItem pause = new JMenuItem("Pause/resume");
		JMenuItem sreset = new JMenuItem("Reset stats");
		JMenuItem treset = new JMenuItem("Reset totals");
		JMenuItem configkeys = new JMenuItem("Keys");
		JMenuItem colorcustom = new JMenuItem("Configure colours");
		JMenuItem backlog = new JMenuItem("Backlog");
		JCheckBoxMenuItem colorenable = new JCheckBoxMenuItem("Enable custom colours");
		JCheckBoxMenuItem tAll = new JCheckBoxMenuItem("Track all keys");
		JCheckBoxMenuItem overlay = new JCheckBoxMenuItem("Overlay osu!");
		JCheckBoxMenuItem p0 = new JCheckBoxMenuItem("No digits beyond the decimal point");
		JCheckBoxMenuItem p1 = new JCheckBoxMenuItem("1 digit beyond the decimal point");
		JCheckBoxMenuItem p2 = new JCheckBoxMenuItem("2 digits beyond the decimal point");
		JCheckBoxMenuItem p3 = new JCheckBoxMenuItem("3 digits beyond the decimal point");
		JCheckBoxMenuItem max = new JCheckBoxMenuItem("Show max");
		JCheckBoxMenuItem avg = new JCheckBoxMenuItem("Show average");
		JCheckBoxMenuItem cur = new JCheckBoxMenuItem("Show current");
		JCheckBoxMenuItem graph = new JCheckBoxMenuItem("Enable graph");
		JCheckBoxMenuItem keys = new JCheckBoxMenuItem("Show keys");
		JCheckBoxMenuItem graphavg = new JCheckBoxMenuItem("Show average");
		JCheckBoxMenuItem[] rates = new JCheckBoxMenuItem[12];
		JMenuItem save = new JMenuItem("Save config");
		JMenuItem load = new JMenuItem("Load config");
		components.add(load);
		components.add(save);
		components.add(size);
		components.add(snap);
		components.add(exit);
		components.add(pause);
		components.add(sreset);
		components.add(treset);
		components.add(configkeys);
		components.add(colorcustom);
		components.add(backlog);
		components.add(colorenable);
		components.add(tAll);
		components.add(overlay);
		components.add(p0);
		components.add(p1);
		components.add(p2);
		components.add(p3);
		components.add(max);
		components.add(avg);
		components.add(cur);
		components.add(graph);
		components.add(keys);
		components.add(graphavg);
		menu.setOpaque(true);
		configure.setOpaque(true);
		general.setOpaque(true);
		precision.setOpaque(true);
		configcolors.setOpaque(true);
		mgraph.setOpaque(true);
		rate.setOpaque(true);
		for(JMenuItem e : components){
			e.setOpaque(true);
		}
		snap.addActionListener((e)->{
			Point loc = Main.frame.getLocationOnScreen();
			Rectangle bounds = Main.frame.getGraphicsConfiguration().getBounds();	
			Main.frame.setLocation(Math.abs(loc.x - bounds.x) < 100 ? bounds.x : 
							  Math.abs((loc.x + Main.frame.getWidth()) - (bounds.x + bounds.width)) < 100 ? bounds.x + bounds.width - Main.frame.getWidth() : loc.x, 
							  Math.abs(loc.y - bounds.y) < 100 ? bounds.y : 
							  Math.abs((loc.y + Main.frame.getHeight()) - (bounds.y + bounds.height)) < 100 ? bounds.y + bounds.height - Main.frame.getHeight() : loc.y);
		});
		exit.addActionListener((e)->{
			Main.exit();
		});
		pause.addActionListener((e)->{
			Main.suspended = !Main.suspended;
		});
		sreset.addActionListener((e)->{
			Main.resetStats();
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
				for(KeyInformation info : Main.config.keyinfo){
					if(info.keycode == key.getKey()){
						continue;
					}
				}
				iter.remove();
			}
		});
		overlay.setSelected(Main.config.overlay);
		overlay.addActionListener((e)->{
			Main.config.overlay = overlay.isSelected();
			Main.frame.setAlwaysOnTop(Main.config.overlay);
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
			Main.configureColors();
			Main.reconfigure();
		});
		colorenable.setSelected(Main.config.customColors);
		colorenable.addActionListener((e)->{
			Main.config.customColors = colorenable.isSelected();
			Main.reconfigure();
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
		rates[0] = new JCheckBoxMenuItem("1000ms", Main.config.updateRate == 1000);
		rates[0].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.config.updateRate = 1000;
			Main.mainLoop();
			rates[0].setSelected(true);
		});
		rate.add(rates[0]);
		rates[1] = new JCheckBoxMenuItem("500ms", Main.config.updateRate == 500);
		rates[1].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.config.updateRate = 500;
			Main.mainLoop();
			rates[1].setSelected(true);
		});
		rate.add(rates[1]);
		rates[2] = new JCheckBoxMenuItem("250ms", Main.config.updateRate == 250);
		rates[2].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.config.updateRate = 250;
			Main.mainLoop();
			rates[2].setSelected(true);
		});
		rate.add(rates[2]);
		rates[3] = new JCheckBoxMenuItem("200ms", Main.config.updateRate == 200);
		rates[3].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.config.updateRate = 200;
			Main.mainLoop();
			rates[3].setSelected(true);
		});
		rate.add(rates[3]);
		rates[4] = new JCheckBoxMenuItem("125ms", Main.config.updateRate == 125);
		rates[4].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.config.updateRate = 125;
			Main.mainLoop();
			rates[4].setSelected(true);
		});
		rate.add(rates[4]);
		rates[5] = new JCheckBoxMenuItem("100ms", Main.config.updateRate == 100);
		rates[5].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.config.updateRate = 100;
			Main.mainLoop();
			rates[5].setSelected(true);
		});
		rate.add(rates[5]);
		rates[6] = new JCheckBoxMenuItem("50ms", Main.config.updateRate == 50);
		rates[6].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.config.updateRate = 50;
			Main.mainLoop();
			rates[6].setSelected(true);
		});
		rate.add(rates[6]);
		rates[7] = new JCheckBoxMenuItem("25ms", Main.config.updateRate == 25);
		rates[7].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.config.updateRate = 25;
			Main.mainLoop();
			rates[7].setSelected(true);
		});
		rate.add(rates[7]);
		rates[8] = new JCheckBoxMenuItem("20ms", Main.config.updateRate == 20);
		rates[8].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.config.updateRate = 20;
			Main.mainLoop();
			rates[8].setSelected(true);
		});
		rate.add(rates[8]);
		rates[9] = new JCheckBoxMenuItem("10ms", Main.config.updateRate == 10);
		rates[9].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.config.updateRate = 10;
			Main.mainLoop();
			rates[9].setSelected(true);
		});
		rate.add(rates[9]);
		rates[10] = new JCheckBoxMenuItem("5ms", Main.config.updateRate == 5);
		rates[10].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.config.updateRate = 5;
			Main.mainLoop();
			rates[10].setSelected(true);
		});
		rate.add(rates[10]);
		rates[11] = new ColoredMenuItem("1ms", Main.config.updateRate == 1);
		rates[11].addActionListener((e)->{
			for(JCheckBoxMenuItem item : rates){
				item.setSelected(false);
			}
			Main.config.updateRate = 1;
			Main.mainLoop();
			rates[11].setSelected(true);
		});
		rate.add(rates[11]);
		for(JMenuItem e : rates){
			components.add(e);
			e.setOpaque(true);
		}
		save.addActionListener((e)->{
			Main.config.saveConfig();
		});
		load.addActionListener((e)->{
			double oldScale = Main.config.size;
			if(Configuration.loadConfiguration()){
				menu.removeAll();
				configure.removeAll();
				general.removeAll();
				precision.removeAll();
				configcolors.removeAll();
				mgraph.removeAll();
				rate.removeAll();
				createMenu();
				SizeManager.scale(1.0D / oldScale);
				Main.reconfigure();
				Main.mainLoop();
			}
		});

		mgraph.add(graph);
		mgraph.add(graphavg);
		mgraph.add(backlog);
		
		general.add(max);
		general.add(avg);
		general.add(cur);
		general.add(keys);
		general.add(overlay);
		general.add(tAll);
		
		configure.add(general);
		configure.add(configkeys);
		configure.add(mgraph);
		configure.add(rate);
		configure.add(configcolors);
		configure.add(precision);
		configure.add(size);
		
		menu.add(configure);
		menu.add(snap);
		menu.add(treset);
		menu.add(sreset);
		menu.add(pause);
		menu.add(load);
		menu.add(save);
		menu.add(exit);
	}
	
	private static final class ColoredMenuItem extends JCheckBoxMenuItem{
		/**
		 * Serial ID
		 */
		private static final long serialVersionUID = 2897377593905514680L;

		private ColoredMenuItem(String txt){
			super(txt);
		}
		
		private ColoredMenuItem(String txt, boolean selected){
			super(txt, selected);
		}
		
		@Override
		protected void paintComponent(Graphics g){
			super.paintComponent(g);//TODO proper icon loading & focus handling
			g.setColor(Main.config.getBackgroundColor());
			g.fillRect(0, 0, this.getHeight(), this.getHeight());
			g.drawImage(ColorManager.checkmark, 0, 0, this.getHeight(), this.getHeight(), 0, 0, 100, 100, this);
		}
	}
}
