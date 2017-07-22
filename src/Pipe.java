
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Pipe {
	private int x, y, height, speed;
	private BufferedImage pipeImage;
	
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
	
	public void tick() {
		this.x -= this.speed;
	}
	
	public boolean isOut() {
		return (this.x + this.pipeImage.getWidth() < 0);
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getWidth() {
		return this.pipeImage.getWidth();
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public BufferedImage getImage() {
		return this.pipeImage;
	}
}
