import java.util.ArrayList;

public class Game {
	private ArrayList<Bird> birds;
	private ArrayList<Pipe> pipes;
	private int score, spawnInterval, interval, birdsAlive;
	private int width, height;
	private double backgroundx, backgroundSpeed;
	
	public Game(int width, int height) {
		birds = new ArrayList<Bird>();
		pipes = new ArrayList<Pipe>();
		score = 0;
		interval = 0;
		spawnInterval = 90;
		backgroundx = 0;
		backgroundSpeed = 0.5;
		this.width = width;
		this.height = height;
		
		Bird b = new Bird();
		birds.add(b);
		
		birdsAlive = birds.size();
	}
	
	public void tick() {
		backgroundx += backgroundSpeed;
		
		double nextHole;
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
					b.kill();
				} else {
					for (int j = 0; j < pipes.size(); j++) {
						Pipe p = pipes.get(j);
                        if (b.getX() < p.getX() + p.getWidth() && b.getX() + b.getWidth() > p.getX()
                         && b.getY() < p.getY() + p.getHeight() && b.getY() + b.getHeight() > p.getY()) {
                            b.kill();
                            break;
                        }
					}
				}
			}
		}
		
		for (int i = 0; i < pipes.size(); i++) {
			Pipe p = pipes.get(i);
			p.tick();
			if (p.isOut()) {
				pipes.remove(i);
				i--;
			}
		}
	
		if (interval == 0) {
			int delta = 50;
			int holeSize = 120;
			int pos = (int) (Math.round(Math.random() * (height - delta * 2 - holeSize)) - delta);
			
			Pipe p = new Pipe(width, 0, pos);
			pipes.add(p);
			
			p = new Pipe(width, pos + holeSize, height);
			pipes.add(p);
		}
		
		interval = (interval + 1) % spawnInterval;
		
		score++;
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
}
