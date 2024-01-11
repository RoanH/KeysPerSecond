package dev.roanh.kps.config.group;

import java.util.List;
import java.util.Map;

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.SettingGroup;
import dev.roanh.kps.config.setting.IntSetting;
import dev.roanh.kps.config.setting.ProxySetting;
import dev.roanh.kps.panels.BasePanel;

public class LayoutSettings extends SettingGroup{
	/**
	 * The offset from the border of a panel to the actual panel content.
	 */
	private final IntSetting borderOffset = new IntSetting("borderOffset", 0, Integer.MAX_VALUE, 2);
	/**
	 * The pixel size of one grid cell in the program.
	 */
	private final IntSetting cellSize = new IntSetting("clellSize", BasePanel.imageSize, Integer.MAX_VALUE, 22);
	
	public LayoutSettings(){
		super("layout");
	}
	
	public int getCellSize(){
		return cellSize.getValue();
	}
	
	public int getBorderOffset(){
		return borderOffset.getValue();
	}

	public void setBorderOffset(int offset){
		borderOffset.update(offset);
		validate();
	}
	
	public void setCellSize(int size){
		cellSize.update(size);
		validate();
	}
	
	private boolean validate(){
		if(borderOffset.getValue() > cellSize.getValue() - BasePanel.imageSize){
			borderOffset.update(cellSize.getValue() - BasePanel.imageSize);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean parse(Map<String, String> data){
		boolean defaultUsed = findAndParse(data, borderOffset, cellSize);
		return validate() || defaultUsed;
	}

	@Override
	public void writeItems(IndentWriter out){
		borderOffset.write(out);
		cellSize.write(out);
	}
	
	public void collectLegacyProxies(List<Setting<?>> proxyList){
		proxyList.add(ProxySetting.of("borderOffset", borderOffset));
		proxyList.add(ProxySetting.of("cellSize", cellSize));
	}
}
