package me.roan.kps.ui.model;

import javax.swing.AbstractSpinnerModel;

public class SpecialNumberModel extends AbstractSpinnerModel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 4181535442197142288L;
	
	private int value;
	private final String special;
	private ValueChangeListener listener;
	
	public SpecialNumberModel(int value, String special, ValueChangeListener listener){
		this.value = value;
		this.special = special;
		this.listener = listener;
	}

	@Override
	public void setValue(Object value) {
		this.value = value instanceof String ? -1 : (int)value;
		listener.valueChanged(this.value);
		fireStateChanged();
	}

	@Override
	public Object getNextValue() {
		System.out.println("next called: " + (value + 1));
		return value + 1;
	}

	@Override
	public Object getPreviousValue() {
		return value <= 0 ? special : (value - 1);
	}

	@Override
	public Object getValue() {
		return value == -1 ? special : value;
	}
	
	@FunctionalInterface
	public static abstract interface ValueChangeListener{
		
		public abstract void valueChanged(int newValue);
	}
}
