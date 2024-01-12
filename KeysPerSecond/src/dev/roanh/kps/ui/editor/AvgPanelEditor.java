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

import dev.roanh.kps.Main;
import dev.roanh.kps.config.group.AveragePanelSettings;

public class AvgPanelEditor extends PanelEditor{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -6191919120127582754L;

	public AvgPanelEditor(AveragePanelSettings config, boolean live){
		super(config, live);
		
		labels.add(new JLabel("Precision: "));
		JComboBox<String> values = new JComboBox<String>(new String[]{"No digits beyond the decimal point", "1 digit beyond the decimal point", "2 digits beyond the decimal point", "3 digits beyond the decimal point"});
		fields.add(values);
		values.setSelectedIndex(config.getPrecision());
		values.addActionListener(e->{
			config.setPrecision(values.getSelectedIndex());
			if(live){
				Main.frame.repaint();
			}
		});
	}
}
