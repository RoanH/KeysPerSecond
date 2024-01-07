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
package dev.roanh.kps.config;

import java.util.List;
import java.util.Map;

public abstract interface SettingGroup{

	public abstract boolean parse(Map<String, String> data);
	
	//has to support adding note
	public abstract List<Setting<?>> collectSettings();
	
	public default boolean findAndParse(Map<String, String> data, Setting<?>... settings){
		boolean modified = false;
		for(Setting<?> setting : settings){
			String val = data.get(setting.getKey());
			if(val == null){
				setting.reset();
				modified = true;
			}else{
				modified |= setting.parse(val);
			}
		}
		return modified;
	}
}
