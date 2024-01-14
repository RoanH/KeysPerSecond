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

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.roanh.kps.Main;
import dev.roanh.kps.config.group.AveragePanelSettings;
import dev.roanh.kps.config.group.CommandSettings;
import dev.roanh.kps.config.group.CurrentPanelSettings;
import dev.roanh.kps.config.group.GraphSettings;
import dev.roanh.kps.config.group.KeyPanelSettings;
import dev.roanh.kps.config.group.LayoutSettings;
import dev.roanh.kps.config.group.MaxPanelSettings;
import dev.roanh.kps.config.group.PositionSettings;
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

/**
 * This class contains all the configurable
 * properties for the program
 * @author Roan
 */
public class Configuration{
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
	private UpdateRateSetting updateRate = new UpdateRateSetting("updateRate", UpdateRate.MS_100);
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
	private PositionSettings position = new PositionSettings();
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
	
	public Configuration(){
		this(null);
		panels.add(new MaxPanelSettings());
		panels.add(new AveragePanelSettings());
		panels.add(new CurrentPanelSettings());
	}
	
	/**
	 * Constructs a new configuration object.
	 * @param data The data file.
	 */
	public Configuration(Path data){
		this.data = data;
	}
	
	protected List<Setting<?>> getLegacySettings(Version version){
		List<Setting<?>> settings = new ArrayList<Setting<?>>();
		
		if(version.isBefore(8, 2)){
			settings.add(ProxySetting.of("trackAllKeys", trackAllKeys, trackAllButtons));
		}
		
		if(version.isBefore(8, 8)){
			statsSaving.collectLegacyProxies(settings);
			commands.collectLegacyProxies(settings);
			layout.collectLegacyProxies(settings);
			theme.collectLegacyProxies(settings);
			position.collectLegacyProxies(settings);
			
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
	
	protected List<Setting<?>> getSettings(){
		return Arrays.asList(overlay, trackAllKeys, trackAllButtons, updateRate, enableModifiers);
	}
	
	protected List<SettingGroup> getSettingGroups(){
		return Arrays.asList(position, theme, commands, layout, statsSaving);
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
	
	public PositionSettings getFramePosition(){
		return position;
	}
	
	public CommandSettings getCommands(){
		return commands;
	}
	
	public SettingList<GraphSettings> getGraphSettings(){
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
	 * Checks if overlay mode is enabled.
	 * @return True if overlay mode is enabled.
	 */
	public boolean isOverlayMode(){
		return overlay.getValue();
	}
	
	/**
	 * Sets the update rate for aggregate panels.
	 * @param rate The new update rate.
	 */
	public void setUpdateRate(UpdateRate rate){
		updateRate.update(rate);
	}
	
	/**
	 * Saves this configuration file
	 * @param pos Whether or not the ask
	 *        to save the on screen position
	 *        of the program
	 */
	public final void saveConfig(boolean pos){
		boolean savepos = (!pos) ? false : (Dialog.showConfirmDialog("Do you want to save the onscreen position of the program?"));
		Path saveloc = Dialog.showFileSaveDialog(ConfigParser.KPS_NEW_EXT, "config");
		if(saveloc != null){
			try(PrintWriter out = new PrintWriter(Files.newBufferedWriter(saveloc))){
				write(new IndentWriter(out), savepos);
				Dialog.showMessageDialog("Configuration saved succesfully.");
			}catch(Exception e1){
				e1.printStackTrace();
				Dialog.showErrorDialog("Failed to save the config!");
			}
		}
	}

	public void write(IndentWriter out, boolean pos){
		out.println("version: " + Main.VERSION);
		out.println();

		out.println("# General");
		for(Setting<?> setting : getSettings()){
			setting.write(out);
		}

		for(SettingGroup group : getSettingGroups()){
			if(group == position){
				if(pos && Main.frame.isVisible()){
					out.println();
					position.update(Main.frame.getLocationOnScreen());
					position.write(out);
				}
			}else{
				out.println();
				group.write(out);
			}
		}

		for(SettingList<?> list : getSettingLists()){
			out.println();
			list.write(out);
		}
	}
}