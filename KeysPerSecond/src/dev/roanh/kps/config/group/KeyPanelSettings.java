package dev.roanh.kps.config.group;

import java.util.List;
import java.util.Map;

import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.setting.BooleanSetting;
import dev.roanh.kps.config.setting.IntSetting;

public class KeyPanelSettings extends PanelSettings{
	private final BooleanSetting visible = new BooleanSetting("visible", true);
	private final IntSetting keycode = new IntSetting("keycode", Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
	
	public KeyPanelSettings(){
		super("keys", "");
	}
	
	public int getKeyCode(){
		return keycode.getValue();
	}
	
	public boolean isVisible(){
		return visible.getValue();
	}
	
	@Override
	public boolean parse(Map<String, String> data){
		return super.parse(data) | findAndParse(data, visible, keycode);
	}
	
	@Override
	public List<Setting<?>> collectSettings(){
		List<Setting<?>> data = super.collectSettings();
		data.add(visible);
		data.add(keycode);
		return data;
	}
}
