/**
 * Copyright (c) 2016 Kristian Kraljic
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package lc.kra.system.mouse.event;

import java.util.EventObject;

public class GlobalMouseEvent extends EventObject {
	private static final long serialVersionUID = -8194688548489965445L;
	
	/**
	 * Key transition states (up / down)
	 */
	public static final int
		TS_UP = 0, //Mouse button lifted up
		TS_DOWN = 1, //Mouse button pressed down
		TS_MOVE = 2, //Mouse moved
		TS_WHEEL = 3; //Mouse wheel scrolled up / down
	/**
	 * Mouse button identifiers
	 */
	public static final int
		BUTTON_NO = 0x0, //No mouse button
		BUTTON_LEFT = 1<<0, //Left mouse button
		BUTTON_RIGHT = 1<<1, //Right mouse button
		BUTTON_MIDDLE = 1<<4; //Middle mouse button
	
	/**
	 * Wheel delta parameter
	 */
	public static final int
		WHEEL_DELTA = 120; //Default wheel delta parameter
	
	protected int transitionState, button, buttons, x, y, delta;

	public GlobalMouseEvent(Object source, int transitionState, int button, int buttons, int x, int y, int delta) {
		super(source);
		this.transitionState = transitionState;
		this.button = button;
		this.buttons = buttons;
		this.x = x;
		this.y = y;
		this.delta = delta;
	}
	
	/**
	 * Returns the transition state (mouse up/down, move or mouse wheel) for this mouse event.
	 * 
	 * @return either one of TS_UP, TS_DOWN, TS_MOVE, TS_WHEEL.
	 * @see {@link #TS_UP}
	 * @see {@link #TS_DOWN}
	 * @see {@link #TS_MOVE}
	 * @see {@link #TS_WHEEL}
	 */
	public int getTransitionState() { return transitionState; }
	
	/**
	 * Returns the mouse current button pressed on TS_UP or TS_DOWN transition states.
	 * 
	 * @return One of BUTTON_LEFT, BUTTON_RIGHT, BUTTON_MIDDLE or BUTTON_NO if not in transition state {@link #TS_UP} or {@link #TS_DOWN}.
	 * @see #getTransitionState()
	 * @see {@link #BUTTON_NO}
	 * @see {@link #BUTTON_LEFT}
	 * @see {@link #BUTTON_RIGHT}
	 * @see {@link #BUTTON_MIDDLE}
	 */
	public int getButton() { return button; }
	/**
	 * Returns the bitwise or of all buttons currently pressed on the mouse. If just
	 * one button is pressed the same value as for {@link #getButton()} is returned.
	 * 
	 * @return The bitwise addition of {@link #BUTTON_LEFT}, {@link #BUTTON_RIGHT} and {@link #BUTTON_MIDDLE}.
	 * @see {@link #getButton()}
	 */
	public int getButtons() { return buttons; }
	
	/**
	 * Returns the absolute horizontal screen coordinate where this mouse event occurred.
	 * 
	 * @return An absolute X position of the cursor on the screen.
	 */
	public int getX() { return x; }
	/**
	 * Returns the absolute vertical screen coordinate where this mouse event occurred.
	 * 
	 * @return An absolute Y position of the cursor on the screen.
	 */
	public int getY() { return y; }
	
	/**
	 * By default this method returns a multiple of {@link #WHEEL_DELTA} indicating the scroll speed of the
	 * users mouse wheel. As e.g. for freely-rotating wheels with no notches a finder finer-resolution is
	 * set, in a higher interval.
	 * 
	 * @return The relative change (delta) of the mouse wheel movement.
	 */
	public int getDelta() { return delta; }
	
    /**
     * Returns a String representation of this GlobalMouseEvent.
     *
     * @return  A a String representation of this GlobalMouseEvent.
     */
	@Override public String toString() {
		StringBuilder builder = new StringBuilder().append(x).append(',').append(y);
		if(buttons!=BUTTON_NO||transitionState==TS_WHEEL) {
			builder.append(" [");
			if((buttons&BUTTON_LEFT)!=BUTTON_NO)
				builder.append("left,");
			if((buttons&BUTTON_RIGHT)!=BUTTON_NO)
				builder.append("right,");
			if((buttons&BUTTON_MIDDLE)!=BUTTON_NO)
				builder.append("middle,");
			if(transitionState==TS_WHEEL)
				builder.append("delta ").append(delta).append(',');
			return builder.deleteCharAt(builder.length()-1).append(']').toString();
		} else return builder.toString();
	}
}