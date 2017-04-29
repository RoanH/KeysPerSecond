package me.roan.kps;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jnativehook.keyboard.NativeKeyEvent;

public class CommandKeys {
	
	protected static CMD CP = new CMD(NativeKeyEvent.VC_P, false, true);
	protected static CMD CI = new CMD(NativeKeyEvent.VC_I, false, true);
	protected static CMD CU = new CMD(NativeKeyEvent.VC_U, false, true);
	protected static CMD CY = new CMD(NativeKeyEvent.VC_Y, false, true);
	protected static CMD CT = new CMD(NativeKeyEvent.VC_T, false, true);
	protected static CMD CR = new CMD(NativeKeyEvent.VC_R, false, true); 
	
	protected static class CMD{
		private final boolean alt;
		private final boolean ctrl;
		private final int keycode;

		protected CMD(int keycode, boolean alt, boolean ctrl){
			this.alt = alt;
			this.ctrl = ctrl;
			this.keycode = keycode;
		}
		
		protected final boolean matches(int keycode, boolean alt, boolean ctrl){
			if((this.keycode != keycode) || (this.alt && !alt) || (this.ctrl && !ctrl)){
				return false;
			}else{
				return true;
			}
		}
		
		@Override
		public String toString(){
			return (ctrl ? "Ctrl + " : "") + (alt ? "Alt + " : "") + NativeKeyEvent.getKeyText(keycode);
		}
	}

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
		if(JOptionPane.showOptionDialog(Main.frame.isVisible() ? Main.frame : null, form, "Keys per second", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"OK", "Cancel"}, 0) == 0){
			if(Main.lastevent == null){
				return null;
			}
			CMD cmd = new CMD(Main.lastevent.getKeyCode(), alt.isSelected(), ctrl.isSelected());
			if(JOptionPane.showOptionDialog(Main.frame.isVisible() ? Main.frame : null, "Set command key to: " + cmd.toString(), "Keys per second", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"OK", "Cancel"}, 0) == 0){
				return cmd;
			}
		}
		return null;
	}
}
