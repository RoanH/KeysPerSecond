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

/**
 * Legacy setting that handles the various show settings
 * for certain panels in the legacy configuration format.
 * If these settings are detected and set to false the
 * relevant panel is removed from the configuration.
 * @author Roan
 */
public class LegacyPanelShowSetting extends BooleanSetting{
	/**
	 * The list with panels to update if the item needs to be removed.
	 */
	private SettingList<? extends LocationSettings> data;
	/**
	 * The item to remove if the setting is found and set to false.
	 */
	private LocationSettings item;
	
	/**
	 * Creates a new legacy panel show setting.
	 * @param key The configuration key.
	 * @param data The list with panels to update.
	 * @param item The item to potentially remove.
	 */
	public LegacyPanelShowSetting(String key, SettingList<? extends LocationSettings> data, LocationSettings item){
		super(key, true);
		this.data = data;
		this.item = item;
	}

	@Override
	public boolean parse(String data){
		boolean defaultUsed = super.parse(data);
		
		if(!getValue()){
			this.data.remove(item);
		}
		
		return defaultUsed;
	}
}
