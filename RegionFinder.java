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
	private static final int maxColorDiff = 25;				// how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 50; 				// how many points in a region to be worth considering

	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage visitedImage;						// the image which stores 
	private BufferedImage recoloredImage;                   // the image with identified regions recolored

	ArrayList<Point> pointStack = new ArrayList<Point>();						// stack for x
	ArrayList<Point> regionOriginPoints = new ArrayList<Point>();
	ArrayList<ArrayList<Point>> regions = new ArrayList<ArrayList<Point>>();
	
	public Color targetColor;
	
	public RegionFinder() {
		this.image = null;
	}

	public RegionFinder(BufferedImage image) {
		this.image = image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}
	
	public int getTargetRGB() {
		return targetColor.getRGB();
	}

	public BufferedImage getRecoloredImage() {
		return recoloredImage;
	}

	/**
	 * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
	 */
	private void clearVisited() {
		for (int chosenX = 0; chosenX < image.getWidth(); chosenX ++) {			// For all pixels
			for (int chosenY = 0; chosenY < image.getHeight(); chosenY ++) {
				setVisited(chosenX, chosenY, false);
			}
		}
	}
	
	private void setVisited(int x, int y, Boolean z) {
		if (z) {visitedImage.setRGB(x, y, 1);}
		else {visitedImage.setRGB(x, y, 0);}
	}
	
	private void setVisited(Point point, Boolean z) {
		if (z) {visitedImage.setRGB((int)point.getX(), (int)point.getY(), 1);}
		else {visitedImage.setRGB((int)point.getX(), (int)point.getY(), 0);}
	}
	
	private Boolean getVisited(int x, int y) {
		if (visitedImage.getRGB(x, y) == 0) {
			return false;
		}
		return true;
	}
	
	public void findRegions(Color targetColor) {
		this.targetColor = targetColor;
		visitedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		clearVisited();
		for (int chosenX = 0; chosenX < image.getWidth(); chosenX ++) {			// For all pixels
			for (int chosenY = 0; chosenY < image.getHeight(); chosenY ++) {
				if (matchRGB(image.getRGB(chosenX, chosenY), targetColor) && !getVisited(chosenX, chosenY)) {
					Point originPoint = new Point(chosenX, chosenY);
					pointStack.add(originPoint);
					regions.add(0, new ArrayList<Point>());
					setVisited(originPoint, true);					// Set to visited
					while (pointStack.size() > 0) {
						Point chosenPoint = pointStack.get(0);
						for (int nextY = Math.max((int)chosenPoint.getY() - 1, 0); nextY <= Math.min((int)chosenPoint.getY() + 1, image.getHeight() - 1); nextY++) {									// For y one above and one below
							for (int nextX = Math.max((int)chosenPoint.getX() - 1, 0); nextX <= Math.min((int)chosenPoint.getX() + 1, image.getWidth() - 1); nextX++) {								// For x one above and one below
								if (!getVisited(nextX, nextY) && matchRGB(image.getRGB(nextX, nextY), targetColor)) {
									Point nextPoint = new Point(nextX, nextY);
									pointStack.add(nextPoint);
									setVisited(nextPoint, true);					// Set to visited
									regions.get(0).add(nextPoint);
								}
							}
						}
						pointStack.remove(0);					
					}
					if (regions.get(0).size() <= minRegion) {
						regions.remove(0);
					}
				
				}
			}
		}
	}

	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
	 */
	private static boolean matchRGB(int rgb, Color target) {
		Color color = new Color(rgb);
		if(Math.abs(color.getRed() - target.getRed()) < maxColorDiff 
				&& Math.abs(color.getGreen() - target.getGreen()) < maxColorDiff 
				&& Math.abs(color.getBlue() - target.getBlue()) < maxColorDiff){
			return true;
		}
		return false;
	}

	/**
	 * Returns the largest region detected (if any region has been detected)
	 */
	public ArrayList<Point> largestRegion() {
		System.out.println("Largest Region initiated");
		if(regions.size() > 0) {
			ArrayList<Point> largest = regions.get(0);	// Initialize largest to first region
		for (int i = 0; i < regions.size(); i++) {
			if (largest.size() < regions.get(i).size()) {
				largest = regions.get(i);
			}
		}
		System.out.println(largest.size());
		return largest;
		}
	}
	
	public Color getSwapColor(Color color) {
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();				
		Color newColor = new Color(green, blue, red);
		return newColor;
	}

	/**
	 * Sets recoloredImage to be a copy of image, 
	 * but with each region a uniform random color, 
	 * so we can see where they are
	 */
	public void recolorImage() {
		clearVisited();
		recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
		for (int i = 0; i < regions.size(); i++) {
			int randomColor = (int) Math.floor(Math.random() * 256 * 256 * 256); 	// Set color to make region
			for (int k = 0; k < regions.get(i).size(); k ++) {
				Point point = regions.get(i).get(k);
				recoloredImage.setRGB((int)point.getX(), (int)point.getY(), randomColor);
			}
		}
	}
}
