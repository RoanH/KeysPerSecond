package me.roan.kps.ui.model;

import me.roan.kps.layout.LayoutValidator.FieldListener;

/**
 * SpecialNumberModel with <i>end</i>
 * as the special value
 * @author Roan
 * @see SpecialNumberModel
 */
public class EndNumberModel extends SpecialNumberModel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -2971851618376128616L;

	/**
	 * Constructs a new EndNumberModel with the
	 * given initial value, validator and listener
	 * @param value The initial model value
	 * @param validator The validator to use to validate value changes
	 * @param listener The listener to inform of value changes
	 */
	public EndNumberModel(int value, FieldListener validator, ValueChangeListener listener){
		super(value, "end", validator, listener);
	}
}
