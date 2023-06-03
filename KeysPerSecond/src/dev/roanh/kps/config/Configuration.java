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
package dev.roanh.kps.config;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import dev.roanh.kps.GraphMode;
import dev.roanh.kps.KeyInformation;
import dev.roanh.kps.Main;
import dev.roanh.kps.RenderingMode;
import dev.roanh.kps.Statistics;
import dev.roanh.kps.CommandKeys.CMD;
import dev.roanh.kps.layout.Positionable;
import dev.roanh.kps.panels.BasePanel;
import dev.roanh.util.Dialog;
import dev.roanh.util.FileSelector;
import dev.roanh.util.FileSelector.FileExtension;

/**
 * This class contains all the configurable
 * properties for the program
 * @author Roan
 */
public class Configuration{
	/**
	 * Extension filter for all KeysPerSecond configuration files.
	 */
	private static final FileExtension KPS_ALL_EXT = FileSelector.registerFileExtension("KeysPerSecond config", "kps", "kpsconf", "kpsconf2", "kpsconf3");
	/**
	 * Extension filter for the current KeysPerSecond configuration file format.
	 */
	private static final FileExtension KPS_NEW_EXT = FileSelector.registerFileExtension("KeysPerSecond config", "kps");
	/**
	 * Extension filter for legacy KeysPerSecond configuration file formats.
	 */
	private static final FileExtension KPS_LEGACY_EXT = FileSelector.registerFileExtension("Legacy KeysPerSecond config", "kpsconf", "kpsconf2", "kpsconf3");

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
	public boolean enableModifiers = false;

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
	public Color foreground = Color.CYAN;
	/**
	 * Background color
	 */
	public Color background = Color.BLACK;
	/**
	 * Foreground opacity in case transparency is enabled
	 */
	public float opacityfg = 1.0F;
	/**
	 * Background opacity in case transparency is enabled
	 */
	public float opacitybg = 1.0F;

	//precision
	/**
	 * How many digits to display for avg
	 */
	public int precision = 0;

	//command keys
	/**
	 * Reset stats command key
	 */
	private CMD commandResetStats = new CMD(NativeKeyEvent.VC_P, false, true);
	/**
	 * Reset totals command key
	 */
	private CMD commandResetTotals = new CMD(NativeKeyEvent.VC_I, false, true);
	/**
	 * Exit command key
	 */
	private CMD commandExit = new CMD(NativeKeyEvent.VC_U, false, true);
	/**
	 * Hide/show command key
	 */
	private CMD commandHide = new CMD(NativeKeyEvent.VC_Y, false, true);
	/**
	 * Pause command key
	 */
	private CMD commandPause = new CMD(NativeKeyEvent.VC_T, false, true);
	/**
	 * Reload command key
	 */
	private CMD commandReload = new CMD(NativeKeyEvent.VC_R, false, true);

	//special panels / layout
	/**
	 * Configuration for the average panel.
	 */
	public Positionable avgPanel = new Positionable(-1, 0, 2, 3, RenderingMode.VERTICAL){
		
		@Override
		public String getName(){
			return "AVG";
		}
	};
	/**
	 * Configuration for the max panel.
	 */
	public Positionable maxPanel = new Positionable(-1, 0, 2, 3, RenderingMode.VERTICAL){
		
		@Override
		public String getName(){
			return "MAX";
		}
	};
	/**
	 * Configuration for the current panel.
	 */
	public Positionable curPanel = new Positionable(-1, 0, 2, 3, RenderingMode.VERTICAL){
		
		@Override
		public String getName(){
			return "CUR";
		}
	};
	/**
	 * Configuration for the total panel.
	 */
	public Positionable totPanel = new Positionable(-1, 0, 2, 3, RenderingMode.VERTICAL){
		
		@Override
		public String getName(){
			return "AVG";
		}
	};
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
	 * The x position of the graph (-1 is end)
	 */
	private int graphX = 0;
	/**
	 * The y position of the graph (-1 is end)
	 */
	private int graphY = -1;
	/**
	 * The width of the graph (-1 is max)
	 */
	private int graphWidth = -1;
	/**
	 * The height of the graph (-1 is max)
	 */
	private int graphHeight = 3;
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
	 * statistics auto saving file name
	 */
	public String statsFormat = "'kps stats' yyyy-MM-dd HH.mm.ss'.kpsstats'";
	/**
	 * The statistics auto saving save interval in milliseconds
	 */
	public long statsSaveInterval = TimeUnit.MINUTES.toMillis(10);
	/**
	 * Whether statistics are saved on exit.
	 */
	public boolean saveStatsOnExit = false;
	/**
	 * Whether statistics are loaded on launch.
	 */
	public boolean loadStatsOnLaunch = false;
	/**
	 * The file to save/load statistics to/from on exit/launch.
	 */
	public String statsSaveFile = Objects.toString(System.getProperty("user.home"), "") + File.separator + "stats.kpsstats";

