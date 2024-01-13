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
package dev.roanh.kps.config.group;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import dev.roanh.kps.Statistics;
import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.SettingGroup;
import dev.roanh.kps.config.legacy.LegacyProxyStore;
import dev.roanh.kps.config.legacy.ProxySetting;
import dev.roanh.kps.config.setting.BooleanSetting;
import dev.roanh.kps.config.setting.LongSetting;
import dev.roanh.kps.config.setting.PathSetting;

/**
 * Settings for automatic statistics saving.
 * @author Roan
 * @see Statistics
 */
public class StatsSavingSettings extends SettingGroup implements LegacyProxyStore{
	/**
	 * Whether or not to periodically save the stats to a file.
	 */
	private final BooleanSetting autoSave = new BooleanSetting("autoSave", false);
	/**
	 * The folder to auto save stats to.
	 */
	private final PathSetting autoDestination = new PathSetting("autoDestination", Objects.toString(System.getProperty("user.home"), "") + File.separator + "kpsstats");
	/**
	 * The date time formatter pattern to use for the statistics auto saving file name.
	 */
	private final PathSetting autoFormat = new PathSetting("autoFormat", "'kps stats' yyyy-MM-dd HH.mm.ss'.kpsstats'");
	/**
	 * The statistics auto saving save interval in milliseconds.
	 */
	private final LongSetting autoInterval = new LongSetting("autoInterval", 1, Long.MAX_VALUE, TimeUnit.MINUTES.toMillis(10));
	/**
	 * Whether statistics are saved on exit.
	 */
	private final BooleanSetting saveOnExit = new BooleanSetting("saveOnExit", false);
	/**
	 * Whether statistics are loaded on launch.
	 */
	private final BooleanSetting loadOnLaunch = new BooleanSetting("loadOnLaunch", false);
	/**
	 * The file to save/load statistics to/from on exit/launch.
	 */
	private final PathSetting saveFile = new PathSetting("saveFile", Objects.toString(System.getProperty("user.home"), "") + File.separator + "stats.kpsstats");

	/**
	 * Constructs new stats saving settings.
	 */
	public StatsSavingSettings(){
		super("statsSaving");
	}
	
	/**
	 * Checks if periodic stats saving is enabled.
	 * @return True if periodic stats saving is enabled.
	 */
	public boolean isAutoSaveEnabled(){
		return autoSave.getValue();
	}
	
	/**
	 * Gets the folder periodic stats are saved too.
	 * @return The folder to periodically save stats to.
	 * @see #isAutoSaveEnabled()
	 */
	public String getAutoSaveDestination(){
		return autoDestination.getValue();
	}
	
	/**
	 * Gets the file name format used for periodic stats saving.
	 * @return The file name format used.
	 * @see #isAutoSaveEnabled()
	 */
	public String getAutoSaveFormat(){
		return autoFormat.getValue();
	}
	
	/**
	 * Gets the interval stats are saved at.
	 * @return The automatic stats saving interval.
	 * @see #isAutoSaveEnabled()
	 */
	public long getAutoSaveInterval(){
		return autoInterval.getValue();
	}
	
	/**
	 * Checks if saving statics on program exit is enabled.
	 * @return True if statistics are saved on exit.
	 */
	public boolean isSaveOnExitEnabled(){
		return saveOnExit.getValue();
	}
	
	/**
	 * Checks if loading statics on program launch is enabled.
	 * @return True if statistics are loaded on launch.
	 */
	public boolean isLoadOnLaunchEnabled(){
		return loadOnLaunch.getValue();
	}
	
	/**
	 * Gets the file statistics are saved and loaded from on exit and launch.
	 * @return The auto save file.
	 * @see #isSaveOnExitEnabled()
	 * @see #isLoadOnLaunchEnabled()
	 */
	public String getSaveFile(){
		return saveFile.getValue();
	}
	
	/**
	 * Enables or disables periodic stats saving.
	 * @param enabled True to enable stats saving.
	 */
	public void setAutoSaveEnabled(boolean enabled){
		autoSave.update(enabled);
	}
	
	/**
	 * Sets the folder to periodically save stats to.
	 * @param dest The stats save folder.
	 */
	public void setAutoSaveDestination(String dest){
		autoDestination.update(dest);
	}
	
	/**
	 * Sets the file name format used for periodic stats saving.
	 * @param format The file name format to use.
	 */
	public void setAutoSaveFormat(String format){
		autoFormat.update(format);
	}
	
	/**
	 * Sets the interval stats are automatically saved at.
	 * @param millis The save interval in milliseconds.
	 */
	public void setAutoSaveInterval(long millis){
		autoInterval.update(millis);
	}
	
	/**
	 * Enables or disables saving statistics on exit.
	 * @param enabled True to enable saving statistics on exit.
	 */
	public void setSaveOnExitEnabled(boolean enabled){
		saveOnExit.update(enabled);
	}
	
	/**
	 * Enables or disables loading statistics on launch.
	 * @param enabled True to enable loading statistics on launch.
	 */
	public void setLoadOnLaunchEnabled(boolean enabled){
		loadOnLaunch.update(enabled);
	}
	
	/**
	 * Sets the file that statistics are saved/loaded from on exit/launch.
	 * @param file The statistics save file.
	 */
	public void setSaveFile(String file){
		saveFile.update(file);
	}

	@Override
	public boolean parse(Map<String, String> data){
		return findAndParse(data, autoSave, autoDestination, autoFormat, autoInterval, saveOnExit, loadOnLaunch, saveFile);
	}
	
	@Override
	public void writeItems(IndentWriter out){
		autoSave.write(out);
		autoDestination.write(out);
		autoFormat.write(out);
		autoInterval.write(out);
		saveOnExit.write(out);
		loadOnLaunch.write(out);
		saveFile.write(out);
	}

	@Override
	public void collectLegacyProxies(List<Setting<?>> proxyList){
		proxyList.add(ProxySetting.of("autoSaveStats", autoSave));
		proxyList.add(ProxySetting.of("statsDest", autoDestination));
		proxyList.add(ProxySetting.of("statsFormat", autoFormat));
		proxyList.add(ProxySetting.of("statsSaveInterval", autoInterval));
		proxyList.add(ProxySetting.of("saveStatsOnExit", saveOnExit));
		proxyList.add(ProxySetting.of("loadStatsOnLaunch", loadOnLaunch));
		proxyList.add(ProxySetting.of("statsSaveFile", saveFile));
	}
}
