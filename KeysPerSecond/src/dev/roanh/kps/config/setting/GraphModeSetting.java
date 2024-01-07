package dev.roanh.kps.config.setting;

import dev.roanh.kps.GraphMode;
import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;

public class GraphModeSetting extends Setting<GraphMode>{

	public GraphModeSetting(String key, GraphMode defaultValue){
		super(key, defaultValue);
	}

	@Override
	public boolean parse(String data){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void write(IndentWriter out){
		// TODO Auto-generated method stub
		
	}

}
