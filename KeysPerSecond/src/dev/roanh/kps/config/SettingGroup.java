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

import java.util.Map;

/**
 * Group of regular settings that can be handled as a single unit.
 * @author Roan
 */
public abstract class SettingGroup{
	/**
	 * The key identifier for this group.
	 */
	private final String key;
	
	/**
	 * Constructs a new settings group with the given identifier.
	 * @param key The 
	 */
	public SettingGroup(String key){
		this.key = key;
	}
	
	/**
	 * Gets the identifier key for this group.
	 * @return The identifier for this group.
	 */
	public String getKey(){
		return key;
	}

	/**
	 * Parses all the settings under this group.
	 * @param data The setting data.
	 * @return True if default values were used during the parsing process.
	 * @see #findAndParse(Map, Setting...)
	 * @see Setting#parse(String)
	 */
	public abstract boolean parse(Map<String, String> data);
	
	/**
	 * Writes all the individual settings that are contained in this group.
	 * @param out The writer to write to.
	 */
	public abstract void writeItems(IndentWriter out);
	
	/**
	 * Writes this entire settings group.
	 * @param out The writer to write.
	 */
	public final void write(IndentWriter out){
		out.println(key + ":");
		out.increaseIndent();
		writeItems(out);
		out.decreaseIndent();
	}
	
	/**
	 * Resolves the given settings by their key from the given map of
	 * data and attempts to parse the provided data for each setting.
	 * @param data The key-value setting data.
	 * @param settings The settings to resolve and parse.
	 * @return True if default values were used during the parsing process.
	 * @see Setting#parse(String)
	 */
	protected boolean findAndParse(Map<String, String> data, Setting<?>... settings){
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
