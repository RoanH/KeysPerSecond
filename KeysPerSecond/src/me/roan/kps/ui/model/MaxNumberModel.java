package me.roan.kps.ui.model;

public class MaxNumberModel extends SpecialNumberModel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -7707259211999223448L;

	public MaxNumberModel(int value, ValueChangeListener listener) {
		super(value, "max", listener);
	}
}
