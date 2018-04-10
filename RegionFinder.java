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
	private static final int maxColorDiff = 50;				// how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 20; 				// how many points in a region to be worth considering

	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage visitedImage;						// the image which stores 
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
//		for (int x = 0; x < image.getWidth(); x++) {
//			for (int y = 0; y < image.getHeight(); y++) {
//				pixelArray.add(new Pixel(x, y, image.getRGB(x, y)));
//			}
//		}
//		regions = new ArrayList<ArrayList<Pixel>>();
		visitedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		for (int x = 0; x < image.getWidth(); x ++) {
			for (int y = 0; y < image.getHeight(); y ++) {
				visitedImage.setRGB(x, y, 0);
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
		ArrayList<Integer> targetsX = new ArrayList<Integer>();
		ArrayList<Integer> targetsY = new ArrayList<Integer>();
		ArrayList<Integer> regionOriginsX = new ArrayList<Integer>();
		ArrayList<Integer> regionOriginsY = new ArrayList<Integer>();
		ArrayList<Integer> regionsSize = new ArrayList<Integer>();
		for (int chosenX = 0; chosenX < image.getWidth(); chosenX ++) {			// For all pixels
			for (int chosenY = 0; chosenY < image.getHeight(); chosenY ++) {
				if (visitedImage.getRGB(chosenX, chosenY) == 0 && matchRGB(image.getRGB(chosenX, chosenY), targetColor.getRGB())) {
					visitedImage.setRGB(chosenX, chosenY, 1);				// Set to visited
					int thisRegionSize = 0;
					regionOriginsX.add(chosenX);
					regionOriginsY.add(chosenY);
					targetsX.add(chosenX);
					targetsY.add(chosenY);
					while (targetsX.size() > 0) {
						chosenX = targetsX.get(0);
						chosenY = targetsY.get(0);
						for (int nextY = Math.max(chosenY - 1, 0); nextY < Math.min(chosenY + 1, image.getHeight()); nextY++) {									// For y one above and one below
							for (int nextX = Math.max(chosenX - 1, 0); nextX < Math.min(chosenX + 1, image.getWidth()); nextX++) {								// For x one above and one below
								if (visitedImage.getRGB(chosenX, chosenY) == 0 && matchRGB(image.getRGB(chosenX, chosenY), targetColor.getRGB())) {
									targetsX.add(nextX);
									targetsY.add(nextY);
									thisRegionSize += 1;
								}
							}
						}
					}
					regionsSize.add(thisRegionSize);
				}
			}
		}
			
		for (int i = 0; i < pixelArray.size(); i++) {
			Pixel initializer = pixelArray.get(i);
			if (!initializer.getVisited() && matchRGB(initializer.getRGB(), targetColor.getRGB())) {
				stack.add(initializer);
				initializer.setVisited(true);
				ArrayList<Pixel> potentialRegion = new ArrayList<Pixel>();
				while (stack.size() > 0) {
					Pixel popped = stack.get(stack.size() - 1);
					if (matchRGB(initializer.getRGB(), targetColor.getRGB())) {
//						if (0 < popped.getY() + 1 && popped.getY() + 1 < image.getHeight() - 1) {			// Check if chosen y is between bounds
//							Pixel poppedDown = new Pixel(popped.getX(), popped.getY() + 1);
//							if (poppedDown.getVisited() == false && matchRGB(targetColor.getRGB(), image.getRGB(poppedDown.getX(), poppedDown.getY()))) {
//								stack.add(poppedDown);
//							}
//						}
//						if (0 < popped.getY() + 1 && popped.getY() + 1 < image.getHeight() - 1) {			// Check if chosen y is between bounds
//							Pixel poppedUp = new Pixel(popped.getX(), popped.getY() - 1);
//							if (poppedUp.getVisited() == false && matchRGB(targetColor.getRGB(), image.getRGB(poppedUp.getX(), poppedUp.getY()))) {
//								stack.add(poppedUp);
//							}
//						}
//						if (0 < popped.getX() - 1 && popped.getX() + 1 < image.getWidth() - 1) {			// Check if chosen y is between bounds
//							Pixel poppedLeft = new Pixel(popped.getX() - 1, popped.getY());
//							if (poppedLeft.getVisited() == false && matchRGB(targetColor.getRGB(), image.getRGB(poppedLeft.getX(), poppedLeft.getY()))) {
//								stack.add(poppedLeft);
//							}
//						}
//						if (0 < popped.getX() + 1 && popped.getX() + 1 < image.getWidth() - 1) {			// Check if chosen y is between bounds
//							Pixel poppedRight = new Pixel(popped.getX() + 1, popped.getY());
//							if (poppedRight.getVisited() == false && matchRGB(targetColor.getRGB(), image.getRGB(poppedRight.getX(), poppedRight.getY()))) {
//								stack.add(poppedRight);
//							}
//						}
								
						for (int y = Math.max(popped.getY() - 1, 0); y < Math.min(popped.getY() + 1, image.getHeight()); y++) {									// For y one above and one below
								for (int x = Math.max(popped.getX() - 1, 0); x < Math.min(popped.getX() + 1, image.getWidth()); x++) {								// For x one above and one below
									Pixel pixelChosen = pixelArray.get(y * image.getWidth() + x);
									if (!pixelChosen.getVisited() && matchRGB(targetColor.getRGB(), image.getRGB(pixelChosen.getX(), pixelChosen.getY()))) {
										stack.add(pixelChosen);
										pixelChosen.setVisited(true);
										
									}
								}
						}
					}
					popped.setVisited(true);
					stack.remove(popped);
					potentialRegion.add(popped);
					//System.out.println(new Color(image.getRGB(popped.getX(), popped.getY())).getRed());
				}
				if (potentialRegion.size() >= minRegion) {
					regions.add(potentialRegion);
				}
			}
		}
	}

	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
	 */
	private static boolean matchRGB(int rgb1, int rgb2) {
		Color color1 = new Color(rgb1);
		Color color2 = new Color(rgb2);
		if(Math.abs(color1.getRed() - color2.getRed()) < maxColorDiff 
				&& Math.abs(color1.getGreen() - color2.getGreen()) < maxColorDiff 
				&& Math.abs(color1.getBlue() - color2.getBlue()) < maxColorDiff){
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
		// First copy the original
		recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
		// Now recolor the regions in it
		for(int i = 0; i < regions.size(); i++) {
			System.out.println(regions.get(i).size());
			int randomColor = (int) Math.floor(Math.random() * 256 * 256 * 256);
			randomColor = (256*256*256-1);
			for(int j = 0; j < regions.get(i).size(); j++) {
				//Color swappedColor = getSwapColor(color); for extra credit, not now
				recoloredImage.setRGB(regions.get(i).get(j).getX(), regions.get(i).get(j).getY(), randomColor);
			}
		}
	}

}
