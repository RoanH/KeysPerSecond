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

import java.util.Map;

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.SettingGroup;
import dev.roanh.kps.config.setting.IntSetting;
import dev.roanh.kps.layout.LayoutPosition;

public class LocationSettings extends SettingGroup implements LayoutPosition{
	/**
	 * The x position of the panel (-1 is end).
	 */
	protected final IntSetting x;
	/**
	 * The y position of the panel (-1 is end).
	 */
	protected final IntSetting y;
	/**
	 * The width of the panel (-1 is max).
	 */
	protected final IntSetting width;
	/**
	 * The height of the panel (-1 is max).
	 */
	protected final IntSetting height;
	
	protected LocationSettings(String key, int x, int y, int width, int height){
		super(key);
		this.x = new IntSetting("x", -1, Integer.MAX_VALUE, x);
		this.y = new IntSetting("y", -1, Integer.MAX_VALUE, y);
		this.width = new IntSetting("width", -1, Integer.MAX_VALUE, width);
		this.height = new IntSetting("height", -1, Integer.MAX_VALUE, height);
		validate();
	}
	
	public void setX(int x){
		this.x.update(x);
		validate();
	}
	
	public void setY(int y){
		this.y.update(y);
		validate();
	}
	
	public void setWidth(int width){
		this.width.update(width);
		validate();
	}
	
	public void setHeight(int height){
		this.height.update(height);
		validate();
	}
	
	private boolean validate(){
		boolean defaultUsed = false;
		
		if(x.getValue() == -1 && width.getValue() == -1){
			x.reset();
			width.reset();
			defaultUsed = true;
		}
		
		if(y.getValue() == -1 && height.getValue() == -1){
			y.reset();
			height.reset();
			defaultUsed = true;
		}
		
		return defaultUsed;
	}
	
	@Override
	public int getLayoutX(){
		return x.getValue();
	}

	@Override
	public int getLayoutY(){
		return y.getValue();
	}

	@Override
	public int getLayoutWidth(){
		return width.getValue();
	}

	@Override
	public int getLayoutHeight(){
		return height.getValue();
	}
	
	@Override
	public boolean parse(Map<String, String> data){
		boolean defaultUsed = findAndParse(data, x, y, width, height);
		return validate() || defaultUsed;
	}
	
	@Override
	public void writeItems(IndentWriter out){
		x.write(out);
		y.write(out);
		width.write(out);
		height.write(out);
	}
}
