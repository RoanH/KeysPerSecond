package dev.roanh.kps.config.group;

import java.util.List;

import dev.roanh.kps.config.PanelType;
import dev.roanh.kps.config.setting.ProxySetting;
import dev.roanh.kps.panels.TotPanel;

public class TotalPanelSettings extends SpecialPanelSettings{

	public TotalPanelSettings(){
		super(PanelType.TOTAL, "TOT");
	}

	@Override
	public TotPanel createPanel(){
		return new TotPanel(this);
	}
	
	public void collectLegacyProxies(List<ProxySetting<?>> proxyList){
		proxyList.add(ProxySetting.of("totX", x));
		proxyList.add(ProxySetting.of("totY", y));
		proxyList.add(ProxySetting.of("totWidth", width));
		proxyList.add(ProxySetting.of("totHeight", height));
		proxyList.add(ProxySetting.of("totMode", mode));
	}
}
