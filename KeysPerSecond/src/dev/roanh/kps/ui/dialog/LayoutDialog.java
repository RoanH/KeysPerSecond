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
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import dev.roanh.kps.GraphMode;
import dev.roanh.kps.KeyInformation;
import dev.roanh.kps.Main;
import dev.roanh.kps.RenderingMode;
import dev.roanh.kps.layout.LayoutValidator;
import dev.roanh.kps.layout.Positionable;
import dev.roanh.kps.panels.BasePanel;
import dev.roanh.kps.ui.model.DynamicInteger;
import dev.roanh.kps.ui.model.EndNumberModel;
import dev.roanh.kps.ui.model.MaxNumberModel;
import dev.roanh.kps.ui.model.SpecialNumberModel.ValueChangeListener;
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

		JPanel fields = new JPanel(new GridLayout(0, 5, 2, 2));
		JPanel modes = new JPanel(new GridLayout(0, 1, 0, 2));

		fields.add(new JLabel("Key", SwingConstants.CENTER));
		fields.add(new JLabel("X", SwingConstants.CENTER));
		fields.add(new JLabel("Y", SwingConstants.CENTER));
		fields.add(new JLabel("Width", SwingConstants.CENTER));
		fields.add(new JLabel("Height", SwingConstants.CENTER));
		modes.add(new JLabel("Mode", SwingConstants.CENTER));

		for(KeyInformation i : Main.config.keyinfo){
			createListItem(i, fields, modes, live);
		}
		if(Main.config.showAvg){
			createListItem(Main.config.avgPanel, fields, modes, live);
		}
		if(Main.config.showMax){
			createListItem(Main.config.maxPanel, fields, modes, live);
		}
		if(Main.config.showCur){
			createListItem(Main.config.curPanel, fields, modes, live);
		}
		if(Main.config.showTotal){
			createListItem(Main.config.totPanel, fields, modes, live);
		}

		JPanel keys = new JPanel(new BorderLayout());
		keys.add(fields, BorderLayout.CENTER);
		keys.add(modes, BorderLayout.LINE_END);

		JPanel view = new JPanel(new BorderLayout());
		view.add(keys, BorderLayout.PAGE_START);
		view.add(new JPanel(), BorderLayout.CENTER);
		
		JPanel gridSize = new JPanel(new GridLayout(2, 2, 0, 5));
		gridSize.setBorder(BorderFactory.createTitledBorder("Size"));
		gridSize.add(new JLabel("Cell size: "));
		JSpinner gridSpinner = new JSpinner(new SpinnerNumberModel(Main.config.cellSize, BasePanel.imageSize, Integer.MAX_VALUE, 1));
		gridSize.add(gridSpinner);
		gridSize.add(new JLabel("Panel border offset: "));
		JSpinner gapSpinner = new JSpinner(new SpinnerNumberModel(Main.config.borderOffset, 0, new DynamicInteger(()->(Main.config.cellSize - BasePanel.imageSize)), 1));
		gapSpinner.addChangeListener((e)->{
			Main.config.borderOffset = (int)gapSpinner.getValue();
			if(live){
				Main.reconfigure();
			}
		});
		gridSize.add(gapSpinner);
		gridSpinner.addChangeListener((e)->{
			Main.config.cellSize = (int)gridSpinner.getValue();
			if(Main.config.borderOffset > Main.config.cellSize - BasePanel.imageSize){
				Main.config.borderOffset = Main.config.cellSize - BasePanel.imageSize;
				gapSpinner.setValue(Main.config.borderOffset);
			}
			if(live){
				Main.reconfigure();
			}
		});
		
		form.add(gridSize, BorderLayout.PAGE_START);
		
		JScrollPane pane = new JScrollPane(view);
		pane.setBorder(BorderFactory.createTitledBorder("Panels"));
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		pane.setPreferredSize(new Dimension(450, 200));

		form.add(pane, BorderLayout.CENTER);

		JPanel graphLayout = new JPanel(new GridLayout(5, 2, 0, 5));
		graphLayout.setBorder(BorderFactory.createTitledBorder("Graph"));
		graphLayout.add(new JLabel("Graph mode: "));
		JComboBox<Object> graphMode = new JComboBox<Object>(GraphMode.values());
		graphMode.setSelectedItem(Main.config.graphMode);
		graphLayout.add(graphMode);

		LayoutValidator validator = new LayoutValidator();
		validator.getXField().setModel(new EndNumberModel(Main.config.getGraphX(), validator.getXField(), update(Main.config::setGraphX, live)));
		validator.getYField().setModel(new EndNumberModel(Main.config.getGraphY(), validator.getYField(), update(Main.config::setGraphY, live)));
		validator.getWidthField().setModel(new MaxNumberModel(Main.config.getGraphWidth(), validator.getWidthField(), update(Main.config::setGraphWidth, live)));
		validator.getHeightField().setModel(new MaxNumberModel(Main.config.getGraphHeight(), validator.getHeightField(), update(Main.config::setGraphHeight, live)));

		graphLayout.add(new JLabel("Graph x position: "));
		JSpinner x = new JSpinner(validator.getXField().getModel());
		x.setEditor(new SpecialNumberModelEditor(x));
		x.setEnabled(Main.config.graphMode == GraphMode.INLINE);
		graphLayout.add(x);

		graphLayout.add(new JLabel("Graph y position: "));
		JSpinner y = new JSpinner(validator.getYField().getModel());
		y.setEditor(new SpecialNumberModelEditor(y));
		y.setEnabled(Main.config.graphMode == GraphMode.INLINE);
		graphLayout.add(y);

		graphLayout.add(new JLabel("Graph width: "));
		JSpinner w = new JSpinner(validator.getWidthField().getModel());
		w.setEditor(new SpecialNumberModelEditor(w));
		graphLayout.add(w);

		graphLayout.add(new JLabel("Graph height: "));
		JSpinner h = new JSpinner(validator.getHeightField().getModel());
		h.setEditor(new SpecialNumberModelEditor(h));
		graphLayout.add(h);

		graphMode.addActionListener((e)->{
			Main.config.graphMode = (GraphMode)graphMode.getSelectedItem();
			if(graphMode.getSelectedItem() == GraphMode.INLINE){
				x.setEnabled(true);
				y.setEnabled(true);
			}else{
				x.setEnabled(false);
				y.setEnabled(false);
			}
			if(live){
				Main.reconfigure();
			}
		});

		form.add(graphLayout, BorderLayout.PAGE_END);

		Dialog.showMessageDialog(form, true, ModalityType.APPLICATION_MODAL);
		Main.content.hideGrid();
	}

	/**
	 * Creates a editable list item for the
	 * layout configuration dialog
	 * @param info The positionable that links the 
	 *        editor to the underlying data
	 * @param fields The GUI panel that holds all the fields
	 * @param modes The GUI panel that holds all the modes
	 * @param live Whether or not edits should be displayed in real time
	 */
	private static final void createListItem(Positionable info, JPanel fields, JPanel modes, boolean live){
		fields.add(new JLabel(info.getName(), SwingConstants.CENTER));

		LayoutValidator validator = new LayoutValidator();
		validator.getXField().setModel(new EndNumberModel(info.getX(), validator.getXField(), update(info::setX, live)));
		validator.getYField().setModel(new EndNumberModel(info.getY(), validator.getYField(), update(info::setY, live)));
		validator.getWidthField().setModel(new MaxNumberModel(info.getWidth(), validator.getWidthField(), update(info::setWidth, live)));
		validator.getHeightField().setModel(new MaxNumberModel(info.getHeight(), validator.getHeightField(), update(info::setHeight, live)));

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

		JComboBox<RenderingMode> mode = new JComboBox<RenderingMode>(RenderingMode.values());
		mode.setSelectedItem(info.getRenderingMode());
		mode.addActionListener((e)->{
			info.setRenderingMode((RenderingMode)mode.getSelectedItem());
			if(live){
				Main.reconfigure();
			}
		});
		modes.add(mode);
	}
	
	/**
	 * Construct a value change listener that set
	 * new values to the given field and optionally
	 * updates the main GUI.
	 * @param field The field to update with new values.
	 * @param live Whether to update the GUI on updates.
	 * @return The newly constructed change listener.
	 */
	private static final ValueChangeListener update(IntConsumer field, boolean live){
		return val->{
			field.accept(val);
			if(live){
				Main.reconfigure();
			}
		};
	}
	
	/**
	 * Simple editor that allows edits in the default editor field.
	 * @author Roan
	 */
	private static final class SpecialNumberModelEditor extends DefaultEditor{
		/**
		 * Serial ID.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Constructs a new special number model editor for the given spinner.
		 * @param spinner The spinner to create an editor for.
		 */
		public SpecialNumberModelEditor(JSpinner spinner){
			super(spinner);
			
			JFormattedTextField disp = getTextField();
			disp.setEditable(true);
			disp.setHorizontalAlignment(JTextField.RIGHT);
		}
	}
}
