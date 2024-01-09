package dev.roanh.kps.config;

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
	private final ListItemConstructor<SpecialPanelSettings> ctor;
	
	private <T extends PanelSettings> PanelType(String key, Supplier<SpecialPanelSettings> ctor){
		this.key = key;
		this.ctor = ListItemConstructor.constructThenParse(ctor);
	}
	
	public static ParsedItem<SpecialPanelSettings> construct(Map<String, String> data){
		String key = data.get("type");
		for(PanelType type : values()){
			if(type.key.equals(key)){
				return type.ctor.construct(data);
			}
		}
		
		//TODO invalid key so nothing at all?
		return null;
	}
}
