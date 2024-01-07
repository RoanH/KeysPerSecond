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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.SettingGroup;
import dev.roanh.kps.config.setting.IntSetting;

public class LocationSettings implements SettingGroup{
	/**
	 * The x position of the panel.
	 */
	private final IntSetting x;
	/**
	 * The y position of the panel.
	 */
	private final IntSetting y;
	/**
	 * The width of the panel.
	 */
	private final IntSetting width;
	/**
	 * The height of the panel.
	 */
	private final IntSetting height;
	
	protected LocationSettings(int x, int y, int width, int height){
		this.x = new IntSetting("x", -1, Integer.MAX_VALUE, x);
		this.y = new IntSetting("y", -1, Integer.MAX_VALUE, y);
		this.width = new IntSetting("width", -1, Integer.MAX_VALUE, width);
		this.height = new IntSetting("height", -1, Integer.MAX_VALUE, height);
	}
	
	/**
	 * Gets the x position for this panel
	 * @return The x position for this panel
	 */
	public int getX(){
		return x.getValue();
	}

	/**
	 * Gets the y position for this panel
	 * @return The y position for this panel
	 */
	public int getY(){
		return y.getValue();
	}

	/**
	 * Gets the width for this panel
	 * @return The width for this panel
	 */
	public int getWidth(){
		return width.getValue();
	}

	/**
	 * Gets the height for this panel
	 * @return The height for this panel
	 */
	public int getHeight(){
		return height.getValue();
	}
	
	@Override
	public boolean parse(Map<String, String> data){
		return findAndParse(data, x, y, width, height);
	}

	@Override
	public List<Setting<?>> collectSettings(){
		List<Setting<?>> data = new ArrayList<Setting<?>>();
		data.add(x);
		data.add(y);
		data.add(width);
		data.add(height);
		return data;
	}
}
