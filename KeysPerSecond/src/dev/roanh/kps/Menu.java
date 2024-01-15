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

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.plaf.basic.BasicMenuUI;

import dev.roanh.kps.config.ConfigParser;
import dev.roanh.kps.config.UpdateRate;
import dev.roanh.kps.config.group.KeyPanelSettings;
import dev.roanh.kps.panels.GraphPanel;
import dev.roanh.kps.ui.dialog.AboutDialog;
import dev.roanh.kps.ui.dialog.ColorDialog;
import dev.roanh.kps.ui.dialog.CommandKeysDialog;
import dev.roanh.kps.ui.dialog.KeysDialog;
import dev.roanh.kps.ui.dialog.LayoutDialog;
import dev.roanh.kps.ui.dialog.StatsSavingDialog;

/**
 * This class handles everything related to the popup menus.
 * @author Roan
 */
public class Menu{
	/**
	 * The right click menu.
	 */
	protected static final JPopupMenu menu = new JPopupMenu();
	/**
	 * The configuration menu.
	 */
	private static final JMenu configure = new JMenu("Configure");
	/**
	 * The general menu.
	 */
	private static final JMenu general = new JMenu("General");
	/**
	 * Update rate menu.
	 */
	private static final JMenu rate = new JMenu("Update rate");
	/**
	 * Reset menu.
	 */
	private static final JMenu reset = new JMenu("Reset");
	/**
	 * Load/Save menu.
	 */
	private static final JMenu saveLoad = new JMenu("Save / Load");
	/**
	 * The pause menu item.
	 */
	protected static final JCheckBoxMenuItem pause = new JCheckBoxMenuItem("Pause");
	/**
	 * The icon for the system tray.
	 */
	private static TrayIcon trayIcon = null;

	/**
	 * Repaints the component border
	 */
	protected static final void repaint(){
		Border border = BorderFactory.createLineBorder(Main.config.getTheme().getForeground().getColor());
		menu.setBorder(border);
		configure.getPopupMenu().setBorder(border);
		general.getPopupMenu().setBorder(border);
		rate.getPopupMenu().setBorder(border);
		reset.getPopupMenu().setBorder(border);
		saveLoad.getPopupMenu().setBorder(border);
	}

