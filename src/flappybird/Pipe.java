package flappybird;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

class Pipe {
	private int x, y, height, speed;
	private BufferedImage pipeImage;
	
	/**
	 * Create a new pipe
	 * @param x x-coordinate of the pipe
	 * @param y y-coordinate of the pipe
	 * @param height Height of the pipe
	 * @param top Whether the pipe's base is at the top of the screen or bottom
	 */
	public Pipe(int x, int y, int height, boolean top) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.speed = 3;
		
		String file = (top ? "img/pipetop.png" : "img/pipebottom.png");
		
		try {
			this.pipeImage = ImageIO.read(new File(file));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Perform an update tick
	 */
	public void tick() {
		this.x -= this.speed;
	}
	
	/**
	 * Check if the pipe has left the game screen
	 * @return True if the pipe is off the screen, false otherwise
	 */
	public boolean isOut() {
		return (this.x + this.pipeImage.getWidth() < 0);
	}
	
	/**
	 * Get the x-coordinates of the pipe
	 * @return The pipe's current x-coordinates
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * Get the y-coordinates of the pipe
	 * @return The pipe's current y-coordinates
	 */
	public int getY() {
		return this.y;
	}
	
	/**
	 * Get the width of the pipe
	 * @return The pipe's width
	 */
	public int getWidth() {
		return this.pipeImage.getWidth();
	}
	
	/**
	 * Get the height of the pipe
	 * @return The pipe's height
	 */
	public int getHeight() {
		return this.height;
	}
	
	/**
	 * Get the image used for the pipe
	 * @return The pipe's sprite
	 */
	public BufferedImage getImage() {
		return this.pipeImage;
	}
}
