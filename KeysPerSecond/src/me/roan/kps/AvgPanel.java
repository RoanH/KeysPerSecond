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
		return String.format("%." + Main.config.precision + "f", Main.getAvg());
	}
	
	@Override
	public int getIndex() {
		return Main.config.posAvg;
	}
}