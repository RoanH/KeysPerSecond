package dev.roanh.kps.config.group;

import java.util.List;

import dev.roanh.kps.config.PanelType;
import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.setting.ProxySetting;
import dev.roanh.kps.panels.TotPanel;
import dev.roanh.kps.ui.dialog.PanelEditor;

public class TotalPanelSettings extends SpecialPanelSettings{

	public TotalPanelSettings(){
		super(PanelType.TOTAL, "TOT");
	}

	@Override
	public TotPanel createPanel(){
		return new TotPanel(this);
	}
	
	@Override
	public void showEditor(boolean live){
		PanelEditor.showEditor(this, live);
	}
	
	public void collectLegacyProxies(List<Setting<?>> proxyList){
		proxyList.add(ProxySetting.of("totX", x));
		proxyList.add(ProxySetting.of("totY", y));
		proxyList.add(ProxySetting.of("totWidth", width));
		proxyList.add(ProxySetting.of("totHeight", height));
		proxyList.add(ProxySetting.of("totMode", mode));
	}
}
