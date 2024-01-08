package dev.roanh.kps.config.group;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.SettingGroup;
import dev.roanh.kps.config.setting.BooleanSetting;
import dev.roanh.kps.config.setting.LongSetting;
import dev.roanh.kps.config.setting.ProxySetting;
import dev.roanh.kps.config.setting.StringSetting;

public class StatsSavingSettings extends SettingGroup{
	//TODO do we want to introduce a file path setting? -- also more validation see the formatter for paths in the model package
	/**
	 * Whether or not to periodically save the stats to a file
	 */
	private final BooleanSetting autoSave = new BooleanSetting("autoSave", false);
	/**
	 * The folder to auto save stats to
	 */
	private final StringSetting autoDestination = new StringSetting("autoDestination", Objects.toString(System.getProperty("user.home"), ""));
	/**
	 * The date time formatter pattern to use for the statistics auto saving file name
	 */
	private final StringSetting autoFormat = new StringSetting("autoFormat", "'kps stats' yyyy-MM-dd HH.mm.ss'.kpsstats'");
	/**
	 * The statistics auto saving save interval in milliseconds
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
	private final StringSetting saveFile = new StringSetting("saveFile", Objects.toString(System.getProperty("user.home"), "") + File.separator + "stats.kpsstats");

	public StatsSavingSettings(){
		super("statsSaving");
	}
	
	public boolean isAutoSaveEnabled(){
		return autoSave.getValue();
	}
	
	public String getAutoSaveDestination(){
		return autoDestination.getValue();
	}
	
	public String getAutoSaveFormat(){
		return autoFormat.getValue();
	}
	
	public long getAutoSaveInterval(){
		return autoInterval.getValue();
	}
	
	public boolean isSaveOnExitEnabled(){
		return saveOnExit.getValue();
	}
	
	public boolean isLoadOnLaunchEnabled(){
		return loadOnLaunch.getValue();
	}
	
	public String getSaveFile(){
		return saveFile.getValue();
	}
	
	public void setAutoSaveEnabled(boolean enabled){
		autoSave.update(enabled);
	}
	
	public void setAutoSaveDestination(String dest){
		autoDestination.update(dest);
	}
	
	public void setAutoSaveFormat(String format){
		autoFormat.update(format);
	}
	
	public void setAutoSaveInterval(long millis){
		autoInterval.update(millis);
	}
	
	public void setSaveOnExitEnabled(boolean enabled){
		saveOnExit.update(enabled);
	}
	
	public void setLoadOnLaunchEnabled(boolean enabled){
		loadOnLaunch.update(enabled);
	}
	
	public void setSaveFile(String file){
		saveFile.update(file);
	}

	@Override
	public boolean parse(Map<String, String> data){
		return findAndParse(data, autoSave, autoDestination, autoFormat, autoInterval, saveOnExit, loadOnLaunch, saveFile);
	}
	
	@Override
	public void write(IndentWriter out){
		out.println("# Statistics auto saving");
		out.println("statsSaving:");
		out.increaseIndent();
		autoSave.write(out);
		autoDestination.write(out);
		autoFormat.write(out);
		autoInterval.write(out);
		saveOnExit.write(out);
		loadOnLaunch.write(out);
		saveFile.write(out);
		out.decreaseIndent();
	}

	public void collectLegacyProxies(List<ProxySetting<?>> proxyList){
		proxyList.add(ProxySetting.of("autoSaveStats", autoSave));
		proxyList.add(ProxySetting.of("statsDest", autoDestination));
		proxyList.add(ProxySetting.of("statsFormat", autoFormat));
		proxyList.add(ProxySetting.of("statsSaveInterval", autoInterval));
		proxyList.add(ProxySetting.of("saveStatsOnExit", saveOnExit));
		proxyList.add(ProxySetting.of("loadStatsOnLaunch", loadOnLaunch));
		proxyList.add(ProxySetting.of("statsSaveFile", saveFile));
	}
}
