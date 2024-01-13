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
package dev.roanh.kps.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import dev.roanh.kps.Main;
import dev.roanh.kps.config.Configuration;
import dev.roanh.kps.config.PanelType;
import dev.roanh.kps.config.SettingList;
import dev.roanh.kps.config.group.SpecialPanelSettings;
import dev.roanh.util.ClickableLink;
import dev.roanh.util.Dialog;
import dev.roanh.util.Util;

public class MainDialog extends JPanel{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -2620857098469751291L;
	
	
	
	private static Configuration config;
	
	private CheckBoxPanel options = new CheckBoxPanel();
	
	
	
	public MainDialog(){
		super(new BorderLayout());
		
		add(buildLeftPanel(), BorderLayout.CENTER);
		add(buildRightPanel(), BorderLayout.LINE_END);
		add(buildBottomPanel(), BorderLayout.PAGE_END);
	}
	
	private JPanel buildBottomPanel(){
		JLabel forum = new JLabel("<html><font color=blue><u>Forums</u></font> -</html>", SwingConstants.RIGHT);
		forum.addMouseListener(new ClickableLink("https://osu.ppy.sh/community/forums/topics/552405"));
		
		JLabel git = new JLabel("<html>- <font color=blue><u>GitHub</u></font></html>", SwingConstants.LEFT);
		git.addMouseListener(new ClickableLink("https://github.com/RoanH/KeysPerSecond"));
		
		JPanel links = new JPanel(new GridLayout(1, 2, -2, 0));
		links.add(forum);
		links.add(git);
		
		JPanel info = new JPanel(new GridLayout(2, 1, 0, 2));
		info.add(Util.getVersionLabel("KeysPerSecond", Main.VERSION));
		info.add(links);
		return info;
	}
	
	private JPanel buildLeftPanel(){
		//info
		JLabel info = new JLabel("<html><body style='width:210px'>You can either configure the program on this screen or use the right <b>right click</b> menu after the program is already visible to see changes take effect in real time.</body></html>");
		info.setBorder(BorderFactory.createTitledBorder("Information"));
		
		//main configuration
		JPanel main = new JPanel(new GridLayout(2, 1));
		main.setBorder(BorderFactory.createTitledBorder("Main Configuration"));

		JButton keys = new JButton("Configure Keys & Buttons");
		main.add(keys);
		keys.addActionListener(e->KeysDialog.configureKeys(config.getKeySettings(), false));
		
		JButton layout = new JButton("Configure Graphs & Panels");
		main.add(layout);
		layout.addActionListener(e->LayoutDialog.configureLayout(false));

		//left panel
		JPanel left = new JPanel(new BorderLayout());
		left.add(info, BorderLayout.PAGE_START);
		left.add(main, BorderLayout.CENTER);
		left.add(options, BorderLayout.PAGE_END);
		return left;
	}
	
	private JPanel buildRightPanel(){
		//configuration
		JPanel configuration = new JPanel(new GridLayout(2, 1));
		configuration.setBorder(BorderFactory.createTitledBorder("Configuration"));
		
		JButton load = new JButton("Load config");
		configuration.add(load);
		load.addActionListener(e->{
			if(Configuration.loadConfiguration()){
				options.syncBoxes();
			}
		});
		
		JButton save = new JButton("Save config");
		configuration.add(save);
		save.addActionListener(e->config.saveConfig(false));
		
		//settings
		JPanel settings = new JPanel(new GridLayout(4, 1));
		settings.setBorder(BorderFactory.createTitledBorder("Settings"));
		
		JButton updaterate = new JButton("Update rate");
		settings.add(updaterate);
		updaterate.addActionListener(e->UpdateRateDialog.configureUpdateRate());
		
		JButton color = new JButton("Colours");
		settings.add(color);
		color.addActionListener(e->ColorDialog.configureColors(config.getTheme(), false));
		
		JButton autoSave = new JButton("Stats saving");
		settings.add(autoSave);
		autoSave.addActionListener(e->StatsSavingDialog.configureStatsSaving(Main.config.getStatsSavingSettings(), false));
		
		JButton cmdkeys = new JButton("Commands");
		settings.add(cmdkeys);
		cmdkeys.addActionListener(e->CommandKeysDialog.configureCommandKeys(config.getCommands()));
		
		//about
		JPanel aboutPanel = new JPanel(new BorderLayout());
		aboutPanel.setBorder(BorderFactory.createTitledBorder("About"));
		JButton about = new JButton("About");
		aboutPanel.add(about);
		about.addActionListener(e->AboutDialog.showAbout());
		
		//right panel
		JPanel right = new JPanel(new BorderLayout());
		right.add(configuration, BorderLayout.PAGE_START);
		right.add(settings, BorderLayout.CENTER);
		right.add(aboutPanel, BorderLayout.PAGE_END);
		return right;
	}
	
