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
package dev.roanh.kps.ui.component;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.function.IntConsumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;

import dev.roanh.kps.Main;
import dev.roanh.kps.config.SettingList;
import dev.roanh.kps.config.group.GraphSettings;
import dev.roanh.kps.config.group.KeyPanelSettings;
import dev.roanh.kps.config.group.LocationSettings;
import dev.roanh.kps.config.group.PanelSettings;
import dev.roanh.kps.layout.LayoutValidator;
import dev.roanh.kps.ui.model.EndNumberModel;
import dev.roanh.kps.ui.model.MaxNumberModel;
import dev.roanh.kps.ui.model.SpecialNumberModel.ValueChangeListener;
import dev.roanh.kps.ui.model.SpecialNumberModelEditor;

/**
 * Table like view for displaying panels.
 * @author Roan
 * @see PanelSettings
 * @see KeyPanelSettings
 * @see GraphSettings
 */
public class TablePanel extends JPanel{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 4467273936432261623L;
	/**
	 * The JPanel holding all the table rows.
	 */
	private JPanel rows = new JPanel(new GridLayout(0, 1, 0, 2));
	/**
	 * True if the GUI is already live and updates should be reflected in real time.
	 */
	private boolean live;
	/**
	 * True if the table contains editors for the panel location and size.
	 * @see LocationSettings
	 */
	private boolean location;
	
	/**
	 * Creates a new table panel with the given settings.
	 * @param name The header name for the first column.
	 * @param location True if location editors should be added.
	 * @param live True if the GUI is live and should be updated in real time.
	 */
	public TablePanel(String name, boolean location, boolean live){
		super(new BorderLayout());
		this.live = live;
		this.location = location;
		
		addHeaders(name);

		setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		add(rows, BorderLayout.PAGE_START);
		add(new JPanel(), BorderLayout.CENTER);
	}
	
	/**
	 * Adds the table headers.
	 * @param name The name for the first column.
	 */
	private void addHeaders(String name){
		JPanel row = new JPanel(new GridLayout(0, location ? 7 : 3, 2, 0));
		row.add(new JLabel(name, SwingConstants.CENTER));
		
		if(location){
			row.add(new JLabel("X", SwingConstants.CENTER));
			row.add(new JLabel("Y", SwingConstants.CENTER));
			row.add(new JLabel("Width", SwingConstants.CENTER));
			row.add(new JLabel("Height", SwingConstants.CENTER));
		}
		
		row.add(new JLabel("Settings", SwingConstants.CENTER));
		row.add(new JLabel("Delete", SwingConstants.CENTER));
		rows.add(row);
	}
	
	/**
	 * Checks if this table is operating in live mode where updates are reflected in real time.
	 * @return True if this table is in live mode.
	 */
	public boolean isLive(){
		return live;
	}
	
	/**
	 * Adds a list of graph rows to this table.
	 * @param graphs The graph settings to create rows for.
	 */
	public void addGraphs(SettingList<GraphSettings> graphs){
		for(GraphSettings graph : graphs){
			addPanelRow(graphs, graph);
		}
	}
	
	/**
	 * Adds a list of panel rows to this table.
	 * @param panels The panel settings to create rows for.
	 */
	public void addPanels(SettingList<? extends PanelSettings> panels){
		for(PanelSettings panel : panels){
			addPanelRow(panels, panel);
		}
	}
	
	/**
	 * Add a row to the table to configure the given settings.
	 * @param panels The list to update when the row is removed.
	 * @param info The settings to configure with the row.
	 */
	public void addPanelRow(SettingList<? extends PanelSettings> panels, PanelSettings info){
		JPanel row = new JPanel(new GridLayout(0, location ? 7 : 3, 2, 0));
		
		//name
		JLabel nameLabel = new JLabel(info.getName(), SwingConstants.CENTER);
		row.add(nameLabel);

		//location
		if(location){
			LayoutValidator validator = new LayoutValidator();
			validator.getXField().setModel(new EndNumberModel(info.getLayoutX(), validator.getXField(), update(info::setX)));
			validator.getYField().setModel(new EndNumberModel(info.getLayoutY(), validator.getYField(), update(info::setY)));
			validator.getWidthField().setModel(new MaxNumberModel(info.getLayoutWidth(), validator.getWidthField(), update(info::setWidth)));
			validator.getHeightField().setModel(new MaxNumberModel(info.getLayoutHeight(), validator.getHeightField(), update(info::setHeight)));

			JSpinner x = new JSpinner(validator.getXField().getModel());
			x.setEditor(new SpecialNumberModelEditor(x));
			row.add(x);

			JSpinner y = new JSpinner(validator.getYField().getModel());
			y.setEditor(new SpecialNumberModelEditor(y));
			row.add(y);

			JSpinner w = new JSpinner(validator.getWidthField().getModel());
			w.setEditor(new SpecialNumberModelEditor(w));
			row.add(w);

			JSpinner h = new JSpinner(validator.getHeightField().getModel());
			h.setEditor(new SpecialNumberModelEditor(h));
			row.add(h);
		}

		//edit
		JButton edit = new JButton("Edit");
		row.add(edit);
		edit.addActionListener(e->{
			info.showEditor(live);
			nameLabel.setText(info.getName());
		});
		
		//delete
		JButton delete = new JButton("Remove");
		row.add(delete);
		delete.addActionListener(e->{
			panels.remove(info);
			rows.remove(row);
			revalidate();
			if(live){
				if(info instanceof KeyPanelSettings){
					Main.removeKey(((KeyPanelSettings)info).getKeyCode());
				}
				
				Main.reconfigure();
			}
		});
		
		rows.add(row);
	}
	
	/**
	 * Construct a value change listener that sets new values
	 * to the given field and optionally updates the main GUI.
	 * @param field The field to update with new values.
	 * @return The newly constructed change listener.
	 */
	private ValueChangeListener update(IntConsumer field){
		return val->{
			field.accept(val);
			if(live){
				Main.reconfigure();
			}
		};
	}
}
