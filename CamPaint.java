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
	private char displayMode = 'w';			// what to display: 'w': live webcam, 'r': recolored image, 'p': painting
	private RegionFinder finder;			// handles the finding
	private Color targetColor; // color of regions of interest (set by mouse press)
	private Color paintColor = Color.blue;	// the color to put into the painting from the "brush"
	private BufferedImage painting;			// the resulting masterpiece
	private Boolean isMouseClicked = false;
	
	/**
	 * Initializes the region finder and the drawing
	 */
	public CamPaint() {
		finder = new RegionFinder();
		clearPainting();
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
		// TODO: YOUR CODE HERE
		if (displayMode == 'w') {
			g.drawImage(image, 0, 0, null);
		}
		if (displayMode == 'p') {
			g.drawImage(painting, 0, 0, null);
		}
		if (displayMode == 'r') {
			g.drawImage(finder.getRecoloredImage(),  0,  0, null);
		}
	}

	/**
	 * Webcam method, here finding regions and updating the painting.
	 */
	@Override
	public void processImage() {
		// TODO: YOUR CODE HERE
		finder.setImage(image);
		if (targetColor != null) {
			finder.findRegions(targetColor);
			if (finder.regions.size() != 0) {
				finder.recolorImage();
				if (displayMode == 'p') {
					ArrayList<Point> theRegion = finder.largestRegion();
					Color swappedColor = getSwapColor(targetColor);
					if(isMouseClicked) {
						for (int i = 0; i < theRegion.size() - 1; i++) {
							Point currentPixel = theRegion.get(i);
							painting.setRGB((int)currentPixel.getX(), (int)currentPixel.getY(), swappedColor.getRGB());
						}
					}	
				}
			}
		}
	}

	/**
	 * Overrides the DrawingGUI method to set the track color.
	 */
	@Override
	public void handleMousePress(int x, int y) {
		// TODO: YOUR CODE HERE
		targetColor = new Color(image.getRGB(x, y));
		System.out.println("Clicked " + String.valueOf(x) + "," + String.valueOf(y)+ ". Target color is now (" 
				+ String.valueOf(targetColor.getRed()) + "," 
				+ String.valueOf(targetColor.getGreen()) + "," 
				+ String.valueOf(targetColor.getBlue()) + ").");
		isMouseClicked = true;
	}
	
	/**
	 * Custom getSwapColor method
	 */
	public Color getSwapColor(Color color) {
			int red = color.getRed();
			int green = color.getGreen();
			int blue = color.getBlue();				
			Color newColor = new Color(green, blue, red);
			return newColor;
	}

	/**
	 * DrawingGUI method, here doing various drawing commands
	 */
	public void handleKeyPress(char k) {
		if (k == 'p' || k == 'r' || k == 'w') { // display: painting, recolored image, or webcam
			displayMode = k;
			System.out.println("Mode is now " + k);
		}
		else if (k == 'c') { // clear
			clearPainting();
			System.out.println("Cleared painting!");
		}
		else if (k == 'o') { // save the recolored image
			saveImage(finder.getRecoloredImage(), "pictures/recolored.png", "png");
			System.out.println("Saved recolored to pictures.");

		}
		else if (k == 's') { // save the painting
			saveImage(painting, "pictures/painting.png", "png");
			System.out.println("Saved painting to pictures.");

		}
		else {
			System.out.println("unexpected key " + k);
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