package dev.roanh.kps.config.group;

import dev.roanh.kps.panels.BasePanel;

public abstract class SpecialPanelSettings extends PanelSettings{

	public SpecialPanelSettings(String defaultName){
		super("panels", defaultName);
	}

	public abstract BasePanel createPanel();
}
