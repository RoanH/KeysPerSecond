package dev.roanh.kps.config.group;

import dev.roanh.kps.panels.BasePanel;
import dev.roanh.kps.panels.TotPanel;

public class TotalPanelSettings extends SpecialPanelSettings{

	public TotalPanelSettings(){
		super("TOT");
	}

	@Override
	public TotPanel createPanel(){
		return new TotPanel(this);
	}
}
