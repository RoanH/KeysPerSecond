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
	protected static Image graph_lower_right;//TODO
	protected static Image graph_lower_left;
	protected static Image graph_lower_middle;
	protected static Image graph_side_left;
	protected static Image graph_side_right;
	protected static Image graph_upper_right;
	protected static Image graph_upper_left;
	protected static Image graph_upper_middle;
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
	 * Checkmark icon
	 */
	protected static Image checkmark;
	/**
	 * Arrow icon
	 */
	protected static Image arrow;
	
	/**
	 * Prepares the colours and images
	 * used by the program.
	 * @param graph Whether or not to load resources for the graph
	 * @param custom Whether or not the supplied colours are custom colours
	 * @throws IOException When an IOException occurs
	 */
	public static final void prepareImages(boolean graph, boolean custom) throws IOException{
		if(custom){
			if(Main.config.getForegroundOpacity() != 1.0F || Main.config.getBackgroundOpacity() != 1.0F){
				transparency = true;
			}
			alphaAqua = new Color(Main.config.getForegroundColor().getRed(), Main.config.getForegroundColor().getGreen(), Main.config.getForegroundColor().getBlue(), 51);
			if(Main.config.mode == RenderingMode.VERTICAL){
				pressed = dye(ImageIO.read(ClassLoader.getSystemResource("hit.png")));
				unpressed = dye(ImageIO.read(ClassLoader.getSystemResource("key.png")));
			}else if(Main.config.mode == RenderingMode.HORIZONTAL){
				pressed = dye(ImageIO.read(ClassLoader.getSystemResource("hit_horizontal.png")));
				unpressed = dye(ImageIO.read(ClassLoader.getSystemResource("key_horizontal.png")));
			}
			if(graph){
				graph_lower_right  = dye(ImageIO.read(ClassLoader.getSystemResource("graph_lower_right.png")));
				graph_lower_left   = dye(ImageIO.read(ClassLoader.getSystemResource("graph_lower_left.png")));
				graph_lower_middle = dye(ImageIO.read(ClassLoader.getSystemResource("graph_lower_middle.png")));
				graph_side_left    = dye(ImageIO.read(ClassLoader.getSystemResource("graph_side_left.png")));
				graph_side_right   = dye(ImageIO.read(ClassLoader.getSystemResource("graph_side_right.png")));
				graph_upper_right  = dye(ImageIO.read(ClassLoader.getSystemResource("graph_upper_right.png")));
				graph_upper_left   = dye(ImageIO.read(ClassLoader.getSystemResource("graph_upper_left.png")));
				graph_upper_middle = dye(ImageIO.read(ClassLoader.getSystemResource("graph_upper_middle.png")));
			}
		}else{
			alphaAqua = new Color(0.0F, 1.0F, 1.0F, 0.2F);
			if(Main.config.mode == RenderingMode.VERTICAL){
				pressed = ImageIO.read(ClassLoader.getSystemResource("hit.png"));
				unpressed = ImageIO.read(ClassLoader.getSystemResource("key.png"));
			}else if(Main.config.mode == RenderingMode.HORIZONTAL){
				pressed = ImageIO.read(ClassLoader.getSystemResource("hit_horizontal.png"));
				unpressed = ImageIO.read(ClassLoader.getSystemResource("key_horizontal.png"));
			}
			if(graph){
				graph_lower_right  = ImageIO.read(ClassLoader.getSystemResource("graph_lower_right.png"));
				graph_lower_left   = ImageIO.read(ClassLoader.getSystemResource("graph_lower_left.png"));
				graph_lower_middle = ImageIO.read(ClassLoader.getSystemResource("graph_lower_middle.png"));
				graph_side_left    = ImageIO.read(ClassLoader.getSystemResource("graph_side_left.png"));
				graph_side_right   = ImageIO.read(ClassLoader.getSystemResource("graph_side_right.png"));
				graph_upper_right  = ImageIO.read(ClassLoader.getSystemResource("graph_upper_right.png"));
				graph_upper_left   = ImageIO.read(ClassLoader.getSystemResource("graph_upper_left.png"));
				graph_upper_middle = ImageIO.read(ClassLoader.getSystemResource("graph_upper_middle.png"));
			}
		}
		checkmark = dye(ImageIO.read(ClassLoader.getSystemResource("checkmark.png")));
		arrow = dye(ImageIO.read(ClassLoader.getSystemResource("arrow.png")));
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
				img.setRGB(x, y, repaintPixel(img.getRGB(x, y), Main.config.getForegroundColor()));
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
