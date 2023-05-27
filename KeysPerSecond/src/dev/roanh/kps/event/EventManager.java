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
	 * Registers a new input source to this event manager.
	 * @param source The input soruce to register.
	 */
	public void registerInputSource(InputSource source){
		sources.add(source);
	}

	/**
	 * Unregisters a key press listener from this manager.
	 * @param listener The listener to unregister.
	 */
	public void unregisterKeyPressListener(KeyPressListener listener){
		keyPressListeners.remove(listener);
	}

	/**
	 * Unregisters a button press listener from this manager.
	 * @param listener The listener to unregister.
	 */
	public void unregisterButtonPressListener(ButtonPressListener listener){
		buttonPressListeners.remove(listener);
	}

	/**
	 * Unregisters a key release listener from this manager.
	 * @param listener The listener to unregister.
	 */
	public void unregisterKeyReleaseListener(KeyReleaseListener listener){
		keyReleaseListeners.remove(listener);
	}

	/**
	 * Unregisters a button release listener from this manager.
	 * @param listener The listener to unregister.
	 */
	public void unregisterButtonReleaseListener(ButtonReleaseListener listener){
		buttonReleaseListeners.remove(listener);
	}
}
