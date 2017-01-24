package me.roan.kps;

/**
 * Panel used to display the
 * current keys pressed per second<br>
 * However since the actual 'current'
 * time frame is still on going this
 * actually displays the keys per second
 * from the previous second
 * @author Roan
 */
public final class NowPanel extends BasePanel {
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 8816524158873355997L;

	@Override
	protected String getTitle() {
		return "CUR";
	}

	@Override
	protected String getValue() {
		return String.valueOf(Main.prev);
	}
}