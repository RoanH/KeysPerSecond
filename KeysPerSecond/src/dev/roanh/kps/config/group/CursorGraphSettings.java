package dev.roanh.kps.config.group;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import dev.roanh.kps.config.GraphType;
import dev.roanh.kps.config.setting.StringSetting;
import dev.roanh.kps.panels.BasePanel;
import dev.roanh.kps.panels.CursorGraphPanel;

public class CursorGraphSettings extends GraphPanelSettings{
	private final StringSetting display = new StringSetting("display", GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getIDstring());

	public CursorGraphSettings(){
		super(GraphType.CURSOR, 0, -1, -1, 3, "Cursor");
	}
	
	public GraphicsDevice getDisplay(){
		for(GraphicsDevice screen : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()){
			if(screen.getIDstring().equals(display.getValue())){
				return screen;
			}
		}
		
		return null;
	}

	//TODO unit tests
	
	@Override
	public void showEditor(boolean live){
		// TODO Auto-generated method stub
		
	}

	@Override
	public BasePanel createGraph(){
		return new CursorGraphPanel(this);
	}
}
