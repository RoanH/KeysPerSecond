package me.roan.kps;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jnativehook.keyboard.NativeKeyEvent;

import me.roan.util.Dialog;

/**
 * This class is used to configure
 * the command keys for the program
 * @author Roan
 */
public class CommandKeys{
	/**
	 * Whether or not ctrl is down
	 */
	protected static boolean isCtrlDown = false;
	/**
	 * Whether or not alt is down
	 */
	protected static boolean isAltDown = false;
	/**
	 * Whether or not shift is down
	 */
	protected static boolean isShiftDown = false;
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
	protected static final int getExtendedKeyCode(int code){
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
	protected static final int getExtendedKeyCode(int code, boolean shift, boolean ctrl, boolean alt){
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
	 * Simple class to represent
	 * a command key
	 * @author Roan
	 */
	protected static class CMD{
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
		protected CMD(int keycode, boolean alt, boolean ctrl){
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
		protected final boolean matches(int keycode){
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

	/**
	 * Prompts the user for a new command key
	 * @return The new command key or null
	 */
	protected static CMD askForNewKey(){
		JPanel form = new JPanel(new GridLayout(3, 1));
		JLabel txt = new JLabel("Press a key and click 'OK'");
		JPanel a = new JPanel(new BorderLayout());
		JPanel c = new JPanel(new BorderLayout());
		JCheckBox ctrl = new JCheckBox();
		JCheckBox alt = new JCheckBox();
		c.add(ctrl, BorderLayout.LINE_START);
		c.add(new JLabel("Ctrl"), BorderLayout.CENTER);
		a.add(alt, BorderLayout.LINE_START);
		a.add(new JLabel("Alt"), BorderLayout.CENTER);
		form.add(txt);
		form.add(c);
		form.add(a);
		if(Dialog.showOptionDialog(form)){
			if(Main.lastevent == null){
				return null;
			}
			CMD cmd = new CMD(Main.lastevent.getKeyCode(), isAltDown || alt.isSelected(), isCtrlDown || ctrl.isSelected());
			if(Dialog.showConfirmDialog("Set command key to: " + cmd.toString())){
				return cmd;
			}
		}
		return null;
	}
}
