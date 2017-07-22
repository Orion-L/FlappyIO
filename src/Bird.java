
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Bird {
	private static final double GRAVITY_ACCL = 0.3;
	private static final int FLAP_ACCL = -6;
	
	private int x, y;
	private double velocity;
	private boolean alive;
	private BufferedImage birdImage;
	
	public Bird(int x, int y) {
		this.x = x;
		this.y = y;
		this.velocity = 0;
		this.alive = true;
		
		try {
			this.birdImage = ImageIO.read(new File("img/bird.png"));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void flap() {
		this.velocity = FLAP_ACCL;
	}
	
	public void tick() {
		this.velocity += GRAVITY_ACCL;
		this.y += this.velocity;
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
	
	public double getVelocity() {
		return this.velocity;
	}
}
