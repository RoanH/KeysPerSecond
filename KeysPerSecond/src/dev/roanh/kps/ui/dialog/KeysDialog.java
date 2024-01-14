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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Dialog.ModalityType;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import dev.roanh.kps.CommandKeys;
import dev.roanh.kps.Main;
import dev.roanh.kps.config.Configuration;
import dev.roanh.kps.config.SettingList;
import dev.roanh.kps.config.group.KeyPanelSettings;
import dev.roanh.kps.event.listener.KeyPressListener;
import dev.roanh.kps.layout.LayoutPosition;
import dev.roanh.kps.ui.component.TablePanel;
import dev.roanh.util.Dialog;

/**
 * Logic for the key setup dialog.
 * @author Roan
 */
public class KeysDialog extends JPanel implements KeyPressListener{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -1799584925602660258L;
	/**
	 * The extended key code for the most recent key press.
	 */
	private int lastKey = -1;
	/**
	 * The text field showing the most recently pressed key.
	 * @see #lastKey
	 */
	private JTextField pressed = new JTextField("<press a key>", 25);
	/**
	 * Table model showing all added keys and buttons.
	 */
//	private KeysModel model = new KeysModel();
	private SettingList<KeyPanelSettings> config;
	private boolean live;
	
	private TablePanel keys;

	
	/**
	 * Constructs a new KeysDialog.
	 * @see #configureKeys(SettingList, boolean)
	 */
	private KeysDialog(SettingList<KeyPanelSettings> config, boolean live){
		super(new BorderLayout());
		this.config = config;
		this.live = live;
		
		//left panel showing added keys
		JPanel left = new JPanel(new BorderLayout());
		left.setBorder(BorderFactory.createTitledBorder("Currently added keys"));
//		left.add(new JLabel("You can remove a key or update its display name and visbility below."), BorderLayout.PAGE_START);
		//JTable keys = new JTable();
		keys = new TablePanel("Key", false, live);
//		keys.setModel(model);
//		keys.setDragEnabled(false);
		
		keys.addKeys(config);
		JScrollPane pane = new JScrollPane(keys);
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		pane.setPreferredSize(new Dimension(300, 200));
		left.add(pane, BorderLayout.CENTER);
		
		//right panel for adding keys/buttons
		JPanel right = new JPanel(new GridLayout(2, 1));
		
		//adding keys
		JPanel addKey = new JPanel(new BorderLayout());
		addKey.setBorder(BorderFactory.createTitledBorder("Keys"));
		pressed.setHorizontalAlignment(JTextField.CENTER);
		pressed.setEditable(false);
		addKey.add(pressed, BorderLayout.CENTER);
		JButton add = new JButton("Add key");
		add.addActionListener(e->commitKey());
		addKey.add(add, BorderLayout.PAGE_END);
		right.add(addKey, BorderLayout.PAGE_START);
		
		//adding buttons
		JPanel addButton = new JPanel(new GridBagLayout());
		addButton.setBorder(BorderFactory.createTitledBorder("Mouse Buttons"));
		GridBagConstraints cons = new GridBagConstraints();
		cons.weightx = 1;
		cons.weighty = 1;
		cons.fill = GridBagConstraints.BOTH;
		cons.gridheight = 1;
		cons.gridwidth = 2;
		cons.gridx = 0;
		cons.gridy = 0;
		addButton.add(newButton(1, "M1 (left click)"), cons);
		cons.gridy = 1;
		addButton.add(newButton(2, "M2 (right click)"), cons);
		cons.gridy = 2;
		addButton.add(newButton(3, "M3 (mouse wheel)"), cons);
		cons.gridy = 3;
		cons.gridwidth = 1;
		addButton.add(newButton(4, "M4"), cons);
		cons.gridx = 1;
		addButton.add(newButton(5, "M5"), cons);
		right.add(addButton, BorderLayout.CENTER);
		
		add(left, BorderLayout.CENTER);
		add(right, BorderLayout.LINE_END);
	}
	
	/**
	 * Constructs a new button that when pressed registers a mouse button.
	 * @param code The ID of the mouse button to register.
	 * @param text The display text for the button.
	 * @return The constructed button.
	 */
	private JButton newButton(int code, String text){
		JButton button = new JButton(text);
		button.addActionListener(e->{
			KeyPanelSettings info = new KeyPanelSettings(placePanel(), Main.getExtendedButtonCode(code));
//			KeyInformation key = new KeyInformation("M" + code, Main.getExtendedButtonCode(code), false, false, false, true);
			if(config.contains(info)){
//				KeyInformation.autoIndex -= 2;
				Dialog.showMessageDialog("The M" + code + " button was already added before.\nIt was not added again.");
			}else{
				config.add(info);
				keys.addPanelRow(config, info);
				keys.revalidate();
//				model.fireTableDataChanged();
				if(live){
					Main.reconfigure();
				}
			}
		});
		
		return button;
	}
	
