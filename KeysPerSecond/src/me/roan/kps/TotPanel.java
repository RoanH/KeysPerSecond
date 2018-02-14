package me.roan.kps;

/**
 * Panel used to display the
 * total number of keys pressed
 * @author Roan
 */
public final class TotPanel extends BasePanel {
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 5628759695450014071L;
	/**
	 * Total number of hits
	 */
	protected static int hits;

	@Override
	protected String getTitle() {
		return "TOT";
	}

	@Override
	protected String getValue() {
		return String.valueOf(hits);
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