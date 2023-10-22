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

public abstract class Setting<T>{
	protected final String key;
	protected T value;
	private T defaultValue;
	
	protected Setting(String key, T defaultValue){
		this.key = key;
		this.defaultValue = defaultValue;
		value = defaultValue;
	}
	
	protected abstract boolean parse(String data);
	
	protected abstract void write(IndentWriter out);
	
	public String getKey(){
		return key;
	}
	
	public void update(T newValue){
		value = newValue;
	}
	
	public void reset(){
		value = defaultValue;
	}
	
	public T getValue(){
		return value;
	}
}
