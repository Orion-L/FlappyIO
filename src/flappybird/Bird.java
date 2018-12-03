package flappybird;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

class Bird {
	private static final double GRAVITY_ACCL = 0.3;
	private static final int FLAP_ACCL = -6;
	
	private int x, y;
	private double velocity;
	private boolean alive;
	private BufferedImage birdImage;
	
	/**
	 * Generate a new bird
	 * @param x x-coordinate of the new bird
	 * @param y y-coordinate of the new bird
	 */
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
	
	/**
	 * Perform a flap
	 */
	public void flap() {
		this.velocity = FLAP_ACCL;
	}
	
	/**
	 * Perform an update tick
	 */
	public void tick() {
		this.velocity += GRAVITY_ACCL;
		this.y += this.velocity;
	}
	
	/**
	 * Kill the bird
	 */
	public void kill() {
		this.alive = false;
	}
	
	/**
	 * Check if the bird is alive
	 * @return Returns the status of the bird
	 */
	public boolean isAlive() {
		return this.alive;
	}
	
	/**
	 * Get the x-coordinate of the bird
	 * @return The bird's current x-coordinate
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * Get the y-coordinate of the bird
	 * @return The bird's current y-coordinate
	 */
	public int getY() {
		return this.y;
	}
	
	/**
	 * Get the width of the bird
	 * @return The bird's width
	 */
	public int getWidth() {
		return this.birdImage.getWidth();
	}
	
	/**
	 * Get the height of the bird
	 * @return The bird's height
	 */
	public int getHeight() {
		return this.birdImage.getHeight();
	}
	
	/**
	 * Get the image used for the bird
	 * @return The bird's sprite
	 */
	public BufferedImage getImage() {
		return this.birdImage;
	}
	
	/**
	 * Get the velocity of the bird
	 * @return The bird's current velocity
	 */
	public double getVelocity() {
		return this.velocity;
	}
}
