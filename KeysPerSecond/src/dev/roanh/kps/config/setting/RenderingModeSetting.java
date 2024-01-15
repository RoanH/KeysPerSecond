/*
 * KeysPerSecond: An open source input statistics displayer.
 * Copyright (C) 2017  Roan Hofland (roan@roanh.dev).  All rights reserved.
 * GitHub Repository: https://github.com/RoanH/KeysPerSecond
 *
 * KeysPerSecond is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KeysPerSecond is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dev.roanh.kps.config.setting;

import java.util.Locale;

import dev.roanh.kps.RenderingMode;
import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;

/**
 * Setting for rendering modes.
 * @author Roan
 * @see RenderingMode
 */
public class RenderingModeSetting extends Setting<RenderingMode>{

	/**
	 * Constructs a new rendering mode setting.
	 * @param key The setting key.
	 * @param defaultValue The default setting value.
	 */
	public RenderingModeSetting(String key, RenderingMode defaultValue){
		super(key, defaultValue);
	}

	@Override
	public boolean parse(String data){
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
			case "HORIZONTAL_TDANS":
				update(RenderingMode.DIAGONAL1);
				return false;
			case "HORIZONTAL_TDAN2":
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
	public void write(IndentWriter out){
		out.println(key + ": " + value.name());
	}
}
