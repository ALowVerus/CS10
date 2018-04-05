package cs10;

import java.awt.Graphics;

public class Blob {

	protected double x, y, r = 5;  // Instance variables describing position and radius
	protected double dx, dy, dr;  // Instance variables describing velocity and growth
	
	public Blob() {
		// Do nothing at default
	}
	
	public Blob(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Blob(double x, double y, double r) {
		this.x = x;
		this.y = y;
		this.r = r;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getX() {
		return x;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public double getY() {
		return y;
	}

	public void setR(double r) {
		this.r = r;
	}
	
	public double getR() {
		return r;
	}
	
	public void setVelocity(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void setGrowth(double dr) {
		this.dr = dr;
	}
	
	public void step() {
		x += dx;
		y += dy;
		r += dr;
	}
	
	public boolean contains(double x2, double y2) {
		double dx = x - x2;
		double dy = y - y2;
		return dx * dx + dy * dy <= r * r;
	}

	public void draw(Graphics g) {
		g.fillOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));
	}

}
