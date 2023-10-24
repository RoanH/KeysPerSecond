package dev.roanh.kps.config.group;

import java.util.List;
import java.util.Map;

import dev.roanh.kps.RenderingMode;
import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.setting.RenderingModeSetting;
import dev.roanh.kps.config.setting.StringSetting;

public class PanelSettings extends LocationSettings{
	private final StringSetting name;
	private final RenderingModeSetting mode = new RenderingModeSetting("mode", RenderingMode.VERTICAL);
	
	public PanelSettings(String defaultName){
		name = new StringSetting("name", defaultName);
	}

	public RenderingMode getRenderingMode(){
		return mode.getValue();
	}

	public String getName(){
		return name.getValue();
	}

	@Override
	public boolean parse(Map<String, String> data){
		return super.parse(data) | findAndParse(data, name) | findAndParse(data, mode);
	}

	@Override
	public List<Setting<?>> collectSettings(){
		List<Setting<?>> data = super.collectSettings();
		data.add(name);
		data.add(mode);
		return data;
	}
}
