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
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.function.IntConsumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import dev.roanh.kps.Main;
import dev.roanh.kps.config.group.GraphSettings;
import dev.roanh.kps.config.group.KeyPanelSettings;
import dev.roanh.kps.config.group.LayoutSettings;
import dev.roanh.kps.config.group.LocationSettings;
import dev.roanh.kps.config.group.PanelSettings;
import dev.roanh.kps.layout.LayoutValidator;
import dev.roanh.kps.panels.BasePanel;
import dev.roanh.kps.ui.model.DynamicInteger;
import dev.roanh.kps.ui.model.EndNumberModel;
import dev.roanh.kps.ui.model.MaxNumberModel;
import dev.roanh.kps.ui.model.SpecialNumberModel.ValueChangeListener;
import dev.roanh.kps.ui.model.SpecialNumberModelEditor;
import dev.roanh.util.Dialog;

/**
 * Logic for the layout configuration dialog.
 * @author Roan
 */
public class LayoutDialog{

	/**
	 * Shows the layout configuration dialog
	 * @param live Whether or not changes should be
	 *        applied in real time
	 */
	public static final void configureLayout(boolean live){
		Main.content.showGrid();
		JPanel form = new JPanel(new BorderLayout());

		//general layout settings
		LayoutSettings layout = Main.config.getLayout();
		JPanel gridSize = new JPanel(new GridLayout(2, 2, 0, 5));
		gridSize.setBorder(BorderFactory.createTitledBorder("Size"));
		gridSize.add(new JLabel("Cell size: "));
		JSpinner gridSpinner = new JSpinner(new SpinnerNumberModel(layout.getCellSize(), BasePanel.imageSize, Integer.MAX_VALUE, 1));
		gridSize.add(gridSpinner);
		gridSize.add(new JLabel("Panel border offset: "));
		JSpinner gapSpinner = new JSpinner(new SpinnerNumberModel(layout.getBorderOffset(), 0, new DynamicInteger(()->(layout.getCellSize() - BasePanel.imageSize)), 1));
		gapSpinner.addChangeListener((e)->{
			layout.setBorderOffset((int)gapSpinner.getValue());
			if(live){
				Main.reconfigure();
			}
		});
		gridSize.add(gapSpinner);
		gridSpinner.addChangeListener((e)->{
			layout.setCellSize((int)gridSpinner.getValue());
			gapSpinner.setValue(layout.getBorderOffset());
			if(live){
				Main.reconfigure();
			}
		});
		
		form.add(gridSize, BorderLayout.PAGE_START);
		
		//panel configuration
		TablePanel panelView = new TablePanel("Panel");
		
		for(KeyPanelSettings key : Main.config.getKeySettings()){
			panelView.addPanelRow(key, live);
		}
		
		for(PanelSettings panel : Main.config.getPanels()){
			panelView.addPanelRow(panel, live);
		}
		
		JScrollPane pane = new JScrollPane(panelView);
		pane.setBorder(BorderFactory.createTitledBorder("Panels"));
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		pane.setPreferredSize(new Dimension(450, 200));

		form.add(pane, BorderLayout.CENTER);

		//graph configuration
		TablePanel graphView = new TablePanel("Graph");
		for(GraphSettings graph : Main.config.getGraphSettings()){
			graphView.addGraphRow(graph, live);
		}
		
		JScrollPane graphPane = new JScrollPane(graphView);
		graphPane.setBorder(BorderFactory.createTitledBorder("Graphs"));
		graphPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		form.add(graphPane, BorderLayout.PAGE_END);
		
		Dialog.showMessageDialog(form, true, ModalityType.APPLICATION_MODAL);
		Main.content.hideGrid();
	}

	private static final class TablePanel extends JPanel{
		/**
		 * Serial ID.
		 */
		private static final long serialVersionUID = 4467273936432261623L;
		private JPanel fields = new JPanel(new GridLayout(0, 6, 2, 2));
		
		public TablePanel(String name){
			super(new BorderLayout());
			
			fields.add(new JLabel(name, SwingConstants.CENTER));
			fields.add(new JLabel("X", SwingConstants.CENTER));
			fields.add(new JLabel("Y", SwingConstants.CENTER));
			fields.add(new JLabel("Width", SwingConstants.CENTER));
			fields.add(new JLabel("Height", SwingConstants.CENTER));
			fields.add(new JLabel("Settings", SwingConstants.CENTER));
			
			add(fields, BorderLayout.PAGE_START);
			add(new JPanel(), BorderLayout.CENTER);
		}
		
		private void addGraphRow(GraphSettings info, boolean live){
			fields.add(new JLabel("KPS", SwingConstants.CENTER));
			
			addLocationFields(info, live);
			
			JButton edit = new JButton("Edit");
			fields.add(edit);
			edit.addActionListener(e->{
//				info.showEditor(live);
//				nameLabel.setText(info.getName());
				//TODO
			});
		}
		
//		/**
//		 * Creates a editable list item for the
//		 * layout configuration dialog
//		 * @param info The positionable that links the 
//		 *        editor to the underlying data
//		 * @param fields The GUI panel that holds all the fields
//		 * @param live Whether or not edits should be displayed in real time
//		 */
		private void addPanelRow(PanelSettings info, boolean live){
			JLabel nameLabel = new JLabel(info.getName(), SwingConstants.CENTER);
			fields.add(nameLabel);

			addLocationFields(info, live);

			JButton edit = new JButton("Edit");
			fields.add(edit);
			edit.addActionListener(e->{
				info.showEditor(live);
				nameLabel.setText(info.getName());
			});
		}
		
		private void addLocationFields(LocationSettings info, boolean live){
			LayoutValidator validator = new LayoutValidator();
			validator.getXField().setModel(new EndNumberModel(info.getLayoutX(), validator.getXField(), update(info::setX, live)));
			validator.getYField().setModel(new EndNumberModel(info.getLayoutY(), validator.getYField(), update(info::setY, live)));
			validator.getWidthField().setModel(new MaxNumberModel(info.getLayoutWidth(), validator.getWidthField(), update(info::setWidth, live)));
			validator.getHeightField().setModel(new MaxNumberModel(info.getLayoutHeight(), validator.getHeightField(), update(info::setHeight, live)));

			JSpinner x = new JSpinner(validator.getXField().getModel());
			x.setEditor(new SpecialNumberModelEditor(x));
			fields.add(x);

			JSpinner y = new JSpinner(validator.getYField().getModel());
			y.setEditor(new SpecialNumberModelEditor(y));
			fields.add(y);

			JSpinner w = new JSpinner(validator.getWidthField().getModel());
			w.setEditor(new SpecialNumberModelEditor(w));
			fields.add(w);

			JSpinner h = new JSpinner(validator.getHeightField().getModel());
			h.setEditor(new SpecialNumberModelEditor(h));
			fields.add(h);
		}
		
		/**
		 * Construct a value change listener that sets new values
		 * to the given field and optionally updates the main GUI.
		 * @param field The field to update with new values.
		 * @param live Whether to update the GUI on updates.
		 * @return The newly constructed change listener.
		 */
		private final ValueChangeListener update(IntConsumer field, boolean live){
			return val->{
				field.accept(val);
				if(live){
					Main.reconfigure();
				}
			};
		}
	}
}
