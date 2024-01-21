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

import java.awt.GraphicsDevice;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import dev.roanh.kps.Main;
import dev.roanh.kps.config.group.CursorGraphSettings;

/**
 * Editor for cursor graph settings.
 * @author Roan
 * @see CursorGraphSettings
 */
public class CursorGraphEditor extends Editor{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 2126285185766458287L;

	/**
	 * Constructs a new editor.
	 * @param config The configuration to update.
	 * @param live If updates should be reflected in real time.
	 */
	public CursorGraphEditor(CursorGraphSettings config, boolean live){
		super("Graph Settings");
		
		labels.add(new JLabel("Display: "));
		GraphicsDevice[] screens = CursorGraphSettings.getScreens();
		String[] screenNames = new String[screens.length];
		int selectedIdx = 0;
		for(int i = 0; i < screens.length; i++){
			GraphicsDevice screen = screens[i];
			screenNames[i] = screen.getIDstring() + " (" + screen.getDisplayMode().getWidth() + "x" + screen.getDisplayMode().getHeight() + ")";
			if(screen.getIDstring().equals(config.getDisplayId())){
				selectedIdx = i;
			}
		}
		
		JComboBox<String> screenSelector = new JComboBox<String>(screenNames);
		screenSelector.setSelectedIndex(selectedIdx);
		fields.add(screenSelector);
		screenSelector.addActionListener(e->{
			config.setDisplay(screens[screenSelector.getSelectedIndex()].getIDstring());
			if(live){
				Main.reconfigure();
			}
		});
		
		labels.add(new JLabel("Backlog (milliseconds): "));
		JSpinner backlog = new JSpinner(new SpinnerNumberModel(config.getBacklog(), 0, Integer.MAX_VALUE, 100));
		backlog.addChangeListener(e->config.setBacklog((int)backlog.getValue()));
		fields.add(backlog);
	}
}
