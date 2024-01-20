package dev.roanh.kps.config.group;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import dev.roanh.kps.config.setting.StringSetting;

public class CursorGraphSettings extends PanelSettings{
	private final StringSetting display = new StringSetting("display", GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getIDstring());

	public CursorGraphSettings(){
		super("graphs", 0, -1, -1, 3, "Cursor");
	}
	
	public GraphicsDevice getDisplay(){
		for(GraphicsDevice screen : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()){
			if(screen.getIDstring().equals(display.getValue())){
				return screen;
			}
		}
		
		return null;
	}

	@Override
	public void showEditor(boolean live){
		// TODO Auto-generated method stub
		
	}
}
