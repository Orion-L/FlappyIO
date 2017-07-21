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
		this.gravity = 0;
		this.velocity = 0.3;
		this.jump = -6;
		this.alive = true;
		
		try {
			this.birdImage = ImageIO.read(new File("img/bird.png"));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void flap() {
		this.gravity = this.jump;
	}
	
	public void tick() {
		this.gravity += this.velocity;
		this.y += this.gravity;
	}
	
	public void kill() {
		this.alive = false;
	}
	
	public boolean isAlive() {
		return this.alive;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getWidth() {
		return this.birdImage.getWidth();
	}
	
	public int getHeight() {
		return this.birdImage.getHeight();
	}
	
	public BufferedImage getImage() {
		return this.birdImage;
	}
	
	public double getGravity() {
		return this.gravity;
	}
}
