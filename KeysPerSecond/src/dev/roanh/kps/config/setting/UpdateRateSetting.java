package dev.roanh.kps.config.setting;

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.UpdateRate;

public class UpdateRateSetting extends Setting<UpdateRate>{

	public UpdateRateSetting(String key, UpdateRate defaultValue){
		super(key, defaultValue);
	}

	@Override
	public boolean parse(String data){
		try{
			update(UpdateRate.fromMs(Integer.parseInt(data)));
			return false;
		}catch(IllegalArgumentException e){
			reset();
			return true;
		}
	}

	@Override
	public void write(IndentWriter out){
		out.println(key + ": " + value.getRate());
	}
}
