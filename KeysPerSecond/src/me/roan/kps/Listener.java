package me.roan.kps;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Listener for dragging the dialog
 * @author Roan
 */
public class Listener implements MouseMotionListener{
	/**
	 * Previous location of the mouse on the screen
	 */
	private Point from = null;
	/**
	 * Instance
	 */
	protected static final Listener INSTANCE = new Listener();
	
	@Override
	public void mouseDragged(MouseEvent e) {
		Point to = e.getPoint();
		if(from == null){
			from = to;
			return;
		}
		Point at = Main.frame.getLocation();
		int x = at.x + (to.x - from.x);
		int y = at.y + (to.y - from.y);
		Main.frame.setLocation(new Point(x, y));
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
