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
import dev.roanh.kps.config.legacy.LegacyCompatibleKeyConstructor;
import dev.roanh.kps.config.legacy.LegacyPanelShowSetting;
import dev.roanh.kps.config.legacy.ProxySetting;
import dev.roanh.kps.config.setting.BooleanSetting;
import dev.roanh.kps.config.setting.UpdateRateSetting;
import dev.roanh.kps.layout.LayoutPosition;
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
	 * The original configuration file
	 */
	private final Path data;
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
	/**
	 * Default colour scheme settings.
	 */
	private ThemeSettings theme = new ThemeSettings();
	/**
	 * Command key configuration.
	 */
	private CommandSettings commands = new CommandSettings();
	/**
	 * General layout settings.
	 */
	private LayoutSettings layout = new LayoutSettings();
	/**
	 * Automatic statistics saving settings.
	 */
	private StatsSavingSettings statsSaving = new StatsSavingSettings();
	/**
	 * Graph settings.
	 */
	private SettingList<GraphSettings> graphs = new SettingList<GraphSettings>("graphs", ListItemConstructor.constructThenParse(GraphSettings::new));
	/**
	 * Special panel settings.
	 */
	private SettingList<SpecialPanelSettings> panels = new SettingList<SpecialPanelSettings>("panels", PanelType::construct);
	/**
	 * Key panel configuration.
	 */
	private SettingList<KeyPanelSettings> keys = new SettingList<KeyPanelSettings>("keys", new LegacyCompatibleKeyConstructor());
	
	//TODO OLD LOGIC ------------------------
	
	//general
	/**
	 * Whether or not to show the max value
	 */
	@Deprecated
	public boolean showMax = true;
	/**
	 * Whether or not to show the average value
	 */
	@Deprecated
	public boolean showAvg = true;
	/**
	 * Whether or not to show the current value
	 */
	@Deprecated
	public boolean showCur = true;
	/**
	 * Whether or not to show the keys
	 */
	@Deprecated
	protected boolean showKeys = true;
	/**
	 * Whether or not to show the graph
	 */
	@Deprecated
	public boolean showGraph = false;
	/**
	 * Whether or not to show the total number of hits
	 */
	@Deprecated
	public boolean showTotal = false;

	
	public Configuration(){
		this(null);
		panels.add(new MaxPanelSettings());
		panels.add(new AveragePanelSettings());
		panels.add(new CurrentPanelSettings());
	}
	
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
		new ConfigParser().parse(Files.newBufferedReader(config), c);
		return c;
	}
	
	protected List<Setting<?>> getSettings(){
		List<Setting<?>> settings = new ArrayList<Setting<?>>();
		settings.add(overlay);
		settings.add(trackAllKeys);
		settings.add(trackAllButtons);
		settings.add(updateRate);
		settings.add(enableModifiers);
		return settings;
	}
	
	protected List<Setting<?>> getLegacySettings(Version version){
		List<Setting<?>> settings = new ArrayList<Setting<?>>();
		
		if(version.isBefore(8, 2)){
			settings.add(ProxySetting.of("trackAllKeys", false, trackAllKeys, trackAllButtons));
		}
		
		if(version.isBefore(8, 8)){
			statsSaving.collectLegacyProxies(settings);
			commands.collectLegacyProxies(settings);
			layout.collectLegacyProxies(settings);
			theme.collectLegacyProxies(settings);
			
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
			
			settings.add(new BooleanSetting("showKeys", true));
			settings.add(new LegacyPanelShowSetting("showMax", panels, max));
			settings.add(new LegacyPanelShowSetting("showAvg", panels, avg));
			settings.add(new LegacyPanelShowSetting("showCur", panels, current));
			settings.add(new LegacyPanelShowSetting("showTotal", panels, total));
			settings.add(new LegacyPanelShowSetting("graphEnabled", graphs, graph));
		}
		
		return settings;
	}
	
	protected List<SettingGroup> getSettingGroups(){
		return Arrays.asList(theme, commands, layout, statsSaving);
	}
	
	protected List<SettingList<? extends SettingGroup>> getSettingLists(){
		return Arrays.asList(graphs, panels, keys);
	}
	
	public boolean isValid(){
		return !graphs.isEmpty() || !panels.isEmpty() || !keys.isEmpty();
	}
	
	/**
	 * Gets the location on disk for this configuration file.
	 * @return The on disk location of the configuration file.
	 */
	public final Path getPath(){
		return data;
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
	
	public SettingList<GraphSettings> getGraphSettings(){
//		if(graphs.size() == 0){//TODO this is a hack
//			return new GraphSettings();
//		}
//		
//		//TODO consider empty
//		return graphs.get(0);
		
		return graphs;
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
				args[1] = args[1].trim();
				switch(args[0].trim()){
				case "position":
					Main.frame.setLocation(parsePosition(args[1]));
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
	
	//TODO write
	public void write(IndentWriter out){
		out.println("version: " + Main.VERSION);
		out.println();
		
		out.println("# General");
		for(Setting<?> setting : getSettings()){
			setting.write(out);
		}
		
		//TODO onscreen position
		
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
//				out.println("customColors: " + customColors);
//				out.println("foregroundColor: [r=" + foreground.getRed() + ",g=" + foreground.getGreen() + ",b=" + foreground.getBlue() + "]");
//				out.println("backgroundColor: [r=" + background.getRed() + ",g=" + background.getGreen() + ",b=" + background.getBlue() + "]");
//				out.println("foregroundOpacity: " + opacityfg);
//				out.println("backgroundOpacity: " + opacitybg);
				out.println();
				if(savepos && Main.frame.isVisible()){
					out.println("# Position");
					out.println("position: [x=" + Main.frame.getLocationOnScreen().x + ",y=" + Main.frame.getLocationOnScreen().y + "]");
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