package dev.roanh.kps.panels;

import dev.roanh.kps.Main;
import dev.roanh.kps.RenderingMode;

/**
 * Panel used to display the
 * average keys pressed per second
 * @author Roan
 */
public final class AvgPanel extends BasePanel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 5628759695450014071L;
	/**
	 * Static instance of this panel that is reused all the time
	 */
	public static final AvgPanel INSTANCE = new AvgPanel();

	/**
	 * Constructs a new average panel
	 */
	private AvgPanel(){
		sizeChanged();
	}

	@Override
	protected String getTitle(){
		return "AVG";
	}

	@Override
	protected String getValue(){
		return String.format("%1$." + Main.config.precision + "f", Main.avg);
	}

	@Override
	public int getLayoutX(){
		return Main.config.avgPanel.getX();
	}

	@Override
	public int getLayoutY(){
		return Main.config.avgPanel.getY();
	}

	@Override
	public int getLayoutWidth(){
		return Main.config.avgPanel.getWidth();
	}

	@Override
	public int getLayoutHeight(){
		return Main.config.avgPanel.getHeight();
	}

	@Override
	protected RenderingMode getRenderingMode(){
		return Main.config.avgPanel.getRenderingMode();
	}
}