	/**
	 * Creates the popup menu
	 */
	protected static final void createMenu(){
		List<JMenuItem> components = new ArrayList<JMenuItem>();
		JMenuItem snap = new JMenuItem("Snap to edges");
		JMenuItem exit = new JMenuItem("Exit");
		JMenuItem sreset = new JMenuItem("Reset statistics");
		JMenuItem sresetmax = new JMenuItem("Reset maximum");
		JMenuItem sresetavg = new JMenuItem("Reset average");
		JMenuItem sresettot = new JMenuItem("Reset total");
		JMenuItem sresetgraph = new JMenuItem("Reset graphs");
		JMenuItem treset = new JMenuItem("Reset key totals");
		JMenuItem configkeys = new JMenuItem("Keys & buttons");
		JMenuItem colorcustom = new JMenuItem("Configure colours");
		JMenuItem statsSaving = new JMenuItem("Stats saving");
		JMenuItem commandkeys = new JMenuItem("Commands");
		JMenuItem layout = new JMenuItem("Layout panels & graphs");
		JMenuItem about = new JMenuItem("About");
		JMenuItem minimizeButton = new JMenuItem("Hide window");
		JCheckBoxMenuItem tAllKeys = new JCheckBoxMenuItem("Track all keys");
		JCheckBoxMenuItem tAllButtons = new JCheckBoxMenuItem("Track all buttons");
		JCheckBoxMenuItem overlay = new JCheckBoxMenuItem("Overlay mode");
		JCheckBoxMenuItem modifiers = new JCheckBoxMenuItem("Key-modifier tracking");
		JMenuItem save = new JMenuItem("Save config");
		JMenuItem load = new JMenuItem("Load config");
		JMenuItem saveStats = new JMenuItem("Save stats");
		JMenuItem loadStats = new JMenuItem("Load stats");
		components.add(saveStats);
		components.add(loadStats);
		components.add(load);
		components.add(statsSaving);
		components.add(layout);
		components.add(about);
		components.add(minimizeButton);
		components.add(save);
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
		components.add(tAllKeys);
		components.add(tAllButtons);
		components.add(overlay);
		components.add(commandkeys);
		components.add(modifiers);
		configure.setUI(new MenuUI());
		general.setUI(new MenuUI());
		rate.setUI(new MenuUI());
		reset.setUI(new MenuUI());
		saveLoad.setUI(new MenuUI());
		for(JMenuItem e : components){
			e.setUI(new MenuItemUI());
		}
		snap.addActionListener((e)->{
			JFrame frame = (JFrame)KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
			Point loc = frame.getLocationOnScreen();
			Rectangle bounds = frame.getGraphicsConfiguration().getBounds();
			frame.setLocation(Math.abs(loc.x - bounds.x) < 100 ? bounds.x : Math.abs((loc.x + frame.getWidth()) - (bounds.x + bounds.width)) < 100 ? bounds.x + bounds.width - frame.getWidth() : loc.x,
			                  Math.abs(loc.y - bounds.y) < 100 ? bounds.y : Math.abs((loc.y + frame.getHeight()) - (bounds.y + bounds.height)) < 100 ? bounds.y + bounds.height - frame.getHeight() : loc.y);
		});
		exit.addActionListener((e)->{
			Main.exit();
		});
		about.addActionListener((e)->{
			AboutDialog.showAbout();
		});
		minimizeButton.addActionListener(e->{
			minimizeToSystemTray();
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
			Main.hits = 0;
		});
		sresetgraph.addActionListener((e)->{
			Main.graphs.forEach(GraphPanel::reset);
		});
		commandkeys.addActionListener((e)->{
			CommandKeysDialog.configureCommandKeys(Main.config.getCommands());
		});
		treset.addActionListener((e)->{
			Main.resetTotals();
		});
		tAllKeys.setSelected(Main.config.isTrackAllKeys());
		tAllKeys.addActionListener((e)->{
			Main.config.setTrackAllKeys(tAllKeys.isSelected());
			Main.keys.entrySet().removeIf(entry->!CommandKeys.isMouseButton(entry.getKey()) && !Main.config.getKeySettings().contains(entry.getKey(), KeyPanelSettings::getKeyCode));
		});
		tAllButtons.setSelected(Main.config.isTrackAllButtons());
		tAllButtons.addActionListener((e)->{
			Main.config.setTrackAllButtons(tAllButtons.isSelected());
			Main.keys.entrySet().removeIf(entry->CommandKeys.isMouseButton(entry.getKey()) && !Main.config.getKeySettings().contains(entry.getKey(), KeyPanelSettings::getKeyCode));
		});
		overlay.setSelected(Main.config.isOverlayMode());
		overlay.addActionListener((e)->{
			Main.config.setOverlayMode(overlay.isSelected());
			Main.reconfigure();
		});
		configkeys.addActionListener((e)->{
			KeysDialog.configureKeys(Main.config.getKeySettings(), true);
		});
		colorcustom.addActionListener((e)->{
			ColorDialog.configureColors(Main.config.getTheme(), true);
		});
		modifiers.setSelected(Main.config.isKeyModifierTrackingEnabled());
		modifiers.addActionListener((e)->{
			Main.config.setKeyModifierTrackingEnabled(modifiers.isSelected());
		});
		layout.addActionListener((e)->{
			LayoutDialog.configureLayout(true);
		});

		List<JCheckBoxMenuItem> rates = new ArrayList<JCheckBoxMenuItem>();
		for(UpdateRate val : UpdateRate.values()){
			JCheckBoxMenuItem item = new JCheckBoxMenuItem(val.toString(), Main.config.getUpdateRate() == val);
			item.addActionListener(e->{
				for(JCheckBoxMenuItem box : rates){
					box.setSelected(false);
				}
				Main.changeUpdateRate(val);
				item.setSelected(true);
			});
			rates.add(item);
			rate.add(item);
			components.add(item);
			item.setUI(new MenuItemUI());
		}

		save.addActionListener((e)->{
			Main.config.saveConfig(true);
		});
		load.addActionListener((e)->{
			if(ConfigParser.loadConfiguration()){
				resetData();
			}
		});
		saveStats.addActionListener((e)->{
			Statistics.saveStats();
		});
		loadStats.addActionListener((e)->{
			Statistics.loadStats();
		});
		statsSaving.addActionListener((e)->{
			StatsSavingDialog.configureStatsSaving(Main.config.getStatsSavingSettings(), true);
		});

		reset.add(treset);
		reset.add(sreset);
		reset.add(sresetmax);
		reset.add(sresetavg);
		reset.add(sresettot);
		reset.add(sresetgraph);

		general.add(overlay);
		general.add(tAllKeys);
		general.add(tAllButtons);
		general.add(modifiers);

		configure.add(general);
		configure.add(configkeys);
		configure.add(layout);
		configure.add(rate);
		configure.add(colorcustom);
		configure.add(statsSaving);
		configure.add(commandkeys);

		saveLoad.add(load);
		saveLoad.add(save);
		saveLoad.add(loadStats);
		saveLoad.add(saveStats);

		menu.add(configure);
		menu.add(saveLoad);
		menu.add(pause);
		menu.add(snap);
		menu.add(reset);
		menu.add(about);
		menu.add(minimizeButton);
		menu.add(exit);
	}

