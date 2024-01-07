package dev.roanh.kps.config;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import dev.roanh.kps.config.ListItemConstructor.ParsedItem;
import dev.roanh.kps.config.group.AveragePanelSettings;
import dev.roanh.kps.config.group.CurrentPanelSettings;
import dev.roanh.kps.config.group.MaxPanelSettings;
import dev.roanh.kps.config.group.PanelSettings;
import dev.roanh.kps.config.group.SpecialPanelSettings;
import dev.roanh.kps.panels.AvgPanel;
import dev.roanh.kps.panels.BasePanel;

public enum PanelType{
	MAX("max", MaxPanelSettings::new),
	AVG("avg", AveragePanelSettings::new),
	CURRENT("current", CurrentPanelSettings::new),//TODO current/cur/kps/now ???
	TOTAL;
	
	//TODO final
	private String key;
	private ListItemConstructor<SpecialPanelSettings> ctor;
	
	private PanelType(){
		
	}
	
	private <T extends PanelSettings> PanelType(String key, Supplier<SpecialPanelSettings> ctor){
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
