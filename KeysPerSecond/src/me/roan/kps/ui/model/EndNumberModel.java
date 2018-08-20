package me.roan.kps.ui.model;

public class EndNumberModel extends SpecialNumberModel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -2971851618376128616L;

	public EndNumberModel(int value, ValueChangeListener listener) {
		super(value, "end", listener);
	}
}
