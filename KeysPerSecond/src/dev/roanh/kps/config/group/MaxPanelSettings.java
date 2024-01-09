package dev.roanh.kps.config.group;

import java.util.List;

import dev.roanh.kps.config.setting.ProxySetting;
import dev.roanh.kps.panels.MaxPanel;

public class MaxPanelSettings extends SpecialPanelSettings{

	public MaxPanelSettings(){
		super("MAX");
	}
	
	@Override
	public MaxPanel createPanel(){
		return new MaxPanel(this);
	}
	
	public void collectLegacyProxies(List<ProxySetting<?>> proxyList){
		proxyList.add(ProxySetting.of("maxX", x));
		proxyList.add(ProxySetting.of("maxY", y));
		proxyList.add(ProxySetting.of("maxWidth", width));
		proxyList.add(ProxySetting.of("maxHeight", height));
		proxyList.add(ProxySetting.of("maxMode", mode));
	}
}
