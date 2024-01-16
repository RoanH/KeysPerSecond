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
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.prefs.BackingStoreException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dev.roanh.kps.config.ConfigLoader;
import dev.roanh.kps.config.Configuration;
import dev.roanh.kps.ui.model.FilePathFormatterFactory;
import dev.roanh.util.Dialog;

/**
 * Dialog used to configure the default configuration.
 * @author Roan
 */
public class DefaultConfigDialog extends JPanel{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 7089667269158157654L;
	/**
	 * The text field holding the default config.
	 */
	private JTextField selectedFile = new JFormattedTextField(new FilePathFormatterFactory(), Objects.toString(ConfigLoader.getDefaultConfig(), ""));

	/**
	 * Constructs a new default config dialog.
	 */
	private DefaultConfigDialog(){
		super(new BorderLayout(0, 5));
		
		add(new JLabel("You can configure a default configuration to be opened automatically on launch."), BorderLayout.PAGE_START);
		add(new JLabel("Config: "), BorderLayout.LINE_START);
		add(selectedFile, BorderLayout.CENTER);
		
		JButton select = new JButton("Select");
		add(select, BorderLayout.LINE_END);
		select.addActionListener(e->{
			Path file = Dialog.showFileOpenDialog(Configuration.KPS_NEW_EXT);
			if(file != null){
				selectedFile.setText(file.toAbsolutePath().toString());
			}
		});
	}
	
	/**
	 * Shows a dialog to configure the default configuration file to use.
	 */
	public static final void showDefaultConfigDialog(){
		DefaultConfigDialog dialog = new DefaultConfigDialog();
		try{
			switch(Dialog.showDialog(dialog, new String[]{"Save", "Remove Default Config", "Cancel"})){
			case 0:
				ConfigLoader.setDefaultConfig(Paths.get(dialog.selectedFile.getText()));
				break;
			case 1:
				ConfigLoader.setDefaultConfig(null);
				break;
			case 2:
			default:
				break;
			}
		}catch(BackingStoreException | InvalidPathException e){
			e.printStackTrace();
			Dialog.showErrorDialog("Failed to save default config, cause: " + e.getMessage());
		}
	}
}
