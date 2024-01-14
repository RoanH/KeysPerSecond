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
import dev.roanh.kps.config.UpdateRate;

/**
 * Setting for update rates.
 * @author Roan
 * @see UpdateRate
 */
public class UpdateRateSetting extends Setting<UpdateRate>{

	/**
	 * Constructs a new update rate setting.
	 * @param key The configuration setting key.
	 * @param defaultValue The default setting value.
	 */
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
