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

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import dev.roanh.kps.event.listener.KeyPressListener;
import dev.roanh.kps.event.listener.KeyReleaseListener;

/**
 * This class is used to manage the command keys for
 * the program and anything else that requires modifiers.
 * @author Roan
 */
public class CommandKeys implements KeyPressListener, KeyReleaseListener{
	/**
	 * Whether or not ctrl is down
	 */
	public static boolean isCtrlDown = false;
	/**
	 * Whether or not alt is down
	 */
	public static boolean isAltDown = false;
	/**
	 * Whether or not shift is down
	 */
	public static boolean isShiftDown = false;
	/**
	 * Key code block mask
	 */
	public static final int KEYCODE_MASK = 0xFFFF;
	/**
	 * Requires ctrl mask
	 */
	public static final int CTRL_MASK = 0b0001_00000000_00000000;
	/**
	 * Requires alt mask
	 */
	public static final int ALT_MASK = 0b0010_00000000_00000000;
	/**
	 * Requires shift mask
	 */
	public static final int SHIFT_MASK = 0b0100_00000000_00000000;
	/**
	 * Right modifier variant mask (eg. Right shift)
	 */
	public static final int RIGHT_MASK = 0b1000_00000000_00000000;
	/**
	 * Mouse button mask
	 */
	public static final int MOUSE_MASK = 0x80000000;
	/**
	 * New format mask
	 */
	public static final int FORMAT_MASK = 0b1_0000_00000000_00000000;
	/**
	 * Right shift virtual key code
	 */
	public static final int VC_RSHIFT = 0x0E36;
	/**
	 * Right shift key code
	 */
	public static final int RSHIFT = VC_RSHIFT | FORMAT_MASK | RIGHT_MASK;
	/**
	 * Left shift key code
	 */
	public static final int LSHIFT = NativeKeyEvent.VC_SHIFT | FORMAT_MASK;
	/**
	 * Ctrl key code
	 */
	public static final int CTRL = NativeKeyEvent.VC_CONTROL | FORMAT_MASK;
	/**
	 * Alt key code
	 */
	public static final int ALT = NativeKeyEvent.VC_ALT | FORMAT_MASK;
	
	/**
	 * Gets the extended key code for the given
	 * key code assuming that shift, ctrl and
	 * alt are only involved if they are 
	 * currently pressed
	 * @param code The key code to get the extended key code for
	 * @return The extended key code for this code
	 */
	public static final int getExtendedKeyCode(int code){
		return getExtendedKeyCode(code, isShiftDown, isCtrlDown, isAltDown);
	}
	
	/**
	 * Gets the extended key code for this event, this key code
	 * includes modifiers
	 * @param code The original key code
	 * @param shift If shift is involved
	 * @param ctrl If ctrl is involved
	 * @param alt If alt is involved
	 * @return The extended key code for this event
	 */
	public static final int getExtendedKeyCode(int code, boolean shift, boolean ctrl, boolean alt){
		if(code == NativeKeyEvent.VC_SHIFT){
			return LSHIFT;
		}else if(code == VC_RSHIFT){
			return RSHIFT;
		}else if(code == NativeKeyEvent.VC_CONTROL){
			return CTRL;
		}else if(code == NativeKeyEvent.VC_ALT){
			return ALT;
		}else{
			return code | (shift ? SHIFT_MASK : 0) | (ctrl ? CTRL_MASK : 0) | (alt ? ALT_MASK : 0) | FORMAT_MASK;
		}
	}
	
	/**
	 * Tests if the given key code is the 
	 * keycode of either the right or left
	 * shift key
	 * @param code The keycode to test
	 * @return True if this keycode belong
	 *         to a shift key
	 */
	public static boolean isShift(int code){
		return code == LSHIFT || code == RSHIFT;
	}
	
	/**
	 * Tests if the given key code
	 * requires ctrl as a modifier
	 * @param code The key code to test
	 * @return True if the given key code
	 *         requires ctrl to be activated
	 */
	public static boolean hasCtrl(int code){
		return (code & CTRL_MASK) != 0;
	}
	
	/**
	 * Tests if the given key code
	 * requires alt as a modifier
	 * @param code The key code to test
	 * @return True if the given key code
	 *         requires alt to be activated
	 */
	public static boolean hasAlt(int code){
		return (code & ALT_MASK) != 0;
	}
	
	/**
	 * Tests if the given key code
	 * requires shift as a modifier
	 * @param code The key code to test
	 * @return True if the given key code
	 *         requires shift to be activated
	 */
	public static boolean hasShift(int code){
		return (code & SHIFT_MASK) != 0;
	}
	
	/**
	 * Checks if the given code represents
	 * a mouse button
	 * @param code The code to check
	 * @return True if the given code
	 *         represents a mouse button
	 */
	public static boolean isMouseButton(int code){
		return (code & MOUSE_MASK) != 0;
	}
	
	/**
	 * Gets the base key code for the extended key code,
	 * this is the key code without modifiers
	 * @param code The extended key code
	 * @return The base key code
	 */
	public static final int getBaseKeyCode(int code){
		return code & CommandKeys.KEYCODE_MASK;
	}
	
	/**
	 * Gets a string with all the modifiers for the given extended key code.<br>
	 * For example: <code>Ctrl + S</code>
	 * @param code The extended keycode to format.
	 * @return The key string with modifier keys.
	 */
	public static String formatExtendedCode(int code){
		String name = KeyInformation.getKeyText(getBaseKeyCode(code));
		if(hasCtrl(code)){
			name = "Ctrl + " + name;
		}
		if(hasAlt(code)){
			name = "Alt + " + name;
		}
		if(hasShift(code)){
			name = "Shift + " + name;
		}
		return name;
	}

	@Override
	public void onKeyRelease(int code){
		if(code == NativeKeyEvent.VC_ALT){
			isAltDown = false;
		}else if(code == NativeKeyEvent.VC_CONTROL){
			isCtrlDown = false;
		}else if(code == NativeKeyEvent.VC_SHIFT || code == VC_RSHIFT){
			isShiftDown = false;
		}
	}

	@Override
	public void onKeyPress(int code){
		if(!isAltDown){
			isAltDown = code == NativeKeyEvent.VC_ALT;
		}
		if(!isCtrlDown){
			isCtrlDown = code == NativeKeyEvent.VC_CONTROL;
		}
		if(!isShiftDown){
			isShiftDown = code == NativeKeyEvent.VC_SHIFT || code == VC_RSHIFT;
		}
	}
}
