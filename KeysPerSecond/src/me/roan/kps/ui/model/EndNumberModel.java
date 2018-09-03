package me.roan.kps.ui.model;

import me.roan.kps.layout.LayoutValidator.FieldListener;

public class EndNumberModel extends SpecialNumberModel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -2971851618376128616L;

	public EndNumberModel(int value, FieldListener validator, ValueChangeListener listener) {
		super(value, "end", validator, listener);
	}
}
