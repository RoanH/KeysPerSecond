package dev.roanh.kps.config.group;

import java.util.List;
import java.util.Map;

import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.SettingGroup;

public class StatsSavingSettings extends SettingGroup{
	
	

	public StatsSavingSettings(){
		super("statsSaving");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean parse(Map<String, String> data){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Setting<?>> collectSettings(){
		// TODO Auto-generated method stub
		return null;
	}

}
