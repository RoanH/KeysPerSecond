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
package lc.kra.system.mouse;

import static lc.kra.system.mouse.event.GlobalMouseEvent.BUTTON_NO;
import static lc.kra.system.mouse.event.GlobalMouseEvent.TS_DOWN;
import static lc.kra.system.mouse.event.GlobalMouseEvent.TS_MOVE;
import static lc.kra.system.mouse.event.GlobalMouseEvent.TS_UP;
import static lc.kra.system.mouse.event.GlobalMouseEvent.TS_WHEEL;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import lc.kra.system.LibraryLoader;
import lc.kra.system.mouse.event.GlobalMouseEvent;
import lc.kra.system.mouse.event.GlobalMouseListener;

public class GlobalMouseHook {
	private static final int STATUS_SUCCESS = 0;
	
	private NativeMouseHook mouseHook;
	
	private BlockingQueue<GlobalMouseEvent> inputBuffer =
		new LinkedBlockingQueue<GlobalMouseEvent>();
	private boolean libraryLoad; int buttons = BUTTON_NO;
	
	private List<GlobalMouseListener> listeners = new CopyOnWriteArrayList<GlobalMouseListener>();
	private Thread eventDispatcher = new Thread() {{
			setName("Global Mouse Hook Dispatcher");
			setDaemon(true);
		}
		
		public void run() {
			try {
				// while the global mouse hook is alive, try to take events and dispatch them
				while(GlobalMouseHook.this.isAlive()) {
					GlobalMouseEvent event = inputBuffer.take();
					switch(event.getTransitionState()) {
					case TS_UP:
						mouseReleased(event);
						break;
					case TS_DOWN:
						mousePressed(event);
						break;
					case TS_MOVE:
						mouseMoved(event);
						break;
					case TS_WHEEL:
						mouseWheel(event);
						break;
					}
				}
			} catch(InterruptedException e) { /* thread got interrupted, break */ }
		}
	};
	
	/**
	 * Instantiate a new GlobalMouseHook.
	 * 
	 * The constructor first tries to load the native library. On failure a {@link UnsatisfiedLinkError}
	 * is thrown. Afterwards the native mouse hook is initialized. A {@link RuntimeException} is raised
	 * in case the hook could not be established.
	 * 
	 * Two separate threads are started by the class. The HookThread and a separate EventDispatcherThread.
	 * 
	 * @throws UnsatisfiedLinkError Thrown if loading the native library failed
	 * @throws RuntimeException Thrown if registering the low-level mouse hook failed
	 */
	public GlobalMouseHook() throws UnsatisfiedLinkError {
		if(!libraryLoad) { LibraryLoader.loadLibrary("mousehook"); libraryLoad = true; }
		
		// register a mouse hook (throws a RuntimeException in case something goes wrong)
		mouseHook = new NativeMouseHook() {
			/**
			 * Handle the input transitionState create event and add it to the inputBuffer
			 */
			@Override public void handleMouse(int transitionState, int button, int x, int y, int delta) {
				inputBuffer.add(new GlobalMouseEvent(this, transitionState, button, buttons^=button, x, y, delta));			
			}
		};
		
		// start the event dispatcher after a successful hook
		eventDispatcher.start();
	}

	/**
	 * Adds a global mouse listener
	 * 
	 * @param listener The listener to add
	 */
	public void addMouseListener(GlobalMouseListener listener) { listeners.add(listener); }
	/**
	 * Removes a global mouse listener
	 * 
	 * @param listener The listener to remove
	 */
	public void removeMouseListener(GlobalMouseListener listener) { listeners.remove(listener); }

	/**
	 * Invoke mousePressed (transition state TS_DOWN) for all registered listeners
	 * 
	 * @param event A global mouse event
	 */
	private void mousePressed(GlobalMouseEvent event) {
		for(GlobalMouseListener listener:listeners)
			listener.mousePressed(event);
	}
	/**
	 * Invoke mouseReleased (transition state TS_UP) for all registered listeners
	 * 
	 * @param event A global mouse event
	 */
	private void mouseReleased(GlobalMouseEvent event) {
		for(GlobalMouseListener listener:listeners)
			listener.mouseReleased(event);
	}
	/**
	 * Invoke mouseMoved (transition state TS_MOVE) for all registered listeners
	 * 
	 * @param event A global mouse event
	 */
	private void mouseMoved(GlobalMouseEvent event) {
		for(GlobalMouseListener listener:listeners)
			listener.mouseMoved(event);
	}
	/**
	 * Invoke mouseWheel (transition state TS_WHEEL) for all registered listeners
	 * 
	 * @param event A global mouse event
	 */
	private void mouseWheel(GlobalMouseEvent event) {
		for(GlobalMouseListener listener:listeners)
			listener.mouseWheel(event);
	}

	/**
	 * Checks whether the mouse hook is still alive and capturing inputs
	 * 
	 * @return true if the mouse hook is alive
	 */
	public boolean isAlive() { return mouseHook!=null&&mouseHook.isAlive(); }
	/**
	 * Shutdown the mouse hook in case it is still alive.
	 * 
	 * This method does nothing if the hook already shut down and will block until shut down.
	 */
	public void shutdownHook() {
		if(isAlive()) {
			mouseHook.unregisterHook();
			try { mouseHook.join(); }
			catch(InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private abstract class NativeMouseHook extends Thread {
		private int status;
		
		public NativeMouseHook()  {
			super("Global Mouse Hook Thread");
			setDaemon(false); setPriority(MAX_PRIORITY);
			synchronized(this) {
				try { start(); wait(); }
				catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				
				if(status!=STATUS_SUCCESS)
					throw new RuntimeException("Low-level mouse hook failed ("+status+")");
			}
		}
		
		@Override public void run() {
			status = registerHook();
			synchronized(this) {
				notifyAll(); }
		}

		public native final int registerHook();
		public native final void unregisterHook();
		
		public abstract void handleMouse(int transitionState, int button, int x, int y, int delta);
	}
}