package dev.roanh.kps.panels;

import dev.roanh.kps.Main;
import dev.roanh.kps.RenderingMode;

/**
 * Panel used to display the
 * total number of keys pressed
 * @author Roan
 */
public final class TotPanel extends BasePanel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 5628759695450014071L;
	/**
	 * Total number of hits
	 */
	public static int hits;
	/**
	 * Static instance of this panel that is reused all the time
	 */
	public static final TotPanel INSTANCE = new TotPanel();

	/**
	 * Constructs a new total panel
	 */
	private TotPanel(){
		sizeChanged();
	}

	@Override
	protected String getTitle(){
		return "TOT";
	}

	@Override
	protected String getValue(){
		return String.valueOf(hits);
	}

	@Override
	public int getLayoutX(){
		return Main.config.tot_x;
	}

	@Override
	public int getLayoutY(){
		return Main.config.tot_y;
	}

	@Override
	public int getLayoutWidth(){
		return Main.config.tot_w;
	}

	@Override
	public int getLayoutHeight(){
		return Main.config.tot_h;
	}

	@Override
	protected RenderingMode getRenderingMode(){
		return Main.config.tot_mode;
	}
}