	private class CheckBoxPanel extends JPanel{
		/**
		 * Serial ID.
		 */
		private static final long serialVersionUID = 7814497194364064857L;
		private final JCheckBox overlay = new JCheckBox();
		private final JCheckBox allKeys = new JCheckBox();
		private final JCheckBox allButtons = new JCheckBox();
		private final JCheckBox modifiers = new JCheckBox();
		
		private CheckBoxPanel(){
			super(new BorderLayout());
			setBorder(BorderFactory.createTitledBorder("Options"));
			
			JPanel labels = new JPanel(new GridLayout(4, 0));
			labels.add(new JLabel("Overlay mode: "));
			labels.add(new JLabel("Track all keys"));
			labels.add(new JLabel("Track all buttons"));
			labels.add(new JLabel("Key-modifier tracking"));
			add(labels, BorderLayout.CENTER);
			
			JPanel boxes = new JPanel(new GridLayout(4, 0));
			boxes.add(overlay);
			boxes.add(allKeys);
			boxes.add(allButtons);
			boxes.add(modifiers);
			add(boxes, BorderLayout.LINE_END);
			
			overlay.addActionListener(e->config.setOverlayMode(overlay.isSelected()));
			allKeys.addActionListener(e->config.setTrackAllKeys(allKeys.isSelected()));
			allButtons.addActionListener(e->config.setTrackAllButtons(allButtons.isSelected()));
			modifiers.addActionListener(e->config.setKeyModifierTrackingEnabled(modifiers.isSelected()));
			
			syncBoxes();
		}
		
		private void syncBoxes(){
			overlay.setSelected(config.isOverlayMode());
			allKeys.setSelected(config.isTrackAllKeys());
			allButtons.setSelected(config.isTrackAllButtons());
			modifiers.setSelected(config.isKeyModifierTrackingEnabled());
		}
	}
	
	
	public static final void configureAlt(){
		config = Main.config;
		
		Dialog.showDialog(new MainDialog(), new String[]{"OK", "Exit"});
		
	}
	
	
	
