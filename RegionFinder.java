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

	private ArrayList<Integer> regionOriginsX = new ArrayList<Integer>();		// list of origins for found regions, indexes match OriginsY and Size
	private ArrayList<Integer> regionOriginsY = new ArrayList<Integer>();		// list of origins for found regions, indexes match OriginsX and Size
	private ArrayList<Integer> regionsSize = new ArrayList<Integer>();			// list of origins for found regions, indexes match OriginsX and OriginsY
	ArrayList<Integer> stackX = new ArrayList<Integer>();						// stack for x
	ArrayList<Integer> stackY = new ArrayList<Integer>();						// stack for y
	
	public int targetRGB;
	
	public RegionFinder() {
		this.image = null;
	}

	public RegionFinder(BufferedImage image) {
		this.image = image;
		visitedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		clearVisited();
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
		System.out.println("Boop");
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
	
	// THERE IS A PROBLEM HERE WITH COLORS. HOW DO THEY WORK? WHAT SHOULD WE USE?
	private void setVisited(int x, int y, Boolean z) {
		Color black = new Color(0, 0, 0);  // These 2 lines have been added to get around single-integer colors
		Color white = new Color(255, 255, 255);
		if (z) {
			visitedImage.setRGB(x, y, white.getRGB());
			// System.out.println("True! " + String.valueOf(x) + "," + String.valueOf(y) + " Value turned to " + String.valueOf(visitedImage.getRGB(x, y)));
		}
		else {
			visitedImage.setRGB(x, y, black.getRGB());
			// System.out.println("False! " + String.valueOf(x) + "," + String.valueOf(y) + " turned to " + String.valueOf(visitedImage.getRGB(x, y)));
		}
		
	}
	
	// THERE IS A PROBLEM WITH RETRIEVING RGB. PLEASE EXPLAIN- IT DOESN'T GIVE WHAT WE PUT IN!
	private Boolean getVisited(int x, int y) {
		if (new Color(visitedImage.getRGB(x, y)).equals(Color.black)) {
			//System.out.println("True! " + String.valueOf(x) + "," + String.valueOf(y) + " Value turned to " + String.valueOf(visitedImage.getRGB(x, y)));
			return false;
		}
		return true;
	}
	
	public void findRegions(Color targetColor) {
		
		int targetRGB = targetColor.getRGB();
		clearVisited();
		
		for (int chosenX = 0; chosenX < image.getWidth(); chosenX ++) {			// For all pixels
			for (int chosenY = 0; chosenY < image.getHeight(); chosenY ++) {
				//System.out.println(visitedImage.getRGB(chosenX, chosenY));
				
				if (!getVisited(chosenX, chosenY) && matchRGB(image.getRGB(chosenX, chosenY), targetRGB)) {
					int thisRegionSize = 0;
					int specialX = chosenX;
					int specialY = chosenY;
					stackX.add(chosenX);
					stackY.add(chosenY);
					//System.out.println("StackX 0 is " + String.valueOf(stackX.get(0)));
					//System.out.println("StackY 0 is " + String.valueOf(stackY.get(0)));
					
					while (stackX.size() > 0) {
						chosenX = stackX.get(0);
						chosenY = stackY.get(0);
						setVisited(chosenX, chosenY, true);					// Set to visited
						//System.out.println("Origin determined! Chosen " + String.valueOf(chosenX) + "," + String.valueOf(chosenY) + ", muh dood");
						
						for (int nextY = Math.max(chosenY - 1, 0); nextY < Math.min(chosenY + 1, image.getHeight()); nextY++) {									// For y one above and one below
							for (int nextX = Math.max(chosenX - 1, 0); nextX < Math.min(chosenX + 1, image.getWidth()); nextX++) {								// For x one above and one below
								System.out.println("nextX is.... " + String.valueOf(nextX) + "!!!!! nextY is..... " + String.valueOf(nextY));
								//System.out.println(getVisited(nextX, nextY));
								
								if (!getVisited(nextX, nextY)) {
									//System.out.println("Image rgb is " + String.valueOf(image.getRGB(nextX, nextY)));
									//System.out.println("Target rgb is " + String.valueOf(targetRGB));
									
									// THIS STATEMENT NEVER RUNS! THE STACK IS NEVER INCREASED IN SIZE, SO SIZE OF STACK REMAINS 0 OR 1 FROM THE INITIALIZER. REGION SIZE IS ALSO 0.
									System.out.println(image.getRGB(nextX, nextY));
									System.out.println(targetRGB);
									if (matchRGB(image.getRGB(nextX, nextY), targetRGB)) {
										System.out.println("Region size incremented! Next " + String.valueOf(nextX) + "," + String.valueOf(nextY) + ",  size is " + String.valueOf(thisRegionSize) + ", bo-yeeeeeeeee");
										stackX.add(nextX);
										stackY.add(nextY);
										thisRegionSize += 1;
									}
								}
							}
						}
						stackX.remove(0);
						stackY.remove(0);
						//System.out.println("StackX 0 is " + String.valueOf(stackX.get(0)));
						//System.out.println("StackY 0 is " + String.valueOf(stackY.get(0)));
					}
					//System.out.println("This region's size is: " + String.valueOf(thisRegionSize));
					if(thisRegionSize >= minRegion) {
						regionsSize.add(thisRegionSize);
						regionOriginsX.add(specialX);
						regionOriginsY.add(specialY);
						System.out.println("Region added to lists!");
					}
				}
			}
		}
		clearVisited();
//		System.out.println("OriginsX " + String.valueOf(regionOriginsX.size())
//		+ " OriginsY " + String.valueOf(regionOriginsY.size()) 
//		+ " Region size " + String.valueOf(regionsSize.size()));
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
		
		System.out.println("recoloring initiated");
		// First copy the original
		recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
		
		// Now recolor the regions in it
		for (int i = 0; i < regionsSize.size(); i++) {
			//System.out.println(i);
			int randomColor = (int) Math.floor(Math.random() * 256 * 256 * 256); 	// Set color to make region
			//randomColor = (256*256*256-1);											// Initialize to white for testing
			
			for (int n = 0; n < regionsSize.size(); n++) {							// For all pixels
				int chosenX = regionOriginsX.get(n);
				int chosenY = regionOriginsY.get(n);
				stackX.add(chosenX);
				stackY.add(chosenY);
				
				while (stackX.size() > 0) {
					chosenX = stackX.get(0);
					chosenY = stackY.get(0);
					setVisited(chosenX, chosenY, true);								// Set to visited
					//System.out.println(visitedImage.getRGB(chosenX, chosenY));
					
					for (int nextY = Math.max(chosenY - 1, 0); nextY < Math.min(chosenY + 1, image.getHeight()); nextY++) {									// For y one above and one below
						for (int nextX = Math.max(chosenX - 1, 0); nextX < Math.min(chosenX + 1, image.getWidth()); nextX++) {								// For x one above and one below
							if (!getVisited(chosenX, chosenY) && matchRGB(image.getRGB(chosenX, chosenY), targetRGB)) {
								//System.out.println(nextX);
								stackX.add(nextX);
								stackY.add(nextY);
							}
						}
					}
					recoloredImage.setRGB(stackX.get(0), stackY.get(0), randomColor);
					stackX.remove(0);
					stackY.remove(0);
				}
			}
		}
		clearVisited();
	}

}
