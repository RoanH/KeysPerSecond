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
package dev.roanh.kps.ui.dialog;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dev.roanh.kps.Main;
import dev.roanh.kps.RenderingMode;
import dev.roanh.kps.config.group.PanelSettings;
import dev.roanh.kps.ui.model.FieldChangeListener;
import dev.roanh.util.Dialog;

public class PanelEditor extends JPanel{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -2982608015931683558L;
	protected JPanel labels = new JPanel(new GridLayout(0, 1, 0, 2));
	protected JPanel fields = new JPanel(new GridLayout(0, 1, 0, 2));

	public PanelEditor(PanelSettings config, boolean live){
		super(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder("Panel Specific Settings"));
		this.add(labels, BorderLayout.LINE_START);
		this.add(fields, BorderLayout.CENTER);
		
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
	
	public static final void showEditor(PanelSettings config, boolean live){
		showEditor(new PanelEditor(config, live));
	}
	
	public static final void showEditor(PanelEditor editor){
		Dialog.showMessageDialog(editor);
	}
}
