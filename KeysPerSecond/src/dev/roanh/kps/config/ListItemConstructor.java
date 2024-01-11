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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@FunctionalInterface
public abstract interface ListItemConstructor<T extends SettingGroup>{

	//null indicates invalid/malformed/error/etc
	public abstract ParsedItem<T> construct(List<String> data);
	
	public static <T extends SettingGroup> ListItemConstructor<T> constructThenParse(Supplier<T> ctor){
		return data->{
			Map<String, String> map = buildMap(data);
			if(map == null){
				return null;
			}
			
			return constructThenParse(ctor, map);
		};
	}
	
	public static <T extends SettingGroup> ParsedItem<T> constructThenParse(Supplier<T> ctor, Map<String, String> data){
		T item = ctor.get();
		return ParsedItem.of(item.parse(data), item);
	}
	
	//null for invalid/not possible
	public static Map<String, String> buildMap(List<String> data){
		Map<String, String> map = new HashMap<String, String>();
		for(String line : data){
			int mark = line.indexOf(':');
			if(mark == -1){
				return null;
			}
			
			map.put(line.substring(0, mark).trim(), line.substring(mark + 1, line.length()).trim());
		}
		return map;
	}
	
	public static final class ParsedItem<T>{
		private final boolean defaultUsed;
		private final T item;
		
		private ParsedItem(boolean defaultUsed, T item){
			this.defaultUsed = defaultUsed;
			this.item = item;
		}
		
		public boolean defaultUsed(){
			return defaultUsed;
		}
		
		public T item(){
			return item;
		}
		
		public static final <T extends SettingGroup> ParsedItem<T> of(boolean defaultUsed, T item){
			return new ParsedItem<T>(defaultUsed, item);
		}
	}
}
