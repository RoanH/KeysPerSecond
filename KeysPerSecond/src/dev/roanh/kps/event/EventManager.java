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
package dev.roanh.kps.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import dev.roanh.kps.event.listener.ButtonPressListener;
import dev.roanh.kps.event.listener.ButtonReleaseListener;
import dev.roanh.kps.event.listener.KeyPressListener;
import dev.roanh.kps.event.listener.KeyReleaseListener;
import dev.roanh.kps.event.listener.MouseMoveListener;

/**
 * Class responsible for managing input events.
 * @author Roan
 */
public class EventManager{
	/**
	 * List of registered key press listeners.
	 */
	private List<KeyPressListener> keyPressListeners = new CopyOnWriteArrayList<KeyPressListener>();
	/**
	 * List of registered key release listeners.
	 */
	private List<KeyReleaseListener> keyReleaseListeners = new CopyOnWriteArrayList<KeyReleaseListener>();
	/**
	 * List of registered mouse button press listeners.
	 */
	private List<ButtonPressListener> buttonPressListeners = new CopyOnWriteArrayList<ButtonPressListener>();
	/**
	 * List of registered mouse button release listeners.
	 */
	private List<ButtonReleaseListener> buttonReleaseListeners = new CopyOnWriteArrayList<ButtonReleaseListener>();
	/**
	 * List of registered mouse move listeners.
	 */
	private List<MouseMoveListener> mouseMoveListeners = new CopyOnWriteArrayList<MouseMoveListener>();
	/**
	 * List of input sources providing input to this event manager.
	 */
	private List<InputSource> sources = new ArrayList<InputSource>();
	
	/**
	 * Registers a new key press listener to this manager.
	 * @param listener The listener to register.
	 */
	public void registerKeyPressListener(KeyPressListener listener){
		keyPressListeners.add(listener);
	}
	
	/**
	 * Registers a new key release listener to this manager.
	 * @param listener The listener to register.
	 */
	public void registerKeyReleaseListener(KeyReleaseListener listener){
		keyReleaseListeners.add(listener);
	}
	
	/**
	 * Registers a new mouse button press listener to this manager.
	 * @param listener The listener to register.
	 */
	public void registerButtonPressListener(ButtonPressListener listener){
		buttonPressListeners.add(listener);
	}
	
	/**
	 * Registers a new mouse button release listener to this manager.
	 * @param listener The listener to register.
	 */
	public void registerButtonReleaseListener(ButtonReleaseListener listener){
		buttonReleaseListeners.add(listener);
	}
	
	/**
	 * Registers a new mouse move listener to this manager.
	 * @param listener The listener to register.
	 */
	public void registerMouseMoveListener(MouseMoveListener listener){
		mouseMoveListeners.add(listener);
	}
	
	/**
	 * Fires a new press event for the key with the given key code.
	 * @param code The key code of the key that was pressed.
	 */
	public void fireKeyPressEvent(int code){
		for(KeyPressListener listener : keyPressListeners){
			listener.onKeyPress(code);
		}
	}
	
	/**
	 * Fires a new release event for the key with the given key code.
	 * @param code The key code of the key that was release.
	 */
	public void fireKeyReleaseEvent(int code){
		for(KeyReleaseListener listener : keyReleaseListeners){
			listener.onKeyRelease(code);
		}
	}
	
	/**
	 * Fires a new release event for the button with the given ID.
	 * @param button The ID of the button that was released.
	 */
	public void fireButtonReleaseEvent(int button){
		for(ButtonReleaseListener listener : buttonReleaseListeners){
			listener.onButtonRelease(button);
		}
	}

	/**
	 * Fires a new press event for the button with the given ID.
	 * @param button The ID of the button that was pressed.
	 */
	public void fireButtonPressEvent(int button){
		for(ButtonPressListener listener : buttonPressListeners){
			listener.onButtonPress(button);
		}
	}
	
	/**
	 * Fires a new mouse move event for the given target coordinate.
	 * @param x The x coordinate the cursor moved to.
	 * @param y The y coordinate the cursor moved to.
	 */
	public void fireMouseMoveEvent(int x, int y){
		for(MouseMoveListener listener : mouseMoveListeners){
			listener.onMouseMove(x, y);
		}
	}

	/**
	 * Registers a new input source to this event manager.
	 * @param source The input source to register.
	 */
	public void registerInputSource(InputSource source){
		sources.add(source);
	}

	/**
	 * Deregisters a key press listener from this manager.
	 * @param listener The listener to deregister.
	 */
	public void deregisterKeyPressListener(KeyPressListener listener){
		keyPressListeners.remove(listener);
	}

	/**
	 * Deregisters a button press listener from this manager.
	 * @param listener The listener to deregister.
	 */
	public void deregisterButtonPressListener(ButtonPressListener listener){
		buttonPressListeners.remove(listener);
	}

	/**
	 * Deregisters a key release listener from this manager.
	 * @param listener The listener to deregister.
	 */
	public void deregisterKeyReleaseListener(KeyReleaseListener listener){
		keyReleaseListeners.remove(listener);
	}

	/**
	 * Deregisters a button release listener from this manager.
	 * @param listener The listener to deregister.
	 */
	public void deregisterButtonReleaseListener(ButtonReleaseListener listener){
		buttonReleaseListeners.remove(listener);
	}
	
	/**
	 * Deregisters a mouse move listener from this manager.
	 * @param listener The listener to deregister.
	 */
	public void deregisterMouseMoveListener(MouseMoveListener listener){
		mouseMoveListeners.remove(listener);
	}
}
