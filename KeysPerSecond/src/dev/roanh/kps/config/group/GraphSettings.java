/*
 * KeysPerSecond: An open source input statistics displayer.
 * Copyright (C) 2017  Roan Hofland (roan@roanh.dev).  All rights reserved.
 * GitHub Repository: https://github.com/RoanH/KeysPerSecond
 *
 * KeysPerSecond is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KeysPerSecond is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dev.roanh.kps.config.group;

import java.util.List;
import java.util.Map;

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.LegacyProxyStore;
import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.setting.BooleanSetting;
import dev.roanh.kps.config.setting.IntSetting;
import dev.roanh.kps.config.setting.ProxySetting;
import dev.roanh.kps.panels.GraphPanel;

/**
 * Configuration for graph panels.
 * @author Roan
 * @see GraphPanel
 */
public class GraphSettings extends LocationSettings implements LegacyProxyStore{
	/**
	 * Whether to draw the horizontal average line or not.
	 */
	private final BooleanSetting showAvg = new BooleanSetting("showAvg", true);
	/**
	 * Number of points the graph consists of.
	 */
	private final IntSetting backlog = new IntSetting("backlog", 2, Short.MAX_VALUE, 30);

	/**
	 * Creates new graph settings.
	 */
	public GraphSettings(){
		super("graphs", 0, -1, -1, 3);
	}
	
	/**
	 * Check if the average should be drawn in the graph.
	 * @return True if the average KPS should be drawn in the graph.
	 */
	public boolean isAverageVisible(){
		return showAvg.getValue();
	}
	
	/**
	 * Gets the backlog for the graph, this is the number of events
	 * or data points tracked for the graph. Each update cycle an
	 * event is generated so this value is affected by the update rate.
	 * @return The backlog for the graph.
	 */
	public int getBacklog(){
		return backlog.getValue();
	}
	
	/**
	 * Sets if the average should be shown in the graph.
	 * @param visible True if the average KPS should be drawn.
	 */
	public void setAverageVisible(boolean visible){
		showAvg.update(visible);
	}
	
	/**
	 * Sets the event backlog size for the graph.
	 * @param backlog The new backlog value.
	 * @see #getBacklog()
	 */
	public void setBacklog(int backlog){
		this.backlog.update(backlog);
	}
	
	/**
	 * Creates a new graph panel with this configuration.
	 * @return The newly created graph panel.
	 */
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
	
	@Override
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
