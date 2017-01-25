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
package lc.kra.system.keyboard;

import static lc.kra.system.keyboard.event.GlobalKeyEvent.TS_DOWN;
import static lc.kra.system.keyboard.event.GlobalKeyEvent.VK_CONTROL;
import static lc.kra.system.keyboard.event.GlobalKeyEvent.VK_LCONTROL;
import static lc.kra.system.keyboard.event.GlobalKeyEvent.VK_LMENU;
import static lc.kra.system.keyboard.event.GlobalKeyEvent.VK_LSHIFT;
import static lc.kra.system.keyboard.event.GlobalKeyEvent.VK_MENU;
import static lc.kra.system.keyboard.event.GlobalKeyEvent.VK_RCONTROL;
import static lc.kra.system.keyboard.event.GlobalKeyEvent.VK_RMENU;
import static lc.kra.system.keyboard.event.GlobalKeyEvent.VK_RSHIFT;
import static lc.kra.system.keyboard.event.GlobalKeyEvent.VK_RWIN;
import static lc.kra.system.keyboard.event.GlobalKeyEvent.VK_SHIFT;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JOptionPane;

import lc.kra.system.LibraryLoader;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import lc.kra.system.keyboard.event.GlobalKeyListener;

public class GlobalKeyboardHook {
	private static final int STATUS_SUCCESS = 0;
	
	private NativeKeyboardHook keyboardHook;
	
	private BlockingQueue<GlobalKeyEvent> inputBuffer =
		new LinkedBlockingQueue<GlobalKeyEvent>();
	private boolean libraryLoad, menuPressed, shiftPressed, controlPressed, extendedKey;
	
	private List<GlobalKeyListener> listeners = new CopyOnWriteArrayList<GlobalKeyListener>();
	private Thread eventDispatcher = new Thread() {{
			setName("Global Keyboard Hook Dispatcher");
			setDaemon(true);
		}
		
		public void run() {
			try {
				// while the global keyboard hook is alive, try to take events and dispatch them
				while(GlobalKeyboardHook.this.isAlive()) {
					GlobalKeyEvent event = inputBuffer.take();
					if(event.getTransitionState()==TS_DOWN)
					     keyPressed(event);
					else keyReleased(event);
				}
			} catch(InterruptedException e) { /* thread got interrupted, break */ }
		}
	};
	
	/**
	 * Instantiate a new GlobalKeyboardHook.
	 * 
	 * The constructor first tries to load the native library. On failure a {@link UnsatisfiedLinkError}
	 * is thrown. Afterwards the native keyboard hook is initialized. A {@link RuntimeException} is raised
	 * in case the hook could not be established.
	 * 
	 * Two separate threads are started by the class. The HookThread and a separate EventDispatcherThread.
	 * 
	 * @throws UnsatisfiedLinkError Thrown if loading the native library failed
	 * @throws RuntimeException Thrown if registering the low-level keyboard hook failed
	 */
	public GlobalKeyboardHook() throws UnsatisfiedLinkError {
		if(!libraryLoad) { LibraryLoader.loadLibrary("keyboardhook"); libraryLoad = true; }
		
		// register a keyboard hook (throws a RuntimeException in case something goes wrong)
		keyboardHook = new NativeKeyboardHook() {
			/**
			 * Handle the input virtualKeyCode and transitionState, create event and add it to the inputBuffer
			 */
			@Override public void handleKey(int virtualKeyCode, int transitionState, char keyChar) {
				switchControlKeys(virtualKeyCode, transitionState);
				inputBuffer.add(new GlobalKeyEvent(this, virtualKeyCode, transitionState, keyChar, menuPressed, shiftPressed, controlPressed, extendedKey));			
			}
		};
		
		// start the event dispatcher after a successful hook
		eventDispatcher.start();
	}

	/**
	 * Adds a global key listener
	 * 
	 * @param listener The listener to add
	 */
	public void addKeyListener(GlobalKeyListener listener) { listeners.add(listener); }
	/**
	 * Removes a global key listener
	 * 
	 * @param listener The listener to remove
	 */
	public void removeKeyListener(GlobalKeyListener listener) { listeners.remove(listener); }

	/**
	 * Invoke keyPressed (transition state TS_DOWN) for all registered listeners
	 * 
	 * @param event A global key event
	 */
	private void keyPressed(GlobalKeyEvent event) {
		for(GlobalKeyListener listener:listeners)
			listener.keyPressed(event);
	}
	/**
	 * Invoke keyReleased (transition state TS_UP) for all registered listeners
	 * 
	 * @param event A global key event
	 */
	private void keyReleased(GlobalKeyEvent event) {
		for(GlobalKeyListener listener:listeners)
			listener.keyReleased(event);
	}

	/**
	 * Checks whether the keyboard hook is still alive and capturing inputs
	 * 
	 * @return true if the keyboard hook is alive
	 */
	public boolean isAlive() { return keyboardHook!=null&&keyboardHook.isAlive(); }
	/**
	 * Shutdown the keyboard hook in case it is still alive.
	 * 
	 * This method does nothing if the hook already shut down and will block until shut down.
	 */
	public void shutdownHook() {
		if(isAlive()) {
			keyboardHook.unregisterHook();
			try { keyboardHook.join(); }
			catch(InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private abstract class NativeKeyboardHook extends Thread {
		private int status;
		
		public NativeKeyboardHook()  {
			super("Global Keyboard Hook Thread");
			setDaemon(false); setPriority(MAX_PRIORITY);
			synchronized(this) {
				try { start(); wait(); }
				catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				
				if(status!=STATUS_SUCCESS)
					throw new RuntimeException("Low-level keyboard hook failed ("+status+")");
			}
		}
		
		@Override public void run() {
			try{	
				status = registerHook();
			}catch(UnsatisfiedLinkError e){
				JOptionPane.showMessageDialog(null, "Failed to load native library >.<", "Keys per second", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(0);
			}
			synchronized(this) {
				notifyAll(); }
		}

		public native final int registerHook();
		public native final void unregisterHook();
		
		public abstract void handleKey(int virtualKeyCode, int transitionState, char keyChar);
	}
	
	/**
	 * Switch control states for menu/shift/control
	 */
	private void switchControlKeys(int virtualKeyCode, int transitionState) {
		boolean downTransition = transitionState==TS_DOWN;
		switch(virtualKeyCode) {
		case VK_RWIN: extendedKey = downTransition; break;
		case VK_RMENU: extendedKey = downTransition;
		case VK_MENU: case VK_LMENU:
			menuPressed = downTransition;
			break;
		case VK_RSHIFT: extendedKey = downTransition;
		case VK_SHIFT: case VK_LSHIFT:
			shiftPressed = downTransition;
			break;
		case VK_RCONTROL: extendedKey = downTransition;
		case VK_CONTROL: case VK_LCONTROL:
			controlPressed = downTransition;
			break;
		}
	}
}