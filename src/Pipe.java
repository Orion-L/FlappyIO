
public class Pipe {
	private int x, y, width, height, speed;
	
	public Pipe(int x, int y, int height) {
		this.x = x;
		this.y = y;
		width = 50;
		this.height = height;
		speed = 3;
	}
	
	public void tick() {
		x -= speed;
	}
	
	public boolean isOut() {
		return (x + width < 0);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
