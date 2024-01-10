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
