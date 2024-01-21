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
import dev.roanh.kps.config.group.LineGraphSettings;

/**
 * Editor for line graph settings.
 * @author Roan
 * @see LineGraphSettings
 */
public class LineGraphEditor extends Editor{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 2459364509023481281L;

	/**
	 * Constructs a new editor.
	 * @param config The configuration to update.
	 * @param live If updates should be reflected in real time.
	 */
	public LineGraphEditor(LineGraphSettings config, boolean live){
		super("Graph Settings");

		labels.add(new JLabel("Backlog (milliseconds): "));
		JSpinner backlog = new JSpinner(new SpinnerNumberModel(config.getBacklog(), 2, Integer.MAX_VALUE, 100));
		backlog.addChangeListener(e->config.setBacklog((int)backlog.getValue()));
		fields.add(backlog);
		
		labels.add(new JLabel("Maximum (Y cap): "));
		JSpinner max = new JSpinner(new SpinnerNumberModel(config.getMaxValue(), 1, Integer.MAX_VALUE, 1));
		fields.add(max);
		max.addChangeListener(e->{
			config.setMaxValue((int)max.getValue());
			Main.resetGraphs();
		});
		
		labels.add(new JLabel("Show average: "));
		JCheckBox avg = new JCheckBox("", config.isAverageVisible());
		fields.add(avg);
		avg.addActionListener(e->{
			config.setAverageVisible(avg.isSelected());
			Main.frame.repaint();
		});
	}
}
