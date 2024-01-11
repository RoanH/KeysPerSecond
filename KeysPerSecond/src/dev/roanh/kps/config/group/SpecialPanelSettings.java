package dev.roanh.kps.config.group;

import dev.roanh.kps.config.PanelType;
import dev.roanh.kps.panels.BasePanel;

public abstract class SpecialPanelSettings extends PanelSettings{
	private final PanelType type;

	public SpecialPanelSettings(PanelType type, String defaultName){
		super("panels", defaultName);
		this.type = type;
	}

	public abstract BasePanel createPanel();
	
	public PanelType getType(){
		return type;
	}
}
