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
	private List<KeyPressListener> keyPressListeners = new CopyOnWriteArrayList<KeyPressListener>();
	private List<KeyReleaseListener> keyReleaseListeners = new CopyOnWriteArrayList<KeyReleaseListener>();
	private List<ButtonPressListener> buttonPressListeners = new CopyOnWriteArrayList<ButtonPressListener>();
	private List<ButtonReleaseListener> buttonReleaseListeners = new CopyOnWriteArrayList<ButtonReleaseListener>();
	private List<InputSource> sources = new ArrayList<InputSource>();
	
	public void registerKeyPressListener(KeyPressListener listener){
		keyPressListeners.add(listener);
	}
	
	public void registerKeyReleaseListener(KeyReleaseListener listener){
		keyReleaseListeners.add(listener);
	}
	
	public void registerButtonPressListener(ButtonPressListener listener){
		buttonPressListeners.add(listener);
	}
	
	public void registerButtonReleaseListener(ButtonReleaseListener listener){
		buttonReleaseListeners.add(listener);
	}
	
	public void fireKeyPressEvent(int code){
		for(KeyPressListener listener : keyPressListeners){
			listener.onKeyPress(code);
		}
	}
	
	public void fireKeyReleaseEvent(int code){
		for(KeyReleaseListener listener : keyReleaseListeners){
			listener.onKeyRelease(code);
		}
	}
	
	public void fireButtonReleaseEvent(int button){
		for(ButtonReleaseListener listener : buttonReleaseListeners){
			listener.onButtonRelease(button);
		}
	}

	public void fireButtonPressEvent(int button){
		for(ButtonPressListener listener : buttonPressListeners){
			listener.onButtonPress(button);
		}
	}

	public void registerInputSource(InputSource source){
		sources.add(source);
	}

	public void unregisterKeyPressListener(KeyPressListener listener){
		keyPressListeners.remove(listener);
	}

	public void unregisterButtonPressListener(ButtonPressListener listener){
		buttonPressListeners.remove(listener);
	}

	public void unregisterKeyReleaseListener(KeyReleaseListener listener){
		keyReleaseListeners.remove(listener);
	}

	public void unregisterButtonReleaseListener(ButtonReleaseListener listener){
		buttonReleaseListeners.remove(listener);
	}
}
