package dev.roanh.kps.config.group;

import java.util.Map;

import dev.roanh.kps.GraphMode;
import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.setting.BooleanSetting;
import dev.roanh.kps.config.setting.GraphModeSetting;
import dev.roanh.kps.config.setting.IntSetting;

public class GraphSettings extends LocationSettings{
	private final GraphModeSetting mode = new GraphModeSetting("mode", GraphMode.INLINE);
	private final BooleanSetting showAvg = new BooleanSetting("showAvg", true);
	private final IntSetting backlog = new IntSetting("backlog", 1, Short.MAX_VALUE, 30);//TODO note that I put a lower limit here compared with the editor

	public GraphSettings(){
		super("graphs", 0, -1, -1, 3);
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
}
