package dev.roanh.kps.ui.model;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@FunctionalInterface
public abstract interface FieldChangeListener extends DocumentListener{
	
	public abstract void onChange(DocumentEvent e);

	@Override
	public default void insertUpdate(DocumentEvent e){
		onChange(e);
	}

	@Override
	public default void removeUpdate(DocumentEvent e){
		onChange(e);
	}

	@Override
	public default void changedUpdate(DocumentEvent e){
		onChange(e);
	}
}
