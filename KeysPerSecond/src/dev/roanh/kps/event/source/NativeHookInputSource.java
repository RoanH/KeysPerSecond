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
package dev.roanh.kps.event.source;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseMotionListener;

import dev.roanh.kps.event.EventManager;
import dev.roanh.kps.event.InputSource;

/**
 * Input source of key and mouse event as reported by JNativeHook.
 * @author Roan
 */
public class NativeHookInputSource extends InputSource implements NativeKeyListener, NativeMouseListener, NativeMouseMotionListener{

	/**
	 * Constructs a new JNativeHook input source.
	 * @param manager The event manager to report to.
	 * @throws NativeHookException When some exception occurs.
	 */
	public NativeHookInputSource(EventManager manager) throws NativeHookException{
		super(manager);
		
		//Make sure the native hook is always unregistered
		Runtime.getRuntime().addShutdownHook(new Thread(this::cleanup));
		
		//Initialise native library
		System.setProperty("jnativehook.lib.path", System.getProperty("java.io.tmpdir"));
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.WARNING);
		logger.setUseParentHandlers(false);
		GlobalScreen.registerNativeHook();
		
		GlobalScreen.addNativeKeyListener(this);
		GlobalScreen.addNativeMouseListener(this);
		GlobalScreen.addNativeMouseMotionListener(this);
	}
	
	/**
	 * Cleans up all resources used by JNativeHook.
	 */
	private void cleanup(){
		try{
			GlobalScreen.unregisterNativeHook();
		}catch(NativeHookException e1){
			e1.printStackTrace();
		}
	}
	
	@Override
	public void nativeKeyPressed(NativeKeyEvent event){
		manager.fireKeyPressEvent(event.getKeyCode());
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent event){
		manager.fireKeyReleaseEvent(event.getKeyCode());
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent event){
	}
			
	@Override
	public void nativeMouseClicked(NativeMouseEvent event){
	}

	@Override
	public void nativeMousePressed(NativeMouseEvent event){
		manager.fireButtonPressEvent(event.getButton());
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent event){
		manager.fireButtonReleaseEvent(event.getButton());
	}
	
	@Override
	public void nativeMouseMoved(NativeMouseEvent event){
		manager.fireMouseMoveEvent(event.getX(), event.getY());
	}
	
	@Override
	public void nativeMouseDragged(NativeMouseEvent event){
		manager.fireMouseMoveEvent(event.getX(), event.getY());
	}
}
