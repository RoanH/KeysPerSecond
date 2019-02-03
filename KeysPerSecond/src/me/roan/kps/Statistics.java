package me.roan.kps;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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
	protected static final void configureAutoSave(){//XXX autosave
		//dest folder
		//overwrite
		//interval
		//on exit
		//data
		
		//Main.statsFuture
		//Main.statsScheduler
		//TimeUnit.MICROSECONDS.toNanos(duration)
		//format DateTimeFormatter.ofPattern(...).format(Instant.now());
		
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		
		JPanel panel = new JPanel(new GridLayout(6, 1));
		panel.add(new JLabel("Periodically save the statistics so far to a file"));
		
		JPanel destInterval = new JPanel(new BorderLayout());
		JPanel labels = new JPanel(new GridLayout(2, 1, 0, 2));
		JPanel fields = new JPanel(new GridLayout(2, 1, 0, 2));
		JPanel extras = new JPanel(new GridLayout(2, 1, 0, 2));
		
		JButton seldest = new JButton("Select");
		JTextField ldest = new JTextField("");
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
		JSpinner time = new JSpinner(new SpinnerNumberModel(Main.config.statsSaveInterval / bestUnit.unit.toMillis(1), 1, Long.MAX_VALUE, 1));
		labels.add(new JLabel("Save interval: "));
		fields.add(time);
		JPanel unitPanel = new JPanel(new BorderLayout());
		unitPanel.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
		unitPanel.add(timeUnit, BorderLayout.CENTER);
		extras.add(unitPanel);
		
		destInterval.add(labels, BorderLayout.LINE_START);
		destInterval.add(fields, BorderLayout.CENTER);
		destInterval.add(extras, BorderLayout.LINE_END);
		
		
		//JCheckBox overwrite = new JCheckBox("Overwrite when saving");
		JCheckBox onExit = new JCheckBox("Attempt to save on exit");
		
		panel.add(destInterval);
		//panel.add(overwrite);
		//panel.add(date);
		panel.add(onExit);
		
		//TODO also a toggle for it all
		
		Main.showConfirmDialog(panel);
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
