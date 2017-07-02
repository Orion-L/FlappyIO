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
		speed = 3;
		
		String file;
		if (top) {
			file = "img/pipetop.png";
		} else {
			file = "img/pipebottom.png";
		}
		
		try {
			pipeImage = ImageIO.read(new File(file));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void tick() {
		x -= speed;
	}
	
	public boolean isOut() {
		return (x + pipeImage.getWidth() < 0);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return pipeImage.getWidth();
	}
	
	public int getHeight() {
		return height;
	}
	
	public BufferedImage getImage() {
		return pipeImage;
	}
}
