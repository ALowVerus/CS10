import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 * 
 * @author Chris Bailey-Kellogg, Winter 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 * @author CBK, Spring 2015, updated for CamPaint
 * @author Aidan Low and Eitan Vilker, PS 1
 */
public class RegionFinder {
	private static final int maxColorDiff = 20;				// how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 50; 				// how many points in a region to be worth considering

	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage recoloredImage;                   // the image with identified regions recolored

	private ArrayList<ArrayList<Pixel>> regions;			// a region is a list of points
															// so the identified regions are in a list of lists of points

	private ArrayList<Pixel> stack = new ArrayList<Pixel>();
	public ArrayList<Pixel> pixelArray = new ArrayList<Pixel>();
	public int targetRGB;
	
	public RegionFinder() {
		this.image = null;
	}

	public RegionFinder(BufferedImage image) {
		this.image = image;
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				pixelArray.add(new Pixel(x, y, image.getRGB(x, y)));
			}
		}
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}
	
	public int getTargetRGB() {
		return targetRGB;
	}

	public BufferedImage getRecoloredImage() {
		return recoloredImage;
	}

	/**
	 * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
	 */
	public void findRegions(Color targetColor) {
		for (int i = 0; i < pixelArray.size(); i++) {
			Pixel initializer = pixelArray.get(i);
			if (initializer.getVisited() == false && matchRGB(initializer.getRGB(), targetRGB)) {
				stack.clear();
				stack.set(0, pixelArray.get(i));
				while (stack.size() > 0) {
					Pixel popped = stack.get(stack.size() - 1);
					if (popped.getVisited() == false && matchRGB(initializer.getRGB(), targetRGB)) {
						for (int y = -1; y <= 1; y ++) {									// For y one above and one below
							if (0 < popped.getY() + y && popped.getY() + y < image.getHeight() - 1) {			// Check if chosen y is between bounds
								for (int x = 1; x <= 3; x++) {								// For x one above and one below
									if (0 < popped.getX() + x && popped.getX() + x < image.getWidth() - 1) {		// Check if chosen x is between bounds
										Pixel pixelChosen = pixelArray.get(popped.getY() * image.getWidth() + popped.getX());
										if (pixelChosen.getVisited() == false) {
											stack.add(pixelChosen);
										}
									}
								}
							}
						}
					};
					popped.setVisited(true);
					stack.remove(popped);
				}
			}
		}
				
	}

	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
	 */
	private static boolean matchRGB(int rgb1, int rgb2) {	// Pretty sure this isn't how dif works.
		if ( Math.abs(rgb1 - rgb2) < maxColorDiff ) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the largest region detected (if any region has been detected)
	 */
	public ArrayList<Pixel> largestRegion() {
		ArrayList<Pixel> largest = regions.get(0);	// Initialize largest to first region
		for (int i = 0; i < regions.size(); i++) {
			if (largest.size() < regions.get(i).size()) {
				largest = regions.get(i);
			}
		}
		targetRGB = largest.get(0).getRGB();
		return largest;
	}

	/**
	 * Sets recoloredImage to be a copy of image, 
	 * but with each region a uniform random color, 
	 * so we can see where they are
	 */
	public void recolorImage() {
		// First copy the original
		recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
		// Now recolor the regions in it
		// TODO: YOUR CODE HERE
	}

}
