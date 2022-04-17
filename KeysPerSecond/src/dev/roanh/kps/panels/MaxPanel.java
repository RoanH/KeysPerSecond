package dev.roanh.kps.panels;

import dev.roanh.kps.Main;
import dev.roanh.kps.RenderingMode;

/**
 * Panel used to display the
 * maximum keys pressed per second
 * @author Roan
 */
public final class MaxPanel extends BasePanel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 8816524158873355997L;
	/**
	 * Static instance of this panel that is reused all the time
	 */
	public static final MaxPanel INSTANCE = new MaxPanel();

	/**
	 * Constructs a new maximum panel
	 */
	private MaxPanel(){
		sizeChanged();
	}

	@Override
	protected String getTitle(){
		return "MAX";
	}

	@Override
	protected String getValue(){
		return String.valueOf(Main.max);
	}

	@Override
	public int getLayoutX(){
		return Main.config.maxPanel.getX();
	}

	@Override
	public int getLayoutY(){
		return Main.config.maxPanel.getY();
	}

	@Override
	public int getLayoutWidth(){
		return Main.config.maxPanel.getWidth();
	}

	@Override
	public int getLayoutHeight(){
		return Main.config.maxPanel.getHeight();
	}

	@Override
	protected RenderingMode getRenderingMode(){
		return Main.config.maxPanel.getRenderingMode();
	}
}