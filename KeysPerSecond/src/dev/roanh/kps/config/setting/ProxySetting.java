package dev.roanh.kps.config.setting;

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;

public class ProxySetting<T> extends Setting<T>{
	private final Setting<T>[] targets;
	
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
	
	@SafeVarargs
	public static final <T> ProxySetting<T> of(String key, T defaultValue, Setting<T>... targets){
		return new ProxySetting<T>(key, defaultValue, targets);
	}
	
	public static final <T> ProxySetting<T> of(String key, Setting<T> target){
		return new ProxySetting<T>(key, target.getDefaultValue(), target);
	}
}
