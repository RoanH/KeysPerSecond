package dev.roanh.kps.config.group;

import dev.roanh.kps.panels.NowPanel;

public class CurrentPanelSettings extends SpecialPanelSettings{

	public CurrentPanelSettings(){
		super("KPS");
	}
	
	@Override
	public NowPanel createPanel(){
		return new NowPanel(this);
	}
}
