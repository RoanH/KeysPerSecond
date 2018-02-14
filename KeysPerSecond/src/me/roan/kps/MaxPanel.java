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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLayoutY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLayoutWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLayoutHeight() {
		// TODO Auto-generated method stub
		return 0;
	}
}