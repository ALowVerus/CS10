
public class Pixel {

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
		return visited;
	}
	
	public void setVisited(boolean z) {
		self.visited = z;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

}