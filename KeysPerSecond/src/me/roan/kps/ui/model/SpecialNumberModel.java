package me.roan.kps.ui.model;

import javax.swing.AbstractSpinnerModel;

import me.roan.kps.layout.LayoutValidator.FieldListener;

public class SpecialNumberModel extends AbstractSpinnerModel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 4181535442197142288L;
	
	private int value;
	private final String special;
	private ValueChangeListener listener;
	private FieldListener validator;
	
	public SpecialNumberModel(int value, String special, FieldListener validator, ValueChangeListener listener){
		this.value = value;
		this.special = special;
		this.validator = validator;
		validator.setModel(this);
		this.listener = listener;
	}
	
	public boolean isSpecialValueSelected(){
		return value == -1;
	}

	@Override
	public void setValue(Object value){
		this.value = ((String)value).equals(special) ? -1 : Integer.valueOf((String)value);
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

	@FunctionalInterface
	public static abstract interface ValueChangeListener{

		public abstract void valueChanged(int newValue);
	}
}
