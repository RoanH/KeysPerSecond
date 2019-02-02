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

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
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
		
		JPanel dest = new JPanel(new BorderLayout());
		JButton seldest = new JButton("Select");
		JTextField ldest = new JTextField("");
		dest.add(ldest, BorderLayout.CENTER);
		dest.add(seldest, BorderLayout.LINE_END);
		dest.add(new JLabel("Save location: "), BorderLayout.LINE_START);
		seldest.addActionListener((e)->{
			if(chooser.showOpenDialog(Main.frame) == JFileChooser.APPROVE_OPTION){
				ldest.setText(chooser.getSelectedFile().getAbsolutePath());
			}
		});
		
		
		
		
		//JCheckBox overwrite = new JCheckBox("Overwrite when saving");
		JCheckBox onExit = new JCheckBox("Attempt to save on exit");
		
		panel.add(dest);
		//panel.add(comp);
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
}
