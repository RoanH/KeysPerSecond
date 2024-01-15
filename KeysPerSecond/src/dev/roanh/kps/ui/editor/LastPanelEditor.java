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
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import dev.roanh.kps.Main;
import dev.roanh.kps.config.group.LastPanelSettings;

/**
 * Editor for the last panel settings.
 * @author Roan
 * @see LastPanelSettings
 */
public class LastPanelEditor extends DataPanelEditor{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -6191919120127582754L;

	/**
	 * Constructs a new last panel settings editor.
	 * @param config The settings to update.
	 * @param live If updates should be reflected in real time.
	 */
	public LastPanelEditor(LastPanelSettings config, boolean live){
		super(config, live);
		
		labels.add(new JLabel("Time units: "));
		JSpinner units = new JSpinner(new SpinnerNumberModel(config.getUnitCount(), 1, 4, 1));
		fields.add(units);
		units.addChangeListener(e->{
			config.setUnitCount((int)units.getValue());
			if(live){
				Main.resetPanels();
			}
		});
		
		labels.add(new JLabel("Show milliseconds: "));
		JCheckBox millis = new JCheckBox("", config.showMillis());
		fields.add(millis);
		millis.addActionListener(e->{
			config.setShowMillis(millis.isSelected());
			if(live){
				Main.resetPanels();
			}
		});
	}
}
