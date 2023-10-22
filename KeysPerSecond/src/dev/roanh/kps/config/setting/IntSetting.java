package dev.roanh.kps.config.setting;

import java.io.PrintWriter;

import dev.roanh.kps.config.Setting;

public class IntSetting extends Setting<Integer>{
	private final int min;
	private final int max;
	
	protected IntSetting(String key, int min, int max, int defaultValue){
		super(key, defaultValue);
		this.min = min;
		this.max = max;
	}

	@Override
	protected boolean parse(String data){
		try{
			int val = Integer.parseInt(data);
			if(min <= val && val <= max){
				update(val);
				return false;
			}else{
				reset();
				return true;
			}
		}catch(NumberFormatException e){
			reset();
			return true;
		}
	}

	@Override
	protected void write(PrintWriter out){
		out.println(key + ": " + value);
	}
}
