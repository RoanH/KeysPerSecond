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

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dev.roanh.kps.CommandKeys;
import dev.roanh.kps.config.group.CommandSettings;
import dev.roanh.kps.config.setting.CommandKeySetting;
import dev.roanh.kps.event.listener.KeyPressListener;
import dev.roanh.kps.Main;
import dev.roanh.util.Dialog;

/**
 * Dialog that allows the user to manage command keys.
 * @author Roan
 */
public class CommandKeysDialog extends JPanel implements KeyPressListener{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -469320790352861738L;
	/**
	 * The extended key code of the most recently pressed key.
	 */
	private int lastKey = -1;
	/**
	 * The text field showing the most recently pressed key.
	 * @see #lastKey
	 */
	private JTextField preview = new JTextField(25);

	/**
	 * Constructs a new command keys dialog.
	 * @param config The configuration to update.
	 */
	private CommandKeysDialog(CommandSettings config){
		super(new GridLayout(6, 2, 10, 2));

		add(new JLabel("Reset stats:"));
		add(newButton(config.getCommandResetStats()));

		add(new JLabel("Exit the program:"));
		add(newButton(config.getCommandExit()));

		add(new JLabel("Reset totals:"));
		add(newButton(config.getCommandResetTotals()));

		add(new JLabel("Show/hide GUI:"));
		add(newButton(config.getCommandHide()));

		add(new JLabel("Pause/Resume:"));
		add(newButton(config.getCommandPause()));

		add(new JLabel("Reload config:"));
		add(newButton(config.getCommandReload()));
	}
	
	/**
	 * Constructs a new button shows a dialog to update a command key.
	 * @param key The command key to update.
	 * @return The constructed button.
	 */
	private JButton newButton(CommandKeySetting key){
		JButton button = new JButton(key.toDisplayString());
		button.addActionListener((e)->{
			askForNewKey(key);
			button.setText(key.toDisplayString());
		});

		return button;
	}

	/**
	 * Show the command key configuration dialog.
	 * @param config The configuration to update.
	 */
	public static final void configureCommandKeys(CommandSettings config){
		CommandKeysDialog dialog = new CommandKeysDialog(config);
		Main.eventManager.registerKeyPressListener(dialog);
		Dialog.showMessageDialog(dialog);
		Main.eventManager.unregisterKeyPressListener(dialog);
	}
	
	/**
	 * Prompts the user to configure a command key.
	 * @param key The command key to configure.
	 */
	private void askForNewKey(CommandKeySetting key){
		JPanel form = new JPanel(new GridLayout(2, 1));
		JLabel txt = new JLabel("Press a key and click 'Save' or press 'Unbind'");
		lastKey = -1;
		preview.setText(key.toDisplayString());
		preview.setEditable(false);
		preview.setHorizontalAlignment(JTextField.CENTER);
		form.add(txt);
		form.add(preview);
		
		switch(Dialog.showDialog(form, new String[]{"Save", "Unbind", "Cancel"})){
		case 0:
			final int code = lastKey;
			if(code != -1){
				key.update(code);
			}
			break;
		case 1:
			key.unbind();
			break;
		}
	}

	@Override
	public void onKeyPress(int code){
		lastKey = CommandKeys.getExtendedKeyCode(code, false, CommandKeys.isCtrlDown, CommandKeys.isAltDown);
		preview.setText(CommandKeys.formatExtendedCode(lastKey));
	}
}
