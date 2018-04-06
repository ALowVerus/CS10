
public class Pixel {
	private int x, y;
	private boolean visited;
	
	public Pixel(int x, int y) {
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

}