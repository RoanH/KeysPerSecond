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
import java.util.function.Supplier;

import dev.roanh.kps.config.ListItemConstructor.ParsedItem;
import dev.roanh.kps.config.group.AveragePanelSettings;
import dev.roanh.kps.config.group.CurrentPanelSettings;
import dev.roanh.kps.config.group.LastPanelSettings;
import dev.roanh.kps.config.group.MaxPanelSettings;
import dev.roanh.kps.config.group.SpecialPanelSettings;
import dev.roanh.kps.config.group.TotalPanelSettings;

/**
 * Enum of special panel types that exist.
 * @author Roan
 * @see SpecialPanelSettings
 */
public enum PanelType{
	/**
	 * Maximum panel that shows the highest recorded KPS.
	 */
	MAX("max", MaxPanelSettings::new),
	/**
	 * Average panel that shows the average KPS.
	 */
	AVG("avg", AveragePanelSettings::new),
	/**
	 * Current panel that shows the current KPS.
	 */
	CURRENT("current", CurrentPanelSettings::new),
	/**
	 * Total panel that shows the total number of hits.
	 */
	TOTAL("total", TotalPanelSettings::new),
	/**
	 * Last panel that shows the time since the last tracked input.
	 */
	LAST("last", LastPanelSettings::new);
	
	/**
	 * The panel type identifier.
	 */
	private final String key;
	/**
	 * A constructor to create a new configuration for a panel type.
	 */
	private final Supplier<SpecialPanelSettings> ctor;
	
	/**
	 * Constructs a new panel type.
	 * @param key The panel type identifier.
	 * @param ctor A supplier of new panel settings.
	 */
	private PanelType(String key, Supplier<SpecialPanelSettings> ctor){
		this.key = key;
		this.ctor = ctor;
	}
	
	/**
	 * Creates new settings for this panel type.
	 * @return The newly created settings.
	 */
	public SpecialPanelSettings newSettings(){
		return ctor.get();
	}
	
	/**
	 * Gets the identifier key for this panel type.
	 * @return The identifier for this panel type.
	 */
	public String getKey(){
		return key;
	}
	
	/**
	 * Constructs new panel settings based on the given list of configuration data.
	 * @param data The panel configuration data.
	 * @return The parsed panel settings or null if the data
	 *         did not encoded valid panel settings.
	 * @see ListItemConstructor
	 */
	public static ParsedItem<SpecialPanelSettings> construct(List<String> data){
		Map<String, String> info = ListItemConstructor.buildMap(data);
		if(info == null){
			return null;
		}
		
		String key = info.get("type");
		for(PanelType type : values()){
			if(type.key.equals(key)){
				return ListItemConstructor.constructThenParse(type.ctor, info);
			}
		}
		
		return null;
	}
}
