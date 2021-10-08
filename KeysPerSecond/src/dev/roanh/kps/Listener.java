package dev.roanh.kps;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Listener for dragging the dialog
 * @author Roan
 */
public class Listener implements MouseMotionListener, MouseListener{
	/**
	 * Previous location of the mouse on the screen
	 */
	private Point from = null;

	/**
	 * Constructs a new movement listener
	 * for the given frame
	 * @param frame The frame to create
	 *        the listener for
	 */
	protected Listener(JFrame frame){
		frame.addMouseMotionListener(this);
		frame.addMouseListener(this);
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
			SwingUtilities.invokeLater(()->{
				Menu.menu.show(e.getComponent(), e.getX(), e.getY());
			});
		}
	}

	@Override
	public void mouseEntered(MouseEvent e){
	}

	@Override
	public void mouseExited(MouseEvent e){
	}

	static{
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher(){
			/**
			 * To prevent new heap allocations
			 * on each event
			 */
			private Point tmp = new Point();

			@Override
			public boolean dispatchKeyEvent(KeyEvent e){
				if(!Menu.menu.isVisible()){
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
						}
					}
				}
				return false;
			}
		});
	}
}
