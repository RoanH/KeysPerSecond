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

/**
 * Interface for functions that can construct new items for a settings list.
 * @author Roan
 * @param <T> The type of the constructed items.
 * @see SettingList
 */
@FunctionalInterface
public abstract interface ListItemConstructor<T extends SettingGroup>{

	/**
	 * Constructs a new list item from the given raw data.
	 * @param data The data to parse item a new list item.
	 * @return The result of attempting to parse the given data
	 *         or null if the data could not be parsed at all due
	 *         to it being invalid, malformed, or causing an error.
	 */
	public abstract ParsedItem<T> construct(List<String> data);
	
	/**
	 * Creates a new list item constructor that first constructs a new
	 * setting group using the supplied constructor and then calls its
	 * parse method after the raw list item data has been turned into
	 * a key-value map.
	 * @param <T> The type of the constructed list items.
	 * @param ctor The constructor to use to create new settings group objects.
	 * @return The created list item constructor.
	 * @see #constructThenParse(Supplier, Map)
	 * @see SettingGroup#parse(Map)
	 * @see #buildMap(List)
	 */
	public static <T extends SettingGroup> ListItemConstructor<T> constructThenParse(Supplier<T> ctor){
		return data->{
			Map<String, String> map = buildMap(data);
			if(map == null){
				return null;
			}
			
			return constructThenParse(ctor, map);
		};
	}
	
	/**
	 * Creates a new list item constructor that first constructs a new
	 * setting group using the supplied constructor and then calls its
	 * parse method.
	 * @param <T> The type of the constructed list items.
	 * @param ctor The constructor to use to create new settings group objects.
	 * @param data The data to parse.
	 * @return The created list item constructor.
	 * @see SettingGroup#parse(Map)
	 */
	public static <T extends SettingGroup> ParsedItem<T> constructThenParse(Supplier<T> ctor, Map<String, String> data){
		T item = ctor.get();
		return ParsedItem.of(item.parse(data), item);
	}
	
	/**
	 * Builds a key-value map by splitting each data item on the colon character.
	 * @param data The data items to transform.
	 * @return A map with key-value entries or null if not all data could be split.
	 */
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
	
	/**
	 * Result of parsing a new list item.
	 * @author Roan
	 * @param <T> The type of the parsed result item.
	 */
	public static final class ParsedItem<T extends SettingGroup>{
		/**
		 * True if a default value was used during parsing.
		 */
		private final boolean defaultUsed;
		/**
		 * The parsed item.
		 */
		private final T item;
		
		/**
		 * Constructs a new parsed item.
		 * @param defaultUsed True if a default value was used.
		 * @param item The parsed item.
		 */
		private ParsedItem(boolean defaultUsed, T item){
			this.defaultUsed = defaultUsed;
			this.item = item;
		}
		
		/**
		 * Returns if a default value was used during item parsing.
		 * @return True if a default value was used during parsing.
		 */
		public boolean defaultUsed(){
			return defaultUsed;
		}
		
		/**
		 * Gets the item that was constructed.
		 * @return The parsed item.
		 */
		public T item(){
			return item;
		}

		/**
		 * Constructs a new parsed item.
		 * @param <T> The type of the parsed result item.
		 * @param defaultUsed True if a default value was used.
		 * @param item The parsed item.
		 * @return The parsed item.
		 */
		public static final <T extends SettingGroup> ParsedItem<T> of(boolean defaultUsed, T item){
			return new ParsedItem<T>(defaultUsed, item);
		}
	}
}