	/**
	 * Registers a new key to be tracked. The key that will be
	 * registered is tracked by {@link #lastKey}.
	 * @see #lastKey
	 */
	private void commitKey(){
		if(lastKey == -1){
			Dialog.showMessageDialog("Please press a key first.");
			return;
		}
		
		KeyPanelSettings info = new KeyPanelSettings(placePanel(), lastKey);
		if(config.contains(info)){
//			KeyInformation.autoIndex -= 2;
			Dialog.showMessageDialog("That key was already added before.\nIt was not added again.");
		}else{
			config.add(info);
//			model.fireTableDataChanged();
			keys.addPanelRow(config, info);
			keys.revalidate();
			if(live){
				Main.reconfigure();
			}
		}
	}
	
	@Override
	public void onKeyPress(int code){
		lastKey = Main.getExtendedKeyCode(code);
		pressed.setText("<" + CommandKeys.formatExtendedCode(lastKey) + ">");
		pressed.repaint();
	}

	/**
	 * Shows the key configuration dialog
	 */
	public static final void configureKeys(SettingList<KeyPanelSettings> config, boolean live){
		KeysDialog dialog = new KeysDialog(config, live);
		Main.eventManager.registerKeyPressListener(dialog);
		Dialog.showMessageDialog(dialog, true, ModalityType.APPLICATION_MODAL);
		Main.eventManager.unregisterKeyPressListener(dialog);
	}
	
	/**
	 * Computes a location where a panel of default size can be
	 * placed without overlapping any existing panels.
	 * @return A location where a panel can be placed without overlap.
	 */
	private Point placePanel(){
		return placePanel(Main.config, 2, 3);
	}
	
	/**
	 * Computes a location where a panel of the given the given size can be
	 * placed without overlapping any existing panels.
	 * @param config The configuration get all panels from.
	 * @param width The width of the panel to place.
	 * @param height The height of the panel to place.
	 * @return A location where the requested panel can be placed without overlap.
	 */
	private Point placePanel(Configuration config, int width, int height){
		//determine a valid stretch of rows not blocked by max width panels
		List<LayoutPosition> components = config.getLayoutComponents();
		boolean[] hconf = new boolean[components.stream().filter(lp->lp.getLayoutY() != -1).mapToInt(lp->lp.getLayoutY() + lp.getLayoutHeight()).max().orElse(0)];
		
		for(LayoutPosition lp : components){
			if(lp.getLayoutY() != -1){
				for(int i = 0; i < lp.getLayoutHeight(); i++){
					hconf[lp.getLayoutY() + i] = lp.getLayoutWidth() == -1;
				}
			}
		}
		
		int row = findRange(hconf, height);
		
		//determine a valid stretch of columns
		int maxw = components.stream().filter(lp->lp.getLayoutX() != -1).mapToInt(lp->lp.getLayoutX() + lp.getLayoutWidth()).max().orElse(0);
		boolean[] conflicts = new boolean[maxw];
		
		for(LayoutPosition lp : components){
			if(row <= lp.getLayoutY() && lp.getLayoutY() < row + height && lp.getLayoutX() != -1){
				for(int i = 0; i < lp.getLayoutWidth(); i++){
					conflicts[lp.getLayoutX() + i] = true;
				}
			}
		}

		return new Point(findRange(conflicts, width), row);
	}
	
	/**
	 * Finds the first index of a range of false values of
	 * the given size in the given array of conflicts.
	 * @param conflicts An array with conflict values.
	 * @param size The length of the range to find.
	 * @return The first index of a valid range, possibly
	 *         one such that the range runs out of the array.
	 */
	private int findRange(boolean[] conflicts, int size){
		int free = 0;
		for(int i = 0; i < conflicts.length; i++){
			if(conflicts[i]){
				free = 0;
			}else{
				free++;
				if(free >= size){
					return i - size + 1;
				}
			}
		}

		return conflicts.length - free;
	}

	/**
	 * Table model that displays all configured keys.
	 * @author Roan
	 */
	//TODO obsolete I guess
	private class KeysModel extends DefaultTableModel{
		/**
		 * Serial ID
		 */
		private static final long serialVersionUID = -5510962859479828507L;

		@Override
		public int getRowCount(){
			return config == null ? 0 : config.size();
		}

		@Override
		public int getColumnCount(){
			return 3;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex){
			switch(columnIndex){
			case 0:
				return config.get(rowIndex).getName();
			case 1:
				return config.get(rowIndex).isVisible();
			case 2:
				return false;
			default:
				return null;
			}
		}

		@Override
		public String getColumnName(int col){
			switch(col){
			case 0:
				return "Key";
			case 1:
				return "Visible";
			case 2:
				return "Remove";
			default:
				return null;
			}
		}

		@Override
		public Class<?> getColumnClass(int columnIndex){
			if(columnIndex == 1 || columnIndex == 2){
				return Boolean.class;
			}
			return super.getColumnClass(columnIndex);
		}

		@Override
		public boolean isCellEditable(int row, int col){
			return true;
		}

		@Override
		public void setValueAt(Object value, int row, int col){
			switch(col){
			case 0:
				config.get(row).setName((String)value);
				break;
			case 1:
				config.get(row).setVisible((boolean)value);
				break;
			case 2:
				if((boolean)value == true){
					Main.removeKey(config.remove(row).getKeyCode());
					this.fireTableDataChanged();
				}
				break;
			}
		}
	}
}
