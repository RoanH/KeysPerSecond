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
package dev.roanh.kps.ui.component;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

import dev.roanh.kps.Main;
import dev.roanh.kps.config.ThemeColor;
import dev.roanh.util.Dialog;

/**
 * Simple color picker implementation.
 * @author Roan
 */
public class ColorPicker extends JPanel implements MouseListener{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 3576402284968096657L;
	/**
	 * The function to use to read the current color.
	 */
	private Supplier<ThemeColor> read;
	/**
	 * The function to use to save a newly selected color.
	 */
	private Consumer<ThemeColor> write;
	/**
	 * Color chooser instance
	 */
	private final JColorChooser chooser = new JColorChooser();
	
	/**
	 * Constructs a new color picker.
	 * @param read The function to use to read the current color.
	 * @param write The function to use to save a newly selected color.
	 * @param live True if the GUI is already live and updates should be reflected in real time.
	 */
	public ColorPicker(Supplier<ThemeColor> read, Consumer<ThemeColor> write, boolean live){
		this.read = read;
		this.write = c->{
			write.accept(c);
			if(live){
				Main.reconfigure();
			}
		};
		
		setBorder(BorderFactory.createLoweredBevelBorder());
		setBackground(read.get().getColor());
		this.addMouseListener(this);
	}
	
	@Override
	public void mouseClicked(MouseEvent e){
		chooser.setColor(getBackground());
		if(Dialog.showSaveDialog(chooser)){
			setBackground(chooser.getColor());
			write.accept(new ThemeColor(chooser.getColor().getRGB(), read.get().getAlpha()));
		}
	}

	@Override
	public void mousePressed(MouseEvent e){
	}

	@Override
	public void mouseReleased(MouseEvent e){
	}

	@Override
	public void mouseEntered(MouseEvent e){
	}

	@Override
	public void mouseExited(MouseEvent e){
	}
}
