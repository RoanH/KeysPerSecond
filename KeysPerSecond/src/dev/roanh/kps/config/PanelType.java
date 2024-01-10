package dev.roanh.kps.config;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import dev.roanh.kps.config.ListItemConstructor.ParsedItem;
import dev.roanh.kps.config.group.AveragePanelSettings;
import dev.roanh.kps.config.group.CurrentPanelSettings;
import dev.roanh.kps.config.group.MaxPanelSettings;
import dev.roanh.kps.config.group.PanelSettings;
import dev.roanh.kps.config.group.SpecialPanelSettings;
import dev.roanh.kps.config.group.TotalPanelSettings;

public enum PanelType{
	MAX("max", MaxPanelSettings::new),
	AVG("avg", AveragePanelSettings::new),
	CURRENT("current", CurrentPanelSettings::new),//TODO current/cur/kps/now ???
	TOTAL("total", TotalPanelSettings::new);
	
	private final String key;
	private final Supplier<SpecialPanelSettings> ctor;
	
	private <T extends PanelSettings> PanelType(String key, Supplier<SpecialPanelSettings> ctor){
		this.key = key;
		this.ctor = ctor;
	}
	
	public static ParsedItem<SpecialPanelSettings> construct(List<String> data){
		Map<String, String> info = ListItemConstructor.buildMap(data);
		if(info == null){
			return null;
		}
		
		String key = info.get("type");
		for(PanelType type : values()){
			if(type.key.equals(key)){
				return ListItemConstructor.constructThenParse(type.ctor, info);
			}
		}
		
		return null;
	}
}
