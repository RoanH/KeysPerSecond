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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import dev.roanh.kps.Statistics;
import dev.roanh.kps.config.group.StatsSavingSettings;
import dev.roanh.kps.ui.model.FilePathFormatterFactory;
import dev.roanh.util.ClickableLink;
import dev.roanh.util.Dialog;

public class StatsSavingDialog extends JPanel{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 2934388616261272855L;
	private JCheckBox saveOnExit;
	private JCheckBox loadOnStart;
	private JComboBox<Unit> timeUnit = new JComboBox<Unit>(Unit.values());
	private JTextField selectedFile;
	private JCheckBox enabled;
	private JFormattedTextField format;
	private JTextField ldest;
	private JSpinner time;

	private StatsSavingDialog(StatsSavingSettings config){
		super(new BorderLayout());
		
		JPanel endPanel = new JPanel(new BorderLayout());
		endPanel.setBorder(BorderFactory.createTitledBorder("Save on exit"));
		saveOnExit = new JCheckBox("Save statistics to a file on exit", config.isSaveOnExitEnabled());
		loadOnStart = new JCheckBox("Load saved statistics from a file on launch", config.isLoadOnLaunchEnabled());

		JPanel selectFile = new JPanel(new BorderLayout(2, 0));
		selectFile.add(new JLabel("Save location: "), BorderLayout.LINE_START);
		selectedFile = new JTextField(config.getSaveFile());
		selectFile.add(selectedFile, BorderLayout.CENTER);
		JButton select = new JButton("Select");
		selectFile.add(select, BorderLayout.LINE_END);
		select.addActionListener((e)->{
			Path dir = Dialog.showFileSaveDialog(Statistics.KPS_STATS_EXT, "stats");
			if(dir != null){
				selectedFile.setText(dir.toAbsolutePath().toString());
			}
		});
		
		ActionListener stateTask = e->{
			boolean enabled = saveOnExit.isSelected() || loadOnStart.isSelected();
			selectedFile.setEnabled(enabled);
			select.setEnabled(enabled);
		};
		
		saveOnExit.addActionListener(stateTask);
		loadOnStart.addActionListener(stateTask);
		stateTask.actionPerformed(null);
		
		endPanel.add(saveOnExit, BorderLayout.PAGE_START);
		endPanel.add(loadOnStart, BorderLayout.CENTER);
		endPanel.add(selectFile, BorderLayout.PAGE_END);
		
		JPanel periodicPanel = new JPanel(new BorderLayout());
		periodicPanel.setBorder(BorderFactory.createTitledBorder("Periodic saving"));
		JCheckBox enabled = new JCheckBox("Periodically save the statistics so far to a file", config.isAutoSaveEnabled());
		
		BorderLayout layout = new BorderLayout();
		layout.setHgap(2);
		JPanel settings = new JPanel(layout);
		JPanel labels = new JPanel(new GridLayout(3, 1, 0, 2));
		JPanel fields = new JPanel(new GridLayout(3, 1, 0, 2));
		JPanel extras = new JPanel(new GridLayout(3, 1, 0, 2));
		
		JButton seldest = new JButton("Select");
		ldest = new JTextField(config.getAutoSaveDestination());
		fields.add(ldest);
		extras.add(seldest);
		labels.add(new JLabel("Save location: "));
		seldest.addActionListener((e)->{
			Path dir = Dialog.showFolderOpenDialog();
			if(dir != null){
				ldest.setText(dir.toAbsolutePath().toString());
			}
		});
		
		Unit bestUnit = Unit.fromMillis(config.getAutoSaveInterval());
		timeUnit.setSelectedItem(bestUnit);
		time = new JSpinner(new SpinnerNumberModel(Long.valueOf(config.getAutoSaveInterval() / bestUnit.unit.toMillis(1)), Long.valueOf(1L), Long.valueOf(Long.MAX_VALUE), Long.valueOf(1L)));
		labels.add(new JLabel("Save interval: "));
		fields.add(time);
		JPanel unitPanel = new JPanel(new BorderLayout());
		unitPanel.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
		unitPanel.add(timeUnit, BorderLayout.CENTER);
		extras.add(unitPanel);
		
		format = new JFormattedTextField(new FilePathFormatterFactory(), config.getAutoSaveFormat());
		format.setColumns(30);
		format.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		JButton help = new JButton("Help");
		labels.add(new JLabel("Save format: "));
		fields.add(format);
		extras.add(help);
		help.addActionListener(this::showFormatHelp);
		
		settings.add(labels, BorderLayout.LINE_START);
		settings.add(fields, BorderLayout.CENTER);
		settings.add(extras, BorderLayout.LINE_END);
		
		periodicPanel.add(enabled, BorderLayout.PAGE_START);
		periodicPanel.add(settings, BorderLayout.CENTER);
		
		ActionListener enabledTask = (e)->{
			ldest.setEnabled(enabled.isSelected());
			seldest.setEnabled(enabled.isSelected());
			format.setEnabled(enabled.isSelected());
			help.setEnabled(enabled.isSelected());
			time.setEnabled(enabled.isSelected());
			timeUnit.setEnabled(enabled.isSelected());
		};
		enabled.addActionListener(enabledTask);
		enabledTask.actionPerformed(null);
		
		this.add(endPanel, BorderLayout.PAGE_START);
		this.add(periodicPanel, BorderLayout.PAGE_END);
	}
	
