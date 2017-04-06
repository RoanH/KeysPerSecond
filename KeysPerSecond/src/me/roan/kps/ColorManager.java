package me.roan.kps;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Central class for managing
 * colours and resources
 * @author Roan
 */
public class ColorManager {
	/**
	 * Image for a pressed key
	 * Image taken from osu!lazer
	 * https://cloud.githubusercontent.com/assets/191335/16511435/17acd2f2-3f8b-11e6-8b50-5fccba819ce5.png
	 */
	protected static Image pressed;
	/**
	 * Image for an unpressed key<br>
	 * Image taken from osu!lazer
	 * https://cloud.githubusercontent.com/assets/191335/16511432/17ac5232-3f8b-11e6-95b7-33f9a4df0b7c.png
	 */
	protected static Image unpressed;
	/**
	 * Left side of the graph border
	 */
	protected static Image gleft = null;
	/**
	 * Right side of the graph border
	 */
	protected static Image gright = null;
	/**
	 * Middle section of the graph border
	 */
	protected static Image gmid = null;
	/**
	 * Color used to fill the area underneath the graph
	 */
	protected static Color alphaAqua;
	/**
	 * Whether or not transparency is enabled
	 */
	protected static boolean transparency = false;
	/**
	 * Transparent color
	 */
	protected static final Color transparent = new Color(0.0F, 0.0F, 0.0F, 0.0F);

	/**
	 * Prepares the colours and images
	 * used by the program.
	 * @param graph Whether or not to load resources for the graph
	 * @param custom Whether or not the supplied colours are custom colours
	 * @throws IOException When an IOException occurs
	 */
	public static final void prepareImages(boolean graph, boolean custom) throws IOException{
		if(custom){
			if(Main.config.opacityfg != 1.0F || Main.config.opacitybg != 1.0F){
				transparency = true;
			}
			alphaAqua = new Color(Main.config.foreground.getRed(), Main.config.foreground.getGreen(), Main.config.foreground.getBlue(), 51);
			pressed = dye(ImageIO.read(ClassLoader.getSystemResource("hit.png")));
			unpressed = dye(ImageIO.read(ClassLoader.getSystemResource("key.png")));
			if(graph){
				gright = dye(ImageIO.read(ClassLoader.getSystemResource("graphright.png")));
				gleft = dye(ImageIO.read(ClassLoader.getSystemResource("graphleft.png")));
				gmid = dye(ImageIO.read(ClassLoader.getSystemResource("graphmiddle.png")));
			}
		}else{
			alphaAqua = new Color(0.0F, 1.0F, 1.0F, 0.2F);
			pressed = ImageIO.read(ClassLoader.getSystemResource("hit.png"));
			unpressed = ImageIO.read(ClassLoader.getSystemResource("key.png"));
			if(graph){
				gright = ImageIO.read(ClassLoader.getSystemResource("graphright.png"));
				gleft = ImageIO.read(ClassLoader.getSystemResource("graphleft.png"));
				gmid = ImageIO.read(ClassLoader.getSystemResource("graphmiddle.png"));
			}
		}
	}
	
	/**
	 * Parses the given image in
	 * order to change its colours
	 * to the set {@link #foreground}
	 * colour
	 * @param img The image to parse
	 * @return The recoloured image
	 */
	private static final Image dye(BufferedImage img){
		for(int x = 0; x < img.getWidth(); x++){
			for(int y = 0; y < img.getHeight(); y++){
				img.setRGB(x, y, repaintPixel(img.getRGB(x, y), Main.config.foreground));
			}
		}
		return img;
	}
	
	/**
	 * Mixes the two given colours
	 * where the new color is made
	 * twice as prominent as the 
	 * original color while the
	 * alpha value is copied from
	 * the original color
	 * @param original The original color
	 * @param newcolor The new color
	 * @return The two mixed colours
	 */
	private static final int repaintPixel(int original, Color newcolor){
		return (((original >> 24) & 0x000000FF) << 24) | (((((original >> 16) & 0x000000FF) + 2 * newcolor.getRed()) / 3) << 16) | (((((original >> 8) & 0x000000FF) + 2 * newcolor.getGreen()) / 3) << 8) | (((original & 0x000000FF) + 2 * newcolor.getBlue()) / 3);
	}
}
