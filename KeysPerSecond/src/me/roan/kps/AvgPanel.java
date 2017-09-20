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
		return Main.config.precision == 0 ? String.valueOf((int)Main.avg) : String.valueOf(String.format("%1$." + Main.config.precision + "f", (double)((int)(Main.avg * Math.pow(10, Main.config.precision)) / Math.pow(10, Main.config.precision))));
	}
	
	@Override
	public int getIndex() {
		return Main.config.posAvg;
	}
}