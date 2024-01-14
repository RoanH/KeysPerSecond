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

import dev.roanh.kps.RenderingMode;
import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.setting.RenderingModeSetting;

/**
 * Settings for a named value display panel.
 * @author Roan
 */
public abstract class DataPanelSettings extends PanelSettings{
	/**
	 * The rendering mode used the panel.
	 */
	protected final RenderingModeSetting mode = new RenderingModeSetting("mode", RenderingMode.VERTICAL);

	/**
	 * Constructs new panel settings.
	 * @param key The settings group key.
	 * @param x The x position of the panel.
	 * @param y The y position of the panel.
	 * @param width The width of the panel.
	 * @param height The height of the panel.
	 * @param defaultName The display name of the panel.
	 */
	protected DataPanelSettings(String key, int x, int y, int width, int height, String defaultName){
		super(key, x, y, width, height, defaultName);
	}
	
	/**
	 * Constructs a new panel with default dimensions.
	 * @param key The settings group key.
	 * @param defaultName The display name of the panel.
	 */
	protected DataPanelSettings(String key, String defaultName){
		super(key, defaultName);
	}
	
	/**
	 * Gets the rendering mode for this panel.
	 * @return The rendering mode for this panel.
	 * @see RenderingMode
	 */
	public RenderingMode getRenderingMode(){
		return mode.getValue();
	}
	
	/**
	 * Sets the rendering mode for the panel.
	 * @param mode The new rendering mode.
	 */
	public void setRenderingMode(RenderingMode mode){
		this.mode.update(mode);
	}
	
	@Override
	public boolean parse(Map<String, String> data){
		return super.parse(data) | findAndParse(data, mode);
	}
	
	@Override
	public void writeItems(IndentWriter out){
		super.writeItems(out);
		mode.write(out);
	}
}
