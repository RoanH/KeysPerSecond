/*
 * KeysPerSecond: An open source input statistics displayer.
 * Copyright (C) 2017  Roan Hofland (roan@roanh.dev).  All rights reserved.
 * GitHub Repository: https://github.com/RoanH/KeysPerSecond
 *
 * KeysPerSecond is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KeysPerSecond is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dev.roanh.kps.ui.model;

import javax.swing.AbstractSpinnerModel;

import dev.roanh.kps.layout.LayoutValidator.FieldListener;

/**
 * Special number model for spinner that allows
 * for one custom value to be added. This custom
 * value will take the position of the value -1.
 * @author Roan
 * @see EndNumberModel
 * @see MaxNumberModel
 */
public class SpecialNumberModel extends AbstractSpinnerModel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 4181535442197142288L;
	/**
	 * The current value of this spinner
	 */
	private int value;
	/**
	 * The name of the special value
	 */
	private final String special;
	/**
	 * The listener to notify of value changes
	 */
	private ValueChangeListener listener;
	/**
	 * The validator to use to check the validity of new values
	 */
	private FieldListener validator;

	/**
	 * Constructs a new SpecialNumberModel with
	 * the given arguments.
	 * @param value The initial value for the model
	 * @param special The name of the special value
	 * @param validator The validator to use
	 * @param listener The listener to notify of value changes
	 */
	public SpecialNumberModel(int value, String special, FieldListener validator, ValueChangeListener listener){
		this.value = value;
		this.special = special;
		this.validator = validator;
		validator.setModel(this);
		this.listener = listener;
	}

	/**
	 * Checks to see if the current value
	 * is the special (-1) value
	 * @return Whether or not the special
	 *         value is currently selected
	 */
	public boolean isSpecialValueSelected(){
		return value == -1;
	}

	@Override
	public void setValue(Object value){
		this.value = ((String)value).equals(special) ? -1 : Integer.parseInt((String)value);
		listener.valueChanged(this.value);
		fireStateChanged();
	}

	@Override
	public Object getNextValue(){
		return String.valueOf(value + 1);
	}

	@Override
	public Object getPreviousValue(){
		if(value <= 0){
			if(validator.specialValid()){
				return special;
			}else{
				return String.valueOf(value);
			}
		}else{
			return String.valueOf(value - 1);
		}
	}

	@Override
	public Object getValue(){
		return value == -1 ? special : String.valueOf(value);
	}

	/**
	 * Listener that is notified of value changes
	 * for a SpecialNumberModel
	 * @author Roan
	 */
	@FunctionalInterface
	public static abstract interface ValueChangeListener{
		/**
		 * Called when a new value was requested
		 * @param newValue The requested new value
		 */
		public abstract void valueChanged(int newValue);
	}
}
