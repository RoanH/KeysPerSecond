package me.roan.kps.ui.model;

import me.roan.kps.layout.LayoutValidator.FieldListener;

/**
 * SpecialNumberModel with <i>max</i>
 * as the special value
 * @author Roan
 * @see SpecialNumberModel
 */
public class MaxNumberModel extends SpecialNumberModel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -7707259211999223448L;

	/**
	 * Constructs a new MaxNumberModel with the
	 * given initial value, validator and listener
	 * @param value The initial model value
	 * @param validator The validator to use to validate value changes
	 * @param listener The listener to inform of value changes
	 */
	public MaxNumberModel(int value, FieldListener validator, ValueChangeListener listener){
		super(value, "max", validator, listener);
	}
}
