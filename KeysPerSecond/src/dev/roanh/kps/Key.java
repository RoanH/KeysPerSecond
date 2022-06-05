package dev.roanh.kps;

import java.io.Serializable;

import dev.roanh.kps.panels.KeyPanel;

/**
 * This class is used to keep track
 * of how many times a specific key
 * is pressed
 * @author Roan
 */
public class Key{
	/**
	 * Whether or not this key is currently pressed
	 */
	public transient boolean down = false;
	/**
	 * The total number of times this key has been pressed
	 */
	public int count = 0;
	/**
	 * The key in string form<br>
	 * For example: X
	 */
	public String name;
	/**
	 * The graphical display for this key
	 */
	private transient KeyPanel panel = null;
	/**
	 * Whether or not alt has to be down
	 */
	protected boolean alt;
	/**
	 * Whether or not ctrl has to be down
	 */
	protected boolean ctrl;
	/**
	 * Whether or not shift has to be down
	 */
	protected boolean shift;
	
	protected Key(String name, int count, boolean alt, boolean ctrl, boolean shift){
		this(name);
		this.count = count;
		this.alt = alt;
		this.ctrl = ctrl;
		this.shift = shift;
	}

	/**
	 * Constructs a new Key object
	 * for the key with the given
	 * name
	 * @param name The name of the key
	 * @see #name
	 */
	protected Key(String name){
		this.name = name;
	}

	/**
	 * Creates a new KeyPanel with this
	 * objects as its data source
	 * @param i The information object for this key
	 * @return A new KeyPanel
	 */
	protected KeyPanel getPanel(KeyInformation i){
		return panel != null ? panel : (panel = new KeyPanel(this, i));
	}

	/**
	 * Called when a key is pressed
	 */
	protected void keyPressed(){
		if(!down){
			count++;
			down = true;
			Main.tmp.incrementAndGet();
			if(panel != null){
				panel.repaint();
			}
		}
	}

	/**
	 * Called when a key is released
	 */
	protected void keyReleased(){
		if(down){
			down = false;
			if(panel != null){
				panel.repaint();
			}
		}
	}
}
