import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;


public class Pixel {
	private int x, y;
	private boolean visited;
	private int rgb;
	
	public Pixel(int x, int y, int rgb) {
		this.x = x;
		this.y = y;
		this.rgb = rgb;
		this.visited = false;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean getVisited() {
		return this.visited;
	}
	
	public void setVisited(boolean z) {
		this.visited = z;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setRGB(int rgb) {
		this.rgb = rgb;
	}
	
	public int getRGB() {
		return this.rgb;
	}

}