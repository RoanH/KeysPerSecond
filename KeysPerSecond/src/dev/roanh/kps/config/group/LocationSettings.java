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

/**
 * General settings for panels that can be places at some position in the GUI.
 * @author Roan
 * @see LayoutPosition
 */
public abstract class LocationSettings extends SettingGroup implements LayoutPosition{
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
	
	/**
	 * Constructs new layout settings with the given default values.
	 * Provided default values are assumed to be valid.
	 * @param key The settings group key for this group. 
	 * @param x The x position of the panel.
	 * @param y The y position of the panel.
	 * @param width The width of the panel.
	 * @param height The height of the panel.
	 * @see #validate()
	 */
	protected LocationSettings(String key, int x, int y, int width, int height){
		super(key);
		this.x = new IntSetting("x", -1, Integer.MAX_VALUE, x);
		this.y = new IntSetting("y", -1, Integer.MAX_VALUE, y);
		this.width = new IntSetting("width", -1, Integer.MAX_VALUE, width);
		this.height = new IntSetting("height", -1, Integer.MAX_VALUE, height);
	}
	
	/**
	 * Sets the x position of this panel. If the new value
	 * leads to an invalid configuration values will be adjusted.
	 * @param x The new x position.
	 * @see #validate()
	 */
	public void setX(int x){
		this.x.update(x);
		validate();
	}
	
	/**
	 * Sets the y position of this panel. If the new value
	 * leads to an invalid configuration values will be adjusted.
	 * @param y The new x position.
	 * @see #validate()
	 */
	public void setY(int y){
		this.y.update(y);
		validate();
	}
	
	/**
	 * Sets the width of this panel. If the new value
	 * leads to an invalid configuration values will be adjusted.
	 * @param width The new width.
	 * @see #validate()
	 */
	public void setWidth(int width){
		this.width.update(width);
		validate();
	}
	
	/**
	 * Sets the height of this panel. If the new value
	 * leads to an invalid configuration values will be adjusted.
	 * @param height The new height.
	 * @see #validate()
	 */
	public void setHeight(int height){
		this.height.update(height);
		validate();
	}
	
	/**
	 * Validates that the current configuration is valid.
	 * This involves check that the special 'end' and 'max'
	 * options do not conflict. Note that along an axis
	 * only one property can have such a special value.
	 * <p>
	 * In other words:
	 * <ol>
	 * <li>If x is end then width cannot be max.</li>
	 * <li>If width is max then x cannot be end.</li>
	 * <li>If y is end then height cannot be max.</li>
	 * <li>If height is max then y cannot be end.</li>
	 * </ol>
	 * If these conditions are not satisfied then values will
	 * be adjusted to be valid before this subroutine returns.
	 * @return True if the values had to be adjusted to arrive
	 *         at valid settings, false if no values were adjusted.
	 */
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
