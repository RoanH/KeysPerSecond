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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.ThemeColor;

/**
 * Setting for theme colors.
 * @author Roan
 * @see ThemeColor
 */
public class ColorSetting extends Setting<ThemeColor>{
	/**
	 * Regex for the legacy RGB color format.<br>
	 * Group 1: integer red component.<br>
	 * Group 2: integer green component.<br>
	 * Group 3: integer blue component.
	 */
	private static final Pattern LEGACY_COLOR_REGEX = Pattern.compile("\\[r=(\\d{1,3}),g=(\\d{1,3}),b=(\\d{1,3})]");
	/**
	 * Regex for the new HEX based #RRGGBBAA color format, where alpha is optional.<br>
	 * Group 1: RGB HEX color.<br>
	 * Group 2: alpha HEX color or null if there is no alpha component (defaults to fully opaque).
	 */
	private static final Pattern COLOR_REGEX = Pattern.compile("#([A-F0-9]{6})([A-F0-9]{2})?");
	
	/**
	 * Constructs a new color setting.
	 * @param key The setting key.
	 * @param defaultValue The default setting value.
	 */
	public ColorSetting(String key, ThemeColor defaultValue){
		super(key, defaultValue);
	}
	
	@Override
	public boolean parse(String data){
		Matcher m = LEGACY_COLOR_REGEX.matcher(data);
		if(m.matches()){
			int r = Integer.parseInt(m.group(1));
			int g = Integer.parseInt(m.group(2));
			int b = Integer.parseInt(m.group(3));
			if(r > 255 || g > 255 || b > 255){
				reset();
				return true;
			}else{
				update(new ThemeColor(r, g, b, value.getAlpha()));
				return false;
			}
		}else{
			data = data.toUpperCase(Locale.ROOT);
			m = COLOR_REGEX.matcher(data);
			if(m.matches()){
				int rgb = Integer.parseUnsignedInt(m.group(1), 16);
				String alphaStr = m.group(2);
				int alpha = alphaStr == null ? 0xFF : Integer.parseUnsignedInt(alphaStr, 16);
				update(new ThemeColor(rgb, alpha));
				return false;
			}else{
				reset();
				return true;
			}
		}
	}
	
	@Override
	public void write(IndentWriter out){
		out.println(key + ": #" + value.toHex());
	}
}
