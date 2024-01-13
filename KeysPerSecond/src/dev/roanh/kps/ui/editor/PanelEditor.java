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

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import dev.roanh.kps.Main;
import dev.roanh.kps.RenderingMode;
import dev.roanh.kps.config.group.PanelSettings;
import dev.roanh.kps.ui.listener.FieldChangeListener;

public class PanelEditor extends Editor{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -2982608015931683558L;

	public PanelEditor(PanelSettings config, boolean live){
		super("Panel Specific Settings");

		labels.add(new JLabel("Name: "));
		JTextField name = new JTextField(config.getName());
		fields.add(name);
		name.getDocument().addDocumentListener((FieldChangeListener)e->{
			config.setName(name.getText());
			if(live){
				Main.resetPanels();
			}
		});
		
		labels.add(new JLabel("Mode: "));
		JComboBox<RenderingMode> mode = new JComboBox<RenderingMode>(RenderingMode.values());
		fields.add(mode);
		mode.setSelectedItem(config.getRenderingMode());
		mode.addActionListener((e)->{
			config.setRenderingMode((RenderingMode)mode.getSelectedItem());
			if(live){
				Main.resetPanels();
			}
		});
	}
}
