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

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Map;

import dev.roanh.kps.config.GraphType;
import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.setting.IntSetting;
import dev.roanh.kps.config.setting.StringSetting;
import dev.roanh.kps.panels.CursorGraphPanel;
import dev.roanh.kps.ui.editor.CursorGraphEditor;
import dev.roanh.kps.ui.editor.Editor;

/**
 * Configuration for cursor graph panels.
 * @author Roan
 * @see CursorGraphPanel
 */
public class CursorGraphSettings extends GraphPanelSettings{
	/**
	 * The display being tracked by this panel.
	 */
	private final StringSetting display = new StringSetting("display", getDefaultScreenId());
	/**
	 * The backlog for how much of the cursor move history to show in milliseconds.
	 */
	private final IntSetting backlog = new IntSetting("backlog", 0, Integer.MAX_VALUE, 1000);

	/**
	 * Constructs new cursor graph settings.
	 */
	public CursorGraphSettings(){
		super(GraphType.CURSOR, 0, -1, -1, 6, "Cursor");
	}
	
	/**
	 * Gets the display being tracked by this panel.
	 * @return The display tracked by this panel or
	 *         null if the configured display could
	 *         not be found on the system.
	 */
	public GraphicsDevice getDisplay(){
		for(GraphicsDevice screen : getScreens()){
			if(screen.getIDstring().equals(getDisplayId())){
				return screen;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the ID of the display being tracked by this panel.
	 * @return The ID of the tracked display.
	 */
	public String getDisplayId(){
		return display.getValue();
	}
	
	/**
	 * Gets the backlog size in milliseconds for the graph.
	 * @return The cursor trail tracking backlog in milliseconds.
	 */
	public int getBacklog(){
		return backlog.getValue();
	}
	
	/**
	 * Updates the size of the backlog.
	 * @param backlog The new backlog size in milliseconds.
	 */
	public void setBacklog(int backlog){
		this.backlog.update(backlog);
	}
	
	/**
	 * Sets the ID of the display tracked by this panel.
	 * @param idString The ID of the display being tracked.
	 */
	public void setDisplay(String idString){
		display.update(idString);
	}

	@Override
	public void showEditor(boolean live){
		Editor.showEditor(new CursorGraphEditor(this, live));
	}

	@Override
	public CursorGraphPanel createGraph(){
		return new CursorGraphPanel(this);
	}
	
	@Override
	public boolean parse(Map<String, String> data){
		return super.parse(data) | findAndParse(data, display, backlog);
	}
	
	@Override
	public void writeItems(IndentWriter out){
		super.writeItems(out);
		display.write(out);
		backlog.write(out);
	}
	
	/**
	 * Lists all screens available on the current system.
	 * @return All screens on the current system.
	 */
	public static final GraphicsDevice[] getScreens(){
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
	}
	
	/**
	 * Computes the default screen to use.
	 * @return The identifier of the default screen to use or null for none.
	 */
	private static final String getDefaultScreenId(){
		//this check is primarily to ensure unit tests can run headless
		return GraphicsEnvironment.isHeadless() ? null : getScreens()[0].getIDstring();
	}
}
