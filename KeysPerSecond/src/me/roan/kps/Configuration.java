package me.roan.kps;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jnativehook.keyboard.NativeKeyEvent;

import me.roan.kps.CommandKeys.CMD;
import me.roan.kps.panels.BasePanel;
import me.roan.util.Dialog;

/**
 * This class contains all the configurable
 * properties for the program
 * @author Roan
 */
public class Configuration{

	//general
	/**
	 * Whether or not to show the max value
	 */
	public boolean showMax = true;
	/**
	 * Whether or not to show the average value
	 */
	public boolean showAvg = true;
	/**
	 * Whether or not to show the current value
	 */
	public boolean showCur = true;
	/**
	 * Whether or not to show the keys
	 */
	protected boolean showKeys = true;
	/**
	 * Whether or not to show the graph
	 */
	public boolean showGraph = false;
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
	protected boolean trackAllKeys = false;
	/**
	 * Whether or not to track all mouse button presses
	 */
	protected boolean trackAllButtons = false;
	/**
	 * Whether or not to show the total number of hits
	 */
	public boolean showTotal = false;
	/**
	 * Whether or not the enable tracking key-modifier combinations
	 */
	protected boolean enableModifiers = false;

	//keys
	/**
	 * Key configuration data, can be serialised
	 */
	public List<KeyInformation> keyinfo = new ArrayList<KeyInformation>();

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
	public int precision = 0;

	//command keys
	/**
	 * Reset stats command key
	 */
	protected CMD CP = new CMD(NativeKeyEvent.VC_P, false, true);
	/**
	 * Reset totals command key
	 */
	protected CMD CI = new CMD(NativeKeyEvent.VC_I, false, true);
	/**
	 * Exit command key
	 */
	protected CMD CU = new CMD(NativeKeyEvent.VC_U, false, true);
	/**
	 * Hide/show command key
	 */
	protected CMD CY = new CMD(NativeKeyEvent.VC_Y, false, true);
	/**
	 * Pause command key
	 */
	protected CMD CT = new CMD(NativeKeyEvent.VC_T, false, true);
	/**
	 * Reload command key
	 */
	protected CMD CR = new CMD(NativeKeyEvent.VC_R, false, true);

	//special panels / layout
	/**
	 * The x position of the average panel
	 */
	public int avg_x = -1;
	/**
	 * The y position of the average panel
	 */
	public int avg_y = 0;
	/**
	 * The width of the average panel
	 */
	public int avg_w = 2;
	/**
	 * The height of the average panel
	 */
	public int avg_h = 3;
	/**
	 * The text rendering mode of the average panel
	 */
	public RenderingMode avg_mode = RenderingMode.VERTICAL;
	/**
	 * The x position of the max panel
	 */
	public int max_x = -1;
	/**
	 * The y position of the max panel
	 */
	public int max_y = 0;
	/**
	 * The width position of the max panel
	 */
	public int max_w = 2;
	/**
	 * The height of the max panel
	 */
	public int max_h = 3;
	/**
	 * The text rendering mode of the max panel
	 */
	public RenderingMode max_mode = RenderingMode.VERTICAL;
	/**
	 * The x position of the current panel
	 */
	public int cur_x = -1;
	/**
	 * The y position of the current panel
	 */
	public int cur_y = 0;
	/**
	 * The width of the current panel
	 */
	public int cur_w = 2;
	/**
	 * The height of the current panel
	 */
	public int cur_h = 3;
	/**
	 * The text rendering mode of the current panel
	 */
	public RenderingMode cur_mode = RenderingMode.VERTICAL;
	/**
	 * The x position of the total panel
	 */
	public int tot_x = -1;
	/**
	 * The y position of the total panel
	 */
	public int tot_y = 0;
	/**
	 * The width of the total panel
	 */
	public int tot_w = 2;
	/**
	 * The height o the total panel
	 */
	public int tot_h = 3;
	/**
	 * The text rendering mode of the total panel
	 */
	public RenderingMode tot_mode = RenderingMode.VERTICAL;
	/**
	 * The offset from the border of a panel
	 * to the actual panel content
	 */
	public int borderOffset = 2;
	/**
	 * The pixel size of one cell in this program
	 */
	public int cellSize = 22;

