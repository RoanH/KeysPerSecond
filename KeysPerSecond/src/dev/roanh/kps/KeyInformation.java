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
package dev.roanh.kps;

import java.util.Locale;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import dev.roanh.kps.layout.Positionable;

/**
 * Simple class that holds all
 * the essential information 
 * about a key.
 * @author Roan
 */
public final class KeyInformation extends Positionable{
	/**
	 * The name of this key
	 * @see Key#name
	 */
	public String name;
	/**
	 * The virtual key code of this key<br>
	 * This code represents the key
	 */
	public int keycode;
	/**
	 * Whether or not this key is displayed
	 */
	public boolean visible = true;
	/**
	 * Auto-increment for #x
	 */
	public static volatile int autoIndex = -2;

	/**
	 * Constructs a new KeyInformation
	 * object with the given information
	 * @param name The name of the key
	 * @param code The virtual key code of the key
	 * @param alt Whether or not alt is down
	 * @param ctrl Whether or not ctrl is down
	 * @param shift Whether or not shift is down
	 * @param mouse Whether or not this is a mouse button
	 * @see #name
	 * @see #keycode 
	 */
	public KeyInformation(String name, int code, boolean alt, boolean ctrl, boolean shift, boolean mouse){
		super(autoIndex += 2, 0, 2, 3, RenderingMode.VERTICAL);
		this.keycode = mouse ? code : CommandKeys.getExtendedKeyCode(code, shift, ctrl, alt);
		this.name = mouse ? name : getKeyName(name, keycode);
	}

	/**
	 * Constructs a new KeyInformation
	 * object with the given information
	 * @param name The name of the key
	 * @param code The virtual key code of the key
	 * @param visible Whether or not the key is visible
	 * @param x The x position of the key panel.
	 * @param y The y position of the key panel.
	 * @param width The width of the key panel.
	 * @param height The height of the key panel.
	 * @param mode The text rendering mode of the key panel.
	 * @see #name
	 * @see #keycode
	 */
	protected KeyInformation(String name, int code, boolean visible, int x, int y, int width, int height, RenderingMode mode){
		super(x, y, width, height, mode);
		this.name = name;
		this.keycode = code;
		this.visible = visible;
	}

	/**
	 * Constructs the key name from the key
	 * and modifiers
	 * @param name The name of the key
	 * @param code The virtual key code of the key
	 * @return The full name of this given key
	 */
	protected static final String getKeyName(String name, int code){
		return ((CommandKeys.hasAlt(code) ? "a" : "") + (CommandKeys.hasCtrl(code) ? "c" : "") + (CommandKeys.hasShift(code) ? "s" : "")) + (name.length() == 1 ? name.toUpperCase(Locale.ROOT) : getKeyText(code & CommandKeys.KEYCODE_MASK));
	}
	
	/**
	 * Changes the display name of this
	 * key to the given string. If {@link Key}
	 * panels are active with the same key code
	 * as this {@link KeyInformation} object
	 * then their display name is also updated.
	 * @param name The new display name
	 */
	public void setName(String name){
		this.name = name;
		Main.keys.getOrDefault(keycode, Main.DUMMY_KEY).name = name;
	}

	/**
	 * Gets a string containing all
	 * the modifiers for this key
	 * @return The modifier string
	 */
	public String getModifierString(){
		return (CommandKeys.hasCtrl(keycode) ? "Ctrl + " : "") + (CommandKeys.hasAlt(keycode) ? "Alt + " : "") + (CommandKeys.hasShift(keycode) ? "Shift + " : "");
	}

	@Override
	public String toString(){
		return "[keycode=" + keycode + ",x=" + getX() + ",y=" + getY() + ",width=" + getWidth() + ",height=" + getHeight() + ",mode=" + getRenderingMode().name() + ",visible=" + visible + ",name=\"" + name + "\"]";
	}

	@Override
	public int hashCode(){
		return keycode;
	}

	@Override
	public boolean equals(Object other){
		return other instanceof KeyInformation && keycode == ((KeyInformation)other).keycode;
	}

