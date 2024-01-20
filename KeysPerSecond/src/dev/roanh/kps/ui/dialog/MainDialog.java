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
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import dev.roanh.kps.Main;
import dev.roanh.kps.config.ConfigLoader;
import dev.roanh.kps.config.Configuration;
import dev.roanh.kps.ui.listener.CloseListener;
import dev.roanh.util.ClickableLink;
import dev.roanh.util.Dialog;
import dev.roanh.util.Util;

/**
 * Main configuration dialog shown on first launch.
 * @author Roan
 */
public class MainDialog extends JPanel{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -2620857098469751291L;
	/**
	 * The configuration to update.
	 */
	private Configuration config;
	/**
	 * Panel with check box options.
	 */
	private CheckBoxPanel options;
	
	/**
	 * Constructs a new main dialog.
	 * @param config The configuration to update.
	 */
	public MainDialog(Configuration config){
		super(new BorderLayout());
		this.config = config;
		options = new CheckBoxPanel();
		
		add(buildLeftPanel(), BorderLayout.CENTER);
		add(buildRightPanel(), BorderLayout.LINE_END);
		add(buildBottomPanel(), BorderLayout.PAGE_END);
	}
	
	/**
	 * Builds the bottom GUI panel that shows links and version information.
	 * @return The constructed bottom GUI panel.
	 */
	private JPanel buildBottomPanel(){
		JLabel forum = new JLabel("<html><font color=blue><u>Forums</u></font> -</html>", SwingConstants.RIGHT);
		forum.addMouseListener(new ClickableLink("https://osu.ppy.sh/community/forums/topics/552405"));
		
		JLabel git = new JLabel("<html>- <font color=blue><u>GitHub</u></font></html>", SwingConstants.LEFT);
		git.addMouseListener(new ClickableLink("https://github.com/RoanH/KeysPerSecond"));
		
		JPanel links = new JPanel(new GridLayout(1, 2, -2, 0));
		links.add(forum);
		links.add(git);
		
		JPanel info = new JPanel(new GridLayout(2, 1, 0, 2));
		info.add(Util.getVersionLabel("KeysPerSecond", Main.VERSION.toString()));
		info.add(links);
		return info;
	}
	
	/**
	 * Builds the left GUI panel that shows general info, main configuration and check boxes.
	 * @return The constructed left GUI panel.
	 * @see #options
	 */
	private JPanel buildLeftPanel(){
		//info
		JLabel info = new JLabel("<html><body style='width:210px'>You can either configure the program on this screen or use the right <b>right click</b> menu after the program is already visible to see changes take effect in real time.</body></html>");
		info.setBorder(BorderFactory.createTitledBorder("Information"));
		
		//main configuration
		JPanel main = new JPanel(new GridLayout(2, 1));
		main.setBorder(BorderFactory.createTitledBorder("Main Configuration"));

		JButton keys = new JButton("Configure keys & buttons");
		main.add(keys);
		keys.addActionListener(e->KeysDialog.configureKeys(config.getKeySettings(), false));
		
		JButton layout = new JButton("Configure layout, graphs & panels");
		main.add(layout);
		layout.addActionListener(e->LayoutDialog.configureLayout(false));

		//left panel
		JPanel left = new JPanel(new BorderLayout());
		left.add(info, BorderLayout.PAGE_START);
		left.add(main, BorderLayout.CENTER);
		left.add(options, BorderLayout.PAGE_END);
		return left;
	}
	
