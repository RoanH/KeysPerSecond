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

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import dev.roanh.kps.Main;
import dev.roanh.kps.config.group.KeyPanelSettings;

/**
 * Editor for key panel settings.
 * @author Roan
 * @see KeyPanelSettings
 */
public class KeyPanelEditor extends DataPanelEditor{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 8336531868831372183L;

	/**
	 * Constructs a new editor.
	 * @param config The configuration to update.
	 * @param live If updates should be reflected in real time.
	 */
	public KeyPanelEditor(KeyPanelSettings config, boolean live){
		super(config, live);
		
		labels.add(new JLabel("Visible: "));
		JCheckBox visible = new JCheckBox("(invisible keys only track input)", config.isVisible());
		fields.add(visible);
		visible.addActionListener(e->{
			config.setVisible(visible.isSelected());
			Main.reconfigure();
		});
	}
}
