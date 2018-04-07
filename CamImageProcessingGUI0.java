import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 * A class demonstrating manipulation of image pixels.
 * Version 0: just the core definition
 * Load an image and display it
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Winter 2014, rewritten for BufferedImage
 * @author CBK, Spring 2015, refactored to separate GUI from operations
 * @author Aidan Low, no partner, added mouse motion and image modification functionality
 */
public class CamImageProcessingGUI0 extends CamDrawingGUI {
	private CamImageProcessor0 proc;		// handles the image processing
	private static String origin = "SA3_PicProcessing/";
	private Boolean brushDown = false; 	// init to false to start without drawing
	private char opMode = 'n';			// init to 'n' to start without drawing and prevent error

	/**
	 * Creates the GUI for the image processor, with the window scaled to the to-process image's size
	 */
	public CamImageProcessingGUI0(CamImageProcessor0 proc) {
		super("Image processing", proc.getImage().getWidth(), proc.getImage().getHeight());
		this.proc = proc;
	}

	/**
	 * CamDrawingGUI method, here showing the current image
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(proc.getImage(), 0, 0, null);
		
	}

	/**
	 * CamDrawingGUI method, here dispatching on image processing operations
	 */
	@Override
	public void handleKeyPress(char op) {
		System.out.println("Handling key '"+op+"'");
		opMode = op;
		int startX = 0; int endX = proc.getImage().getWidth();
		int startY = 0; int endY = proc.getImage().getHeight();
		if (opMode=='s') { // save a snapshot
			saveImage(proc.getImage(), origin + "processed.png", "png");
		}
		else if (opMode=='y') { 					// Switch to drawing mode when clicking y
			brushDown = true;
			System.out.println("Drawing mode!");
		}
		else if (opMode=='n') {						// Switch to screen mode when clicking n
			brushDown = false;
			System.out.println("Screen mode!");
		}
		else if (brushDown == false) {
			handleDrawingEvents(startX, startY, endX, endY);
		}
		else {
			System.out.println("Unknown operation");
		}

		repaint(); // Re-draw, since image has changed
	}
	
	@Override
	public void handleMouseMotion(int x, int y) {
		if (brushDown == true) {
			handleDrawingEvents(x, y, x+20, y+20);		// Change the standard darkening, brightening, and flipping
			if (opMode=='m') {							// Only check for random colored square marker if marker is down
				proc.makeColoredSquare(x, y);
			}
			repaint();									// Repaint after modified
		} 
	}
	
	public void handleDrawingEvents(int startX, int startY, int endX, int endY) {		// Abstracting darkening, brightening, and flipping from the marker and page functions, since they are the same checks for both.
		if (opMode=='b') {
			proc.brightenImage(startX, startY, endX, endY);
		}
		else if (opMode=='d') {
			proc.darkenImage(startX, startY, endX, endY);
		}
		else if (opMode=='v') {
			proc.flipVertical(startX, startY, endX, endY);
		}
		else if (opMode=='h') {
			proc.flipHorizontal(startX, startY, endX, endY);
		}
	}

	public static void main(String[] args) { 
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Load the image to process
				BufferedImage baker = loadImage(origin + "baker-400-300.jpg");
				// Create a new processor, and a GUI to handle it
				new CamImageProcessingGUI0(new CamImageProcessor0(baker));
			}
		});
	}
}
