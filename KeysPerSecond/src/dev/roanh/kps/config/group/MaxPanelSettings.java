package dev.roanh.kps.config.group;

import dev.roanh.kps.panels.MaxPanel;

public class MaxPanelSettings extends SpecialPanelSettings{

	public MaxPanelSettings(){
		super("MAX");
	}
	
	@Override
	public MaxPanel createPanel(){
		return new MaxPanel(this);
	}
}
