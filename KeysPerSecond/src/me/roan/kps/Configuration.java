package me.roan.kps;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import me.roan.kps.Main.KeyInformation;

/**
 * This class contains all the configurable
 * properties for the program
 * @author Roan
 */
public class Configuration {

	//general
	/**
	 * Whether or not to show the max value
	 */
	protected boolean showMax = true;
	/**
	 * Whether or not to show the average value
	 */
	protected boolean showAvg = true;
	/**
	 * Whether or not to show the current value
	 */
	protected boolean showCur = true;
	/**
	 * Whether or not to show the keys
	 */
	protected boolean showKeys = true;
	/**
	 * Whether or not to show the graph
	 */
	protected boolean showGraph = false;
	/**
	 * Whether or not the frame forces itself to be the top window
	 */
	protected boolean overlay = false;
	/**
	 * Whether or not to use custom colors
	 */
	protected boolean customColors = false;
	/**
	 * Whether or not to track all key presses
	 */
	protected boolean trackAll = false;
	/**
	 * Whether or not to show the total number of hits
	 */
	protected boolean showTotal = false;
	
	//keys
	/**
	 * Key configuration data, can be serialised
	 */
	protected List<KeyInformation> keyinfo = new ArrayList<KeyInformation>();

	//graph
	/**
	 * Number of points the graph consists of
	 */
	protected int backlog = 30;
	/**
	 * Draw the horizontal average line
	 */
	protected boolean graphAvg = true;

	//update rate
	/**
	 * The amount of milliseconds a single time frame takes
	 */
	protected int updateRate = 1000;

	//colors
	/**
	 * Foreground color
	 */
	protected Color foreground = Color.CYAN;
	/**
	 * Background color
	 */
	protected Color background = Color.BLACK;
	/**
	 * Foreground opacity in case transparency is enabled
	 */
	protected float opacityfg = 1.0F;
	/**
	 * Background opacity in case transparency is enabled
	 */
	protected float opacitybg = 1.0F;

	//precision
	/**
	 * How many digits to display for avg
	 */
	protected int precision = 0;

	//size
	/**
	 * The factor to multiply the frame size with
	 */
	protected double size = 1.0D;
	
	/**
	 * The original configuration file
	 */
	private File data;
	
	/**
	 * Constructs a new configuration object
	 * @param data The data file
	 */
	protected Configuration(File data){
		this.data = data;
	}
	
	/**
	 * @return The background opacity
	 */
	protected final float getBackgroundOpacity(){
		return customColors ? opacitybg : 1.0F;
	}
	
	/**
	 * @return The foreground opacity
	 */
	protected final float getForegroundOpacity(){
		return customColors ? opacityfg : 1.0F;
	}
	
	/**
	 * @return The background color
	 */
	protected final Color getBackgroundColor(){
		return customColors ? background : Color.BLACK;
	}
	
	/**
	 * @return The foreground color
	 */
	protected final Color getForegroundColor(){
		return customColors ? foreground : Color.CYAN;
	}
	
	/**
	 * Reloads the config from file
	 */
	protected final void reloadConfig(){
		Configuration toLoad = new Configuration(data);
		if(data != null){
			if(data.getAbsolutePath().endsWith(".kpsconf")){
				if(toLoad.loadLegacyFormat(data)){
					Main.config = toLoad;
				}else{
					JOptionPane.showMessageDialog(null, "Failed to reload the config!", "Keys per second", JOptionPane.ERROR_MESSAGE);
				}
			}else{
				toLoad.loadNewFormat(data);
				Main.config = toLoad;
			}
		}
	}
	
