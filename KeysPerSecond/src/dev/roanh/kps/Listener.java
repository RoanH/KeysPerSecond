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
package dev.roanh.kps;

import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;

import dev.roanh.kps.ui.editor.EditorProvider;
import dev.roanh.kps.Menu.MenuItemUI;

/**
 * Listener for dragging the dialog
 * @author Roan
 */
public class Listener implements MouseMotionListener, MouseListener, ActionListener{
	/**
	 * Previous location of the mouse on the screen
	 */
	private Point from = null;
	/**
	 * Optional right click menu item to edit the clicked panel.
	 */
	private JMenuItem editSelected = new JMenuItem("Edit panel");
	/**
	 * The editor for the panel last right clicked, if any.
	 */
	private EditorProvider editor;
	
	/**
	 * Constructs a new movement and menu listener.
	 */
	private Listener(){
		editSelected.setUI(new MenuItemUI());
	}
	
	/**
	 * Constructs a new movement and menu listener for the given frame.
	 * @param frame The frame to create the listener for.
	 */
	public static void configureListener(JFrame frame){
		Listener listener = new Listener();
		frame.addMouseMotionListener(listener);
		frame.addMouseListener(listener);
		listener.editSelected.addActionListener(listener);
	}

	@Override
	public void mouseDragged(MouseEvent e){
		Point to = e.getPoint();
		if(from == null){
			from = to;
			return;
		}
		
		Point at = e.getComponent().getLocation();
		int x = at.x + (to.x - from.x);
		int y = at.y + (to.y - from.y);
		e.getComponent().setLocation(new Point(x, y));
	}

	@Override
	public void mouseMoved(MouseEvent e){
		from = e.getPoint();
	}

	@Override
	public void mouseClicked(MouseEvent e){
	}

	@Override
	public void mousePressed(MouseEvent e){
	}

	@Override
	public void mouseReleased(MouseEvent e){
		if(e.getButton() == MouseEvent.BUTTON3){
			Component comp = Main.content.getComponentAt(e.getX(), e.getY());
			if(comp instanceof EditorProvider){
				editor = (EditorProvider)comp;
				Menu.menu.insert(editSelected, 0);
			}else{
				Menu.menu.remove(editSelected);
			}

			Menu.menu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void mouseEntered(MouseEvent e){
	}

	@Override
	public void mouseExited(MouseEvent e){
	}

	@Override
	public void actionPerformed(ActionEvent e){
		if(editor != null){
			editor.showEditor(true);
		}
	}

	static{
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher(){
			/**
			 * To prevent new heap allocations on each event.
			 */
			private Point tmp = new Point();

			@Override
			public boolean dispatchKeyEvent(KeyEvent e){
				if(!Menu.menu.isVisible() && e.getComponent() instanceof JFrame){
					if(e.getID() == KeyEvent.KEY_PRESSED){
						int d = e.isShiftDown() ? 3 : e.isControlDown() ? 2 : 1;
						switch(e.getKeyCode()){
						case KeyEvent.VK_LEFT:
						case KeyEvent.VK_KP_LEFT:
							e.getComponent().getLocation(tmp);
							e.getComponent().setLocation(tmp.x - d, tmp.y);
							break;
						case KeyEvent.VK_RIGHT:
						case KeyEvent.VK_KP_RIGHT:
							e.getComponent().getLocation(tmp);
							e.getComponent().setLocation(tmp.x + d, tmp.y);
							break;
						case KeyEvent.VK_UP:
						case KeyEvent.VK_KP_UP:
							e.getComponent().getLocation(tmp);
							e.getComponent().setLocation(tmp.x, tmp.y - d);
							break;
						case KeyEvent.VK_DOWN:
						case KeyEvent.VK_KP_DOWN:
							e.getComponent().getLocation(tmp);
							e.getComponent().setLocation(tmp.x, tmp.y + d);
							break;
						default:
							break;
						}
					}
				}
				return false;
			}
		});
	}
}
