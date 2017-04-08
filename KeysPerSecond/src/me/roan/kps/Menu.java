package me.roan.kps;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import me.roan.kps.Main.Key;
import me.roan.kps.Main.KeyInformation;

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
	 * Precision menu
	 */
	private static final JMenu precision = new JMenu("Precision");
	/**
	 * Custom colors menu
	 */
	private static final JMenu configcolors = new JMenu("Colours");
	/**
	 * Snap program to screen edges
	 */
	private static final JMenuItem snap = new JMenuItem("Snap to edges");
	/**
	 * Exit the program
	 */
	private static final JMenuItem exit = new JMenuItem("Exit");
	/**
	 * Pause the program
	 */
	private static final JMenuItem pause = new JMenuItem("Pause/resume");
	/**
	 * Reset stats
	 */
	private static final JMenuItem sreset = new JMenuItem("Reset stats");
	/**
	 * Reset totals
	 */
	private static final JMenuItem treset = new JMenuItem("Reset totals");
	/**
	 * The key menu
	 */
	private static final JMenuItem configkeys = new JMenuItem("Keys");
	/**
	 * The custom colors menu
	 */
	private static final JMenuItem colorcustom = new JMenuItem("Configure colours");
	/**
	 * Whether or not to use custom colors
	 */
	private static final JCheckBoxMenuItem colorenable = new JCheckBoxMenuItem("Enable custom colours");
	/**
	 * Whether or not to track all keys
	 */
	private static final JCheckBoxMenuItem tAll = new JCheckBoxMenuItem("Track all keys");
	/**
	 * Whether or not to overlay osu!
	 */
	private static final JCheckBoxMenuItem overlay = new JCheckBoxMenuItem("Overlay osu!");
	/**
	 * Precision of 0
	 */
	private static final JCheckBoxMenuItem p0 = new JCheckBoxMenuItem("No digits beyond the decimal point");
	/**
	 * Precision of 1
	 */
	private static final JCheckBoxMenuItem p1 = new JCheckBoxMenuItem("1 digit beyond the decimal point");
	/**
	 * Precision of 2
	 */
	private static final JCheckBoxMenuItem p2 = new JCheckBoxMenuItem("2 digits beyond the decimal point");
	/**
	 * Precision of 3
	 */
	private static final JCheckBoxMenuItem p3 = new JCheckBoxMenuItem("3 digits beyond the decimal point");
	/**
	 * Whether or not to show the max
	 */
	private static final JCheckBoxMenuItem max = new JCheckBoxMenuItem("Show max");
	/**
	 * Whether or not to show the average
	 */
	private static final JCheckBoxMenuItem avg = new JCheckBoxMenuItem("Show average");
	/**
	 * Whether or not to show current
	 */
	private static final JCheckBoxMenuItem cur = new JCheckBoxMenuItem("Show current");
	/**
	 * Whether or not to show the graph
	 */
	private static final JCheckBoxMenuItem graph = new JCheckBoxMenuItem("Show graph");
	/**
	 * Whether or not to show keys
	 */
	private static final JCheckBoxMenuItem keys = new JCheckBoxMenuItem("Show keys");
	
	/**
	 * Sets the foreground and background
	 * color of the popup menu
	 */
	protected static final void repaint(){
		menu.setForeground(Main.config.foreground);
		configure.setForeground(Main.config.foreground);
		general.setForeground(Main.config.foreground);
		precision.setForeground(Main.config.foreground);
		snap.setForeground(Main.config.foreground);
		exit.setForeground(Main.config.foreground);
		pause.setForeground(Main.config.foreground);
		sreset.setForeground(Main.config.foreground);
		treset.setForeground(Main.config.foreground);
		tAll.setForeground(Main.config.foreground);
		overlay.setForeground(Main.config.foreground);
		p0.setForeground(Main.config.foreground);
		p1.setForeground(Main.config.foreground);
		p2.setForeground(Main.config.foreground);
		p3.setForeground(Main.config.foreground);
		max.setForeground(Main.config.foreground);
		avg.setForeground(Main.config.foreground);
		cur.setForeground(Main.config.foreground);
		graph.setForeground(Main.config.foreground);
		keys.setForeground(Main.config.foreground);
		configkeys.setForeground(Main.config.foreground);
		configcolors.setForeground(Main.config.foreground);
		colorenable.setForeground(Main.config.foreground);
		colorcustom.setForeground(Main.config.foreground);
		
		menu.setBackground(Main.config.background);
		configure.setBackground(Main.config.background);
		general.setBackground(Main.config.background);
		precision.setBackground(Main.config.background);
		snap.setBackground(Main.config.background);
		exit.setBackground(Main.config.background);
		pause.setBackground(Main.config.background);
		sreset.setBackground(Main.config.background);
		treset.setBackground(Main.config.background);
		tAll.setBackground(Main.config.background);
		overlay.setBackground(Main.config.background);
		p0.setBackground(Main.config.background);
		p1.setBackground(Main.config.background);
		p2.setBackground(Main.config.background);
		p3.setBackground(Main.config.background);
		max.setBackground(Main.config.background);
		avg.setBackground(Main.config.background);
		cur.setBackground(Main.config.background);
		graph.setBackground(Main.config.background);
		keys.setBackground(Main.config.background);
		configkeys.setBackground(Main.config.background);
		configcolors.setBackground(Main.config.background);
		colorenable.setBackground(Main.config.background);
		colorcustom.setBackground(Main.config.background);
		
		menu.setBorder(BorderFactory.createLineBorder(Main.config.foreground));
		general.getPopupMenu().setBorder(BorderFactory.createLineBorder(Main.config.foreground));
		precision.getPopupMenu().setBorder(BorderFactory.createLineBorder(Main.config.foreground));
		configure.getPopupMenu().setBorder(BorderFactory.createLineBorder(Main.config.foreground));
		configcolors.getPopupMenu().setBorder(BorderFactory.createLineBorder(Main.config.foreground));
	}

	/**
	 * Creates the popup menu
	 */
	protected static final void createMenu(){
		menu.setOpaque(true);
		configure.setOpaque(true);
		general.setOpaque(true);
		precision.setOpaque(true);
		snap.setOpaque(true);
		exit.setOpaque(true);
		pause.setOpaque(true);
		sreset.setOpaque(true);
		treset.setOpaque(true);
		tAll.setOpaque(true);
		overlay.setOpaque(true);
		p0.setOpaque(true);
		p1.setOpaque(true);
		p2.setOpaque(true);
		p3.setOpaque(true);
		max.setOpaque(true);
		avg.setOpaque(true);
		cur.setOpaque(true);
		graph.setOpaque(true);
		keys.setOpaque(true);
		configcolors.setOpaque(true);
		configkeys.setOpaque(true);
		colorenable.setOpaque(true);
		colorcustom.setOpaque(true);
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
		
		general.add(max);
		general.add(avg);
		general.add(cur);
		general.add(keys);
		general.add(graph);
		general.add(overlay);
		general.add(tAll);
		
		configure.add(general);
		configure.add(configkeys);
		configure.add(configcolors);
		configure.add(precision);
		
		menu.add(configure);
		menu.add(snap);
		menu.add(treset);
		menu.add(sreset);
		menu.add(pause);
		menu.add(exit);
	}
}
