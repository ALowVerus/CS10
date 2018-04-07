import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;


public class Pixel {
	private int x, y;
	private boolean visited;
	private Color color;
	
	public Pixel(int x, int y, BufferedImage image) {
		this.x = x;
		this.y = y;
		this.visited = false;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean getVisited() {
		return true;
	}
	
	public void setVisited(boolean z) {
		this.visited = z;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return this.color;
	}

}