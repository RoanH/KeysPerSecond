package dev.roanh.kps.config.group;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.SettingGroup;
import dev.roanh.kps.config.setting.BooleanSetting;
import dev.roanh.kps.config.setting.LongSetting;
import dev.roanh.kps.config.setting.ProxySetting;
import dev.roanh.kps.config.setting.StringSetting;

public class StatsSavingSettings extends SettingGroup{
	//TODO move javadoc from Configuration
	//TODO do we want to introduce a file path setting?
	private final BooleanSetting autoSave = new BooleanSetting("autoSave", false);
	private final StringSetting autoDestination = new StringSetting("autoDestination", Objects.toString(System.getProperty("user.home"), ""));
	private final StringSetting autoFormat = new StringSetting("autoFormat", "'kps stats' yyyy-MM-dd HH.mm.ss'.kpsstats'");
	private final LongSetting autoInterval = new LongSetting("autoInterval", 1, Long.MAX_VALUE, TimeUnit.MINUTES.toMillis(10));
	private final BooleanSetting saveOnExit = new BooleanSetting("saveOnExit", false);
	private final BooleanSetting loadOnLaunch = new BooleanSetting("loadOnLaunch", false);
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

	@Override
	public boolean parse(Map<String, String> data){
		return findAndParse(data, autoSave, autoDestination, autoFormat, autoInterval, saveOnExit, loadOnLaunch, saveFile);
	}

	@Override
	public List<Setting<?>> collectSettings(){
		// TODO Auto-generated method stub
		return null;
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
