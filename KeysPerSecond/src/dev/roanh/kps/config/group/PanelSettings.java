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
import dev.roanh.kps.config.setting.StringSetting;

public abstract class PanelSettings extends LocationSettings{
	protected final StringSetting name;
	protected final RenderingModeSetting mode = new RenderingModeSetting("mode", RenderingMode.VERTICAL);
	
	public PanelSettings(String key, int x, int y, int width, int height, String defaultName){
		super(key, x, y, width, height);
		name = new StringSetting("name", defaultName);
	}
	
	public PanelSettings(String key, String defaultName){
		this(key, -1, 0, 2, 3, defaultName);
	}

	public RenderingMode getRenderingMode(){
		return mode.getValue();
	}

	public String getName(){
		return name.getValue();
	}
	
	public void setRenderingMode(RenderingMode mode){
		this.mode.update(mode);
	}
	
	public void setName(String name){
		this.name.update(name);
	}
	
	public abstract void showEditor(boolean live);
	
	@Override
	public boolean parse(Map<String, String> data){
		return super.parse(data) | findAndParse(data, name, mode);
	}
	
	@Override
	public void write(IndentWriter out){
		super.write(out);
		name.write(out);
		mode.write(out);
	}
}
