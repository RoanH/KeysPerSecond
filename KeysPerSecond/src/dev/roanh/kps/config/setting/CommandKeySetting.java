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
package dev.roanh.kps.config.setting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.roanh.kps.CommandKeys;
import dev.roanh.kps.config.IndentWriter;

/**
 * Setting for command keys.
 * @author Roan
 */
public class CommandKeySetting extends IntSetting{
	/**
	 * Regex used to parse the legacy command format used until v8.8.<br>
	 * Group 1: command key code.<br>
	 * Group 2: true/false if ctrl needs to be pressed to activate the command.<br>
	 * Group 3: true/false if alt needs to be pressed to activate the command.
	 */
	private static final Pattern LEGACY_COMMAND_REGEX = Pattern.compile("\\[keycode=(\\d+),ctrl=(true|false),alt=(true|false)]");

	/**
	 * Constructs a new command key setting.
	 * @param key The setting key.
	 * @param code The code of the key for this command (not the extended key code).
	 * @param alt If alt has to be down to trigger the command.
	 * @param ctrl If ctrl has to be down to trigger the command.
	 */
	public CommandKeySetting(String key, int code, boolean alt, boolean ctrl){
		super(key, Integer.MIN_VALUE, Integer.MAX_VALUE, CommandKeys.getExtendedKeyCode(code, false, ctrl, alt));
	}
	
	/**
	 * Constructs a new initially unbound command key setting.
	 * @param key The setting key.
	 */
	public CommandKeySetting(String key){
		super(key, Integer.MIN_VALUE, Integer.MAX_VALUE, null);
	}
	
	/**
	 * Tests if the given key activates this setting at the moment.
	 * Note that the result of this subroutine depends both on the
	 * provided key and whether ctrl and alt are currently pressed.
	 * @param code The code of the key that was pressed (not the extended key code).
	 * @return True if the command key matches at the moment.
	 */
	public boolean matches(int code){
		return value != null && value.intValue() == CommandKeys.getExtendedKeyCode(code, false, CommandKeys.isCtrlDown, CommandKeys.isAltDown);
	}
	
	/**
	 * Unbinds this command key to nothing, meaning it never triggers.
	 */
	public void unbind(){
		update(null);
	}
	
	/**
	 * Gets a display string for this command key such as
	 * 'Ctrl + S' or 'Unbound' if the key is not bound.
	 * @return A display string for the command.
	 */
	public String toDisplayString(){
		return value == null ? "Unbound" : CommandKeys.formatExtendedCode(value);
	}

	@Override
	public boolean parse(String data){
		if(data.equalsIgnoreCase("unbound")){
			update(null);
			return false;
		}else{
			Matcher m = LEGACY_COMMAND_REGEX.matcher(data);
			if(m.matches()){
				if(super.parse(m.group(1))){
					return true;
				}
				
				update(CommandKeys.getExtendedKeyCode(value, false, Boolean.parseBoolean(m.group(2)), Boolean.parseBoolean(m.group(3))));
				return false;
			}else{
				return super.parse(data);
			}
		}
	}
	
	@Override
	public void write(IndentWriter out){
		if(value == null){
			out.println(key + ": unbound");
		}else{
			super.write(out);
		}
	}
}
