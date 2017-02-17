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
	protected static final Font font1 = new Font("Dialog", Font.BOLD, 15);
	
	/**
	 * Constructs a new BasePanel
	 */
	protected BasePanel(){
		this.setOpaque(!ColorManager.transparency);
	}

	@Override
	public void paintComponent(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		if(ColorManager.transparency){
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ColorManager.opacityfg));
		}
		g.drawImage(ColorManager.unpressed, 2, 2, null);
		g.setColor(ColorManager.foreground);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setFont(font1);
		g.drawString(getTitle(), (this.getWidth() - g.getFontMetrics().stringWidth(getTitle())) / 2, 30);
		String str = getValue();
		if(str.length() >= 5){
			g.setFont(KeyPanel.font2smallest);
		}else if(str.length() >= 4){
			g.setFont(KeyPanel.font2small);
		}else{
			g.setFont(KeyPanel.font2);
		}
		g.drawString(str, (this.getWidth() - g.getFontMetrics().stringWidth(str)) / 2, 55);
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
