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

import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTextField;

/**
 * Simple editor that allows edits in the default editor field.
 * @author Roan
 */
public class SpecialNumberModelEditor extends DefaultEditor{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 7262846110148041166L;

	/**
	 * Constructs a new special number model editor for the given spinner.
	 * @param spinner The spinner to create an editor for.
	 */
	public SpecialNumberModelEditor(JSpinner spinner){
		super(spinner);
		
		JFormattedTextField disp = getTextField();
		disp.setEditable(true);
		disp.setHorizontalAlignment(JTextField.RIGHT);
	}
}
