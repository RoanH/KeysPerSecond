package dev.roanh.kps.config.setting;

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;

public class LongSetting extends Setting<Long>{
	private final long min;
	private final long max;
	
	public LongSetting(String key, long min, long max, long defaultValue){
		super(key, defaultValue);
		this.min = min;
		this.max = max;
	}

	@Override
	public boolean parse(String data){
		try{
			long val = Long.parseLong(data);
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
	protected void write(IndentWriter out){
		out.println(key + ": " + value);
	}
}
