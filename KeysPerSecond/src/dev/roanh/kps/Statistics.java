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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import dev.roanh.kps.panels.TotPanel;
import dev.roanh.util.ClickableLink;
import dev.roanh.util.Dialog;
import dev.roanh.util.FileSelector;
import dev.roanh.util.FileSelector.FileExtension;

/**
 * Class that handles most of the more complex
 * matters related to statistics.
 * @author Roan
 */
public class Statistics{
	/**
	 * Extension filter for KeysPerSecond statistics files.
	 */
	private static final FileExtension KPS_STATS_EXT = FileSelector.registerFileExtension("KeysPerSecond statistics", "kpsstats");
	/**
	 * Regex used to parse key lines in the statistics save file.
	 */
	private static final Pattern STATS_LINE_REGEX = Pattern.compile("^  - \\[keycode=(-?\\d+),count=(\\d+),alt=(true|false),ctrl=(true|false),shift=(true|false),name=\\\"(.*)\\\"]$");
	/**
	 * Statistics save future
	 */
	protected static ScheduledFuture<?> statsFuture = null;
	/**
	 * Periodic statistics save scheduler
	 */
	protected static ScheduledExecutorService statsScheduler = null;

	/**
	 * Show the auto save statistics configuration dialog
	 * @param live Whether or not the program is already running
	 */
	protected static final void configureAutoSave(boolean live){
		JPanel endPanel = new JPanel(new BorderLayout());
		endPanel.setBorder(BorderFactory.createTitledBorder("Save on exit"));
		JCheckBox saveOnExit = new JCheckBox("Save statistics to a file on exit", Main.config.saveStatsOnExit);
		JCheckBox loadOnStart = new JCheckBox("Load saved statistics from a file on launch", Main.config.loadStatsOnLaunch);

		JPanel selectFile = new JPanel(new BorderLayout(2, 0));
		selectFile.add(new JLabel("Save location: "), BorderLayout.LINE_START);
		JTextField selectedFile = new JTextField(Main.config.statsSaveFile);
		selectFile.add(selectedFile, BorderLayout.CENTER);
		JButton select = new JButton("Select");
		selectFile.add(select, BorderLayout.LINE_END);
		select.addActionListener((e)->{
			Path dir = Dialog.showFileSaveDialog(KPS_STATS_EXT, "stats");
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
		JCheckBox enabled = new JCheckBox("Periodically save the statistics so far to a file", Main.config.autoSaveStats);
		
		BorderLayout layout = new BorderLayout();
		layout.setHgap(2);
		JPanel settings = new JPanel(layout);
		JPanel labels = new JPanel(new GridLayout(3, 1, 0, 2));
		JPanel fields = new JPanel(new GridLayout(3, 1, 0, 2));
		JPanel extras = new JPanel(new GridLayout(3, 1, 0, 2));
		
		JButton seldest = new JButton("Select");
		JTextField ldest = new JTextField(Main.config.statsDest);
		fields.add(ldest);
		extras.add(seldest);
		labels.add(new JLabel("Save location: "));
		seldest.addActionListener((e)->{
			Path dir = Dialog.showFolderOpenDialog();
			if(dir != null){
				ldest.setText(dir.toAbsolutePath().toString());
			}
		});
		
		JComboBox<Unit> timeUnit = new JComboBox<Unit>(Unit.values());
		Unit bestUnit = Unit.fromMillis(Main.config.statsSaveInterval);
		timeUnit.setSelectedItem(bestUnit);
		JSpinner time = new JSpinner(new SpinnerNumberModel(Long.valueOf(Main.config.statsSaveInterval / bestUnit.unit.toMillis(1)), Long.valueOf(1L), Long.valueOf(Long.MAX_VALUE), Long.valueOf(1L)));
		labels.add(new JLabel("Save interval: "));
		fields.add(time);
		JPanel unitPanel = new JPanel(new BorderLayout());
		unitPanel.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
		unitPanel.add(timeUnit, BorderLayout.CENTER);
		extras.add(unitPanel);
		
		JFormattedTextField format = new JFormattedTextField(new AbstractFormatterFactory(){
			@Override
			public AbstractFormatter getFormatter(JFormattedTextField tf){
				return new AbstractFormatter(){
					/**
					 * Serial ID
					 */
					private static final long serialVersionUID = 5956641218097576666L;

					@Override
					public Object stringToValue(String text) throws ParseException{
						for(char ch : new char[]{'/', '\\', '?', '%', '*', ':', '|', '"', '<', '>'}){
							int index = text.indexOf(ch);
							if(index != -1){
								throw new ParseException("Invalid character found", index);
							}
						}
						return text;
					}

					@Override
					public String valueToString(Object value) throws ParseException{
						return value instanceof String ? (String)value : null;
					}
				};
			}
		}, Main.config.statsFormat);
		format.setColumns(30);
		format.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		JButton help = new JButton("Help");
		labels.add(new JLabel("Save format: "));
		fields.add(format);
		extras.add(help);
		help.addActionListener(Statistics::showFormatHelp);
		
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
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(endPanel, BorderLayout.PAGE_START);
		panel.add(periodicPanel, BorderLayout.PAGE_END);
		if(Dialog.showSaveDialog(panel)){
			Main.config.autoSaveStats = enabled.isSelected();
			Main.config.statsDest = ldest.getText();
			Main.config.statsFormat = format.getText();
			long interval = ((Unit)timeUnit.getSelectedItem()).unit.toMillis((long)time.getValue());
			if(Main.config.autoSaveStats){
				if(interval != Main.config.statsSaveInterval){
					Main.config.statsSaveInterval = interval;
					if(live){
						saveStatsTask();
					}
				}
			}else{
				cancelScheduledTask();
				Main.config.statsSaveInterval = interval;
			}
			Main.config.saveStatsOnExit = saveOnExit.isSelected();
			Main.config.loadStatsOnLaunch = loadOnStart.isSelected();
			Main.config.statsSaveFile = selectedFile.getText();
		}
	}
	
	/**
	 * Saves the statistics so to the configured
	 * save file if stats saving on exit is enabled.
	 */
	public static void saveStatsOnExit(){
		if(Main.config.saveStatsOnExit){
			try{
				saveStats(Paths.get(Main.config.statsSaveFile));
			}catch(IOException e){
				e.printStackTrace();
				if(Dialog.showConfirmDialog("Failed to save statistics on exit.\nCause: " + e.getMessage() + "\nAttempt to save again?")){
					saveStatsOnExit();
				}
			}
		}
	}
	
	/**
	 * Shows a help dialog to the user that list some of
	 * the available {@link DateTimeFormatter} options
	 * @param event The {@link ActionEvent} from the button
	 *        that was clicked
	 */
	private static void showFormatHelp(ActionEvent event){
		JLabel help = new JLabel("<html>Format syntax:<br>"
			+ "- Escape strings with single quotes ( ' )<br>"
			+ "- A double single quote is a single quote ( '' becomes ' )<br>"
			+ "- Note that / \\ ? % * : | \" &lt and > are not allowed in file names<br>"
			+ "- <b>yyyy</b> represents the year<br>"
			+ "- <b>MM</b> represents the month of the year<br>"
			+ "- <b>dd</b> represents the day of the month<br>"
			+ "- <b>hh</b> represents the hour of the day<br>"
			+ "- <b>mm</b> represents the minute in the hour<br>"
			+ "- <b>ss</b> represents the second in the minute</html>");
		JLabel more = new JLabel("<html><font color=blue><u>More options can be found in the Javadoc for the DateTimeFormatter.</u></font></html>");
		more.addMouseListener(new ClickableLink("https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html"));
		JPanel text = new JPanel(new BorderLayout());
		text.add(help, BorderLayout.CENTER);
		text.add(more, BorderLayout.PAGE_END);
		Dialog.showMessageDialog(text);
	}
	
	/**
	 * Cancels the automatic statistics saving task
	 */
	public static void cancelScheduledTask(){
		if(statsFuture != null){
			statsFuture.cancel(false);
		}
	}
	
	/**
	 * Starts the statistics saving task or cancels the current
	 * one and stars a new one.
	 */
	public static void saveStatsTask(){
		if(statsScheduler == null){
			statsScheduler = Executors.newSingleThreadScheduledExecutor();
		}
		cancelScheduledTask();
		statsFuture = statsScheduler.scheduleAtFixedRate(()->{
			try{
				Path target = Paths.get(Main.config.statsDest);
				Files.createDirectories(target);
				target.resolve(DateTimeFormatter.ofPattern(Main.config.statsFormat).withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault()).format(Instant.now(Clock.systemDefaultZone())));
				saveStats(target);
			}catch(Exception e){
				//Main priority here is to not interrupt whatever the user is doing
				e.printStackTrace();
			}
		}, Main.config.statsSaveInterval, Main.config.statsSaveInterval, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Saves the statistics logged so far
	 * and asks the user to provide a location
	 * to save to
	 */
	protected static void saveStats(){
		Path file = Dialog.showFileSaveDialog(KPS_STATS_EXT, "stats");
		if(file != null){
			try{
				saveStats(file);
				Dialog.showMessageDialog("Statistics succesfully saved");
			}catch(IOException e){
				e.printStackTrace();
				Dialog.showErrorDialog("Failed to save the statistics!\nCause: " + e.getMessage());
			}
		}
	}

	/**
	 * Saves the statistics logged so far
	 * @param dest The file to save to
	 * @throws IOException When an IOException occurs.
	 */
	private static void saveStats(Path dest) throws IOException{
		try(PrintWriter out = new PrintWriter(Files.newBufferedWriter(dest, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING))){
			out.print("version: ");
			out.println(Main.VERSION);
			out.println();
			out.println("# General");
			out.print("total: ");
			out.println(TotPanel.hits);
			out.print("average: ");
			out.println(Main.avg);
			out.print("maximum: ");
			out.println(Main.max);
			out.print("seconds: ");
			out.println(Main.n);
			out.print("previous: ");
			out.println(Main.prev);
			out.print("current: ");
			out.println(Main.tmp.get());
			out.println();
			out.println("# Keys");
			out.println("keys:");
			for(Entry<Integer, Key> entry : Main.keys.entrySet()){
				Key key = entry.getValue();
				out.print("  - [keycode=");
				out.print(entry.getKey());
				out.print(",count=");
				out.print(key.count);
				out.print(",alt=");
				out.print(key.alt);
				out.print(",ctrl=");
				out.print(key.ctrl);
				out.print(",shift=");
				out.print(key.shift);
				out.print(",name=\"");
				out.print(key.name);
				out.println("\"]");
			}
			out.flush();
			out.close();
		}
	}
	
	/**
	 * Loads the statistics from a file, shows
	 * a prompt to the user for the file.
	 */
	protected static void loadStats(){
		Path file = Dialog.showFileOpenDialog(KPS_STATS_EXT);
		if(file == null){
			return;
		}
		
		try{
			loadStats(file);
			Dialog.showMessageDialog("Statistics succesfully loaded");
		}catch(Exception e){
			e.printStackTrace();
			Dialog.showErrorDialog("Failed to load the statistics!\nCause: " + e.getMessage());
		}
	}

	/**
	 * Loads the statistics from a file
	 * @param file The file to load from.
	 * @throws Exception When an Exception occurs.
	 */
	protected static void loadStats(Path file) throws Exception{
		try(BufferedReader in = Files.newBufferedReader(file)){
			String line;
			while((line = in.readLine()) != null){
				if(line.startsWith("#") || line.isEmpty()){
					continue;
				}
				
				String[] args = line.split(":", 2);
				String value = args.length > 1 ? args[1].trim() : null;
				switch(args[0]){
				case "version":
					break;
				case "total":
					TotPanel.hits = Integer.parseInt(value);
					break;
				case "average":
					Main.avg = Double.parseDouble(value);
					break;
				case "maximum":
					Main.max = Integer.parseInt(value);
					break;
				case "seconds":
					Main.n = Long.parseLong(value);
					break;
				case "previous":
					Main.prev = Integer.parseInt(value);
					break;
				case "current":
					Main.tmp.set(Integer.parseInt(value));
					break;
				case "keys":
					while(true){
						in.mark(100);
						line = in.readLine();
						if(line == null){
							break;
						}
						
						Matcher m = STATS_LINE_REGEX.matcher(line);
						if(m.matches()){
							int code = Integer.parseInt(m.group(1));
							Key key = Main.keys.get(code);
							if(key == null){
								key = new Key(
									m.group(6),
									Integer.parseInt(m.group(2)),
									Boolean.parseBoolean(m.group(3)),
									Boolean.parseBoolean(m.group(4)),
									Boolean.parseBoolean(m.group(5))
								);
								Main.keys.put(code, key);
							}else{
								key.count = Integer.parseInt(m.group(2));
							}
						}else{
							in.reset();
							break;
						}
					}
					break;
				default:
					throw new IllegalArgumentException("Cannot parse line: " + line);
				}
			}
			in.close();
		}catch(MalformedInputException e){
			throw new UnsupportedOperationException("Loading legacy statistics files is unsupported in this version.", e);
		}catch(Exception e){
			throw e;
		}

		Main.frame.repaint();
		Main.graphFrame.repaint();
	}
	
	/**
	 * Enum that has the different
	 * time unit values offered
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
