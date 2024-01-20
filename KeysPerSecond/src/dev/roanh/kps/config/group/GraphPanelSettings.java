package dev.roanh.kps.config.group;

import dev.roanh.kps.config.GraphType;
import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.panels.BasePanel;

public abstract class GraphPanelSettings extends PanelSettings{
	private final GraphType type;

	protected GraphPanelSettings(GraphType type, int x, int y, int width, int height, String defaultName){
		super("graphs", x, y, width, height, defaultName);
		this.type = type;
	}
	
	/**
	 * Creates a new graph panel with this configuration.
	 * @return The newly created graph panel.
	 */
	public abstract BasePanel createGraph();//TODO graph panel?

	@Override
	public void writeItems(IndentWriter out){
//		TODO out.println("type: " + type.getKey());
		super.writeItems(out);
	}
}
