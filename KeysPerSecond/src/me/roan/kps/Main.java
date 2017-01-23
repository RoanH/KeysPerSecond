package me.roan.kps;

import java.io.File;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

public class Main {
	
	static long last = System.currentTimeMillis();
	static long n = 0;
	static int tmp = 0;
	static double avg;
	static int max;

	public static void main(String[] args) {
        // might throw a UnsatisfiedLinkError if the native library fails to load or a RuntimeException if hooking fails 
		System.load(new File("keyboardhook-windows-amd64.dll").getAbsolutePath());
		
		GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();
		

        keyboardHook.addKeyListener(new GlobalKeyAdapter() {
        	boolean x = false;
        	boolean y = false;
            @Override public void keyPressed(GlobalKeyEvent event) {
            	if(event.getVirtualKeyCode()==GlobalKeyEvent.VK_X && !x){
            		x = true;
            		tmp++;
            	}
            	if(event.getVirtualKeyCode()==GlobalKeyEvent.VK_Y && !y){
            		y = true;
            		tmp++;
            	}
                if(event.getVirtualKeyCode()==GlobalKeyEvent.VK_P){
                	System.out.println("max: " + max + " avg: " + avg);
                	n = 0;
                	avg = 0;
                	max = 0;
                	tmp = 0;
                }
            }
            @Override
            public void keyReleased(GlobalKeyEvent event){
            	if(event.getVirtualKeyCode()==GlobalKeyEvent.VK_X){
            		x = false;
            	}
            	if(event.getVirtualKeyCode()==GlobalKeyEvent.VK_Y){
            		y = false;
            	}
            }
        });

        try {
            while(true){
            	if(System.currentTimeMillis() - last >= 1000){
            		last = System.currentTimeMillis();
            		if(tmp > max){
            			max = tmp;
            		}
               		avg = (avg * (double)n + (double)tmp) / ((double)n + 1.0D);
            		n++;
            		System.out.println(tmp);
            		tmp = 0;
            	}
            }
        }
          finally { keyboardHook.shutdownHook(); }
    }
}
