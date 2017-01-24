package me.roan.kps;

/**
 * Panel used to display the
 * average keys pressed per second
 * @author Roan
 */
public final class AvgPanel extends BasePanel {
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 5628759695450014071L;

	@Override
	protected String getTitle() {
		return "AVG";
	}

	@Override
	protected String getValue() {
		return String.valueOf((int)Main.avg);
	}
}