	/**
	 * Shows a help dialog to the user that list some of
	 * the available {@link DateTimeFormatter} options
	 * @param event The {@link ActionEvent} from the button
	 *        that was clicked
	 */
	private void showFormatHelp(ActionEvent event){
		JLabel help = new JLabel(
			"<html>Format syntax:<br>"
			+ "- Escape strings with single quotes ( ' )<br>"
			+ "- A double single quote is a single quote ( '' becomes ' )<br>"
			+ "- Note that / \\ ? % * : | \" &lt and > are not allowed in file names<br>"
			+ "- <b>yyyy</b> represents the year<br>"
			+ "- <b>MM</b> represents the month of the year<br>"
			+ "- <b>dd</b> represents the day of the month<br>"
			+ "- <b>hh</b> represents the hour of the day<br>"
			+ "- <b>mm</b> represents the minute in the hour<br>"
			+ "- <b>ss</b> represents the second in the minute</html>"
		);
		
		JLabel more = new JLabel("<html><font color=blue><u>More options can be found in the Javadoc for the DateTimeFormatter.</u></font></html>");
		more.addMouseListener(new ClickableLink("https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html"));
		
		JPanel text = new JPanel(new BorderLayout());
		text.add(help, BorderLayout.CENTER);
		text.add(more, BorderLayout.PAGE_END);
		
		Dialog.showMessageDialog(text);
	}

	/**
	 * Show the auto save statistics configuration dialog
	 * @param live Whether or not the program is already running
	 */
	public static final void configureStatsSaving(StatsSavingSettings config, boolean live){
		StatsSavingDialog dialog = new StatsSavingDialog(config);
		if(Dialog.showSaveDialog(dialog)){
			config.setAutoSaveEnabled(dialog.enabled.isSelected());
			config.setAutoSaveDestination(dialog.ldest.getText());
			config.setAutoSaveFormat(dialog.format.getText());
			
			long interval = ((Unit)dialog.timeUnit.getSelectedItem()).unit.toMillis((long)dialog.time.getValue());
			if(config.isAutoSaveEnabled()){
				if(interval != config.getAutoSaveInterval()){
					config.setAutoSaveInterval(interval);
					if(live){
						Statistics.saveStatsTask();
					}
				}
			}else{
				Statistics.cancelScheduledTask();
				config.setAutoSaveInterval(interval);
			}
			
			config.setSaveOnExitEnabled(dialog.saveOnExit.isSelected());
			config.setLoadOnLaunchEnabled(dialog.loadOnStart.isSelected());
			config.setSaveFile(dialog.selectedFile.getText());
		}
	}
	
	/**
	 * Enum that has the different time unit values offered.
	 * @author Roan
	 * @see TimeUnit
	 */
	private static enum Unit{
		/**
		 * Represents the hour unit.
		 * Larger units are not offered.
		 */
		HOUR("Hours", TimeUnit.HOURS, null),
		/**
		 * Represents the minute unit, 60 of
		 * which make up a single {@link #HOUR}.
		 */
		MINUTE("Minutes", TimeUnit.MINUTES, HOUR),
		/**
		 * Represents the second unit, 60 of
		 * which make up a single {@link #MINUTE}.
		 */
		SECOND("Seconds", TimeUnit.SECONDS, MINUTE),
		/**
		 * Represents the millisecond unit, 1000 of
		 * which make up a single {@link #SECOND}.
		 */
		MILLISECOND("Milliseconds", TimeUnit.MILLISECONDS, SECOND);
		
		/**
		 * The {@link TimeUnit} for this unit
		 */
		private TimeUnit unit;
		/**
		 * The display name for this unit
		 */
		private String name;
		/**
		 * The Unit that is one order of magnitude
		 * larger than this one
		 */
		private Unit up;
		
		/**
		 * Constructs a new Unit with the given
		 * display name, {@link TimeUnit} and
		 * Unit that is one order of magnitude larger
		 * @param name The display name for this unit
		 * @param unit The TimeUnit for this unit
		 * @param up The unit one size bigger than this one
		 */
		private Unit(String name, TimeUnit unit, Unit up){
			this.name = name;
			this.unit = unit;
			this.up = up;
		}
		
		/**
		 * Returns the biggest time unit for which a single
		 * unit is a divisor of the given number of millisecond 
		 * and for which the a single unit of the time unit
		 * that is one biggest is bigger than the given
		 * number of milliseconds. If there is no bigger
		 * unit then {@link #HOUR} is returned.
		 * @param millis The number of milliseconds to
		 *        find the best unit for
		 * @return The best unit for the given number
		 *         of milliseconds
		 */
		private static final Unit fromMillis(long millis){
			for(Unit unit : values()){
				if(millis % unit.unit.toMillis(1) == 0 && (unit.up == null || millis < unit.up.unit.toMillis(1))){
					return unit;
				}
			}
			return null;
		}
		
		@Override
		public String toString(){
			return name;
		}
	}
}
