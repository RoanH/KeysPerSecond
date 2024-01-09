package dev.roanh.kps.config.group;

import java.util.List;
import java.util.Map;

import dev.roanh.kps.GraphMode;
import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.setting.BooleanSetting;
import dev.roanh.kps.config.setting.GraphModeSetting;
import dev.roanh.kps.config.setting.IntSetting;
import dev.roanh.kps.config.setting.ProxySetting;
import dev.roanh.kps.panels.GraphPanel;

public class GraphSettings extends LocationSettings{
	private final GraphModeSetting mode = new GraphModeSetting("mode", GraphMode.INLINE);
	private final BooleanSetting showAvg = new BooleanSetting("showAvg", true);
	private final IntSetting backlog = new IntSetting("backlog", 1, Short.MAX_VALUE, 30);//TODO note that I put a lower limit here compared with the editor

	public GraphSettings(){
		super("graphs", 0, -1, -1, 3);
	}
	
	public GraphMode getGraphMode(){
		return mode.getValue();
	}
	
	public boolean isAverageVisible(){
		return showAvg.getValue();
	}
	
	public int getBacklog(){
		return backlog.getValue();
	}
	
	public void setGraphMode(GraphMode mode){
		this.mode.update(mode);
	}
	
	public void setAverageVisible(boolean visible){
		showAvg.update(visible);
	}
	
	public void setBacklog(int backlog){
		this.backlog.update(backlog);
	}
	
	public GraphPanel createPanel(){
		return new GraphPanel(this);
	}

	@Override
	public boolean parse(Map<String, String> data){
		return super.parse(data) | findAndParse(data, mode, showAvg, backlog);
	}
	
	@Override
	public void write(IndentWriter out){
		super.write(out);
		mode.write(out);
		showAvg.write(out);
		backlog.write(out);
	}
	
	public void collectLegacyProxies(List<ProxySetting<?>> proxyList){
		proxyList.add(ProxySetting.of("graphX", x));
		proxyList.add(ProxySetting.of("graphY", y));
		proxyList.add(ProxySetting.of("graphWidth", width));
		proxyList.add(ProxySetting.of("graphHeight", height));
		proxyList.add(ProxySetting.of("graphMode", mode));
		proxyList.add(ProxySetting.of("graphBacklog", backlog));
		proxyList.add(ProxySetting.of("graphAverage", showAvg));
	}
}
