package me.roan.kps.panels;

import me.roan.kps.Key;
import me.roan.kps.KeyInformation;
import me.roan.kps.RenderingMode;

/**
 * Panel to display the number
 * of times a certain key has 
 * been pressed
 * @author Roan
 */
public final class KeyPanel extends BasePanel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 8816524158873355997L;
	/**
	 * The key object associated with this panel<br>
	 * This key object keep track of the amount of this
	 * the assigned key has been hit
	 */
	private Key key;
	/**
	 * The key information object
	 * for this key
	 */
	protected KeyInformation info;

	/**
	 * Constructs a new KeyPanel
	 * with the given key object
	 * @param key The key object to
	 *        associate this panel with
	 * @param i The key information object
	 *        for the key for this panel
	 * @see Key
	 * @see #key
	 */
	public KeyPanel(Key key, KeyInformation i){
		this.key = key;
		info = i;
	}

	@Override
	protected boolean isActive(){
		return key.down;
	}

	@Override
	protected String getTitle(){
		return key.name;
	}

	@Override
	protected String getValue(){
		return String.valueOf(key.count);
	}

	@Override
	public int getLayoutX(){
		return info.getX();
	}

	@Override
	public int getLayoutY(){
		return info.getY();
	}

	@Override
	public int getLayoutWidth(){
		return info.getWidth();
	}

	@Override
	public int getLayoutHeight(){
		return info.getHeight();
	}

	@Override
	protected RenderingMode getRenderingMode(){
		return info.getRenderingMode();
	}
}