	/**
	 * Loads a configuration file (with GUI)
	 * @param saveloc The save location
	 * @return Whether or not the config was loaded successfully
	 */
	protected static final boolean loadConfiguration(){
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Keys per second configuration file", "kpsconf", "kpsconf2"));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if(chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION){
			return false;
		}
		File saveloc = chooser.getSelectedFile();
		Configuration toLoad = new Configuration(saveloc);
		if(saveloc.getAbsolutePath().endsWith(".kpsconf")){
			if(toLoad.loadLegacyFormat(saveloc)){
				JOptionPane.showMessageDialog(null, "Configuration succesfully loaded", "Keys per second", JOptionPane.INFORMATION_MESSAGE);
				Main.config = toLoad;
			}else{
				JOptionPane.showMessageDialog(null, "Failed to load the config!", "Keys per second", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}else{
			if(toLoad.loadNewFormat(saveloc)){
				JOptionPane.showMessageDialog(null, "Configuration succesfully loaded but some default values were used", "Keys per second", JOptionPane.INFORMATION_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(null, "Configuration succesfully loaded", "Keys per second", JOptionPane.INFORMATION_MESSAGE);
			}
			Main.config = toLoad;
		}
		return true;
	}

	/**
	 * Loads a configuration file
	 * @param saveloc The save location
	 * @return Whether or not the config was loaded successfully
	 */
	protected final boolean loadConfig(File saveloc){
		if(saveloc.getAbsolutePath().endsWith(".kpsconf")){
			return loadLegacyFormat(saveloc);
		}else{
			loadNewFormat(saveloc);
			return true;
		}
	}
	
	/**
	 * Loads a new format configuration file
	 * @param saveloc The save location
	 * @return Whether or not some defaults were used
	 */
	private final boolean loadNewFormat(File saveloc){
		boolean modified = false;
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(saveloc)));
			String line;
			while((line = in.readLine()) != null){
				if(line.startsWith("#") || line.isEmpty()){
					continue;
				}
				String[] args = line.replace(" ", "").split(":");
				if(args[0].startsWith("keys")){
					while((line = in.readLine()) != null && (line = line.replace(" ", "")).startsWith("-")){
						try{
							keyinfo.add(parseKey(line.substring(1)));
						}catch(Exception e){
							modified = true;;
						}
					}
				}
				switch(args[0]){
				case "showMax":
					showMax = Boolean.parseBoolean(args[1]);
					break;
				case "showAvg":
					showAvg = Boolean.parseBoolean(args[1]);
					break;
				case "showCur":
					showCur = Boolean.parseBoolean(args[1]);
					break;
				case "showKeys":
					showKeys = Boolean.parseBoolean(args[1]);
					break;
				case "overlay":
					overlay = Boolean.parseBoolean(args[1]);
					break;
				case "trackAllKeys":
					trackAll = Boolean.parseBoolean(args[1]);
					break;
				case "updateRate":
					try{
						updateRate = Integer.parseInt(args[1]);
						if(1000 % updateRate != 0 || updateRate <= 0){
							updateRate = 1000;
							modified = true;
						}
					}catch(NumberFormatException e){
						updateRate = 1000;
						modified = true;
					}
					break;
				case "precision":
					try{
						precision = Integer.parseInt(args[1]);
						if(precision < 0 || precision > 3){
							precision = 0;
							modified = true;
						}
					}catch(NumberFormatException e){
						precision = 0;
						modified = true;
					}
					break;
				case "size":
					try{
						size = Double.parseDouble(args[1]);
						if(size <= 0.0D){
							size = 1.0D;
							modified = true;
						}
					}catch(NumberFormatException e){
						size = 1.0D;
						modified = true;
					}
					break;
				case "graphEnabled":
					showGraph = Boolean.parseBoolean(args[1]);
					break;
				case "graphBacklog":
					try{
						backlog = Integer.parseInt(args[1]);
						if(backlog <= 0){
							backlog = 30;
							modified = true;
						}
					}catch(NumberFormatException e){
						backlog = 30;
						modified = true;
					}
					break;
				case "graphAverage":
					graphAvg = Boolean.parseBoolean(args[1]);
					break;
				case "customColors":
					customColors = Boolean.parseBoolean(args[1]);
					break;
				case "foregroundColor":
					try{
						foreground = parseColor(args[1]);
					}catch(Exception e){
						foreground = Color.CYAN;
						modified = true;
					}
					break;
				case "backgroundColor":
					try{
						background = parseColor(args[1]);
					}catch(Exception e){
						background = Color.BLACK;
						modified = true;
					}
					break;
				case "foregroundOpacity":
					try{
						opacityfg = Float.parseFloat(args[1]);
						if(opacityfg > 1.0F || opacityfg < 0.0F){
							opacityfg = 1.0F;
							modified = true;
						}
					}catch(NumberFormatException e){
						opacityfg = 1.0F;
						modified = true;
					}
					break;
				case "backgroundOpacity":
					try{
						opacitybg = Float.parseFloat(args[1]);
						if(opacitybg > 1.0F || opacitybg < 0.0F){
							opacitybg = 1.0F;
							modified = true;
						}
					}catch(NumberFormatException e){
						opacitybg = 1.0F;
						modified = true;
					}
					break;
				case "position":
					Main.frame.setLocation(parsePosition(args[1]));
					break;
				case "showTotal":
					showTotal = Boolean.parseBoolean(args[1]);
					break;
				}
			}
			in.close();
			return modified;
		}catch(Throwable t){
			t.printStackTrace();
			return true;
		}
	}
	
	/**
	 * Parses the text representatation of a key
	 * to it's actual data
	 * @param arg The text data
	 * @return The key data
	 */
	private final KeyInformation parseKey(String arg){
		String[] args = arg.substring(1, arg.length() - 1).split(",", 4);
		String name = null;
		int index = -1;
		int code = -1;
		boolean visible = false;
		for(String str : args){
			String[] comp = str.split("=", 2);
			switch(comp[0]){
			case "keycode":
				code = Integer.parseInt(comp[1]);
				break;
			case "index":
				index = Integer.parseInt(comp[1]);
				break;
			case "visible":
				visible = Boolean.parseBoolean(comp[1]);
				break;
			case "name":
				name = comp[1].substring(1, comp[1].length() - 1);
				break;
			}
		}
		return new KeyInformation(name, code, visible, index);
	}

	/**
	 * Parses the text representation of a color
	 * to it's actual data
	 * @param arg The text data
	 * @return The color data
	 */
	private final Color parseColor(String arg){
		String[] rgb = arg.substring(1, arg.length() - 1).split(",");
		int r, g, b;
		r = g = b = 0;
		for(String c : rgb){
			String[] comp = c.split("=");
			switch(comp[0]){
			case "r":
				r = Integer.parseInt(comp[1]);
				break;
			case "g":
				g= Integer.parseInt(comp[1]);
				break;
			case "b":
				b = Integer.parseInt(comp[1]);
				break;
			}
		}
		return new Color(r, g, b);
	}
	
	/**
	 * Parses the text representation of the position
	 * to it's actual data
	 * @param arg The text data
	 * @return The position data
	 */
	private final Point parsePosition(String data){
		data = data.replace(" ", "").substring(1, data.length() - 1);
		String[] args = data.split(",");
		Point loc = new Point();
		try{
			for(String arg : args){
				if(arg.startsWith("x=")){
					loc.x = Integer.parseInt(arg.replace("x=", ""));
				}else if(arg.startsWith("y=")){
					loc.y = Integer.parseInt(arg.replace("y=", ""));
				}
			}
		}catch(Exception e){
		}
		return loc;
	}

	/**
	 * Loads a legacy configuration file
	 * @param saveloc The save location
	 * @return Whether or not the config was loaded successfully
	 */
	@SuppressWarnings("unchecked")
	private final boolean loadLegacyFormat(File saveloc){
		try {
			ObjectInputStream objin = new ObjectInputStream(new FileInputStream(saveloc));
			keyinfo = (List<KeyInformation>) objin.readObject();
			showMax = objin.readBoolean();
			showCur = objin.readBoolean();
			showAvg = objin.readBoolean();
			showGraph = objin.readBoolean();
			graphAvg = objin.readBoolean();
			backlog = objin.readInt();
			updateRate = objin.readInt();
			double version = 3.0D;
			if(objin.available() > 0){
				customColors = objin.readBoolean();
				background = (Color)objin.readObject();
				foreground = (Color)objin.readObject();
				if(objin.available() > 0){
					trackAll = objin.readBoolean();
					showKeys = objin.readBoolean();
					if(objin.available() > 0){
						version = objin.readDouble();
					}
				}
			}
			if(version >= 3.9){
				precision = objin.readInt();
			}
			if(version >= 3.10){
				opacitybg = objin.readFloat();
				opacityfg = objin.readFloat();
			}
			if(version >= 4.0D){
				size = objin.readDouble();
			}
			if(version >= 4.2D){
				overlay = objin.readBoolean();
			}
			objin.close();
			for(KeyInformation info : keyinfo){
				if(version < 3.7D){
					info.visible = true;
				}
				if(info.index > KeyInformation.autoIndex){
					KeyInformation.autoIndex = info.index + 1;
				}
			}
			return true;
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
	}

	/**
	 * Saves this configuration file
	 * @param pos Whether or not the ask
	 *        to save the on screen position
	 *        of the program
	 */
	protected final void saveConfig(boolean pos){
		boolean savepos = (!pos) ? false : (JOptionPane.showConfirmDialog(null, "Do you want to save the onscreen position of the program?", "Keys per Second", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Keys per second configuration file", "kpsconf", "kpsconf2"));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if(chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION){
			return;
		};
		File saveloc = new File(chooser.getSelectedFile().getAbsolutePath().endsWith(".kpsconf2") ? chooser.getSelectedFile().getAbsolutePath() : (chooser.getSelectedFile().getAbsolutePath() + ".kpsconf2"));
		if(!saveloc.exists() || (saveloc.exists() && JOptionPane.showConfirmDialog(null, "File already exists, overwrite?", "Keys per second", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)){
			try{
				PrintWriter out = new PrintWriter(new FileOutputStream(saveloc));
				//general
				out.println("# General");
				out.println("showMax: " + showMax);
				out.println("showAvg: " + showAvg);
				out.println("showCur: " + showCur);
				out.println("showTotal: " + showTotal);
				out.println("showKeys: " + showKeys);
				out.println("overlay: " + overlay);
				out.println("trackAllKeys: " + trackAll);
				out.println("updateRate: " + updateRate);
				out.println("precision: " + precision);
				out.println("size: " + size);
				out.println();
				//advanced
				out.println("# Graph");
				out.println("graphEnabled: " + showGraph);
				out.println("graphBacklog: " + backlog);
				out.println("graphAverage: " + graphAvg);
				out.println();
				out.println("# Colors");
				out.println("customColors: " + customColors);
				out.println("foregroundColor: [r=" + foreground.getRed() + ",g=" + foreground.getGreen() + ",b=" + foreground.getBlue() + "]");
				out.println("backgroundColor: [r=" + background.getRed() + ",g=" + background.getGreen() + ",b=" + background.getBlue() + "]");
				out.println("foregroundOpacity: " + opacityfg);
				out.println("backgroundOpacity: " + opacitybg);
				out.println();
				if(savepos){
					out.println("# Position");
					out.println("position: [x=" + Main.frame.getLocationOnScreen().x + ",y=" + Main.frame.getLocationOnScreen().y + "]");
					out.println();
				}
				out.println("# Keys");
				out.println("keys: ");
				for(KeyInformation i : keyinfo){
					out.println("  - " + i.toString());
				}
				out.close();
				out.flush();
				JOptionPane.showMessageDialog(null, "Configuration succesfully saved", "Keys per second", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null, "Failed to save the config!", "Keys per second", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
