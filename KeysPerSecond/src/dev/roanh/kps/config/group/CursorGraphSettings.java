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

public class CursorGraphSettings extends GraphPanelSettings{
	private final StringSetting display = new StringSetting("display", getDefaultScreenId());
	private final IntSetting backlog = new IntSetting("backlog", 0, Integer.MAX_VALUE, 1000);

	public CursorGraphSettings(){
		super(GraphType.CURSOR, 0, -1, -1, 6, "Cursor");
	}
	
	public GraphicsDevice getDisplay(){
		for(GraphicsDevice screen : getScreens()){
			if(screen.getIDstring().equals(getDisplayId())){
				return screen;
			}
		}
		
		return null;
	}
	
	public String getDisplayId(){
		return display.getValue();
	}
	
	public int getBacklog(){
		return backlog.getValue();
	}
	
	public void setBacklog(int backlog){
		this.backlog.update(backlog);
	}
	
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
	
	public static final GraphicsDevice[] getScreens(){
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
	}
	
	private static final String getDefaultScreenId(){
		return GraphicsEnvironment.isHeadless() ? null : getScreens()[0].getIDstring();
	}
}
