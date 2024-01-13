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
package dev.roanh.kps.config.legacy;

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.ThemeColor;
import dev.roanh.kps.config.setting.ColorSetting;

/**
 * Extension of a regular color setting that can deal
 * with the two settings that were used to describe
 * colours in legacy configuration formats. This setting
 * handler the RGB values like normal, but overwrites
 * the alpha of the existing colour instead of fully replacing it.
 * @author Roan
 */
public class LegacyColorProxy extends Setting<Void>{
	/**
	 * The color setting to update with parsed data.
	 */
	private ColorSetting setting;

	/**
	 * Constructs a new legacy color setting proxy.
	 * @param key The legacy configuration setting key.
	 * @param setting The setting to update with parsed values.
	 */
	public LegacyColorProxy(String key, ColorSetting setting){
		super(key, null);
		this.setting = setting;
	}

	@Override
	public boolean parse(String data){
		try{
			float alpha = Float.parseFloat(data);
			if(alpha < 0.0F || alpha > 1.0F){
				setting.reset();
				return true;
			}else{
				setting.update(new ThemeColor(setting.getValue().getRGB(), alpha));
				return false;
			}
		}catch(NumberFormatException e){
			return setting.parse(data);
		}
	}

	@Override
	public void write(IndentWriter out){
		throw new IllegalStateException("Legacy proxy settings should never be written.");
	}
}
