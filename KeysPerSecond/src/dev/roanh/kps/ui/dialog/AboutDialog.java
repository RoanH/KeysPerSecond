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

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import dev.roanh.kps.Main;
import dev.roanh.util.ClickableLink;
import dev.roanh.util.Dialog;
import dev.roanh.util.Util;

/**
 * Simple about dialog displaying general information and useful links.
 * @author Roan
 */
public class AboutDialog extends JPanel{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 7583745921634902889L;

	/**
	 * Constructs a new about dialog.
	 */
	public AboutDialog(){
		super(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.weightx = 1.0D;
		gc.gridx = 0;
		
		JLabel header = new JLabel("KeysPerSecond " + Main.VERSION, SwingConstants.CENTER);
		header.setFont(new Font("Dialog", Font.BOLD, 18));
		add(header, gc);
		
		add(new JLabel(
			"<html>KeysPerSecond is an open source input statistics displayer"
			+ "<br>released for free under the GPLv3. Any bugs and feature"
			+ "<br>requests can be reported on GitHub.</html>"
		), gc);
		
		add(Box.createVerticalStrut(10), gc);
		
		//github
		gc.anchor = GridBagConstraints.LINE_START;
		JPanel line = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
		line.add(new JLabel("<html><b>GitHub:</b></html>", SwingConstants.LEFT), gc);
		add(line, gc);
		
		line = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
		line.add(new JLabel("  - "));
		JLabel label = new JLabel("<html><font color=blue><u>Main Page</u></font></html>");
		label.addMouseListener(new ClickableLink("https://github.com/RoanH/KeysPerSecond"));
		line.add(label);
		add(line, gc);
		
		line = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
		line.add(new JLabel("  - "));
		label = new JLabel("<html><font color=blue><u>Issues</u></font></html>");
		label.addMouseListener(new ClickableLink("https://github.com/RoanH/KeysPerSecond/issues"));
		line.add(label);
		line.add(new JLabel("(bugs & feature requests)"));
		add(line, gc);
		
		line = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
		line.add(new JLabel("  - "));
		label = new JLabel("<html><font color=blue><u>Releases</u></font></html>");
		label.addMouseListener(new ClickableLink("https://github.com/RoanH/KeysPerSecond/releases"));
		line.add(label);
		line.add(new JLabel("(release notes & downloads)"));
		add(line, gc);
		
		//version
		line = new JPanel(new FlowLayout(FlowLayout.LEFT));
		line.add(new JLabel("<html><b>Latest version</b>: "));
		JLabel versionLabel = new JLabel("checking...");
		line.add(versionLabel);
		add(line, gc);
		
		Util.checkVersion("RoanH", "KeysPerSecond", result->result.ifPresent(version->{
			versionLabel.setText(Main.VERSION.equals(version) ? version : (version + "  (update available)"));
		}));
		
		//contact
		line = new JPanel(new FlowLayout(FlowLayout.LEFT));
		line.add(new JLabel("<html><b>Contact</b>:&nbsp;&nbsp;Roan Hofland</html>"));
		JLabel email = new JLabel("<html>&lt;<font color=blue><u>roan@roanh.dev</u></font>&gt;</html>");
		email.addMouseListener(new ClickableLink("mailto:roan@roanh.dev"));
		line.add(email);
		add(line, gc);
	}
	
	/**
	 * Shows an about dialog with useful information.
	 */
	public static void showAbout(){
		Dialog.showMessageDialog(new AboutDialog());
	}
}
