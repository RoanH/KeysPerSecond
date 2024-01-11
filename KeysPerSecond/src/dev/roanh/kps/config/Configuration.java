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
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.roanh.kps.KeyInformation;
import dev.roanh.kps.Main;
import dev.roanh.kps.Statistics;
import dev.roanh.kps.config.group.AveragePanelSettings;
import dev.roanh.kps.config.group.CommandSettings;
import dev.roanh.kps.config.group.CurrentPanelSettings;
import dev.roanh.kps.config.group.GraphSettings;
import dev.roanh.kps.config.group.KeyPanelSettings;
import dev.roanh.kps.config.group.LayoutSettings;
import dev.roanh.kps.config.group.MaxPanelSettings;
import dev.roanh.kps.config.group.SpecialPanelSettings;
import dev.roanh.kps.config.group.StatsSavingSettings;
import dev.roanh.kps.config.group.ThemeSettings;
import dev.roanh.kps.config.group.TotalPanelSettings;
import dev.roanh.kps.config.setting.BooleanSetting;
import dev.roanh.kps.config.setting.ProxySetting;
import dev.roanh.kps.config.setting.UpdateRateSetting;
import dev.roanh.kps.layout.LayoutPosition;
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
	/**
	 * Whether or not the frame forces itself to be the top window
	 */
	private BooleanSetting overlay = new BooleanSetting("overlay", false);
	/**
	 * The amount of milliseconds a single time frame takes
	 */
	private UpdateRateSetting updateRate = new UpdateRateSetting("updateRate", UpdateRate.MS_1000);
	/**
	 * Whether or not the enable tracking key-modifier combinations
	 */
	private BooleanSetting enableModifiers = new BooleanSetting("enableKeyModifierCombinations", false);
	/**
	 * Whether or not to track all key presses
	 */
	private BooleanSetting trackAllKeys = new BooleanSetting("trackAllKeys", false);
	/**
	 * Whether or not to track all mouse button presses
	 */
	private BooleanSetting trackAllButtons = new BooleanSetting("trackAllButtons", false);
	
	
	
	
	
	
	
	
	
	
	
	//OLD LOGIC ------------------------
	
	
	
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
	 * Whether or not to show the total number of hits
	 */
	public boolean showTotal = false;

	//keys
	/**
	 * Key configuration data.
	 */
	private SettingList<KeyPanelSettings> keys = new SettingList<KeyPanelSettings>("keys", new LegacyCompatibleKeyConstructor());
		
	
	//colors
	//TODO themesettings is a thing
	private ThemeSettings theme = new ThemeSettings();
	/**
	 * Whether or not to use custom colors
	 */
	protected boolean customColors = false;
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

	//command keys
	private CommandSettings commands = new CommandSettings();

	//special panels / layout
	private SettingList<SpecialPanelSettings> panels = new SettingList<SpecialPanelSettings>("panels", PanelType::construct);
	
	private LayoutSettings layout = new LayoutSettings();

	//graph
	private SettingList<GraphSettings> graphs = new SettingList<GraphSettings>("graphs", ListItemConstructor.constructThenParse(GraphSettings::new));
	
	//automatic statistics saving
	private StatsSavingSettings statsSaving = new StatsSavingSettings();
	
	
	
	
	
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
		//TODO keep this way of loading data or restore the old two step method that requires a load? -- not having
		//partially initialised objects are makes more sense to me but would require more codebase changes
	}
	
	//TODO refactor into real logic or remove
	public static Configuration newLoadTemporary(Path config) throws IOException{
		Configuration c = new Configuration(config);
		new ConfigParser(null).parse(Files.newBufferedReader(config), c);
		return c;
	}
	
	protected List<Setting<?>> getSettings(){
		List<Setting<?>> settings = new ArrayList<Setting<?>>();
		
		settings.add(overlay);
		settings.add(updateRate);
		settings.add(enableModifiers);

		
		
		//TODO lots more
		
		
		return settings;
	}
	
	protected List<ProxySetting<?>> getLegacySettings(Version version){
		List<ProxySetting<?>> settings = new ArrayList<ProxySetting<?>>();
		
		if(version.isBefore(8, 2)){
			settings.add(ProxySetting.of("trackAllKeys", false, trackAllKeys, trackAllButtons));
		}
		
		if(version.isBefore(8, 8)){
			statsSaving.collectLegacyProxies(settings);
			commands.collectLegacyProxies(settings);
			layout.collectLegacyProxies(settings);
			
			GraphSettings graph = new GraphSettings();
			graph.collectLegacyProxies(settings);
			graphs.add(graph);
			
			MaxPanelSettings max = new MaxPanelSettings();
			max.collectLegacyProxies(settings);
			panels.add(max);
			
			AveragePanelSettings avg = new AveragePanelSettings();
			avg.collectLegacyProxies(settings);
			panels.add(avg);
			
			CurrentPanelSettings current = new CurrentPanelSettings();
			current.collectLegacyProxies(settings);
			panels.add(current);
			
			TotalPanelSettings total = new TotalPanelSettings();
			total.collectLegacyProxies(settings);
			panels.add(total);
		}
		
		return settings;
	}
	
	protected List<SettingGroup> getSettingGroups(){
		return Arrays.asList(statsSaving, commands, layout);//TODO
	}
	
	protected List<SettingList<? extends SettingGroup>> getSettingLists(){
		return Arrays.asList(graphs, keys, panels);//TODO
	}
	
	
	
	
	public ThemeSettings getTheme(){
		return theme;
	}
	
	public List<LayoutPosition> getLayoutComponents(){
		return Stream.concat(panels.stream(), Stream.concat(keys.stream(), graphs.stream())).collect(Collectors.toList());
	}
	
	public LayoutSettings getLayout(){
		return layout;
	}
	
	public int getCellSize(){
		return layout.getCellSize();
	}
	
	public int getBorderOffset(){
		return layout.getBorderOffset();
	}
	
	public CommandSettings getCommands(){
		return commands;
	}
	
	public GraphSettings getGraphSettings(){
		if(graphs.size() == 0){
			return new GraphSettings();
		}
		
		//TODO consider empty
		return graphs.get(0);
	}
	
	public SettingList<SpecialPanelSettings> getPanels(){
		return panels;
	}
	
	public StatsSavingSettings getStatsSavingSettings(){
		return statsSaving;
	}
	
	public SettingList<KeyPanelSettings> getKeySettings(){
		return keys;
	}
	
	public final boolean isKeyModifierTrackingEnabled(){
		return enableModifiers.getValue();
	}
	
	public void setKeyModifierTrackingEnabled(boolean enabled){
		enableModifiers.update(enabled);
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
	 * Gets the update rate for statistic panels.
	 * @return The current update rate.
	 */
	public UpdateRate getUpdateRate(){
		return updateRate.getValue();
	}
	
	/**
	 * Gets the update rate for statistic panels.
	 * @return The current update rate in milliseconds.
	 */
	public int getUpdateRateMs(){
		return updateRate.getValue().getRate();
	}
	
	/**
	 * Checks if all mouse buttons are tracked.
	 * @return True if all mouse buttons are tracked.
	 */
	public boolean isTrackAllButtons(){
		return trackAllButtons.getValue();
	}
	
	/**
	 * Checks if all keys are tracked.
	 * @return True if all keys are tracked.
	 */
	public boolean isTrackAllKeys(){
		return trackAllKeys.getValue();
	}
	
	/**
	 * Sets whether all keys are tracked.
	 * @param track True to track all keys.
	 */
	public void setTrackAllKeys(boolean track){
		trackAllKeys.update(track);
	}
	
	/**
	 * Sets whether all mouse buttons are tracked.
	 * @param track True to track all mouse buttons.
	 */
	public void setTrackAllButtons(boolean track){
		trackAllButtons.update(track);
	}
	
	/**
	 * Sets whether overlay mode is enabled.
	 * @param overlay True to enable overlay mode.
	 */
	public void setOverlayMode(boolean overlay){
		this.overlay.update(overlay);
	}
	
	/**
	 * Sets if tracked keys are shown.
	 * @param show True if tracked key panels should be visible.
	 */
	public void setShowKeys(boolean show){
		showKeys = show;
	}
	
	/**
	 * Checks if overlay mode is enabled.
	 * @return True if overlay mode is enabled.
	 */
	public boolean isOverlayMode(){
		return overlay.getValue();
	}
	
	/**
	 * Checks if tracked keys are shown.
	 * @return True if tracked key panels are visible.
	 */
	public boolean showKeys(){
		return showKeys;
	}
	
	/**
	 * Sets the update rate for aggregate panels.
	 * @param rate The new update rate.
	 */
	public void setUpdateRate(UpdateRate rate){
		updateRate.update(rate);
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
			String line;
			while((line = in.readLine()) != null){
				if(line.startsWith("#") || line.isEmpty()){
					continue;
				}
				String[] args = line.split(":", 2);
//				if(args[0].startsWith("keys")){
//					while((line = in.readLine()) != null && (line = line.trim()).startsWith("-")){
//						try{
//							keyinfo.add(parseKey(line.substring(1).trim(), defaultMode.getValue()));
//						}catch(Exception e){
//							modified = true;
//						}
//					}
//				}
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
				case "graphEnabled":
					showGraph = Boolean.parseBoolean(args[1]);
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
				case "graphPosition":
					Main.graphFrame.setLocation(parsePosition(args[1]));
					break;
				}
			}
			
			
			
			
			
			//TODO this bit of logic needs to be moved -- probably to the statistics class
			if(statsSaving.isLoadOnLaunchEnabled()){
				try{
					Statistics.loadStats(Paths.get(statsSaving.getSaveFile()));
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
	
	private void write(IndentWriter out){
		out.println("version: " + Main.VERSION);
		out.println();
		
		out.println("# General");
		for(Setting<?> setting : getSettings()){
			setting.write(out);
		}
		
		for(SettingGroup group : getSettingGroups()){
			out.println();
			group.write(out);
		}
		
		for(SettingList<?> list : getSettingLists()){
			out.println();
			list.write(out);
		}
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
//				out.println("precision: " + precision);
				out.println("enableKeyModifierCombinations: " + enableModifiers);
				out.println();
				//advanced
				out.println("# Graph");
				out.println("graphEnabled: " + showGraph);
//				out.println("graphBacklog: " + backlog);
//				out.println("graphAverage: " + graphAvg);
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
//				out.println("# Command keys");
//				out.println("keyResetStats: " + getCommandResetStats().toSaveString());
//				out.println("keyExit: " + commandExit.toSaveString());
//				out.println("keyResetTotals: " + commandResetTotals.toSaveString());
//				out.println("keyHide: " + commandHide.toSaveString());
//				out.println("keyPause: " + commandPause.toSaveString());
//				out.println("keyReload: " + commandReload.toSaveString());
				out.println();
				out.println("# Layout");
//				out.println("maxX: " + maxPanel.getX());
//				out.println("maxY: " + maxPanel.getY());
//				out.println("maxWidth: " + maxPanel.getWidth());
//				out.println("maxHeight: " + maxPanel.getHeight());
//				out.println("maxMode: " + maxPanel.getRenderingMode().name());
//				out.println("avgX: " + avgPanel.getX());
//				out.println("avgY: " + avgPanel.getY());
//				out.println("avgWidth: " + avgPanel.getWidth());
//				out.println("avgHeight: " + avgPanel.getHeight());
//				out.println("avgMode: " + avgPanel.getRenderingMode().name());
//				out.println("curX: " + curPanel.getX());
//				out.println("curY: " + curPanel.getY());
//				out.println("curWidth: " + curPanel.getWidth());
//				out.println("curHeight: " + curPanel.getHeight());
//				out.println("curMode: " + curPanel.getRenderingMode().name());
//				out.println("totX: " + totPanel.getX());
//				out.println("totY: " + totPanel.getY());
//				out.println("totWidth: " + totPanel.getWidth());
//				out.println("totHeight: " + totPanel.getHeight());
//				out.println("totMode: " + totPanel.getRenderingMode().name());
//				out.println("graphX: " + graphX);
//				out.println("graphY: " + graphY);
//				out.println("graphWidth: " + graphWidth);
//				out.println("graphHeight: " + graphHeight);
//				out.println("graphMode: " + graphMode.name());
//				out.println("cellSize: " + cellSize);
//				out.println("borderOffset: " + borderOffset);
				out.println();
//				out.println("# Stats auto saving");
//				out.println("autoSaveStats: " + autoSaveStats);
//				out.println("statsDest: " + statsDest);
//				out.println("statsFormat: " + statsFormat);
//				out.println("statsSaveInterval: " + statsSaveInterval);
//				out.println("saveStatsOnExit: " + saveStatsOnExit);
//				out.println("loadStatsOnLaunch: " + loadStatsOnLaunch);
//				out.println("statsSaveFile: " + statsSaveFile);
				out.println();
//				out.println("# Keys");
//				out.println("keys: ");
//				for(KeyInformation i : keyinfo){
//					out.println("  - " + i.toString());
//				}
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