	//graph
	/**
	 * Number of points the graph consists of
	 */
	public int backlog = 30;
	/**
	 * Draw the horizontal average line
	 */
	public boolean graphAvg = true;
	/**
	 * The x position of the graph
	 */
	public int graph_x = 0;
	/**
	 * The y position of the graph
	 */
	public int graph_y = -1;
	/**
	 * The width of the graph
	 */
	public int graph_w = -1;
	/**
	 * The height of the graph
	 */
	public int graph_h = 3;
	/**
	 * Position the graph is rendered in
	 */
	public GraphMode graphMode = GraphMode.INLINE;
	
	//automatic statistics saving
	/**
	 * Whether or not to periodically save the stats to a file
	 */
	public boolean autoSaveStats = false;
	/**
	 * The folder to auto save stats to
	 */
	public String statsDest = Objects.toString(System.getProperty("user.home"), "");
	/**
	 * The date time formatter pattern to use for the
	 * statis auto saving file name
	 */
	public String statsFormat = "'kps stats' yyyy-MM-dd HH.mm.ss'.kpsstats'";
	/**
	 * The statistics auto saving save interval in milliseconds
	 */
	public long statsSaveInterval = TimeUnit.MINUTES.toMillis(10);

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
	 * Gets the background opacity
	 * @return The background opacity
	 */
	public final float getBackgroundOpacity(){
		return customColors ? opacitybg : 1.0F;
	}

	/**
	 * Gets the foreground opacity
	 * @return The foreground opacity
	 */
	public final float getForegroundOpacity(){
		return customColors ? opacityfg : 1.0F;
	}

	/**
	 * Gets the background opacity
	 * @return The background color
	 */
	public final Color getBackgroundColor(){
		return customColors ? background : Color.BLACK;
	}

	/**
	 * Gets the foreground color
	 * @return The foreground color
	 */
	public final Color getForegroundColor(){
		return customColors ? foreground : Color.CYAN;
	}

	/**
	 * Reloads the configuration from file
	 */
	protected final void reloadConfig(){
		Configuration toLoad = new Configuration(data);
		if(data != null){
			if(data.getAbsolutePath().endsWith(".kpsconf")){
				if(toLoad.loadLegacyFormat(data)){
					Main.config = toLoad;
				}else{
					Dialog.showErrorDialog("Failed to reload the config!");
				}
			}else{
				boolean v2 = data.getAbsolutePath().endsWith(".kpsconf2");
				toLoad.loadNewFormat(data, v2);
				if(v2){
					toLoad.graph_x = 0;
					toLoad.graph_y = -1;
					toLoad.graph_w = -1;
					toLoad.graph_h = 3;
					toLoad.graphMode = GraphMode.INLINE;
				}
				Main.config = toLoad;
			}
		}
	}

