package me.roan.kps;

import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * Abstract base class for the 
 * panels that display the
 * average, maximum and current
 * keys per second
 * @author Roan
 */
public abstract class BasePanel extends JPanel {
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 8816524158873355997L;
	/**
	 * Font 1 used to draw the title of the panel
	 */
	protected static Font font1;
	
	/**
	 * Constructs a new BasePanel
	 */
	protected BasePanel(){
		this.setOpaque(!ColorManager.transparency);
		this.addMouseListener(Listener.INSTANCE);
		this.addMouseMotionListener(Listener.INSTANCE);
	}

	@Override
	public void paintComponent(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, Main.config.getBackgroundOpacity()));
		g.setColor(Main.config.getBackgroundColor());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Main.config.getForegroundOpacity()));
		g.drawImage(ColorManager.unpressed, 2, 2, this.getWidth() - 2, this.getHeight() - 2, 0, 0, 40, 64, null);
		g.setColor(Main.config.getForegroundColor());
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setFont(font1);
		if(Main.config.mode == RenderingMode.VERTICAL){
			verticalRenderer(g);
		}else{
			horizontalRenderer(g);
		}
	}
	
	private final void horizontalRenderer(Graphics2D g){
		int baseline = (this.getHeight() / 2) - ((g.getFontMetrics().getAscent() + g.getFontMetrics().getDescent()) / 2) + g.getFontMetrics().getAscent();
		g.drawString(getTitle(), SizeManager.horizontalTextOffset, baseline);
		String str = getValue();
		if(str.length() >= 5){
			g.setFont(KeyPanel.font2smallest);
		}else if(str.length() >= 3){
			g.setFont(KeyPanel.font2small);
		}else{
			g.setFont(KeyPanel.font2);
		}
		g.drawString(str, this.getWidth() - SizeManager.horizontalTextOffset - g.getFontMetrics().stringWidth(str), ((this.getHeight() / 2) - ((g.getFontMetrics().getAscent() + g.getFontMetrics().getDescent()) / 2) + g.getFontMetrics().getAscent()) - baseline);
	}
	
	private final void verticalRenderer(Graphics2D g){
		g.drawString(getTitle(), (this.getWidth() - g.getFontMetrics().stringWidth(getTitle())) / 2, SizeManager.keyTitleTextOffset);
		String str = getValue();
		if(str.length() >= 5){
			g.setFont(KeyPanel.font2smallest);
		}else if(str.length() >= 4){
			g.setFont(KeyPanel.font2small);
		}else{
			g.setFont(KeyPanel.font2);
		}
		g.drawString(str, (this.getWidth() - g.getFontMetrics().stringWidth(str)) / 2, SizeManager.keyDataTextOffset);
	}

	/**
	 * @return The title of this panel
	 */
	protected abstract String getTitle();
	
	/**
	 * @return The value for this panel
	 */
	protected abstract String getValue();
}
