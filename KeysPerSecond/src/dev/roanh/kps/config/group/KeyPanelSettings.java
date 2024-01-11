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

import java.util.Map;
import java.util.Objects;

import dev.roanh.kps.Key;
import dev.roanh.kps.KeyInformation;
import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.setting.BooleanSetting;
import dev.roanh.kps.config.setting.IntSetting;
import dev.roanh.kps.panels.KeyPanel;
import dev.roanh.kps.ui.dialog.KeyPanelEditor;
import dev.roanh.kps.ui.dialog.PanelEditor;

public class KeyPanelSettings extends PanelSettings{
	private final BooleanSetting visible = new BooleanSetting("visible", true);
	private final IntSetting keycode = new IntSetting("keycode", Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
	
	public KeyPanelSettings(){
		super("keys", "");
	}
	
	public KeyPanelSettings(int x, int extendedCode){
		super("keys", x, 0, 2, 3, KeyInformation.getPanelName(extendedCode));
		keycode.update(extendedCode);
	}
	
	public int getKeyCode(){
		return keycode.getValue();
	}
	
	public boolean isVisible(){
		return visible.getValue();
	}
	
	public void setVisible(boolean visible){
		this.visible.update(visible);
	}
	
	public KeyPanel createPanel(Key data){
		return new KeyPanel(data, this);
	}
	
	@Override
	public boolean parse(Map<String, String> data){
		return super.parse(data) | findAndParse(data, visible, keycode);
	}
	
	@Override
	public void write(IndentWriter out){
		super.write(out);
		visible.write(out);
		keycode.write(out);
	}
	
	@Override
	public void showEditor(boolean live){
		PanelEditor.showEditor(new KeyPanelEditor(this, live));
	}
	
	@Override
	public boolean equals(Object obj){
		return (obj instanceof KeyPanelSettings) && ((KeyPanelSettings)obj).keycode.getValue().equals(keycode.getValue());
	}
	
	@Override
	public int hashCode(){
		return Objects.hashCode(keycode.getValue());
	}
}
