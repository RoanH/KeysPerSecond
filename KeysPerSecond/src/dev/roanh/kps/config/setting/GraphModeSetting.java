package dev.roanh.kps.config.setting;

import java.util.Locale;

import dev.roanh.kps.GraphMode;
import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;

public class GraphModeSetting extends Setting<GraphMode>{
	
	public GraphModeSetting(String key, GraphMode defaultValue){
		super(key, defaultValue);
	}

	@Override
	public boolean parse(String data){
		try{
			update(GraphMode.valueOf(data.toUpperCase(Locale.ROOT)));
			return false;
		}catch(IllegalArgumentException e){
			reset();
			return true;
		}
	}

	@Override
	public void write(IndentWriter out){
		// TODO Auto-generated method stub
		
	}

}
