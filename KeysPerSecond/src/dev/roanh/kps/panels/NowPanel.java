package dev.roanh.kps.panels;

import dev.roanh.kps.Main;
import dev.roanh.kps.RenderingMode;

/**
 * Panel used to display the
 * current keys pressed per second<br>
 * However since the actual 'current'
 * time frame is still on going this
 * actually displays the keys per second
 * from the previous second
 * @author Roan
 */
public final class NowPanel extends BasePanel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 8816524158873355997L;
	/**
	 * Static instance of this panel that is reused all the time
	 */
	public static final NowPanel INSTANCE = new NowPanel();

	/**
	 * Constructs a new current panel
	 */
	private NowPanel(){
		sizeChanged();
	}

	@Override
	protected String getTitle(){
		return "CUR";
	}

	@Override
	protected String getValue(){
		return String.valueOf(Main.prev);
	}

	@Override
	public int getLayoutX(){
		return Main.config.cur_x;
	}

	@Override
	public int getLayoutY(){
		return Main.config.cur_y;
	}

	@Override
	public int getLayoutWidth(){
		return Main.config.cur_w;
	}

	@Override
	public int getLayoutHeight(){
		return Main.config.cur_h;
	}

	@Override
	protected RenderingMode getRenderingMode(){
		return Main.config.cur_mode;
	}
}