package me.roan.mousegraph;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

public class Main {
	
	protected static final LinkedList<TimePoint> path = new LinkedList<TimePoint>();
	private static ScheduledExecutorService timer;
	private static Future<?> future;
	private static final JFrame frame = new JFrame("Mouse Graph");
	private static final Graph graph = new Graph();

	public static void main(String[] args){
		
		setupNativeHook();
		
		frame.add(graph);
		frame.setUndecorated(true);
		
		frame.setSize(160, 90);
		frame.setVisible(true);
		
		mainLoop();
	}
	
	/**
	 * Main loop of the program
	 */
	protected static final void mainLoop(){
		if(timer == null){
			timer = Executors.newSingleThreadScheduledExecutor();
		}else{
			future.cancel(false);
		}
		future = timer.scheduleAtFixedRate(()->{
			if(true){
				System.out.println("loop: " + path.size());
				long time = System.currentTimeMillis();
				while(true){
					if(path.size() > 0 && time - path.getLast().time > 10000){
						path.removeLast();
						System.out.println("remove");
					}else{
						System.out.println("keep");
						break;
					}
				}
				System.out.println("loop end");
				graph.repaint();
			}
		}, 0, 100L, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Registers the native libraries and
	 * registers event handlers for mouse events
	 */
	private static final void setupNativeHook(){
		try {
			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			logger.setLevel(Level.WARNING);
			logger.setUseParentHandlers(false);
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			JOptionPane.showMessageDialog(null, "There was a problem registering the native hook: " + ex.getMessage(), "Mouse Graph", JOptionPane.ERROR_MESSAGE);
			try {
				GlobalScreen.unregisterNativeHook();
			} catch (NativeHookException e1) {
				e1.printStackTrace();
			}
			System.exit(1);
		}
		GlobalScreen.addNativeMouseMotionListener(new NativeMouseInputListener(){

			@Override
			public void nativeMouseDragged(NativeMouseEvent arg0) {
				path.addFirst(new TimePoint(arg0.getX(), arg0.getY(), System.currentTimeMillis()));
			}

			@Override
			public void nativeMouseMoved(NativeMouseEvent arg0) {
				path.addFirst(new TimePoint(arg0.getX(), arg0.getY(), System.currentTimeMillis()));
			}

			@Override
			public void nativeMouseClicked(NativeMouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void nativeMousePressed(NativeMouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void nativeMouseReleased(NativeMouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
