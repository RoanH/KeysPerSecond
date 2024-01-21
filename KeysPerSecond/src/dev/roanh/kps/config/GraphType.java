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
import dev.roanh.kps.config.group.CursorGraphSettings;
import dev.roanh.kps.config.group.GraphPanelSettings;
import dev.roanh.kps.config.group.LineGraphSettings;

/**
 * Enum of graph types that exist.
 * @author Roan
 * @see GraphPanelSettings
 */
public enum GraphType{
	/**
	 * Line graph showing the average and current KPS.
	 */
	LINE("line", LineGraphSettings::new),
	/**
	 * Cursor graph showing the cursor trail.
	 */
	CURSOR("cursor", CursorGraphSettings::new);
	
	/**
	 * The graph type identifier.
	 */
	private final String key;
	/**
	 * A constructor to create a new configuration for a graph panel.
	 */
	private final Supplier<GraphPanelSettings> ctor;
	
	/**
	 * Constructs a new graph type.
	 * @param key The graph type identifier.
	 * @param ctor A supplier of new graph settings.
	 */
	private GraphType(String key, Supplier<GraphPanelSettings> ctor){
		this.key = key;
		this.ctor = ctor;
	}
	
	/**
	 * Creates new settings for this graph type.
	 * @return The newly created settings.
	 */
	public GraphPanelSettings newSettings(){
		return ctor.get();
	}
	
	/**
	 * Gets the identifier key for this graph type.
	 * @return The identifier for this graph type.
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
	public static ParsedItem<GraphPanelSettings> construct(List<String> data){
		Map<String, String> info = ListItemConstructor.buildMap(data);
		if(info == null){
			return null;
		}
		
		String key = info.get("type");
		for(GraphType type : values()){
			if(type.key.equals(key)){
				return ListItemConstructor.constructThenParse(type.ctor, info);
			}
		}
		
		//for legacy reasons the fallback type is the line graph
		return ListItemConstructor.constructThenParse(LINE.ctor, info);
	}
}
