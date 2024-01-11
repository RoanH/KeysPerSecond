package dev.roanh.kps.config.group;

import java.util.List;
import java.util.Map;

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.setting.BooleanSetting;
import dev.roanh.kps.config.setting.IntSetting;
import dev.roanh.kps.config.setting.ProxySetting;
import dev.roanh.kps.panels.GraphPanel;

public class GraphSettings extends LocationSettings{
	/**
	 * Whether to draw the horizontal average line or not.
	 */
	private final BooleanSetting showAvg = new BooleanSetting("showAvg", true);
	/**
	 * Number of points the graph consists of.
	 */
	private final IntSetting backlog = new IntSetting("backlog", 1, Short.MAX_VALUE, 30);//TODO note that I put a lower limit here compared with the editor

	public GraphSettings(){
		super("graphs", 0, -1, -1, 3);
	}
	
	public boolean isAverageVisible(){
		return showAvg.getValue();
	}
	
	public int getBacklog(){
		return backlog.getValue();
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
		return super.parse(data) | findAndParse(data, showAvg, backlog);
	}
	
	@Override
	public void write(IndentWriter out){
		super.write(out);
		showAvg.write(out);
		backlog.write(out);
	}
	
	public void collectLegacyProxies(List<Setting<?>> proxyList){
		proxyList.add(ProxySetting.of("graphX", x));
		proxyList.add(ProxySetting.of("graphY", y));
		proxyList.add(ProxySetting.of("graphWidth", width));
		proxyList.add(ProxySetting.of("graphHeight", height));
		proxyList.add(ProxySetting.of("graphBacklog", backlog));
		proxyList.add(ProxySetting.of("graphAverage", showAvg));
		proxyList.add(new LegacyGraphSetting("graphMode", "INLINE"));
		proxyList.add(new LegacyGraphSetting("graphPosition", null));
	}
	
	/**
	 * Small legacy setting that ensures parsing of the legacy
	 * graphMode setting does not throw a warning that a default
	 * was used as long as the configured value was set to the
	 * only currently supported option of inline. In addition this
	 * setting silently discards the legacy graphPosition setting
	 * that accompanied the detached graph mode setting.
	 * @author Roan
	 */
	private static final class LegacyGraphSetting extends Setting<String>{

		/**
		 * Constructs a new legacy graph mode setting.
		 * @param key The setting key, either graphMode or graphPosition.
		 * @param required The only accepted value to not trigger a
		 *        warning that a default value was used. If null all
		 *        values are accepted without generating a warning.
		 */
		private LegacyGraphSetting(String key, String required){
			super(key, required);
		}

		@Override
		public boolean parse(String data){
			return getDefaultValue() != null && !data.equalsIgnoreCase(getDefaultValue());
		}

		@Override
		public void write(IndentWriter out){
			throw new IllegalStateException("Legacy proxy settings should never be written.");
		}
	}
}
