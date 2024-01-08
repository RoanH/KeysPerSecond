package dev.roanh.kps.config.group;

import java.util.Map;

import dev.roanh.kps.RenderingMode;
import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.setting.RenderingModeSetting;
import dev.roanh.kps.config.setting.StringSetting;

public class PanelSettings extends LocationSettings{
	private final StringSetting name;
	private final RenderingModeSetting mode = new RenderingModeSetting("mode", RenderingMode.VERTICAL);
	
	public PanelSettings(String key, String defaultName){
		super(key, -1, 0, 2, 3);
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
		return super.parse(data) | findAndParse(data, name, mode);
	}
	
	@Override
	public void write(IndentWriter out){
		super.write(out);
		name.write(out);
		mode.write(out);
	}
}
