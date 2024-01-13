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
package dev.roanh.kps.ui.listener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Document listener that activates on any event.
 * @author Roan
 */
@FunctionalInterface
public abstract interface FieldChangeListener extends DocumentListener{
	
	/**
	 * Called when the document is modified using an
	 * insert, remove, or change operation.
	 * @param e The document event that occurred.
	 */
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