	/**
	 * The original configuration file
	 */
	private Path data;
	
	/**
	 * Constructs a new configuration object
	 * @param data The data file
	 */
	public Configuration(Path data){
		this.data = data;
	}
	
	/**
	 * Gets the x position of the graph.
	 * @return The x position of the graph.
	 */
	public final int getGraphX(){
		return graphX;
	}
	
	/**
	 * Gets the y position of the graph.
	 * @return The y position of the graph.
	 */
	public final int getGraphY(){
		return graphY;
	}
	
	/**
	 * Gets the width of the graph.
	 * @return The width of the graph.
	 */
	public final int getGraphWidth(){
		return graphWidth;
	}	
	
	/**
	 * Gets the height of the graph.
	 * @return The height of the graph.
	 */
	public final int getGraphHeight(){
		return graphHeight;
	}
	
	/**
	 * Sets the x position of the graph.
	 * @param x The new x position.
	 */
	public final void setGraphX(int x){
		graphX = x;
	}
	
	/**
	 * Sets the y position of the graph.
	 * @param y The new y position.
	 */
	public final void setGraphY(int y){
		graphY = y;
	}
	
	/**
	 * Sets the width of the graph in cells.
	 * @param width The new width of the graph.
	 */
	public final void setGraphWidth(int width){
		graphWidth = width;
	}
	
	/**
	 * Sets the height of the graph in cells.
	 * @param height The new height of the graph.
	 */
	public final void setGraphHeight(int height){
		graphHeight = height;
	}
	
