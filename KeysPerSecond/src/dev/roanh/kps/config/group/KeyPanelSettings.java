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
	
	//TODO move layout logic to the keysdialog and compute there
	public KeyPanelSettings(int x, int extendedCode){
		super("keys", x, 0, 2, 3, KeyInformation.getPanelName(extendedCode));
		keycode.update(extendedCode);
		//TODO probably want to do some layout validation due to the special -1 values
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
		//TODO probably want to do some layout validation due to the special -1 values
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
