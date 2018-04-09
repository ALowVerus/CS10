import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Webcam-based drawing 
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 * 
 * @author Chris Bailey-Kellogg, Spring 2015 (based on a different webcam app from previous terms)
 * @author Aidan Low and Eitan Vilker, PS 1
 */
public class CamPaint extends Webcam {
	private char displayMode = 'p';			// what to display: 'w': live webcam, 'r': recolored image, 'p': painting
	private RegionFinder finder;			// handles the finding
	private Color targetColor;          	// color of regions of interest (set by mouse press)
	private Color paintColor = Color.blue;	// the color to put into the painting from the "brush"
	private BufferedImage painting;			// the resulting masterpiece

	/**
	 * Initializes the region finder and the drawing
	 */
	public CamPaint() {
		clearPainting();
		finder = new RegionFinder(painting);
	}
	
	public Color getSwapColor(Color color) {
			int red = color.getRed();
			int green = color.getGreen();
			int blue = color.getBlue();				
			Color newColor = new Color(green, blue, red);
			return newColor;
		}

	/**
	 * Resets the painting to a blank image
	 */
	protected void clearPainting() {
		painting = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	/**
	 * DrawingGUI method, here drawing one of live webcam, recolored image, or painting, 
	 * depending on display variable ('w', 'r', or 'p')
	 */
	public void draw(Graphics g) {
		g.drawImage(painting, 0, 0, null);
	}

	/**
	 * Webcam method, here finding regions and updating the painting.
	 */
	@Override
	public void processImage() {
		ArrayList<Pixel> theRegion = finder.largestRegion();
		Color swappedColor = getSwapColor((Color) targetColor);
		for (int i = 0; i < theRegion.size() - 1; i++) {
			Pixel currentPixel = theRegion.get(i);
			painting.setRGB(currentPixel.getX(), currentPixel.getY(), swappedColor);
		}
	}

	/**
	 * Overrides the DrawingGUI method to set the track color.
	 */
	@Override
	public void handleMousePress(int x, int y) {
		int targetColor = painting.getRGB(x, y);
	}

	/**
	 * DrawingGUI method, here doing various drawing commands
	 */
	public void handleKeyPress(char k) {
		if (k == 'p' || k == 'r' || k == 'w') { // display: painting, recolored image, or webcam
			displayMode = k;
			System.out.println(k);
		}
		else if (k == 'c') { // clear
			clearPainting();
		}
		else if (k == 'o') { // save the recolored image
			saveImage(finder.getRecoloredImage(), "pictures/recolored.png", "png");
		}
		else if (k == 's') { // save the painting
			saveImage(painting, "pictures/painting.png", "png");
		}
		else {
			System.out.println("unexpected key "+k);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CamPaint();
			}
		});
	}
}