package me.roan.kps;

import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import me.roan.kps.Main.Key;
import me.roan.kps.Main.KeyInformation;

/**
 * Panel to display the number
 * of times a certain key has 
 * been pressed
 * @author Roan
 */
public final class KeyPanel extends JPanel implements LayoutPosition{
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
	 * Font 1 used to display the title of the panel
	 */
	protected static Font font1;
	/**
	 * Font 2 used to display the value of this panel
	 */
	protected static Font font2;
	/**
	 * Font 2 but smaller
	 */
	protected static Font font2small;
	/**
	 * Font 2 small but smaller
	 */
	protected static Font font2smallest;
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
	protected KeyPanel(Key key, KeyInformation i) {
		this.key = key;
		info = i;
		this.setOpaque(!ColorManager.transparency);
	}

	@Override
	public void paintComponent(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		if(Main.config.mode == RenderingMode.VERTICAL){
			verticalRenderer(g);
		}else{
			horizontalRenderer(g);
		}
	}
	
	private final void horizontalRenderer(Graphics2D g){
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, Main.config.getBackgroundOpacity()));
		g.setColor(Main.config.getBackgroundColor());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Main.config.getForegroundOpacity()));
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.drawImage(ColorManager.unpressed, 2, 2, this.getWidth() - 2, this.getHeight() - 2, 0, 0, 64, 40, this);
		if (key.down) {
			g.drawImage(ColorManager.pressed, 2, 2, this.getWidth() - 2, this.getHeight() - 2, 0, 0, 64, 40, this);
			g.setColor(Main.config.getBackgroundColor());
		}else{
			g.setColor(Main.config.getForegroundColor());
		}
		Font nameFont = key.name.length() == 1 ? font1 : BasePanel.font1;
		int baseline = (this.getHeight() / 2) - ((g.getFontMetrics(nameFont).getAscent() + g.getFontMetrics(nameFont).getDescent()) / 2) + g.getFontMetrics(nameFont).getAscent();
		if(Main.config.mode == RenderingMode.HORIZONTAL_NT){
			if(key.count >= 10000){
				g.setFont(font2smallest);
			}else if(key.count >= 1000){
				g.setFont(font2small);
			}else{
				g.setFont(font2);
			}
			String str = String.valueOf(key.count);
			g.drawString(str, SizeManager.horizontalTextOffset, baseline);
			g.setFont(nameFont);
			g.drawString(key.name, this.getWidth() - SizeManager.horizontalTextOffset - g.getFontMetrics().stringWidth(key.name), baseline);
		}else{
			g.setFont(nameFont);
			g.drawString(key.name, SizeManager.horizontalTextOffset, baseline);
			if(key.count >= 10000){
				g.setFont(font2smallest);
			}else if(key.count >= 1000){
				g.setFont(font2small);
			}else{
				g.setFont(font2);
			}
			String str = String.valueOf(key.count);
			g.drawString(str, this.getWidth() - SizeManager.horizontalTextOffset - g.getFontMetrics().stringWidth(str), baseline);
		}
	}
	
	private final void verticalRenderer(Graphics2D g){
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, Main.config.getBackgroundOpacity()));
		g.setColor(Main.config.getBackgroundColor());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Main.config.getForegroundOpacity()));
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.drawImage(ColorManager.unpressed, 2, 2, this.getWidth() - 2, this.getHeight() - 2, 0, 0, 40, 64, this);
		if (key.down) {
			g.drawImage(ColorManager.pressed, 2, 2, this.getWidth() - 2, this.getHeight() - 2, 0, 0, 40, 64, this);
			g.setColor(Main.config.getBackgroundColor());
		}else{
			g.setColor(Main.config.getForegroundColor());
		}
		if(key.name.length() == 1){
			g.setFont(font1);
		}else{
			g.setFont(BasePanel.font1);
		}
		g.drawString(key.name, (this.getWidth() - g.getFontMetrics().stringWidth(key.name)) / 2, SizeManager.keyTitleTextOffset);
		if(key.count >= 10000){
			g.setFont(font2smallest);
		}else if(key.count >= 1000){
			g.setFont(font2small);
		}else{
			g.setFont(font2);
		}
		String str = String.valueOf(key.count);
		g.drawString(str, (this.getWidth() - g.getFontMetrics().stringWidth(str)) / 2, SizeManager.keyDataTextOffset);
	}

	@Override
	public int getIndex() {
		return info.index;
	}
}