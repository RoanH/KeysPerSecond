package dev.roanh.kps.config.setting;

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;

public class StringSetting extends Setting<String>{

	public StringSetting(String key, String defaultValue){
		super(key, defaultValue);
	}

	@Override
	protected boolean parse(String data){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void write(IndentWriter out){
		// TODO Auto-generated method stub
		
	}
}
