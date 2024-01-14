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

/**
 * Precision setting used to format real numbers.
 * @author Roan
 */
public class PrecisionSetting extends IntSetting{
	/**
	 * The format string used for formatting.
	 */
	private String format;

	/**
	 * Constructs a new precision setting.
	 * @param key The setting key.
	 * @param min The minimum precision value.
	 * @param max The maximum precision value.
	 * @param defaultValue The default precision value.
	 */
	public PrecisionSetting(String key, int min, int max, int defaultValue){
		super(key, min, max, defaultValue);
	}
	
	/**
	 * Formats the given double value according to
	 * the precision value configured for this setting.
	 * @param value The value to format.
	 * @return The formatted value.
	 */
	public String format(double value){
		return String.format(format, value);
	}

	@Override
	public void update(Integer newValue){
		super.update(newValue);
		format = "%1$." + getValue() + "f";
	}
}
