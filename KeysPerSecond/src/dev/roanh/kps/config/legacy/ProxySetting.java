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

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;

/**
 * Proxy setting used to map old settings onto new settings.
 * Generally used to set new settings based on the value for
 * old legacy settings that no longer exist.
 * @author Roan
 * @param <T> The type of the setting.
 */
public class ProxySetting<T> extends Setting<T>{
	/**
	 * All the settings to update with received data.
	 */
	private final Setting<T>[] targets;
	
	/**
	 * Constructs a new proxy setting.
	 * @param key The configuration key.
	 * @param defaultValue The default value for this setting.
	 * @param targets A list of target settings to update with received data.
	 */
	@SafeVarargs
	protected ProxySetting(String key, T defaultValue, Setting<T>... targets){
		super(key, defaultValue);
		this.targets = targets;
	}

	@Override
	public boolean parse(String data){
		boolean defaultUsed = false;
		for(Setting<?> setting : targets){
			defaultUsed |= setting.parse(data);
		}
		return defaultUsed;
	}

	@Override
	public void write(IndentWriter out){
		throw new IllegalStateException("Legacy proxy settings should never be written.");
	}
	
	/**
	 * Constructs a new proxy setting.
	 * @param <T> The setting data type.
	 * @param key The configuration key.
	 * @param defaultValue The default setting value.
	 * @param targets A list of settings to update with received data.
	 * @return The newly constructed proxy setting.
	 */
	@SafeVarargs
	public static final <T> ProxySetting<T> of(String key, T defaultValue, Setting<T>... targets){
		return new ProxySetting<T>(key, defaultValue, targets);
	}
	
	/**
	 * Constructs a new proxy setting.
	 * @param <T> The setting data type.
	 * @param key The configuration key.
	 * @param target The target setting to update with received data.
	 * @return The newly constructed proxy setting.
	 */
	public static final <T> ProxySetting<T> of(String key, Setting<T> target){
		return new ProxySetting<T>(key, target.getDefaultValue(), target);
	}
}
