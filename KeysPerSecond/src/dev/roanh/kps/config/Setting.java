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

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Abstract base class for automatically validating settings.
 * @author Roan
 * @param <T> The data type for this setting.
 */
public abstract class Setting<T>{
	/**
	 * The configuration file key identifying this setting.
	 */
	protected final String key;
	/**
	 * The current value for this setting.
	 */
	protected T value;
	/**
	 * The default value for this setting.
	 */
	private T defaultValue;
	
	/**
	 * Constructs a new setting with the given identifier and default value.
	 * @param key The identifier for this setting.
	 * @param defaultValue The default value for this setting.
	 */
	protected Setting(String key, T defaultValue){
		this.key = key;
		this.defaultValue = defaultValue;
		update(defaultValue);
	}
	
	/**
	 * Parses this setting from the given string data. This function will either
	 * call {@link #update(Object)} to set the parsed setting or {@link #reset()}
	 * to signal that the setting could not be parsed from the given data.
	 * @param data The data to attempt to parse.
	 * @return True if the data could not be parsed, meaning the default value was used.
	 */
	public abstract boolean parse(String data);
	
	/**
	 * Writes this setting to the given writer.
	 * @param out The writer to write to.
	 */
	public abstract void write(IndentWriter out);
	
	/**
	 * Gets the key identifier for this setting.
	 * @return The key identifier for this setting.
	 */
	public String getKey(){
		return key;
	}
	
	/**
	 * Updates this setting with a new value. Note
	 * that this method does not validate values.
	 * @param newValue The new value to set.
	 */
	public void update(T newValue){
		value = newValue;
	}
	
	/**
	 * Resets this setting to its default value.
	 */
	public void reset(){
		value = defaultValue;
	}
	
	/**
	 * Gets the current value for this setting.
	 * @return The current value for this setting.
	 */
	public T getValue(){
		return value;
	}
	
	/**
	 * Gets the default value for this setting.
	 * @return The default value for this setting.
	 */
	public T getDefaultValue(){
		return defaultValue;
	}
	
	@Override
	public String toString(){
		StringWriter writer = new StringWriter();
		write(new IndentWriter(new PrintWriter(writer)));
		return "Setting[" + writer.toString().replace('\n', ']');
	}
}
