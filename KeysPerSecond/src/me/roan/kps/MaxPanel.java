package me.roan.kps;

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
}