package dev.roanh.kps.config.setting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import dev.roanh.kps.CommandKeys;
import dev.roanh.kps.config.IndentWriter;

public class CommandKeySetting extends IntSetting{
	/**
	 * Regex used to parse the legacy command format used until v8.8.
	 * Group 1: command key code.
	 * Group 2: true/false if Ctrl needs to be pressed to activate the command.
	 * Group 3: true/false if Alt needs to be pressed to activate the command.
	 */
	private static final Pattern LEGACY_COMMAND_REGEX = Pattern.compile("\\[keycode=(\\d+),ctrl=(true|false),alt=(true|false)]");

	public CommandKeySetting(String key, int code, boolean alt, boolean ctrl){
		super(key, Integer.MIN_VALUE, Integer.MAX_VALUE, CommandKeys.getExtendedKeyCode(code, false, ctrl, alt));
	}
	
	public boolean matches(int code){
		return value != null && value.intValue() == CommandKeys.getExtendedKeyCode(code, false, CommandKeys.isCtrlDown, CommandKeys.isAltDown);
	}
	
	public void unbind(){
		update(null);
	}
	
	public String toDisplayString(){
		if(value == null){
			return "Unbound";
		}else{
			return (CommandKeys.hasCtrl(value) ? "Ctrl + " : "") + (CommandKeys.hasAlt(value) ? "Alt + " : "") + NativeKeyEvent.getKeyText(value & CommandKeys.KEYCODE_MASK);
		}
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