	/**
	 * Gets the location on disk for this configuration file.
	 * @return The on disk location of the configuration file.
	 */
	public final Path getPath(){
		return data;
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
	 * Gets the command for resetting stats.
	 * @return The command for resetting stats.
	 */
	public CMD getCommandResetStats(){
		return commandResetStats;
	}

	/**
	 * Sets the command for resetting stats.
	 * @param command The new command for resetting stats.
	 */
	public void setCommandResetStats(CMD command){
		commandResetStats = command;
	}

	/**
	 * Gets the command for resetting totals.
	 * @return The command for resetting totals.
	 */
	public CMD getCommandResetTotals(){
		return commandResetTotals;
	}

	/**
	 * Sets the command for resetting totals.
	 * @param command The new command for resetting totals.
	 */
	public void setCommandResetTotals(CMD command){
		commandResetTotals = command;
	}
	
	/**
	 * Gets the command for hiding the window.
	 * @return The command for hiding the window.
	 */
	public CMD getCommandHide(){
		return commandHide;
	}

	/**
	 * Sets the command for hiding the window.
	 * @param command The new command for hiding the window.
	 */
	public void setCommandHide(CMD command){
		commandHide = command;
	}
	
	/**
	 * Gets the command for pausing updates.
	 * @return The command for pausing updates.
	 */
	public CMD getCommandPause(){
		return commandPause;
	}

	/**
	 * Sets the command for pausing updates.
	 * @param command The new command for pausing updates.
	 */
	public void setCommandPause(CMD command){
		commandPause = command;
	}
	
	/**
	 * Gets the command for reloading the configuration.
	 * @return The command for reloading the configuration.
	 */
	public CMD getCommandReload(){
		return commandReload;
	}

	/**
	 * Sets the command for reloading the configuration.
	 * @param command The new command for reloading the configuration.
	 */
	public void setCommandReload(CMD command){
		commandReload = command;
	}
	
	/**
	 * Gets the command for exiting the application.
	 * @return The command for exiting the application.
	 */
	public CMD getCommandExit(){
		return commandExit;
	}

	/**
	 * Sets the command for exiting the application.
	 * @param command The new command for exiting the application.
	 */
	public void setCommandExit(CMD command){
		commandExit = command;
	}
	
	public int getUpdateRate(){
		return updateRate;
	}
	
	public boolean isTrackAllButtons(){
		return trackAllButtons;
	}
	
	public boolean isTrackAllKeys(){
		return trackAllKeys;
	}
	
	public void setTrackAllKeys(boolean track){
		trackAllKeys = track;
	}
	
	public void setTrackAllButtons(boolean track){
		trackAllButtons = track;
	}
	
	public void setOverlayMode(boolean overlay){
		this.overlay = overlay;
	}
	
	public void setCustomColors(boolean custom){
		customColors = custom;
	}
	
	public void setShowKeys(boolean show){
		showKeys = show;
	}
	
	public boolean hasCustomColors(){
		return customColors;
	}
	
	public boolean isOverlayMode(){
		return overlay;
	}
	
	public boolean showKeys(){
		return showKeys;
	}
	
	public void setUpdateRate(int rate){
		updateRate = rate;
	}
	
	/**
	 * Reloads the configuration from file
	 */
	public final void reloadConfig(){
		Configuration toLoad = new Configuration(data);
		if(data != null){
			toLoad.load(data);
			Main.config = toLoad;
		}
	}
	
	/**
	 * Loads a configuration file (with GUI)
	 * @return Whether or not the config was loaded successfully
	 */
	public static final boolean loadConfiguration(){
		Path saveloc = Dialog.showFileOpenDialog(KPS_ALL_EXT, KPS_NEW_EXT, KPS_LEGACY_EXT);
		if(saveloc == null){
			return false;
		}else if(saveloc.getFileName().toString().endsWith("kpsconf") || saveloc.getFileName().toString().endsWith("kpsconf2")){
			Dialog.showMessageDialog(
				"You are trying to load a legacy configuration file.\n"
				+ "This is no longer possible with this version of the program.\n"
				+ "You should convert your configuration file first using version 8.4."
			);
			return false;
		}
		Configuration toLoad = new Configuration(saveloc);
		if(toLoad.load(saveloc)){
			Dialog.showMessageDialog("Configuration succesfully loaded but some default values were used");
		}else{
			Dialog.showMessageDialog("Configuration succesfully loaded");
		}
		Main.config = toLoad;
		return true;
	}

	/**
	 * Loads a configuration file silently without
	 * reporting non critical exceptions to the user
	 * @param saveloc The save location
	 * @return Whether or not the configuration was loaded successfully
	 */
	public final boolean loadConfig(Path saveloc){
		load(saveloc);
		return true;
	}

	/**
	 * Loads a configuration file
	 * @param saveloc The save location
	 * @return Whether or not some defaults were used
	 */
	private final boolean load(Path saveloc){
		boolean modified = false;
		try(BufferedReader in = Files.newBufferedReader(saveloc)){
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
							keyinfo.add(parseKey(line.substring(1).trim(), defaultMode));
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
						setCommandResetStats(parseCommand(args[1]));
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "keyExit":
					try{
						commandExit = parseCommand(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "keyResetTotals":
					try{
						commandResetTotals = parseCommand(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "keyHide":
					try{
						commandHide = parseCommand(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "keyPause":
					try{
						commandPause = parseCommand(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "keyReload":
					try{
						commandReload = parseCommand(args[1]);
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
						maxPanel.setX(Integer.parseInt(args[1]));
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "maxY":
					try{
						maxPanel.setY(Integer.parseInt(args[1]));
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "maxWidth":
					try{
						maxPanel.setWidth(Integer.parseInt(args[1]));
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "maxHeight":
					try{
						maxPanel.setHeight(Integer.parseInt(args[1]));
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "maxMode":
					try{
						maxPanel.setRenderingMode(RenderingMode.valueOf(args[1]));
					}catch(IllegalArgumentException e){
						modified = true;
					}
					break;
				case "avgX":
					try{
						avgPanel.setX(Integer.parseInt(args[1]));
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "avgY":
					try{
						avgPanel.setY(Integer.parseInt(args[1]));
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "avgWidth":
					try{
						avgPanel.setWidth(Integer.parseInt(args[1]));
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "avgHeight":
					try{
						avgPanel.setHeight(Integer.parseInt(args[1]));
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "avgMode":
					try{
						avgPanel.setRenderingMode(RenderingMode.valueOf(args[1]));
					}catch(IllegalArgumentException e){
						modified = true;
					}
					break;
				case "curX":
					try{
						curPanel.setX(Integer.parseInt(args[1]));
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "curY":
					try{
						curPanel.setY(Integer.parseInt(args[1]));
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "curWidth":
					try{
						curPanel.setWidth(Integer.parseInt(args[1]));
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "curHeight":
					try{
						curPanel.setHeight(Integer.parseInt(args[1]));
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "curMode":
					try{
						curPanel.setRenderingMode(RenderingMode.valueOf(args[1]));
					}catch(IllegalArgumentException e){
						modified = true;
					}
					break;
				case "totX":
					try{
						totPanel.setX(Integer.parseInt(args[1]));
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "totY":
					try{
						totPanel.setY(Integer.parseInt(args[1]));
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "totWidth":
					try{
						totPanel.setWidth(Integer.parseInt(args[1]));
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "totHeight":
					try{
						totPanel.setHeight(Integer.parseInt(args[1]));
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "totMode":
					try{
						totPanel.setRenderingMode(RenderingMode.valueOf(args[1]));
					}catch(IllegalArgumentException e){
						modified = true;
					}
					break;
				case "graphX":
					try{
						graphX = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "graphY":
					try{
						graphY = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "graphWidth":
					try{
						graphWidth = Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						modified = true;
					}
					break;
				case "graphHeight":
					try{
						graphHeight = Integer.parseInt(args[1]);
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
					break;
				case "saveStatsOnExit":
					saveStatsOnExit = Boolean.parseBoolean(args[1]);
					break;
				case "loadStatsOnLaunch":
					loadStatsOnLaunch = Boolean.parseBoolean(args[1]);
					break;
				case "statsSaveFile":
					statsSaveFile = args[1];
					break;
				}
			}
			if(borderOffset > cellSize - BasePanel.imageSize){
				borderOffset = cellSize - BasePanel.imageSize;
				modified = true;
			}
			if(loadStatsOnLaunch){
				try{
					Statistics.loadStats(Paths.get(statsSaveFile));
				}catch(Exception e){
					e.printStackTrace();
					Dialog.showMessageDialog("Failed to load statistics on launch.\nCause: " + e.getMessage());
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
	 * Parses the text representation of
	 * a command key to it's actual data
	 * @param arg The text data
	 * @return The command key data
	 */
	private final CMD parseCommand(String arg){
		if(arg.equals(CMD.NONE.toSaveString())){
			return CMD.NONE;
		}
		
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
	 * @return The key data
	 */
	private final KeyInformation parseKey(String arg, RenderingMode mode){
		String[] args = arg.substring(1, arg.length() - 1).split(",", 8);
		String name = null;
		int code = -1;
		int x = -1;
		int y = 0;
		int width = 2;
		int height = 3;
		boolean visible = false;
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
		return new KeyInformation(name, code, visible, x, y, width, height, mode);
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
	 * Saves this configuration file
	 * @param pos Whether or not the ask
	 *        to save the on screen position
	 *        of the program
	 */
	public final void saveConfig(boolean pos){
		boolean savepos = (!pos) ? false : (Dialog.showConfirmDialog("Do you want to save the onscreen position of the program?"));
		Path saveloc = Dialog.showFileSaveDialog(KPS_NEW_EXT, "config");
		if(saveloc != null){
			try(PrintWriter out = new PrintWriter(Files.newBufferedWriter(saveloc, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE))){
				//general
				out.print("version: ");
				out.println(Main.VERSION);
				out.println();
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
				out.println("keyResetStats: " + getCommandResetStats().toSaveString());
				out.println("keyExit: " + commandExit.toSaveString());
				out.println("keyResetTotals: " + commandResetTotals.toSaveString());
				out.println("keyHide: " + commandHide.toSaveString());
				out.println("keyPause: " + commandPause.toSaveString());
				out.println("keyReload: " + commandReload.toSaveString());
				out.println();
				out.println("# Layout");
				out.println("maxX: " + maxPanel.getX());
				out.println("maxY: " + maxPanel.getY());
				out.println("maxWidth: " + maxPanel.getWidth());
				out.println("maxHeight: " + maxPanel.getHeight());
				out.println("maxMode: " + maxPanel.getRenderingMode().name());
				out.println("avgX: " + avgPanel.getX());
				out.println("avgY: " + avgPanel.getY());
				out.println("avgWidth: " + avgPanel.getWidth());
				out.println("avgHeight: " + avgPanel.getHeight());
				out.println("avgMode: " + avgPanel.getRenderingMode().name());
				out.println("curX: " + curPanel.getX());
				out.println("curY: " + curPanel.getY());
				out.println("curWidth: " + curPanel.getWidth());
				out.println("curHeight: " + curPanel.getHeight());
				out.println("curMode: " + curPanel.getRenderingMode().name());
				out.println("totX: " + totPanel.getX());
				out.println("totY: " + totPanel.getY());
				out.println("totWidth: " + totPanel.getWidth());
				out.println("totHeight: " + totPanel.getHeight());
				out.println("totMode: " + totPanel.getRenderingMode().name());
				out.println("graphX: " + graphX);
				out.println("graphY: " + graphY);
				out.println("graphWidth: " + graphWidth);
				out.println("graphHeight: " + graphHeight);
				out.println("graphMode: " + graphMode.name());
				out.println("cellSize: " + cellSize);
				out.println("borderOffset: " + borderOffset);
				out.println();
				out.println("# Stats auto saving");
				out.println("autoSaveStats: " + autoSaveStats);
				out.println("statsDest: " + statsDest);
				out.println("statsFormat: " + statsFormat);
				out.println("statsSaveInterval: " + statsSaveInterval);
				out.println("saveStatsOnExit: " + saveStatsOnExit);
				out.println("loadStatsOnLaunch: " + loadStatsOnLaunch);
				out.println("statsSaveFile: " + statsSaveFile);
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