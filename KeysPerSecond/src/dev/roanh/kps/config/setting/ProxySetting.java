package dev.roanh.kps.config.setting;

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;

public class ProxySetting<T> extends Setting<T>{
	private final Setting<T> target;

	private ProxySetting(String key, Setting<T> target){
		super(key, target.getDefaultValue());
		this.target = target;
	}

	@Override
	public boolean parse(String data){
		return target.parse(data);
	}

	@Override
	protected void write(IndentWriter out){
		throw new IllegalStateException("Legacy proxy settings should never be written.");
	}
	
	public static final <T> ProxySetting<T> of(String key, Setting<T> target){
		return new ProxySetting<T>(key, target);
	}
}
