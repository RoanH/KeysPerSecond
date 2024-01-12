package dev.roanh.kps.config.setting;

import dev.roanh.kps.config.SettingList;
import dev.roanh.kps.config.group.LocationSettings;

public class LegacyPanelShowSetting extends BooleanSetting{
	private SettingList<? extends LocationSettings> data;
	private LocationSettings item;
	
	public LegacyPanelShowSetting(String key, SettingList<? extends LocationSettings> data, LocationSettings item){
		super(key, true);
		this.data = data;
		this.item = item;
	}

	@Override
	public boolean parse(String data){
		if(super.parse(data)){
			return true;
		}
		
		if(getValue()){
			return true;
		}else{
			this.data.remove(item);
			return false;
		}
	}
}