	/**
	 * Loads a configuration file (with GUI)
	 * @return Whether or not the config was loaded successfully
	 */
	protected static final boolean loadConfiguration(){
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Keys per second configuration file", "kpsconf", "kpsconf2", "kpsconf3"));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if(chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION){
			return false;
		}
		File saveloc = chooser.getSelectedFile();
		Configuration toLoad = new Configuration(saveloc);
		if(saveloc.getAbsolutePath().endsWith(".kpsconf")){
			if(toLoad.loadLegacyFormat(saveloc)){
				Dialog.showMessageDialog("Configuration succesfully loaded");
				Main.config = toLoad;
			}else{
				Dialog.showErrorDialog("Failed to load the config!");
				return false;
			}
		}else{
			boolean v2 = saveloc.getAbsolutePath().endsWith(".kpsconf2");
			boolean defaults = toLoad.loadNewFormat(saveloc, v2);
			if(v2){
				toLoad.graph_x = 0;
				toLoad.graph_y = -1;
				toLoad.graph_w = -1;
				toLoad.graph_h = 3;
				toLoad.graphMode = GraphMode.INLINE;
				defaults = true;
			}
			if(defaults){
				Dialog.showMessageDialog("Configuration succesfully loaded but some default values were used");
			}else{
				Dialog.showMessageDialog("Configuration succesfully loaded");
			}
			Main.config = toLoad;
		}
		return true;
	}

	/**
	 * Loads a configuration file
	 * @param saveloc The save location
	 * @return Whether or not the configuration was loaded successfully
	 */
	protected final boolean loadConfig(File saveloc){
		if(saveloc.getAbsolutePath().endsWith(".kpsconf")){
			return loadLegacyFormat(saveloc);
		}else{
			boolean v2 = saveloc.getAbsolutePath().endsWith(".kpsconf2");
			loadNewFormat(saveloc, v2);
			if(v2){
				graph_x = 0;
				graph_y = -1;
				graph_w = -1;
				graph_h = 3;
				graphMode = GraphMode.INLINE;
			}
			return true;
		}
	}

	/**
	 * Loads a new format configuration file
	 * @param saveloc The save location
	 * @param v2 Whether or not the configuration is using format v2
	 * @return Whether or not some defaults were used
	 */
	private final boolean loadNewFormat(File saveloc, boolean v2){
		boolean modified = false;
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(saveloc), StandardCharsets.UTF_8));
			RenderingMode defaultMode = RenderingMode.VERTICAL;
			String line;
			while((line = in.readLine()) != null){
				if(line.startsWith("#") || line.isEmpty()){
					continue;
				}
				String[] args = line.split(":", 2);
				if(args[0].startsWith("keys")){
					while((line = in.readLine()) != null && (line = line.trim()).startsWith("-")){
						try{
							keyinfo.add(parseKey(line.substring(1).trim(), defaultMode, v2));
						}catch(Exception e){
							modified = true;
						}
					}
				}
				args[1] = args[1].trim();
				switch(args[0].trim()){
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
					trackAllKeys = Boolean.parseBoolean(args[1]);
					trackAllButtons = trackAllKeys;//for backwards compatibility
					break;
				case "trackAllButtons":
					trackAllButtons = Boolean.parseBoolean(args[1]);
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
				case "keyResetStats":
					try{
						CP = parseCommand(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "keyExit":
					try{
						CU = parseCommand(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "keyResetTotal":
					try{
						CI = parseCommand(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "keyHide":
					try{
						CY = parseCommand(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "keyPause":
					try{
						CT = parseCommand(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "keyReload":
					try{
						CR = parseCommand(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "graphPosition":
					Main.graphFrame.setLocation(parsePosition(args[1]));
					break;
				case "textMode":
					try{
						String mode = args[1].toUpperCase(Locale.ROOT);
						switch(mode){
						case "HORIZONTAL":
							defaultMode = RenderingMode.HORIZONTAL_TN;
							break;
						case "VERTICALS":
						case "HORIZONTAL_TAN":
							defaultMode = RenderingMode.VERTICAL;
							break;
						case "HORIZONTAL_TDAN":
							defaultMode = RenderingMode.DIAGONAL1;
							break;
						case "HORIZONTAL_TDAN2":
							defaultMode = RenderingMode.DIAGONAL3;
							break;
						case "HORIZONTAL_TDANS":
							defaultMode = RenderingMode.DIAGONAL1;
							break;
						case "HORIZONTAL_TDAN2S":
							defaultMode = RenderingMode.DIAGONAL3;
							break;
						default:
							defaultMode = RenderingMode.valueOf(mode);
							break;
						}
					}catch(IllegalArgumentException e){
						modified = true;
					}
					break;
				case "graphMode":
					try{
						graphMode = GraphMode.valueOf(args[1]);
					}catch(IllegalArgumentException e){
						modified = true;
					}
					break;
				case "enableKeyModifierCombinations":
					enableModifiers = Boolean.parseBoolean(args[1]);
					break;
				case "maxX":
					try{
						max_x = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "maxY":
					try{
						max_y = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "maxWidth":
					try{
						max_w = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "maxHeight":
					try{
						max_h = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "maxMode":
					try{
						max_mode = RenderingMode.valueOf(args[1]);
					}catch(IllegalArgumentException e){
						modified = true;
					}
					break;
				case "avgX":
					try{
						avg_x = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "avgY":
					try{
						avg_y = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "avgWidth":
					try{
						avg_w = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "avgHeight":
					try{
						avg_h = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "avgMode":
					try{
						avg_mode = RenderingMode.valueOf(args[1]);
					}catch(IllegalArgumentException e){
						modified = true;
					}
					break;
				case "curX":
					try{
						cur_x = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "curY":
					try{
						cur_y = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "curWidth":
					try{
						cur_w = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "curHeight":
					try{
						cur_h = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "curMode":
					try{
						cur_mode = RenderingMode.valueOf(args[1]);
					}catch(IllegalArgumentException e){
						modified = true;
					}
					break;
				case "totX":
					try{
						tot_x = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "totY":
					try{
						tot_y = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "totWidth":
					try{
						tot_w = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "totHeight":
					try{
						tot_h = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "totMode":
					try{
						tot_mode = RenderingMode.valueOf(args[1]);
					}catch(IllegalArgumentException e){
						modified = true;
					}
					break;
				case "graphX":
					try{
						graph_x = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "graphY":
					try{
						graph_y = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "graphWidth":
					try{
						graph_w = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "graphHeight":
					try{
						graph_h = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "borderOffset":
					try{
						borderOffset = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "cellSize":
					try{
						cellSize = Integer.parseInt(args[1]);
						if(cellSize < BasePanel.imageSize){
							cellSize = BasePanel.imageSize;
							modified = true;
						}
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "autoSaveStats":
					autoSaveStats = Boolean.parseBoolean(args[1]);
					break;
				case "statsSaveInterval":
					try{
						statsSaveInterval = Long.parseLong(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "statsDest":
					if(args.length > 1){
						statsDest = args[1];
					}else{
						modified = true;
					}
					break;
				case "statsFormat":
					if(args.length > 1){
						statsFormat = args[1];
					}else{
						modified = true;
					}
				}
			}
			if(borderOffset > cellSize - BasePanel.imageSize){
				borderOffset = cellSize - BasePanel.imageSize;
				modified = true;
			}
			in.close();
			return modified;
		}catch(Throwable t){
			t.printStackTrace();
			return true;
		}
	}

	/**
	 * Parses the text representation of
	 * a command key to it's actual data
	 * @param arg The text data
	 * @return The command key data
	 */
	private final CMD parseCommand(String arg){
		String[] args = arg.substring(1, arg.length() - 1).split(",");
		int code = -10;
		boolean alt = false;
		boolean ctrl = false;
		for(String str : args){
			String[] data = str.split("=");
			switch(data[0]){
			case "keycode":
				code = Integer.parseInt(data[1]);
				break;
			case "ctrl":
				ctrl = Boolean.parseBoolean(data[1]);
				break;
			case "alt":
				alt = Boolean.parseBoolean(data[1]);
				break;
			}
		}
		return new CMD(code, alt, ctrl);
	}

	/**
	 * Parses the text representation of a key
	 * to it's actual data
	 * @param arg The text data
	 * @param mode The default rendering mode to use
	 * @param v2 Whether or not the configuration is using format v2
	 * @return The key data
	 */
	private final KeyInformation parseKey(String arg, RenderingMode mode, boolean v2){
		String[] args = arg.substring(1, arg.length() - 1).split(",", v2 ? 7 : 11);
		String name = null;
		int code = -1;
		int x = -1;
		int y = 0;
		int width = 2;
		int height = 3;
		boolean visible = false;
		boolean ctrl = false;
		boolean alt = false;
		boolean shift = false;
		for(String str : args){
			String[] comp = str.split("=", 2);
			comp[1] = comp[1].trim();
			switch(comp[0].trim()){
			case "keycode":
				code = Integer.parseInt(comp[1]);
				break;
			case "visible":
				visible = Boolean.parseBoolean(comp[1]);
				break;
			case "name":
				name = comp[1].substring(1, comp[1].length() - 1);
				break;
			case "ctrl":
				ctrl = Boolean.parseBoolean(comp[1]);
				break;
			case "alt":
				alt = Boolean.parseBoolean(comp[1]);
				break;
			case "shift":
				shift = Boolean.parseBoolean(comp[1]);
				break;
			case "x":
				x = Integer.parseInt(comp[1]);
				break;
			case "y":
				y = Integer.parseInt(comp[1]);
				break;
			case "width":
				width = Integer.parseInt(comp[1]);
				break;
			case "height":
				height = Integer.parseInt(comp[1]);
				break;
			case "mode":
				mode = RenderingMode.valueOf(comp[1]);
				break;
			}
		}
		if(!CommandKeys.isNewFormat(code)){
			code = CommandKeys.getExtendedKeyCode(code % 1000, shift, ctrl, alt);
		}
		KeyInformation kinfo = new KeyInformation(name, code, visible);
		kinfo.x = x;
		kinfo.y = y;
		kinfo.width = width;
		kinfo.height = height;
		kinfo.mode = mode;
		return kinfo;
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
				g = Integer.parseInt(comp[1]);
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
	 * @param data The text data
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
	 * @return Whether or not the configuration was loaded successfully
	 */
	@SuppressWarnings("unchecked")
	private final boolean loadLegacyFormat(File saveloc){
		try{
			ObjectInputStream objin = new ObjectInputStream(new FileInputStream(saveloc));
			keyinfo = (List<KeyInformation>)objin.readObject();
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
					trackAllKeys = objin.readBoolean();
					trackAllButtons = trackAllKeys;
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
				objin.readDouble();
			}
			if(version >= 4.2D){
				overlay = objin.readBoolean();
			}
			objin.close();
			for(KeyInformation info : keyinfo){
				if(version < 3.7D){
					info.visible = true;
				}
				KeyInformation.autoIndex = keyinfo.size() * 2 + 2;
			}
			return true;
		}catch(Exception e1){
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
		boolean savepos = (!pos) ? false : (Dialog.showConfirmDialog("Do you want to save the onscreen position of the program?"));
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Keys per second configuration file", "kpsconf3"));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if(chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION){
			return;
		}
		File saveloc = new File(chooser.getSelectedFile().getAbsolutePath().endsWith(".kpsconf3") ? chooser.getSelectedFile().getAbsolutePath() : (chooser.getSelectedFile().getAbsolutePath() + ".kpsconf3"));
		if(!saveloc.exists() || (saveloc.exists() && Dialog.showConfirmDialog("File already exists, overwrite?"))){
			try{
				PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(saveloc), StandardCharsets.UTF_8));
				//general
				out.println("# General");
				out.println("showMax: " + showMax);
				out.println("showAvg: " + showAvg);
				out.println("showCur: " + showCur);
				out.println("showTotal: " + showTotal);
				out.println("showKeys: " + showKeys);
				out.println("overlay: " + overlay);
				out.println("trackAllKeys: " + trackAllKeys);
				out.println("trackAllButtons: " + trackAllButtons);
				out.println("updateRate: " + updateRate);
				out.println("precision: " + precision);
				out.println("enableKeyModifierCombinations: " + enableModifiers);
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
				if(savepos && (Main.frame.isVisible() || Main.graphFrame.isVisible())){
					out.println("# Position");
					if(Main.frame.isVisible()){
						out.println("position: [x=" + Main.frame.getLocationOnScreen().x + ",y=" + Main.frame.getLocationOnScreen().y + "]");
					}
					if(Main.graphFrame.isVisible()){
						out.println("graphPosition: [x=" + Main.graphFrame.getLocationOnScreen().x + ",y=" + Main.graphFrame.getLocationOnScreen().y + "]");
					}
					out.println();
				}
				out.println("# Command keys");
				out.println("keyResetStats: " + CP.toSaveString());
				out.println("keyExit: " + CU.toSaveString());
				out.println("keyResetTotals: " + CI.toSaveString());
				out.println("keyHide: " + CY.toSaveString());
				out.println("keyPause: " + CT.toSaveString());
				out.println("keyReload: " + CR.toSaveString());
				out.println();
				out.println("# Layout");
				out.println("maxX: " + max_x);
				out.println("maxY: " + max_y);
				out.println("maxWidth: " + max_w);
				out.println("maxHeight: " + max_h);
				out.println("maxMode: " + max_mode.name());
				out.println("avgX: " + avg_x);
				out.println("avgY: " + avg_y);
				out.println("avgWidth: " + avg_w);
				out.println("avgHeight: " + avg_h);
				out.println("avgMode: " + avg_mode.name());
				out.println("curX: " + cur_x);
				out.println("curY: " + cur_y);
				out.println("curWidth: " + cur_w);
				out.println("curHeight: " + cur_h);
				out.println("curMode: " + cur_mode.name());
				out.println("totX: " + tot_x);
				out.println("totY: " + tot_y);
				out.println("totWidth: " + tot_w);
				out.println("totHeight: " + tot_h);
				out.println("totMode: " + tot_mode.name());
				out.println("graphX: " + graph_x);
				out.println("graphY: " + graph_y);
				out.println("graphWidth: " + graph_w);
				out.println("graphHeight: " + graph_h);
				out.println("graphMode: " + graphMode.name());
				out.println("cellSize: " + cellSize);
				out.println("borderOffset: " + borderOffset);
				out.println();
				out.println("# Stats auto saving");
				out.println("autoSaveStats: " + autoSaveStats);
				out.println("statsDest: " + statsDest);
				out.println("statsFormat: " + statsFormat);
				out.println("statsSaveInterval: " + statsSaveInterval);
				out.println();
				out.println("# Keys");
				out.println("keys: ");
				for(KeyInformation i : keyinfo){
					out.println("  - " + i.toString());
				}
				out.close();
				out.flush();
				Dialog.showMessageDialog("Configuration succesfully saved");
			}catch(Exception e1){
				e1.printStackTrace();
				Dialog.showErrorDialog("Failed to save the config!");
			}
		}
	}
}
