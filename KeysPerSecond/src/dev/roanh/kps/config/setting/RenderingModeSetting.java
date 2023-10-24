package dev.roanh.kps.config.setting;

import dev.roanh.kps.RenderingMode;
import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;

public class RenderingModeSetting extends Setting<RenderingMode>{

	public RenderingModeSetting(String key, RenderingMode defaultValue){
		super(key, defaultValue);
	}

	@Override
	protected boolean parse(String data){
		//TODO valueOf or also port the legacy reading?
		return false;
	}

	@Override
	protected void write(IndentWriter out){
		out.println(key + ": " + value.name());
	}
}
