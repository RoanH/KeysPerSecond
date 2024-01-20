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
package dev.roanh.kps.ui.editor;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import dev.roanh.util.Dialog;

/**
 * Base class for setting editors.
 * @author Roan
 */
public abstract class Editor extends JPanel{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -3685233769737234444L;
	/**
	 * Panel containing setting names.
	 */
	protected JPanel labels = new JPanel(new GridLayout(0, 1, 0, 2));
	/**
	 * Panel containing setting editors.
	 */
	protected JPanel fields = new JPanel(new GridLayout(0, 1, 0, 2));

	/**
	 * Constructs a new editor with the given title.
	 * @param title The UI title to show.
	 */
	protected Editor(String title){
		super(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder(title));
		this.add(labels, BorderLayout.LINE_START);
		this.add(fields, BorderLayout.CENTER);
	}
	
	/**
	 * Shows the given editor in a dialog.
	 * @param editor The editor to show.
	 */
	public static final void showEditor(Editor editor){
		Dialog.showMessageDialog(editor);
	}
}
