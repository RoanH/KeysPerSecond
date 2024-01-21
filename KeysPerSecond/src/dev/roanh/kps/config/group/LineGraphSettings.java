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

import dev.roanh.kps.config.GraphType;
import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.legacy.LegacyGraphSetting;
import dev.roanh.kps.config.legacy.LegacyProxyStore;
import dev.roanh.kps.config.legacy.ProxySetting;
import dev.roanh.kps.config.setting.BooleanSetting;
import dev.roanh.kps.config.setting.IntSetting;
import dev.roanh.kps.panels.LineGraphPanel;
import dev.roanh.kps.ui.editor.Editor;
import dev.roanh.kps.ui.editor.LineGraphEditor;

/**
 * Configuration for line graph panels.
 * @author Roan
 * @see LineGraphPanel
 */
public class LineGraphSettings extends GraphPanelSettings implements LegacyProxyStore{
	/**
	 * Whether to draw the horizontal average line or not.
	 */
	private final BooleanSetting showAvg = new BooleanSetting("showAvg", true);
	/**
	 * Number of points the graph consists of.
	 */
	private final IntSetting backlog = new IntSetting("backlog", 2, Integer.MAX_VALUE, 3000);
	/**
	 * Maximum graph y value that will be displayed. Any higher values are capped at this value.
	 */
	private final IntSetting max = new IntSetting("max", 1, Integer.MAX_VALUE, Integer.MAX_VALUE);

	/**
	 * Creates new graph settings.
	 */
	public LineGraphSettings(){
		super(GraphType.LINE, 0, -1, -1, 3, "Line");
	}
	
	/**
	 * Sets the new maximum value to be displayed by this graph.
	 * @param max The new graph maximum.
	 */
	public void setMaxValue(int max){
		this.max.update(max);
	}
	
	/**
	 * Gets the maximum y value for this graph.
	 * @return The maximum y value.
	 */
	public int getMaxValue(){
		return max.getValue();
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
	
	@Override
	public LineGraphPanel createGraph(){
		return new LineGraphPanel(this);
	}
	
	@Override
	public void showEditor(boolean live){
		Editor.showEditor(new LineGraphEditor(this, live));
	}

	@Override
	public boolean parse(Map<String, String> data){
		return super.parse(data) | findAndParse(data, showAvg, backlog, max);
	}
	
	@Override
	public void writeItems(IndentWriter out){
		super.writeItems(out);
		showAvg.write(out);
		backlog.write(out);
		max.write(out);
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
}