	/**
	 * Asks the user for a configuration
	 * though a series of dialogs
	 * These dialogs also provide the
	 * option of saving or loading an
	 * existing configuration
	 */
	public static final void configure(){
		config = Main.config;//TODO no
		
		
		
		
		JButton ok = new JButton("OK");
		JButton exit = new JButton("Exit");
		exit.addActionListener(e->Main.exit());
		
		CountDownLatch latch = new CountDownLatch(1);
		ok.addActionListener(e->latch.countDown());
		
		JPanel bottomButtons = new JPanel();
		bottomButtons.add(ok);
		bottomButtons.add(exit);
		
		JPanel dialog = new JPanel(new BorderLayout());
		dialog.add(new MainDialog(), BorderLayout.CENTER);
		dialog.add(bottomButtons, BorderLayout.PAGE_END);
		
		dialog.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		
		
		
		
		
		
		
		
		
		JFrame conf = new JFrame("KeysPerSecond");
		conf.add(dialog);
		conf.pack();
		conf.setResizable(false);
		conf.setLocationRelativeTo(null);
//		List<Image> icons = new ArrayList<Image>();
//		icons.add(Main.icon);
//		icons.add(Main.iconSmall);
//		conf.setIconImages(icons);
//		conf.addWindowListener(Main.onClose);
		conf.setVisible(true);
		
		try{
			latch.await();
		}catch(InterruptedException e1){
		}
		conf.setVisible(false);
		conf.dispose();
	}
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//========================================================================================
	
	
	
	
	/**
	 * Asks the user for a configuration
	 * though a series of dialogs
	 * These dialogs also provide the
	 * option of saving or loading an
	 * existing configuration
	 */
	public static final void configureCopy(){
		config = Main.config;//TODO no
		
		JPanel form = new JPanel(new BorderLayout());
		JPanel boxes = new JPanel(new GridLayout(11, 0));
		JPanel labels = new JPanel(new GridLayout(11, 0));
		JCheckBox ctop = new JCheckBox();
		JCheckBox callKeys = new JCheckBox();
		JCheckBox callButtons = new JCheckBox();
		JCheckBox cmod = new JCheckBox();
		JLabel ltop = new JLabel("Overlay mode: ");
		JLabel lallKeys = new JLabel("Track all keys");
		JLabel lallButtons = new JLabel("Track all buttons");
		JLabel lmod = new JLabel("Key-modifier tracking");
		boxes.add(ctop);
		boxes.add(callKeys);
		boxes.add(callButtons);
		boxes.add(cmod);
		labels.add(ltop);
		labels.add(lallKeys);
		labels.add(lallButtons);
		labels.add(lmod);
		ctop.addActionListener((e)->{
			config.setOverlayMode(ctop.isSelected());
		});
		callKeys.addActionListener((e)->{
			config.setTrackAllKeys(callKeys.isSelected());
		});
		callButtons.addActionListener((e)->{
			config.setTrackAllButtons(callButtons.isSelected());
		});
		cmod.addActionListener((e)->{
			config.setKeyModifierTrackingEnabled(cmod.isSelected());
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
		JButton color = new JButton("Colours");
		JButton layout = new JButton("Layout");
		JButton autoSave = new JButton("Stats saving");
		buttons.add(addkey);
		buttons.add(load);
		buttons.add(save);
		buttons.add(updaterate);
		buttons.add(color);
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
			CommandKeysDialog.configureCommandKeys(config.getCommands());
		});
		addkey.addActionListener((e)->{
			KeysDialog.configureKeys(config.getKeySettings(), false);
		});
		color.addActionListener((e)->{
			ColorDialog.configureColors(config.getTheme(), false);
		});
		save.addActionListener((e)->{
			config.saveConfig(false);
		});
		load.addActionListener((e)->{
			if(!Configuration.loadConfiguration()){
				return;
			}

			callKeys.setSelected(config.isTrackAllKeys());
			callButtons.setSelected(config.isTrackAllButtons());
			ctop.setSelected(config.isOverlayMode());
			cmod.setSelected(config.isKeyModifierTrackingEnabled());
		});
		updaterate.addActionListener((e)->{
			UpdateRateDialog.configureUpdateRate();
		});
		autoSave.addActionListener((e)->{
			StatsSavingDialog.configureStatsSaving(Main.config.getStatsSavingSettings(), false);
		});
		
		
		JPanel info = new JPanel(new GridLayout(2, 1, 0, 2));
		info.add(Util.getVersionLabel("KeysPerSecond", Main.VERSION));
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
		exit.addActionListener(e->Main.exit());
		
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
//		List<Image> icons = new ArrayList<Image>();
//		icons.add(Main.icon);
//		icons.add(Main.iconSmall);
//		conf.setIconImages(icons);
//		conf.addWindowListener(Main.onClose);
		conf.setVisible(true);
		
		try{
			latch.await();
		}catch(InterruptedException e1){
		}
		conf.setVisible(false);
		conf.dispose();
	}
}
