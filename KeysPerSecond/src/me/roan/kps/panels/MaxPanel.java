package me.roan.kps.panels;

import me.roan.kps.Main;
import me.roan.kps.RenderingMode;

/**
 * Panel used to display the
 * maximum keys pressed per second
 * @author Roan
 */
public final class MaxPanel extends BasePanel {
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 8816524158873355997L;
	
	public MaxPanel(){
		sizeChanged();
	}

	@Override
	protected String getTitle() {
		return "MAX";
	}

	@Override
	protected String getValue() {
		return String.valueOf(Main.max);
	}

	@Override
	public int getLayoutX() {
		return Main.config.max_x;
	}

	@Override
	public int getLayoutY() {
		return Main.config.max_y;
	}

	@Override
	public int getLayoutWidth() {
		return Main.config.max_w;
	}

	@Override
	public int getLayoutHeight() {
		return Main.config.max_h;
	}

	@Override
	protected RenderingMode getRenderingMode() {
		return Main.config.max_mode;
	}
}