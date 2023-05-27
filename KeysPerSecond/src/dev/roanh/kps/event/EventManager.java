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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import dev.roanh.kps.event.listener.ButtonPressListener;
import dev.roanh.kps.event.listener.ButtonReleaseListener;
import dev.roanh.kps.event.listener.KeyPressListener;
import dev.roanh.kps.event.listener.KeyReleaseListener;

public class EventManager{
	private List<KeyPressListener> keyPressListeners = new CopyOnWriteArrayList<KeyPressListener>();
	private List<KeyReleaseListener> keyReleaseListeners = new CopyOnWriteArrayList<KeyReleaseListener>();
	private List<ButtonPressListener> buttonPressListeners = new CopyOnWriteArrayList<ButtonPressListener>();
	private List<ButtonReleaseListener> buttonReleaseListeners = new CopyOnWriteArrayList<ButtonReleaseListener>();
	
	
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
		// TODO Auto-generated method stub
		
	}



	public void fireButtonPressEvent(int button){
		// TODO Auto-generated method stub
		
	}
	
	
	public void subscribe(){
		
	}
	
	public void unsubscribe(){
		
	}


	public void registerInputSource(InputSource source){
		// TODO Auto-generated method stub
		
	}

	public void unregisterKeyPressListener(KeyPressListener listener){
		keyPressListeners.remove(listener);
	}
}
