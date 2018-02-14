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
	
	@Override
	public int getLayoutX() {
		return 0;
	}

	@Override
	public int getLayoutY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLayoutWidth() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getLayoutHeight() {
		// TODO Auto-generated method stub
		return 1;
	}
}