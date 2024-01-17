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
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

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
	private TablePanel keys;
	/**
	 * The configuration being updated.
	 */
	private SettingList<KeyPanelSettings> config;
	
	/**
	 * Constructs a new KeysDialog.
	 * @param config The configuration to update.
	 * @param live If the setting live and changes should be reflected in real time.
	 * @see #configureKeys(SettingList, boolean)
	 */
	private KeysDialog(SettingList<KeyPanelSettings> config, boolean live){
		super(new BorderLayout());
		this.config = config;
		
		//left panel showing added keys
		JPanel left = new JPanel(new BorderLayout());
		left.setBorder(BorderFactory.createTitledBorder("Currently added keys"));

		keys = new TablePanel("Key", false, live);
		keys.addPanels(config);
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
			if(config.contains(info)){
				Dialog.showMessageDialog("The M" + code + " button was already added before.\nIt was not added again.");
			}else{
				config.add(info);
				keys.addPanelRow(config, info);
				keys.revalidate();
				if(keys.isLive()){
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
			Dialog.showMessageDialog("That key was already added before.\nIt was not added again.");
		}else{
			config.add(info);
			keys.addPanelRow(config, info);
			keys.revalidate();
			if(keys.isLive()){
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
	 * Shows the key configuration dialog.
	 * @param config The configuration to update.
	 * @param live If the setting live and changes should be reflected in real time.
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
		List<LayoutPosition> components = config.getLayoutComponents();
		
		//filter data and find max Y
		int maxY = 0;
		Iterator<LayoutPosition> iter = components.iterator();
		while(iter.hasNext()){
			LayoutPosition lp = iter.next();
			if(lp.getLayoutWidth() == -1 && lp.getLayoutHeight() == -1){
				//this panel blocks the entire grid so considering it is pointless
				iter.remove();
			}else if(lp.getLayoutX() == -1 || lp.getLayoutY() == -1){
				//panels after the active area never interfere
				iter.remove();
			}else if(lp.getLayoutHeight() != -1){
				maxY = Math.max(maxY, lp.getLayoutY() + lp.getLayoutHeight());
			}
		}
		
		//determine a valid stretch of rows not blocked by max width panels
		boolean[] hconf = new boolean[maxY];
		iter = components.iterator();
		int maxX = 0;
		while(iter.hasNext()){
			LayoutPosition lp = iter.next();
			if(lp.getLayoutWidth() == -1){
				for(int i = 0; i < lp.getLayoutHeight(); i++){
					hconf[lp.getLayoutY() + i] = true;
				}
				iter.remove();
			}else{
				maxX = Math.max(maxX, lp.getLayoutX() + lp.getLayoutWidth());
			}
		}
		
		int row = findRange(hconf, height);
		
		//determine a valid stretch of columns
		boolean[] conflicts = new boolean[maxX];
		for(LayoutPosition lp : components){
			if(lp.getLayoutHeight() == -1 || (row < lp.getLayoutY() + lp.getLayoutHeight() && lp.getLayoutY() < row + height)){
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
}
