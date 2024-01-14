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
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

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
import dev.roanh.kps.config.PanelType;
import dev.roanh.kps.config.SettingList;
import dev.roanh.kps.config.group.GraphSettings;
import dev.roanh.kps.config.group.KeyPanelSettings;
import dev.roanh.kps.config.group.LayoutSettings;
import dev.roanh.kps.config.group.LocationSettings;
import dev.roanh.kps.config.group.PanelSettings;
import dev.roanh.kps.layout.LayoutValidator;
import dev.roanh.kps.panels.BasePanel;
import dev.roanh.kps.ui.component.TablePanel;
import dev.roanh.kps.ui.editor.Editor;
import dev.roanh.kps.ui.editor.GraphEditor;
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
		TablePanel panelView = new TablePanel("Panel", true, live);
		panelView.addPanels(Main.config.getKeySettings());
		panelView.addPanels(Main.config.getPanels());
		
		JScrollPane pane = new JScrollPane(panelView);
		pane.setBorder(BorderFactory.createEmptyBorder());
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		pane.setPreferredSize(new Dimension(600, 200));
		
		JPanel buttons = new JPanel(new GridLayout(1, 4, 2, 0));
		buttons.add(createAddButton(panelView, "Add Maximum", Main.config.getPanels(), PanelType.MAX::newSettings, panelView::addPanelRow, live));
		buttons.add(createAddButton(panelView, "Add Average", Main.config.getPanels(), PanelType.AVG::newSettings, panelView::addPanelRow, live));
		buttons.add(createAddButton(panelView, "Add KPS", Main.config.getPanels(), PanelType.CURRENT::newSettings, panelView::addPanelRow, live));
		buttons.add(createAddButton(panelView, "Add Total", Main.config.getPanels(), PanelType.TOTAL::newSettings, panelView::addPanelRow, live));
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Panels"));
		panel.add(pane, BorderLayout.CENTER);
		panel.add(buttons, BorderLayout.PAGE_END);
		form.add(panel, BorderLayout.CENTER);

		//graph configuration
		TablePanel graphView = new TablePanel("Graph", true, live);
		graphView.addGraphs(Main.config.getGraphSettings());
		
		JScrollPane graphPane = new JScrollPane(graphView);
		graphPane.setBorder(BorderFactory.createEmptyBorder());
		graphPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		graphPane.setPreferredSize(new Dimension(600, 100));
		
		JPanel graphPanel = new JPanel(new BorderLayout());
		graphPanel.setBorder(BorderFactory.createTitledBorder("Graphs"));
		graphPanel.add(graphPane, BorderLayout.CENTER);
		graphPanel.add(createAddButton(graphView, "Add KPS Graph", Main.config.getGraphSettings(), GraphSettings::new, graphView::addGraphRow, live), BorderLayout.PAGE_END);
		form.add(graphPanel, BorderLayout.PAGE_END);
		
		Dialog.showMessageDialog(form, true, ModalityType.APPLICATION_MODAL);
		Main.content.hideGrid();
	}
	
	private static <T extends LocationSettings> JButton createAddButton(TablePanel view, String text, SettingList<T> panels, Supplier<T> settingsCtor, BiConsumer<SettingList<T>, T> viewUpdate, boolean live){
		JButton add = new JButton(text);
		add.addActionListener(e->{
			T settings = settingsCtor.get();
			panels.add(settings);
			viewUpdate.accept(panels, settings);
			view.revalidate();
			if(live){
				Main.reconfigure();
			}
		});
		
		return add;
	}
}
