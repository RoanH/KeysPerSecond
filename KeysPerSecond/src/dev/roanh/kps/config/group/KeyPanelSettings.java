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

import java.awt.Point;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import dev.roanh.kps.CommandKeys;
import dev.roanh.kps.Key;
import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.setting.BooleanSetting;
import dev.roanh.kps.config.setting.IntSetting;
import dev.roanh.kps.panels.KeyPanel;
import dev.roanh.kps.ui.editor.Editor;
import dev.roanh.kps.ui.editor.KeyPanelEditor;

/**
 * Configuration for a key panel.
 * @author Roan
 * @see Key
 * @see KeyPanel
 */
public class KeyPanelSettings extends DataPanelSettings{
	/**
	 * Whether this key panel is visible or not (rendered).
	 * Hidden panels still track input statistics.
	 */
	private final BooleanSetting visible = new BooleanSetting("visible", true);
	/**
	 * The key code for the key associated with the panel. Note
	 * that this could be a mouse button.
	 */
	private final IntSetting keycode = new IntSetting("keycode", Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
	
	/**
	 * Constructs new key settings.
	 */
	public KeyPanelSettings(){
		super("keys", "");
	}
	
	/**
	 * Constructs new key settings for the given key.
	 * @param loc The layout location position to place the key at.
	 * @param extendedCode The key code of the key for the panel.
	 * @see CommandKeys#getExtendedKeyCode(int, boolean, boolean, boolean)
	 */
	public KeyPanelSettings(Point loc, int extendedCode){
		super("keys", loc.x, loc.y, 2, 3, getPanelName(extendedCode));
		keycode.update(extendedCode);
	}
	
	/**
	 * Gets the extended key code for the key associated with this panel.
	 * @return The key code for this panel.
	 */
	public int getKeyCode(){
		return keycode.getValue();
	}
	
	/**
	 * Checks if this panel should be rendered in the GUI or not.
	 * @return True if this panel is visible.
	 */
	public boolean isVisible(){
		return visible.getValue();
	}
	
	/**
	 * Sets if this panel should be rendered or not.
	 * @param visible True if this panel should be rendered.
	 */
	public void setVisible(boolean visible){
		this.visible.update(visible);
	}
	
	/**
	 * Creates a new key panel based on this configuration.
	 * @param data The key input tracking object.
	 * @return A newly created key panel.
	 */
	public KeyPanel createPanel(Key data){
		return new KeyPanel(data, this);
	}
	
	@Override
	public boolean parse(Map<String, String> data){
		return super.parse(data) | findAndParse(data, visible, keycode);
	}
	
	@Override
	public void writeItems(IndentWriter out){
		keycode.write(out);
		super.writeItems(out);
		visible.write(out);
	}
	
	@Override
	public void showEditor(boolean live){
		Editor.showEditor(new KeyPanelEditor(this, live));
	}
	
	@Override
	public boolean equals(Object obj){
		return (obj instanceof KeyPanelSettings) && ((KeyPanelSettings)obj).keycode.getValue().equals(keycode.getValue());
	}
	
	@Override
	public int hashCode(){
		return Objects.hashCode(keycode.getValue());
	}
	
	/**
	 * Determines the default key panel name based on the given extended key code.
	 * @param extendedCode The extended key code to compute a name for.
	 * @return The display name for the given extended key code.
	 */
	private static final String getPanelName(int extendedCode){
		if(CommandKeys.isMouseButton(extendedCode)){
			return "M" + (-extendedCode);
		}else{
			String name = "";
			
			if(CommandKeys.hasCtrl(extendedCode)){
				name += "c";
			}
			
			if(CommandKeys.hasAlt(extendedCode)){
				name += "a";
			}

			if(CommandKeys.hasShift(extendedCode)){
				name += "s";
			}
			
			String text = CommandKeys.getKeyText(extendedCode & CommandKeys.KEYCODE_MASK);
			return name + (text.length() == 1 ? text.toUpperCase(Locale.ROOT) : text);
		}
	}
}
