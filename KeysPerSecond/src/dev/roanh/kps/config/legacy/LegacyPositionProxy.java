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

import java.awt.Point;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.group.PositionSettings;

/**
 * Proxy setting for the legacy position setting.
 * @author Roan
 * @see PositionSettings
 */
public class LegacyPositionProxy extends Setting<Void>{
	/**
	 * Legacy format regex:<br>
	 * Group 1: x coordinate.<br>
	 * Group 2: y coordinate.
	 */
	private static final Pattern LEGACY_POSITION_REGEX = Pattern.compile("\\[x=(\\d+),y=(\\d+)]");
	/**
	 * The position settings to proxy to.
	 */
	private final PositionSettings position;
	
	/**
	 * Constructs a new legacy position setting proxy with the given target to update.
	 * @param setting The setting being proxied.
	 */
	public LegacyPositionProxy(PositionSettings setting){
		super("position", null);
		position = setting;
	}

	@Override
	public boolean parse(String data){
		Matcher m = LEGACY_POSITION_REGEX.matcher(data);
		if(m.matches()){
			try{
				position.update(new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))));
				return false;
			}catch(NumberFormatException e){
				return true;
			}
		}else{
			return true;
		}
	}

	@Override
	public void write(IndentWriter out){
		throw new IllegalStateException("Legacy proxy settings should never be written.");
	}
}
