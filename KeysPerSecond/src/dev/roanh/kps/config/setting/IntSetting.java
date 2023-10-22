package dev.roanh.kps.config.setting;

import java.io.PrintWriter;

import dev.roanh.kps.config.Setting;

public class IntSetting extends Setting<Integer>{
	private int min;
	private int max;
	
	
	protected IntSetting(String key, int min, int max, int defaultValue){
		super(key, defaultValue);
		// TODO Auto-generated constructor stub
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