	/**
	 * Minimises the window to the system tray and hides the main frame.
	 */
	public static void minimizeToSystemTray(){
		Main.frame.setVisible(false);
		if(SystemTray.isSupported()){
			SystemTray systemTray = SystemTray.getSystemTray();

			if(trayIcon == null){
				PopupMenu popupMenu = new PopupMenu();
				MenuItem exit = new MenuItem("Exit");
				popupMenu.add(exit);

				exit.addActionListener(e->{
					Main.exit();
				});

				trayIcon = new TrayIcon(Main.iconSmall, "KeysPerSecond", popupMenu);
				trayIcon.addMouseListener(new MouseListener(){
					
					@Override
					public void mouseClicked(MouseEvent e){
						if(e.getButton() == MouseEvent.BUTTON1){
							restoreFromSystemTray();
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
				});
			}

			try{
				systemTray.add(trayIcon);
			}catch(AWTException e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Removes the system tray icon if present and makes the main frame visible again.
	 */
	public static void restoreFromSystemTray(){
		Main.frame.setVisible(true);
		if(SystemTray.isSupported()){
			SystemTray.getSystemTray().remove(trayIcon);
		}
	}

	/**
	 * Applies a new configuration to the program
	 */
	protected static final void resetData(){
		menu.removeAll();
		configure.removeAll();
		general.removeAll();
		rate.removeAll();
		reset.removeAll();
		saveLoad.removeAll();
		createMenu();
		Main.keys.clear();
		Main.resetStats();
		Main.hits = 0;
		Main.reconfigure();
		Main.mainLoop();
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
		public void installUI(JComponent c){
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
			paintMenuItem((Graphics2D)g, menuItem, hasCursor, defaultTextIconGap);
		}

		/**
		 * Paints a menu item
		 * @param g The graphics to use
		 * @param menuItem The menu item to paint
		 * @param hasCursor Whether or not the cursor is over the component
		 * @param defaultTextIconGap The gap between the text and the icon
		 */
		private static final void paintMenuItem(Graphics2D g, JMenuItem menuItem, boolean hasCursor, int defaultTextIconGap){
			g.setColor(Main.config.getTheme().getBackground().getColor());
			g.fillRect(0, 0, menuItem.getWidth(), menuItem.getHeight());
			if(menuItem instanceof JCheckBoxMenuItem && menuItem.isSelected()){
				g.drawImage(ColorManager.checkmark, 0, 0, 22, 22, 0, 0, 100, 100, menuItem);
			}else if(menuItem instanceof JMenu){
				g.drawImage(ColorManager.arrow, menuItem.getWidth() - 12, 5, menuItem.getWidth(), 17, 0, 0, 128, 128, menuItem);
			}
			
			g.setColor(Main.config.getTheme().getForeground().getColor());
			if(hasCursor){
				g.drawLine(0, 0, menuItem.getWidth(), 0);
				g.drawLine(0, menuItem.getHeight() - 1, menuItem.getWidth(), menuItem.getHeight() - 1);
				Composite prev = g.getComposite();
				g.setComposite(MenuItemUI.mode);
				g.fillRect(0, 0, menuItem.getWidth(), menuItem.getHeight());
				g.setComposite(prev);
			}
			
			FontMetrics fm = menuItem.getFontMetrics(g.getFont());
			g.addRenderingHints(Main.desktopHints);
			g.drawString(menuItem.getText(), 22 + defaultTextIconGap, ((22 - fm.getHeight()) / 2) + fm.getAscent());
		}

		@Override
		public void mouseClicked(MouseEvent e){
		}

		@Override
		public void mousePressed(MouseEvent e){
		}

		@Override
		public void mouseReleased(MouseEvent e){
		}

		@Override
		public void mouseEntered(MouseEvent e){
			hasCursor = true;
		}

		@Override
		public void mouseExited(MouseEvent e){
			hasCursor = false;
		}

		@Override
		public void popupMenuWillBecomeVisible(PopupMenuEvent e){
		}

		@Override
		public void popupMenuWillBecomeInvisible(PopupMenuEvent e){
			hasCursor = false;
		}

		@Override
		public void popupMenuCanceled(PopupMenuEvent e){
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
		public void installUI(JComponent c){
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
			MenuUI.paintMenuItem((Graphics2D)g, menuItem, hasCursor, defaultTextIconGap);
		}

		@Override
		public void mouseClicked(MouseEvent e){
		}

		@Override
		public void mousePressed(MouseEvent e){
		}

		@Override
		public void mouseReleased(MouseEvent e){
		}

		@Override
		public void mouseEntered(MouseEvent e){
			hasCursor = true;
		}

		@Override
		public void mouseExited(MouseEvent e){
			hasCursor = false;
		}

		@Override
		public void popupMenuWillBecomeVisible(PopupMenuEvent e){
		}

		@Override
		public void popupMenuWillBecomeInvisible(PopupMenuEvent e){
			hasCursor = false;
		}

		@Override
		public void popupMenuCanceled(PopupMenuEvent e){
			hasCursor = false;
		}
	}
}
