package me.roan.kps.panels;

import me.roan.kps.Main;
import me.roan.kps.Main.Key;
import me.roan.kps.Main.KeyInformation;

/**
 * Panel to display the number
 * of times a certain key has 
 * been pressed
 * @author Roan
 */
public final class KeyPanel extends BasePanel {
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
	 * @see Key
	 * @see #key
	 */
	public KeyPanel(Key key, KeyInformation i) {
		this.key = key;
		info = i;
	}

	@Override
	protected boolean isActive() {
		return key.down;
	}

	@Override
	protected String getTitle() {
		return key.name;
	}

	@Override
	protected String getValue() {
		return String.valueOf(key.count);
	}

	@Override
	public int getLayoutX() {
		return info.x;
	}

	@Override
	public int getLayoutY() {
		return info.y;
	}

	@Override
	public int getLayoutWidth() {
		return info.width;
	}

	@Override
	public int getLayoutHeight() {
		return info.height;
	}
}