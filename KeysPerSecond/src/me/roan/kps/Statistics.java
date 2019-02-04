package me.roan.kps;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import me.roan.kps.Main.Key;
import me.roan.kps.panels.TotPanel;
import me.roan.kps.Main.ClickableLink;

/**
 * Class that handles most of the more complex
 * matters related to statistics.
 * @author Roan
 */
public class Statistics{
	/**
	 * Stats save future
	 */
	protected static ScheduledFuture<?> statsFuture = null;
	/**
	 * Periodic stats save scheduler
	 */
	protected static ScheduledExecutorService statsScheduler = null;

	/**
	 * Show the auto save statistics configuration dialog
	 */
	protected static final void configureAutoSave(){
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		
		JPanel panel = new JPanel(new BorderLayout());
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
			if(chooser.showOpenDialog(Main.frame) == JFileChooser.APPROVE_OPTION){
				ldest.setText(chooser.getSelectedFile().getAbsolutePath());
			}
		});
		
		JComboBox<Unit> timeUnit = new JComboBox<Unit>(Unit.values());
		Unit bestUnit = Unit.fromMillis(Main.config.statsSaveInterval);
		timeUnit.setSelectedItem(bestUnit);
		JSpinner time = new JSpinner(new SpinnerNumberModel(new Long(Main.config.statsSaveInterval / bestUnit.unit.toMillis(1)), new Long(1), new Long(Long.MAX_VALUE), new Long(1)));
		labels.add(new JLabel("Save interval: "));
		fields.add(time);
		JPanel unitPanel = new JPanel(new BorderLayout());
		unitPanel.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
		unitPanel.add(timeUnit, BorderLayout.CENTER);
		extras.add(unitPanel);
		
		JTextField format = new JTextField(Main.config.statsFormat);
		JButton help = new JButton("Help");
		labels.add(new JLabel("Save format: "));
		fields.add(format);
		extras.add(help);
		help.addActionListener(Statistics::showFormatHelp);
		
		settings.add(labels, BorderLayout.LINE_START);
		settings.add(fields, BorderLayout.CENTER);
		settings.add(extras, BorderLayout.LINE_END);
		
		panel.add(enabled, BorderLayout.PAGE_START);
		panel.add(settings, BorderLayout.CENTER);
		
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
				
		if(Main.showOptionDialog(panel)){
			Main.config.autoSaveStats = enabled.isSelected();
			Main.config.statsDest = ldest.getText();
			Main.config.statsFormat = format.getText();
			long interval = ((Unit)timeUnit.getSelectedItem()).unit.toMillis((long)time.getValue());
			if(Main.config.autoSaveStats){
				if(interval != Main.config.statsSaveInterval){
					Main.config.statsSaveInterval = interval;
					saveStatsTask();
				}
			}else{
				cancelScheduledTask();
				Main.config.statsSaveInterval = interval;
			}
		}
	}
	
	private static void showFormatHelp(ActionEvent event){
		JLabel help = new JLabel("<html>Format syntax:<br>"
			+ "- Escape strings with single quotes ( ' )<br>"
			+ "- A double single quote is a single quote ( '' becomes ' )<br>"
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
		Main.showMessageDialog(text);
	}
	
	private static void cancelScheduledTask(){
		if(statsFuture != null){
			statsFuture.cancel(false);
		}
	}
	
	private static void saveStatsTask(){
		//format DateTimeFormatter.ofPattern(...).format(Instant.now());
		cancelScheduledTask();
		statsFuture = statsScheduler.scheduleAtFixedRate(()->{
			
		}, Main.config.statsSaveInterval, Main.config.statsSaveInterval, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Saves the statistics logged so far
	 * and asks the user to provide a location
	 * to save to
	 */
	protected static void saveStats(){
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Keys per second statistics file", "kpsstats"));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if(chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION){
			return;
		}
		File file = new File(chooser.getSelectedFile().getAbsolutePath().endsWith(".kpsstats") ? chooser.getSelectedFile().getAbsolutePath() : (chooser.getSelectedFile().getAbsolutePath() + ".kpsstats"));
		if(!file.exists() || (file.exists() && Main.showConfirmDialog("File already exists, overwrite?"))){
			if(saveStats(file)){
				Main.showMessageDialog("Statistics succesfully saved");
			}else{
				Main.showErrorDialog("Failed to save the statistics!");
			}
		}
	}

	/**
	 * Saves the statistics logged so far
	 * @param dest The file to save to
	 * @return True if saving was successful, 
	 *         false otherwise
	 */
	private static boolean saveStats(File dest){
		try{
			dest.createNewFile();
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dest));
			out.writeInt(TotPanel.hits);
			out.writeDouble(Main.avg);
			out.writeInt(Main.max);
			out.writeLong(Main.n);
			out.writeInt(Main.prev);
			out.writeInt(Main.tmp.get());
			for(Entry<Integer, Key> key : Main.keys.entrySet()){
				out.writeInt(key.getKey());
				out.writeObject(key.getValue());
			}
			out.flush();
			out.close();
			return true;
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Loads the statistics from a file
	 */
	protected static void loadStats(){
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Keys per second statistics file", "kpsstats"));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if(chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION){
			return;
		}
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(chooser.getSelectedFile()));
			TotPanel.hits = in.readInt();
			Main.avg = in.readDouble();
			Main.max = in.readInt();
			Main.n = in.readLong();
			Main.prev = in.readInt();
			Main.tmp.set(in.readInt());
			while(in.available() > 0){
				int code = in.readInt();
				Key key = Main.keys.get(code);
				Key obj = ((Key)in.readObject());
				if(key != null){
					key.count = obj.count;
				}else{
					Main.keys.put(code, obj);
				}
			}
			in.close();
			Main.frame.repaint();
			Main.graphFrame.repaint();
			Main.showMessageDialog("Statistics succesfully loaded");
		}catch(IOException | ClassNotFoundException e){
			Main.showErrorDialog("Failed to load the statistics!");
		}
	}
	
	private static enum Unit{
		HOUR("Hours", TimeUnit.HOURS, null),
		MINUTE("Minutes", TimeUnit.MINUTES, HOUR),
		SECOND("Seconds", TimeUnit.SECONDS, MINUTE),
		MILLISECOND("Milliseconds", TimeUnit.MILLISECONDS, SECOND);
		
		private TimeUnit unit;
		private String name;
		private Unit up;
		
		private Unit(String name, TimeUnit unit, Unit up){
			this.name = name;
			this.unit = unit;
			this.up = up;
		}
		
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
