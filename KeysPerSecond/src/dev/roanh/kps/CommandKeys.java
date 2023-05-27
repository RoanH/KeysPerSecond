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

/**
 * This class is used to configure
 * the command keys for the program
 * @author Roan
 */
public class CommandKeys{
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
	 * Tests if the given key code is in the 
	 * new keycode format.
	 * @param code The code to test
	 * @return Whether the given key code is in the new format
	 */
	@Deprecated
	public static boolean isNewFormat(int code){
		return isMouseButton(code) || ((code & FORMAT_MASK) != 0);
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
	
//	/**
//	 * Gets a string containing all
//	 * the modifiers for this key
//	 * @return The modifier string
//	 */
	public static String formatExtendedCode(int code){
		String name = NativeKeyEvent.getKeyText(getBaseKeyCode(code));
		if(CommandKeys.hasCtrl(code)){
			name = "Ctrl + " + name;
		}
		if(CommandKeys.hasAlt(code)){
			name = "Alt + " + name;
		}
		if(CommandKeys.hasShift(code)){
			name = "Shift + " + name;
		}
		return name;
	}

	/**
	 * Simple class to represent
	 * a command key
	 * @author Roan
	 */
	public static class CMD{
		/**
		 * Command key that never activates.
		 */
		public static final CMD NONE = new CMD(0, false, false){
			@Override
			public String toSaveString(){
				return "unbound";
			}
			
			@Override
			public String toString(){
				return "Unbound";
			}
			
			@Override
			protected boolean matches(int keycode){
				return false;
			}
		};
		/**
		 * Whether or not alt has
		 * to be pressed
		 */
		private final boolean alt;
		/**
		 * Whether or not ctrl has
		 * to be pressed
		 */
		private final boolean ctrl;
		/**
		 * Key code
		 */
		private final int keycode;

		/**
		 * Constructs a new command key
		 * @param keycode The key code
		 * @param alt Whether or not alt has to be pressed
		 * @param ctrl Whether or not ctrl has to be pressed
		 */
		public CMD(int keycode, boolean alt, boolean ctrl){
			this.alt = alt;
			this.ctrl = ctrl;
			this.keycode = keycode;
		}

		/**
		 * Check to see if the given state
		 * triggers this command key
		 * @param keycode The key that was pressed
		 * @return Whether or not this key code triggers this command key
		 */
		protected boolean matches(int keycode){
			return (this.keycode == keycode) && (this.alt == isAltDown) && (this.ctrl == isCtrlDown);
		}

		@Override
		public String toString(){
			return (ctrl ? "Ctrl + " : "") + (alt ? "Alt + " : "") + NativeKeyEvent.getKeyText(keycode);
		}

		/**
		 * @return The save form of this command key
		 */
		public String toSaveString(){
			return "[keycode=" + keycode + ",ctrl=" + ctrl + ",alt=" + alt + "]";
		}
	}
}
