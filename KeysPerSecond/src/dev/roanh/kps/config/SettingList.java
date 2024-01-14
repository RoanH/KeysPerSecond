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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import dev.roanh.kps.config.ListItemConstructor.ParsedItem;

/**
 * Setting representing a list of setting groups. The key
 * of the groups is ignored and instead represented by the
 * key of this list.
 * @author Roan
 * @param <T> The list data type.
 * @see SettingGroup
 * @see ListItemConstructor
 */
public class SettingList<T extends SettingGroup> implements Iterable<T>{
	/**
	 * The key identifier for this list.
	 */
	private final String key;
	/**
	 * The data items for this list.
	 */
	private final List<T> data = new ArrayList<T>();
	/**
	 * Constructor used to parse new items.
	 */
	private final ListItemConstructor<T> itemConstructor;
	
	/**
	 * Constructs a new settings list.
	 * @param key The key identifier for this list.
	 * @param itemConstructor The constructor to use to parse new list items.
	 */
	public SettingList(String key, ListItemConstructor<T> itemConstructor){
		this.key = key;
		this.itemConstructor = itemConstructor;
	}
	
	/**
	 * Gets the key identifier for this list.
	 * @return The key for this key.
	 */
	public String getKey(){
		return key;
	}
	
	/**
	 * Adds a new item to this list as parsed from the given raw data.
	 * @param itemData The raw settings group data to parse.
	 * @return If true then default values were used when parsing the
	 *         given data, possibly adding an item at all failed completely.
	 */
	public boolean add(List<String> itemData){
		ParsedItem<T> result = itemConstructor.construct(itemData);
		if(result == null){
			return true;
		}
		
		add(result.item());
		return result.defaultUsed();
	}
	
	/**
	 * Adds a new item to this list.
	 * @param group The item to add.
	 */
	public void add(T group){
		data.add(group);
	}
	
	/**
	 * Gets the size of this list.
	 * @return The size of this list.
	 */
	public int size(){
		return data.size();
	}
	
	/**
	 * Gets the item at the given index in this list.
	 * @param index The index to get.
	 * @return The item at the requested index.
	 * @throws IndexOutOfBoundsException When the given index is out of bounds.
	 */
	public T get(int index) throws IndexOutOfBoundsException{
		return data.get(index);
	}
	
	/**
	 * Removes the item at the given index in this list.
	 * @param index The index of the item to remove.
	 * @return The item that was at the given index.
	 * @throws IndexOutOfBoundsException When the given index is out of bounds.
	 */
	public T remove(int index) throws IndexOutOfBoundsException{
		return data.remove(index);
	}
	
	/**
	 * Tests if this list contains the given item.
	 * @param obj The item to find.
	 * @return True if this list contains the given item.
	 */
	public boolean contains(T obj){
		return data.contains(obj);
	}
	
	/**
	 * Test if this list contains an item that is equal to
	 * the given object after being mapped by the given function.
	 * @param <K> The type of the key object to extract.
	 * @param obj The object to search for after mapping all list items.
	 * @param keyExtractor The function to use to map list items.
	 * @return True if this list contains the given item after mapping.
	 */
	public <K> boolean contains(K obj, Function<T, K> keyExtractor){
		return stream().map(keyExtractor).anyMatch(obj::equals);
	}
	
	/**
	 * Returns a stream over the elements in this list.
	 * @return A stream over the elements in this list.
	 */
	public Stream<T> stream(){
		return data.stream();
	}
	
	/**
	 * Removes all items from this list that match the given predicate.
	 * @param condition The predicate to determine which elements to remove.
	 */
	public void removeIf(Predicate<T> condition){
		data.removeIf(condition);
	}
	
	/**
	 * Checks if this list is empty.
	 * @return True if this list is empty.
	 */
	public boolean isEmpty(){
		return data.isEmpty();
	}
	
	/**
	 * Removes all elements from this list.
	 */
	public void clear(){
		data.clear();
	}
	
	/**
	 * Removes the given object from this list.
	 * @param elem The object to remove.
	 * @return True if the object was present and removed successfully.
	 */
	public boolean remove(Object elem){
		return data.remove(elem);
	}
	
	/**
	 * Writes this list to the given writer.
	 * @param out The writer to write to.
	 */
	public void write(IndentWriter out){
		out.println(key + ":");
		for(T item : data){
			out.startListItem();
			item.writeItems(out);
			out.endListItem();
		}
	}

	@Override
	public Iterator<T> iterator(){
		return data.iterator();
	}
}
