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

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;

/**
 * Long setting implementation.
 * @author Roan
 */
public class LongSetting extends Setting<Long>{
	/**
	 * The minimum valid value.
	 */
	private final long min;
	/**
	 * The maximum valid value.
	 */
	private final long max;
	
	/**
	 * Constructs a new long setting.
	 * @param key The setting key to associate the value with.
	 * @param min The minimum allowed value.
	 * @param max The maximum allowed value.
	 * @param defaultValue The default value for this setting.
	 */
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
	public void write(IndentWriter out){
		out.println(key + ": " + value);
	}
}
