import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Bird {
	private int x, y, jump;
	private double gravity, velocity;
	private boolean alive;
	private BufferedImage birdImage;
	
	public Bird(int x, int y) {
		this.x = x;
		this.y = y;
		gravity = 0;
		velocity = 0.3;
		jump = -6;
		alive = true;
		
		try {
			birdImage = ImageIO.read(new File("img/bird.png"));
		} catch (Exception e) {
			System.out.println(e);
		}
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
		return birdImage.getWidth();
	}
	
	public int getHeight() {
		return birdImage.getHeight();
	}
	
	public BufferedImage getImage() {
		return birdImage;
	}
	
	public double getGravity() {
		return gravity;
	}
}
