package dev.roanh.kps.config.group;

import java.util.List;

import dev.roanh.kps.config.PanelType;
import dev.roanh.kps.config.setting.ProxySetting;
import dev.roanh.kps.panels.MaxPanel;
import dev.roanh.kps.ui.dialog.PanelEditor;

public class MaxPanelSettings extends SpecialPanelSettings{

	public MaxPanelSettings(){
		super(PanelType.MAX, "MAX");
	}
	
	@Override
	public MaxPanel createPanel(){
		return new MaxPanel(this);
	}
	
	@Override
	public void showEditor(boolean live){
		PanelEditor.showEditor(this, live);
	}
	
	public void collectLegacyProxies(List<ProxySetting<?>> proxyList){
		proxyList.add(ProxySetting.of("maxX", x));
		proxyList.add(ProxySetting.of("maxY", y));
		proxyList.add(ProxySetting.of("maxWidth", width));
		proxyList.add(ProxySetting.of("maxHeight", height));
		proxyList.add(ProxySetting.of("maxMode", mode));
	}
}
