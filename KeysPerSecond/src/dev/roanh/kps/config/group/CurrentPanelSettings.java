package dev.roanh.kps.config.group;

import java.util.List;

import dev.roanh.kps.config.PanelType;
import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.setting.ProxySetting;
import dev.roanh.kps.panels.NowPanel;
import dev.roanh.kps.ui.dialog.PanelEditor;

public class CurrentPanelSettings extends SpecialPanelSettings{

	public CurrentPanelSettings(){
		super(PanelType.CURRENT, "KPS");
	}
	
	@Override
	public NowPanel createPanel(){
		return new NowPanel(this);
	}
	
	@Override
	public void showEditor(boolean live){
		PanelEditor.showEditor(this, live);
	}
	
	public void collectLegacyProxies(List<Setting<?>> proxyList){
		proxyList.add(ProxySetting.of("curX", x));
		proxyList.add(ProxySetting.of("curY", y));
		proxyList.add(ProxySetting.of("curWidth", width));
		proxyList.add(ProxySetting.of("curHeight", height));
		proxyList.add(ProxySetting.of("curMode", mode));
	}
}
