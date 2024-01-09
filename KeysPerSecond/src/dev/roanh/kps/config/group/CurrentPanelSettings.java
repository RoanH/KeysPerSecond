package dev.roanh.kps.config.group;

import java.util.List;

import dev.roanh.kps.config.setting.ProxySetting;
import dev.roanh.kps.panels.NowPanel;

public class CurrentPanelSettings extends SpecialPanelSettings{

	public CurrentPanelSettings(){
		super("KPS");
	}
	
	@Override
	public NowPanel createPanel(){
		return new NowPanel(this);
	}
	
	public void collectLegacyProxies(List<ProxySetting<?>> proxyList){
		proxyList.add(ProxySetting.of("curX", x));
		proxyList.add(ProxySetting.of("curY", y));
		proxyList.add(ProxySetting.of("curWidth", width));
		proxyList.add(ProxySetting.of("curHeight", height));
		proxyList.add(ProxySetting.of("curMode", mode));
	}
}