	/**
	 * Gets the key name for a key code
	 * @param keyCode The key code
	 * @return The key name
	 */
	public static String getKeyText(int keyCode){
		switch(keyCode){
		case NativeKeyEvent.VC_ESCAPE:
			return "Esc";
		// Begin Function Keys
		case NativeKeyEvent.VC_F1:
			return "F1";
		case NativeKeyEvent.VC_F2:
			return "F2";
		case NativeKeyEvent.VC_F3:
			return "F3";
		case NativeKeyEvent.VC_F4:
			return "F4";
		case NativeKeyEvent.VC_F5:
			return "F5";
		case NativeKeyEvent.VC_F6:
			return "F6";
		case NativeKeyEvent.VC_F7:
			return "F7";
		case NativeKeyEvent.VC_F8:
			return "F8";
		case NativeKeyEvent.VC_F9:
			return "F9";
		case NativeKeyEvent.VC_F10:
			return "F10";
		case NativeKeyEvent.VC_F11:
			return "F11";
		case NativeKeyEvent.VC_F12:
			return "F12";
		case NativeKeyEvent.VC_F13:
			return "F13";
		case NativeKeyEvent.VC_F14:
			return "F14";
		case NativeKeyEvent.VC_F15:
			return "F15";
		case NativeKeyEvent.VC_F16:
			return "F16";
		case NativeKeyEvent.VC_F17:
			return "F17";
		case NativeKeyEvent.VC_F18:
			return "F18";
		case NativeKeyEvent.VC_F19:
			return "F19";
		case NativeKeyEvent.VC_F20:
			return "F20";
		case NativeKeyEvent.VC_F21:
			return "F21";
		case NativeKeyEvent.VC_F22:
			return "F22";
		case NativeKeyEvent.VC_F23:
			return "F23";
		case NativeKeyEvent.VC_F24:
			return "F24";
		// Begin Alphanumeric Zone
		case NativeKeyEvent.VC_BACKQUOTE:
			return "'";
		case NativeKeyEvent.VC_MINUS:
			return "-";
		case NativeKeyEvent.VC_EQUALS:
			return "=";
		case NativeKeyEvent.VC_BACKSPACE:
			return "\u2190";
		case NativeKeyEvent.VC_TAB:
			return "Tab";
		case NativeKeyEvent.VC_CAPS_LOCK:
			return "Cap";
		case NativeKeyEvent.VC_OPEN_BRACKET:
			return "[";
		case NativeKeyEvent.VC_CLOSE_BRACKET:
			return "]";
		case NativeKeyEvent.VC_BACK_SLASH:
			return "\\";
		case NativeKeyEvent.VC_SEMICOLON:
			return ";";
		case NativeKeyEvent.VC_QUOTE:
			return "\"";
		case NativeKeyEvent.VC_ENTER:
			return "\u21B5";
		case NativeKeyEvent.VC_COMMA:
			return ",";
		case NativeKeyEvent.VC_PERIOD:
			return ".";
		case NativeKeyEvent.VC_SLASH:
			return "/";
		case NativeKeyEvent.VC_SPACE:
			return " ";
		// Begin Edit Key Zone
		case NativeKeyEvent.VC_INSERT:
			return "Ins";
		case NativeKeyEvent.VC_DELETE:
			return "Del";
		case NativeKeyEvent.VC_HOME:
			return "\u2302";
		case NativeKeyEvent.VC_END:
			return "End";
		case NativeKeyEvent.VC_PAGE_UP:
			return "\u2191";
		case NativeKeyEvent.VC_PAGE_DOWN:
			return "\u2193";
		// Begin Cursor Key Zone
		case NativeKeyEvent.VC_UP:
			return "\u25B4";
		case NativeKeyEvent.VC_LEFT:
			return "\u25C2";
		case NativeKeyEvent.VC_CLEAR:
			return "Clr";
		case NativeKeyEvent.VC_RIGHT:
			return "\u25B8";
		case NativeKeyEvent.VC_DOWN:
			return "\u25BE";
		// Begin Modifier and Control Keys
		case NativeKeyEvent.VC_SHIFT:
		case CommandKeys.VC_RSHIFT:
			return "\u21D1";
		case NativeKeyEvent.VC_CONTROL:
			return "Ctl";
		case NativeKeyEvent.VC_ALT:
			return "Alt";
		case NativeKeyEvent.VC_META:
			return "\u2318";
		default:
			return NativeKeyEvent.getKeyText(keyCode);
		}
	}

	@Override
	public String getName(){
		return name;
	}
}
