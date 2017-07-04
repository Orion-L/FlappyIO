import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Game {
	private ArrayList<Bird> birds;
	private ArrayList<Pipe> pipes;
	private int score, spawnInterval, interval, birdsAlive;
	private int width, height;
	private double backgroundx, backgroundSpeed;
	private BufferedImage background;
	private Graphics window;
	
	public Game(int width, int height, Graphics window) {
		birds = new ArrayList<Bird>();
		pipes = new ArrayList<Pipe>();
		score = 0;
		interval = 0;
		spawnInterval = 90;
		backgroundx = 0;
		backgroundSpeed = 0.5;
		this.width = width;
		this.height = height;
		this.window = window;
		
		try {
			background = ImageIO.read(new File("img/background.png"));
		} catch (Exception e) {
			System.out.println(e);
		}
		
		Bird b = new Bird(width / 5, height / 2);
		birds.add(b);
		
		birdsAlive = birds.size();
		
	}
	
	public void tick() {
		window.clearRect(0, 0, width, height);
		for (int i = 0; i < Math.ceil(width / background.getWidth()) + 1; i++) {
			window.drawImage(background, (int)(i * background.getWidth() - Math.floor(backgroundx % background.getWidth())), 0, width, height, null);
		}
		
		backgroundx += backgroundSpeed;
		
		double nextHole = 0;
		if (birds.size() > 0) {
			for (int i = 0; i < pipes.size(); i += 2) {
				Pipe p = pipes.get(i);
				if (p.getX() + p.getWidth() > birds.get(0).getX()) {
					nextHole = p.getHeight() / height;
				}
			}
		}
		
		for (int i = 0; i < birds.size(); i++) {
			Bird b = birds.get(i);
			if (b.isAlive()) {
				
				b.tick();
				if (b.getY() >= height || b.getY() + b.getHeight() <= 0) {
					birdsAlive--;
					b.kill();
				} else {
					for (int j = 0; j < pipes.size(); j++) {
						Pipe p = pipes.get(j);
                        if (b.getX() < p.getX() + p.getWidth() && b.getX() + b.getWidth() > p.getX()
                         && b.getY() < p.getY() + p.getHeight() && b.getY() + b.getHeight() > p.getY()) {
                            b.kill();
                            birdsAlive--;
                            break;
                        }
					}
				}
				
				if (!b.isAlive()) {
					continue;
				}
				
				AffineTransform tx = AffineTransform.getRotateInstance(Math.PI / 2 * (b.getGravity() / 20), b.getWidth() / 2, b.getHeight() / 2);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
				
				window.drawImage(op.filter(b.getImage(), null), b.getX(), b.getY(), b.getWidth(), b.getHeight(), null);
			}
		}
		
		if (birdsAlive <= 0) {
			// new game?
			//System.exit(0);
		}
		
		for (int i = 0; i < pipes.size(); i++) {
			Pipe p = pipes.get(i);
			p.tick();
			
			if (p.isOut()) {
				pipes.remove(i);
				i--;
			} else {
				
				window.drawImage(p.getImage(), p.getX(), p.getY(), p.getWidth(), p.getHeight(), null);
			}
		}
	
		if (interval == 0) {
			int delta = 50;
			int holeSize = 120;
			int pos = (int) (Math.round(Math.random() * (height - delta * 2 - holeSize)) + delta);
			
			Pipe p = new Pipe(width, 0, pos, true);
			pipes.add(p);
			
			p = new Pipe(width, pos + holeSize, height, false);
			pipes.add(p);
		}
		
		interval = (interval + 1) % spawnInterval;
		
		score++;

		/**
		window.setColor(new Color(127,127,127));
		window.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		window.drawString("Score = " + score, 0, 0);
		**/
	}
	
	public boolean isGameOver() {
		return (birdsAlive == 0);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public double getBackgroundx() {
		return backgroundx;
	}
	
	public int getScore() {
		return score;
	}
	
	public void flap() {
		for (int i = 0; i < birds.size(); i++) {
			birds.get(i).flap();
		}
	}
}
