package dev.roanh.kps.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import dev.roanh.kps.config.ListItemConstructor.ParsedItem;

public class SettingList<T extends SettingGroup> implements Iterable<T>{
	private final String key;
	private final List<T> data = new ArrayList<T>();
	private final ListItemConstructor<T> itemConstructor;
	
	public SettingList(String key, ListItemConstructor<T> itemConstructor){
		this.key = key;
		this.itemConstructor = itemConstructor;
	}
	
	public String getKey(){
		return key;
	}
	
	public boolean add(List<String> itemData){
		ParsedItem<T> result = itemConstructor.construct(itemData);
		if(result == null){
			return true;
		}
		
		add(result.item());
		return result.defaultUsed();
	}
	
	public void add(T group){
		data.add(group);
	}
	
	public int size(){
		return data.size();
	}
	
	public T get(int index){
		return data.get(index);
	}
	
	public T remove(int index){
		return data.remove(index);
	}
	
	public boolean contains(T obj){
		return data.contains(obj);
	}
	
	public <K> boolean contains(K obj, Function<T, K> keyExtractor){
		return stream().map(keyExtractor).anyMatch(obj::equals);
	}
	
	public Stream<T> stream(){
		return data.stream();
	}
	
	public void removeIf(Predicate<T> condition){
		data.removeIf(condition);
	}
	
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
