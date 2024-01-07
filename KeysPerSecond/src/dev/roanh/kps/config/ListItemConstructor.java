package dev.roanh.kps.config;

import java.util.Map;
import java.util.function.Supplier;

@FunctionalInterface
public abstract interface ListItemConstructor<T extends SettingGroup>{

	public abstract ParsedItem<T> construct(Map<String, String> data);
	
	public static <T extends SettingGroup> ListItemConstructor<T> constructThenParse(Supplier<T> ctor){
		return data->{
			T item = ctor.get();
			return ParsedItem.of(item.parse(data), item);
		};
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
