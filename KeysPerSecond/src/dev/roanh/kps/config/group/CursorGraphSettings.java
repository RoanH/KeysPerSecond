package dev.roanh.kps.config.group;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Map;

import dev.roanh.kps.config.GraphType;
import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.setting.IntSetting;
import dev.roanh.kps.config.setting.StringSetting;
import dev.roanh.kps.panels.BasePanel;
import dev.roanh.kps.panels.CursorGraphPanel;
import dev.roanh.kps.ui.editor.CursorGraphEditor;
import dev.roanh.kps.ui.editor.Editor;

public class CursorGraphSettings extends GraphPanelSettings{
	private final StringSetting display = new StringSetting("display", getScreens()[0].getIDstring());
	private final IntSetting backlog = new IntSetting("backlog", 0, Short.MAX_VALUE, 1000);

	public CursorGraphSettings(){
		super(GraphType.CURSOR, 0, -1, -1, 6, "Cursor");
	}
	
	public GraphicsDevice getDisplay(){
		for(GraphicsDevice screen : getScreens()){
			if(screen.getIDstring().equals(getDisplayId())){
				return screen;
			}
		}
		
		return null;
	}
	
	public String getDisplayId(){
		return display.getValue();
	}
	
	public int getBacklog(){
		return backlog.getValue();
	}
	
	public void setBacklog(int backlog){
		this.backlog.update(backlog);
	}
	
	public void setDisplay(String idString){
		display.update(idString);
	}

	//TODO unit tests
	
	@Override
	public void showEditor(boolean live){
		Editor.showEditor(new CursorGraphEditor(this, live));
	}

	@Override
	public BasePanel createGraph(){
		return new CursorGraphPanel(this);
	}
	
	@Override
	public boolean parse(Map<String, String> data){
		return super.parse(data) | findAndParse(data, display, backlog);
	}
	
	@Override
	public void writeItems(IndentWriter out){
		super.writeItems(out);
		display.write(out);
		backlog.write(out);
	}
	
	public static final GraphicsDevice[] getScreens(){
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
	}
}
