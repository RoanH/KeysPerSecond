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

import dev.roanh.kps.config.SettingList;
import dev.roanh.kps.config.group.LocationSettings;
import dev.roanh.kps.config.setting.BooleanSetting;

public class LegacyPanelShowSetting extends BooleanSetting{
	private SettingList<? extends LocationSettings> data;
	private LocationSettings item;
	
	public LegacyPanelShowSetting(String key, SettingList<? extends LocationSettings> data, LocationSettings item){
		super(key, true);
		this.data = data;
		this.item = item;
	}

	@Override
	public boolean parse(String data){
		if(super.parse(data)){
			return true;
		}
		
		if(getValue()){
			return true;
		}else{
			this.data.remove(item);
			return false;
		}
	}
}
