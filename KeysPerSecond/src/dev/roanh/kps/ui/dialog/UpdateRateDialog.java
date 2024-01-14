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
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import dev.roanh.kps.Main;
import dev.roanh.kps.config.UpdateRate;
import dev.roanh.util.Dialog;

/**
 * Dialog for configuring the update rate of the program.
 * @author Roan
 */
public class UpdateRateDialog extends JPanel{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -726535693978900995L;
	/**
	 * The combo box listing available update rates.
	 */
	private JComboBox<UpdateRate> update = new JComboBox<UpdateRate>(UpdateRate.values());
	
	/**
	 * Constructs a new update rate configuration dialog.
	 */
	private UpdateRateDialog(){
		super(new BorderLayout());
		
		JPanel info = new JPanel(new GridLayout(2, 1, 0, 0));
		info.add(new JLabel("Here you can change the rate at which"));
		info.add(new JLabel("the most panels are updated."));
		
		update.setSelectedItem(Main.config.getUpdateRate());
		update.setRenderer(new RateCellRenderer());
		
		add(info, BorderLayout.PAGE_START);
		add(new JLabel("Update rate: "), BorderLayout.WEST);
		add(update, BorderLayout.CENTER);
	}

	/**
	 * Shows a dialog to configure the update rate.
	 */
	public static final void configureUpdateRate(){
		UpdateRateDialog pconfig = new UpdateRateDialog();
		if(Dialog.showSaveDialog(pconfig)){
			Main.config.setUpdateRate((UpdateRate)pconfig.update.getSelectedItem());
		}
	}
	
	/**
	 * Special cell renderer than colours high and very high update rates.
	 * @author Roan
	 */
	private static final class RateCellRenderer extends DefaultListCellRenderer{
		/**
		 * Serial ID
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus){
			Component item = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			UpdateRate rate = (UpdateRate)value;
			if(rate.isVeryHigh()){
				item.setForeground(Color.MAGENTA);
			}else if(rate.isHigh()){
				item.setForeground(Color.RED);
			}
			
			return item;
		}
	}
}
