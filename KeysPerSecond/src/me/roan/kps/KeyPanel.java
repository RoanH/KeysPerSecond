package me.roan.kps;

import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import me.roan.kps.Main.Key;

/**
 * Panel to display the number
 * of times a certain key has 
 * been pressed
 * @author Roan
 */
public final class KeyPanel extends JPanel {
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
	private static final Font font1 = new Font("Dialog", Font.BOLD, SizeManager.keyPanelFont1Size);
	/**
	 * Font 2 used to display the value of this panel
	 */
	protected static final Font font2 = new Font("Dialog", Font.PLAIN, SizeManager.keyPanelFont2Size);
	/**
	 * Font 2 but smaller
	 */
	protected static final Font font2small = new Font("Dialog", Font.PLAIN, SizeManager.keyPanelFont2smallSize);
	/**
	 * Font 2 small but smaller
	 */
	protected static final Font font2smallest = new Font("Dialog", Font.PLAIN, SizeManager.keyPanelFont2smallestSize);

	/**
	 * Constructs a new KeyPanel
	 * with the given key object
	 * @param key The key object to
	 *        associate this panel with
	 * @see Key
	 * @see #key
	 */
	protected KeyPanel(Key key) {
		this.key = key;
		this.setOpaque(!ColorManager.transparency);
		this.addMouseListener(Listener.INSTANCE);
		this.addMouseMotionListener(Listener.INSTANCE);
	}

	@Override
	public void paintComponent(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		if(ColorManager.transparency){
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Main.config.opacitybg));
			g.setColor(ColorManager.transparent);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		if(Main.config.opacitybg == 1.0F){
			g.setColor(Main.config.background);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		if(ColorManager.transparency){
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Main.config.opacityfg));
		}
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.drawImage(ColorManager.unpressed, 2, 2, this.getWidth() - 2, this.getHeight() - 2, 0, 0, 40, 64, this);
		if (key.down) {
			g.drawImage(ColorManager.pressed, 2, 2, this.getWidth() - 2, this.getHeight() - 2, 0, 0, 40, 64, this);
		}else{
			g.setColor(Main.config.foreground);
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
}