	/**
	 * Builds the right GUI panel shows config saving/loading, settings and about.
	 * @return The constructed right GUI panel.
	 */
	private JPanel buildRightPanel(){
		//configuration
		JPanel configuration = new JPanel(new GridLayout(3, 1));
		configuration.setBorder(BorderFactory.createTitledBorder("Configuration"));
		
		JButton load = new JButton("Load config");
		configuration.add(load);
		load.addActionListener(e->{
			Configuration toLoad = ConfigLoader.loadConfiguration();
			if(toLoad != null){
				config = toLoad;
				Main.applyConfig(toLoad, false);
				options.syncBoxes();
			}
		});
		
		JButton save = new JButton("Save config");
		configuration.add(save);
		save.addActionListener(e->config.saveConfig(false));
		
		JButton defConf = new JButton("Default config");
		configuration.add(defConf);
		defConf.addActionListener(e->DefaultConfigDialog.showDefaultConfigDialog());
		
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
		aboutPanel.setBorder(BorderFactory.createTitledBorder("Help"));
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
	
	/**
	 * Panel with check box style options.
	 * @author Roan
	 */
	private class CheckBoxPanel extends JPanel{
		/**
		 * Serial ID.
		 */
		private static final long serialVersionUID = 7814497194364064857L;
		/**
		 * Whether overlay mode is enabled.
		 */
		private final JCheckBox overlay = new JCheckBox();
		/**
		 * Whether all keys are tracked.
		 */
		private final JCheckBox allKeys = new JCheckBox();
		/**
		 * Whether all buttons are tracked.
		 */
		private final JCheckBox allButtons = new JCheckBox();
		/**
		 * Whether key-modifier tracking is enabled.
		 */
		private final JCheckBox modifiers = new JCheckBox();
		/**
		 * Whether windowed mode is enabled.
		 */
		private final JCheckBox windowed = new JCheckBox();
		
		/**
		 * Constructs a new check box panel.
		 */
		private CheckBoxPanel(){
			super(new BorderLayout());
			setBorder(BorderFactory.createTitledBorder("Options"));
			
			JPanel labels = new JPanel(new GridLayout(5, 1));
			labels.add(new JLabel("Overlay mode: "));
			labels.add(new JLabel("Track all keys: "));
			labels.add(new JLabel("Track all buttons: "));
			labels.add(new JLabel("Key-modifier tracking: "));
			labels.add(new JLabel("Windowed mode:"));
			add(labels, BorderLayout.CENTER);
			
			JPanel boxes = new JPanel(new GridLayout(5, 1));
			boxes.add(overlay);
			boxes.add(allKeys);
			boxes.add(allButtons);
			boxes.add(modifiers);
			boxes.add(windowed);
			add(boxes, BorderLayout.LINE_END);
			
			overlay.addActionListener(e->config.setOverlayMode(overlay.isSelected()));
			allKeys.addActionListener(e->config.setTrackAllKeys(allKeys.isSelected()));
			allButtons.addActionListener(e->config.setTrackAllButtons(allButtons.isSelected()));
			modifiers.addActionListener(e->config.setKeyModifierTrackingEnabled(modifiers.isSelected()));
			windowed.addActionListener(e->config.setWindowedMode(windowed.isSelected()));
			
			syncBoxes();
		}
		
		/**
		 * Syncs all check boxes with current configuration setting.
		 */
		private void syncBoxes(){
			overlay.setSelected(config.isOverlayMode());
			allKeys.setSelected(config.isTrackAllKeys());
			allButtons.setSelected(config.isTrackAllButtons());
			modifiers.setSelected(config.isKeyModifierTrackingEnabled());
			windowed.setSelected(config.isWindowedMode());
		}
	}
	
	/**
	 * Shows the initial configuration dialog for the program.
	 * @param config The configuration to configure.
	 */
	public static final void configure(Configuration config){
		CountDownLatch latch = new CountDownLatch(1);
		JPanel bottomButtons = new JPanel();

		JButton ok = new JButton("OK");
		bottomButtons.add(ok);
		ok.addActionListener(e->{
			if(config.isValid()){
				latch.countDown();
			}else{
				Dialog.showMessageDialog("Please make sure your layout has at least one panel to display.");
			}
		});
		
		JButton exit = new JButton("Exit");
		bottomButtons.add(exit);
		exit.addActionListener(e->Main.exit());
		
		JPanel dialog = new JPanel(new BorderLayout());
		dialog.add(new MainDialog(config), BorderLayout.CENTER);
		dialog.add(bottomButtons, BorderLayout.PAGE_END);
		dialog.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JFrame conf = new JFrame("KeysPerSecond");
		conf.add(dialog);
		conf.pack();
		conf.setResizable(false);
		conf.setLocationRelativeTo(null);
		conf.setIconImages(Arrays.asList(Main.icon, Main.iconSmall));
		conf.addWindowListener(new CloseListener());
		conf.setVisible(true);
		
		try{
			latch.await();
		}catch(InterruptedException e1){
		}
		
		conf.setVisible(false);
		conf.dispose();
	}
}
