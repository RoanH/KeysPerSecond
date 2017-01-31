package me.roan.kps.input;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyListener implements NativeKeyListener {
    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println("Key Pressed: " + e.getKeyCode());

        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
				GlobalScreen.unregisterNativeHook();
			} catch (NativeHookException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        System.out.println("Key Released: " + e.getKeyCode());
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        System.out.println("Key Typed: " + e.getKeyCode());
    }

    public static void main(String[] args) {
        try {
        	// Get the logger for "org.jnativehook" and set the level to warning.
        	Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        	logger.setLevel(Level.WARNING);

        	// Don't forget to disable the parent handlers.
        	logger.setUseParentHandlers(false);
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(new KeyListener());
    }
}