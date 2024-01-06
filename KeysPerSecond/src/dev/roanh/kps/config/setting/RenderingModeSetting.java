package dev.roanh.kps.config.setting;

import java.util.Locale;

import dev.roanh.kps.RenderingMode;
import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;

public class RenderingModeSetting extends Setting<RenderingMode>{

	public RenderingModeSetting(String key, RenderingMode defaultValue){
		super(key, defaultValue);
	}

	@Override
	protected boolean parse(String data){
		try{
			data = data.toUpperCase(Locale.ROOT);
			switch(data){
			case "HORIZONTAL":
				update(RenderingMode.HORIZONTAL_TN);
				return false;
			case "VERTICALS":
			case "HORIZONTAL_TAN":
				update(RenderingMode.VERTICAL);
				return false;
			case "HORIZONTAL_TDAN":
				update(RenderingMode.DIAGONAL1);
				return false;
			case "HORIZONTAL_TDAN2":
				update(RenderingMode.DIAGONAL3);
				return false;
			case "HORIZONTAL_TDANS":
				update(RenderingMode.DIAGONAL1);
				return false;
			case "HORIZONTAL_TDAN2S":
				update(RenderingMode.DIAGONAL3);
				return false;
			default:
				update(RenderingMode.valueOf(data));
				return false;
			}
		}catch(IllegalArgumentException e){
			reset();
			return true;
		}
	}

	@Override
	protected void write(IndentWriter out){
		out.println(key + ": " + value.name());
	}
}
