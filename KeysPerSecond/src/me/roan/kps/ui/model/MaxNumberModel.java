package me.roan.kps.ui.model;

import me.roan.kps.layout.LayoutValidator.FieldListener;

public class MaxNumberModel extends SpecialNumberModel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -7707259211999223448L;

	public MaxNumberModel(int value, FieldListener validator, ValueChangeListener listener) {
		super(value, "max", validator, listener);
	}
}
