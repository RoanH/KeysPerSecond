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
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dev.roanh.kps.CommandKeys;
import dev.roanh.kps.CommandKeys.CMD;
import dev.roanh.kps.config.Configuration;
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
	private CommandKeysDialog(Configuration config){
		super(new GridLayout(6, 2, 10, 2));

		add(new JLabel("Reset stats:"));
		add(newButton(config::getCommandResetStats, config::setCommandResetStats));

		add(new JLabel("Exit the program:"));
		add(newButton(config::getCommandExit, config::setCommandExit));

		add(new JLabel("Reset totals:"));
		add(newButton(config::getCommandResetTotals, config::setCommandResetTotals));

		add(new JLabel("Show/hide GUI:"));
		add(newButton(config::getCommandHide, config::setCommandHide));

		add(new JLabel("Pause/Resume:"));
		add(newButton(config::getCommandPause, config::setCommandPause));

		add(new JLabel("Reload config:"));
		add(newButton(config::getCommandReload, config::setCommandReload));
	}
	
	/**
	 * Constructs a new button that asks for a new command
	 * key and uses the given consumer to save the new command.
	 * @param readFun A supplier that provides the current command.
	 * @param saveFun A consumer that write the new command.
	 * @return The constructed button.
	 */
	private JButton newButton(Supplier<CMD> readFun, Consumer<CMD> saveFun){
		JButton button = new JButton(readFun.get().toString());
		button.addActionListener((e)->{
			CMD command = askForNewKey(readFun.get());
			if(command != null){
				saveFun.accept(command);
				button.setText(command.toString());
			}
		});

		return button;
	}

	/**
	 * Show the command key configuration dialog
	 */
	public static final void configureCommandKeys(){
		CommandKeysDialog dialog = new CommandKeysDialog(Main.config);
		Main.eventManager.registerKeyPressListener(dialog);
		Dialog.showMessageDialog(dialog);
		Main.eventManager.unregisterKeyPressListener(dialog);
	}
	
	/**
	 * Prompts the user for a new command key.
	 * @param current The current command key.
	 * @return The new command key or null.
	 */
	private CMD askForNewKey(CMD current){
		JPanel form = new JPanel(new GridLayout(2, 1));
		JLabel txt = new JLabel("Press a key and click 'Save' or press 'Unbind'");
		lastKey = -1;
		preview.setText(current.toString());
		preview.setEditable(false);
		preview.setHorizontalAlignment(JTextField.CENTER);
		form.add(txt);
		form.add(preview);
		
		switch(Dialog.showDialog(form, new String[]{"Save", "Unbind", "Cancel"})){
		case 0:
			return lastKey == -1 ? null : new CMD(CommandKeys.getBaseKeyCode(lastKey), CommandKeys.hasAlt(lastKey), CommandKeys.hasCtrl(lastKey));
		case 1:
			return CMD.NONE;
		default:
			return null;
		}
	}

	@Override
	public void onKeyPress(int code){
		lastKey = CommandKeys.getExtendedKeyCode(code, false, CommandKeys.isCtrlDown, CommandKeys.isAltDown);
		preview.setText(CommandKeys.formatExtendedCode(lastKey));
	}
}
