package dev.roanh.kps.config.group;

import java.util.List;
import java.util.Map;

import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.setting.StringSetting;

public class PanelSettings extends LocationSettings{
	private StringSetting name;
	
	
	
	
	
	






	public String getName(){
		return name.getValue();
	}





	@Override
	public List<Setting<?>> collectSettings(){
		// TODO Auto-generated method stub
		return super.collectSettings();
	}

	@Override
	public boolean parse(Map<String, String> data){
		// TODO Auto-generated method stub
		return super.parse(data);
	}
}
