
public class Bird {
	private int x, y, width, height, jump;
	private double gravity, velocity;
	private boolean alive;
	
	public Bird() {
		x = 80;
		y = 250;
		width = 40;
		height = 50;
		gravity = 0;
		velocity = 0.3;
		jump = -6;
		alive = true;
	}
	
	public void flap() {
		gravity = jump;
	}
	
	public void tick() {
		gravity += velocity;
		y += gravity;
	}
	
	public void kill() {
		alive = false;
	}
	
	public boolean isAlive() {
		return alive;
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
