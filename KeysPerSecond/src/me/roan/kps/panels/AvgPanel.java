package me.roan.kps.panels;

import me.roan.kps.Main;
import me.roan.kps.RenderingMode;

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
		return Main.config.avg_x;
	}

	@Override
	public int getLayoutY(){
		return Main.config.avg_y;
	}

	@Override
	public int getLayoutWidth(){
		return Main.config.avg_w;
	}

	@Override
	public int getLayoutHeight(){
		return Main.config.avg_h;
	}

	@Override
	protected RenderingMode getRenderingMode(){
		return Main.config.avg_mode;
	}
}