import java.awt.image.BufferedImage;
import java.awt.Color;

/**
 * A class demonstrating manipulation of image pixels.
 * Version 0: just the core definition
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Winter 2014, rewritten for BufferedImage
 * @author CBK, Spring 2015, refactored to separate GUI from operations
 * @author Aidan Low, no partner, added image modification functionality
 */
public class CamImageProcessor0 {
	private BufferedImage image;		// the current image being processed

	/**
	 * @param image		the original
	 */
	public CamImageProcessor0(BufferedImage image) {
		this.image = image;
	}
	
	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	// Custom functions
	
	public int fixAt256(int n){ 				// Make sure color index < 256
		if (n>255) {
			return 255;
			} 
		return n;
	}
	public void changeLight(double lightFactor, int startX, int startY, int endX, int endY) {		// Change light by common factor on use, works for page and marker
		for (int x=startX; x<endX; x++) {
			for (int y=startY; y<endY; y++) {
				Color color = new Color(image.getRGB(x, y));
				int newRed = fixAt256((int)(color.getRed() * lightFactor));
				int newBlue = fixAt256((int)(color.getBlue() * lightFactor));
				int newGreen = fixAt256((int)(color.getGreen() * lightFactor));
				int newRGB = (65536) * newRed + 256 * newGreen + newBlue;
				image.setRGB(x, y, newRGB);
			}
		}
	}
	public void brightenImage(int startX, int startY, int endX, int endY) {					// Change light by a high factor, 4/3
		System.out.println("Brighten!");
		changeLight(4.0/3.0, startX, startY, endX, endY);
	}
	public void darkenImage(int startX, int startY, int endX, int endY) {					// Change light by a low factor, 3/4
		System.out.println("Darken!");
		changeLight(3.0/4.0, startX, startY, endX, endY);
	}
	
	
	public void makeColoredSquare(int mouseX, int mouseY) {						// Make a colored square when mouse is down and op is m.
		System.out.println("Square!");
		for (int y=0; y<20; y++) {
			for (int x=0; x<20; x++) {
				image.setRGB(x + mouseX, y + mouseY, (int)(256 * 256 * 256 * Math.random()));
			}
		}
	}
	
	public void flipPixels(int x1, int y1, int x2, int y2) {					// Abstracting pixel flipping from Vertical and Horizontal functions.
		int RGB1 = image.getRGB(x1, y1);
		int RGB2 = image.getRGB(x2, y2);
		image.setRGB(x1, y1, RGB2);
		image.setRGB(x2, y2, RGB1);
	}
	public void flipVertical(int startX, int startY, int endX, int endY) { 		// Flips pixels vertically. Works for page, but not for marker.
		System.out.println("Flip vertical!");
		for (int y=startY; y<endY / 2; y++) {
			for (int x=startX; x<endX; x++){
				flipPixels(x, y, x, endY + startY - y - 1);
			}
		}
	}
	public void flipHorizontal(int startX, int startY, int endX, int endY) {	// Flips pixels vertically. Works for page, but not for marker.
		System.out.println("Flip horizontal!");
		for (int x=startX; x<endX / 2; x++) {
			for (int y=startY; y<endY; y++){
				flipPixels(x, y, endX + startX - x - 1, y);
			}
		}
	}
}
