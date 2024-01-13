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
package dev.roanh.kps.config.group;

import java.awt.Point;
import java.util.List;
import java.util.Map;

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.SettingGroup;
import dev.roanh.kps.config.legacy.LegacyPositionProxy;
import dev.roanh.kps.config.legacy.LegacyProxyStore;
import dev.roanh.kps.config.setting.IntSetting;

/**
 * Position setting that also tracks if it was ever updated from default.
 * @author Roan
 */
public class PositionSettings extends SettingGroup implements LegacyProxyStore{
	/**
	 * The position x coordinate.
	 */
	private final IntSetting x = new IntSetting("x", 0, Integer.MAX_VALUE, 0);
	/**
	 * The position y coordinate.
	 */
	private final IntSetting y = new IntSetting("y", 0, Integer.MAX_VALUE, 0);
	/**
	 * If this setting was ever updated.
	 */
	private boolean parsed = false;
	
	/**
	 * Constructs a new position setting.
	 */
	public PositionSettings(){
		super("frame");
	}

	/**
	 * Stores a new position in this setting.
	 * @param location The new position.
	 */
	public void update(Point location){
		x.update(location.x);
		y.update(location.y);
		parsed = true;
	}
	
	/**
	 * Tests if this setting was updated with some value at
	 * some point in time. If this is false then the value
	 * of this setting is still the initial default.
	 * @return True if this setting was updated at some point.
	 */
	public boolean hasPosition(){
		return parsed;
	}
	
	/**
	 * Gets the position represented by this settting.
	 * @return The configured point.
	 */
	public Point getLocation(){
		return new Point(x.getValue(), y.getValue());
	}

	@Override
	public boolean parse(Map<String, String> data){
		if(findAndParse(data, x, y)){
			return true;
		}else{
			parsed = true;
			return false;
		}
	}

	@Override
	public void writeItems(IndentWriter out){
		x.write(out);
		y.write(out);
	}

	@Override
	public void collectLegacyProxies(List<Setting<?>> proxyList){
		proxyList.add(new LegacyPositionProxy(this));
	}
}
