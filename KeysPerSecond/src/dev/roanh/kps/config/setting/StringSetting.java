package dev.roanh.kps.config.setting;

import java.io.PrintWriter;

import dev.roanh.kps.config.Setting;

public class StringSetting extends Setting<String>{

	protected StringSetting(String key, String defaultValue){
		super(key, defaultValue);
	}

	@Override
	protected boolean parse(String data){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void write(PrintWriter out){
		// TODO Auto-generated method stub
		
	}
}
