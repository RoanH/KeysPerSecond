package me.roan.kps;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;

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
	public static final int KEYCODE_MASK = 0xFFFF;
	public static final int CTRL_MASK = 0b0001_00000000_00000000;
	public static final int ALT_MASK = 0b0010_00000000_00000000;
	public static final int SHIFT_MASK = 0b0100_00000000_00000000;
	public static final int RIGHT_MASK = 0b1000_00000000_00000000;
	public static final int MOUSE_MASK = 0x80000000;
	public static final int FORMAT_MASK = 0b1_0000_00000000_00000000;
	public static final int VC_RSHIFT = 0x0E36;
	public static final int RSHIFT = VC_RSHIFT | FORMAT_MASK | RIGHT_MASK;
	public static final int LSHIFT = NativeKeyEvent.VC_SHIFT | FORMAT_MASK;
	public static final int RCTRL = NativeKeyEvent.VC_CONTROL | FORMAT_MASK;
	public static final int LCTRL = NativeKeyEvent.VC_CONTROL | FORMAT_MASK;
	public static final int RALT = NativeKeyEvent.VC_ALT | FORMAT_MASK;
	public static final int LALT = NativeKeyEvent.VC_ALT | FORMAT_MASK;
	
	protected static final int getExtendedKeyCode(int code, int modifiers){
		return getExtendedKeyCode(code, modifiers, isShiftDown, isCtrlDown, isAltDown);
	}
	
	/**
	 * Gets the extended key code for this event, this key code
	 * includes modifiers
	 * @param code The original key code
	 * @param
	 * @param
	 * @param
	 * @param modifers The involved modifiers
	 * @return The extended key code for this event
	 */
	protected static final int getExtendedKeyCode(int code, int modifiers, boolean shift, boolean ctrl, boolean alt){
		if(code == NativeKeyEvent.VC_SHIFT){
			return code | FORMAT_MASK;
		}else if(code == VC_RSHIFT){
			return code | FORMAT_MASK | RIGHT_MASK;
		}else if(code == NativeKeyEvent.VC_CONTROL){
			if((modifiers & NativeInputEvent.CTRL_R_MASK) != 0){
				return code | FORMAT_MASK | RIGHT_MASK;
			}else{
				return code | FORMAT_MASK;
			}
		}else if(code == NativeKeyEvent.VC_ALT){
			if((modifiers & NativeInputEvent.ALT_R_MASK) != 0){
				return code | FORMAT_MASK | RIGHT_MASK;
			}else{
				return code | FORMAT_MASK;
			}
		}else{
			return code | (shift ? SHIFT_MASK : 0) | (ctrl ? CTRL_MASK : 0) | (alt ? ALT_MASK : 0) | FORMAT_MASK;
		}
	}
	
	@Deprecated
	public static boolean isModifier(int code){
		return isCtrl(code) || isAlt(code) || isShift(code);
	}
	
	public static boolean isCtrl(int code){
		return code == LCTRL || code == RCTRL;
	}
	
	public static boolean isAlt(int code){
		return code == LALT || code == RALT;
	}
	
	public static boolean isShift(int code){
		return code == LSHIFT || code == RSHIFT;
	}
	
	public static boolean hasCtrl(int code){
		return (code & CTRL_MASK) != 0;
	}
	
	public static boolean hasAlt(int code){
		return (code & ALT_MASK) != 0;
	}
	
	public static boolean hasShift(int code){
		return (code & SHIFT_MASK) != 0;
	}
	
	public static boolean isNewFormat(int code){
		return ((code & MOUSE_MASK) != 0) || ((code & FORMAT_MASK) != 0);
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
		if(Main.showOptionDialog(form)){
			if(Main.lastevent == null){
				return null;
			}
			CMD cmd = new CMD(Main.lastevent.getKeyCode(), isAltDown || alt.isSelected(), isCtrlDown || ctrl.isSelected());
			if(Main.showConfirmDialog("Set command key to: " + cmd.toString())){
				return cmd;
			}
		}
		return null;
	}
}
