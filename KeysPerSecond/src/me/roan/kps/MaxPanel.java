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
	
	/**
	 * Constructs a new MaxPanel
	 */
	protected MaxPanel(){//TODO remove if unneeded
		super();
	}

	@Override
	protected String getTitle() {
		return "MAX";
	}

	@Override
	protected String getValue() {
		return String.valueOf(Main.max);
	}
}