import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import neuroevolver.Neuroevolver;


public class Game extends JPanel implements ActionListener {
	private static final long serialVersionUID = 5676566905984384798L;
	private LinkedList<Bird> birds;
	private LinkedList<Pipe> pipes;
	private int score, spawnInterval, interval, birdsAlive, genNumber, maxScore;
	private int width, height;
	private double backgroundx, backgroundSpeed;
	private BufferedImage background;
	private Neuroevolver neuro;
	
	public Game(int width, int height) {
		// Birds and pipes lists
		this.birds = new LinkedList<Bird>();
		this.pipes = new LinkedList<Pipe>();
		
		this.score = 0;
		this.maxScore = 0;
		
		// Current tick interval and interval to spawn pipe at
		this.interval = 0;
		this.spawnInterval = 90;
		
		// Current background x-coordinate start and speed
		this.backgroundx = 0;
		this.backgroundSpeed = 0.5;
		
		// Game width, height and frame
		this.width = width;
		this.height = height;
		
		// Background image
		try {
			this.background = ImageIO.read(new File("img/background.png"));
		} catch (Exception e) {
			System.out.println(e);
		}
		
		int[] hidden = {1};
		this.neuro = new Neuroevolver(50, 1, 0.5, 0.1, 0.2, 0.25, 0.2, 2, 1, hidden);
		this.neuro.nextGeneration();
		
		for (int i = 0; i < this.neuro.generationSize(); i++) {
			Bird b = new Bird(width / 5, height / 2);
			this.birds.add(b);
		}
		
		this.genNumber = 1;
		
		this.birdsAlive = this.birds.size();
	}
	
	public void tick() {
		// Find y coordinate of next hole
		double nextHole = -1;
		if (this.birds.size() > 0) {
			for (int i = 0; i < this.pipes.size(); i += 2) {
				Pipe p = this.pipes.get(i);
				
				for (int j = 0; j < this.birds.size(); j++) {
					Bird b = this.birds.get(j);
					if (b.isAlive()) {
						if (p.getX() + p.getWidth() > b.getX()) nextHole = p.getHeight() + 60;
						break;
					}
				}
				
				if (nextHole != -1) break;
			}
		}
		
		// Loop through all birds
		for (int i = 0; i < this.birds.size(); i++) {
			Bird b = this.birds.get(i);
			
			// Consider only alive birds
			if (b.isAlive()) {
				// Update the bird
				b.tick();
				
				double[] inputs = {(double) (this.height - b.getY()) / this.height, (this.height - nextHole) / this.height};
				if (this.neuro.evaluateGenome(i, inputs)[0] > 0.55) b.flap();
				
				if (b.getY() >= this.height || b.getY() + b.getHeight() <= 0) {
					// Bird is above or below game screen, kill it
					this.birdsAlive--;
					this.neuro.updateScore(i, this.score);
					b.kill();
				} else {
					for (int j = 0; j < this.pipes.size(); j++) {
						Pipe p = this.pipes.get(j);
                        if (b.getX() < p.getX() + p.getWidth() && b.getX() + b.getWidth() > p.getX()
                         && b.getY() < p.getY() + p.getHeight() && b.getY() + b.getHeight() > p.getY()) {
                            // Bird has hit a pipe, kill it
                        	b.kill();
                            this.birdsAlive--;
        					this.neuro.updateScore(i, this.score);
                            break;
                        }
					}
				}
			}
		}

		// All birds dead, move on to next generation
		if (this.birdsAlive <= 0) {
			restart();
			return;
		}
		
		// Loop through all pipes
		for (int i = 0; i < this.pipes.size(); i++) {
			Pipe p = this.pipes.get(i);
			
			// Update the pipe
			p.tick();
			
			if (p.isOut()) {
				// Pipe is off screen, remove from list
				this.pipes.remove(i);
				i--;
			}
		}
	
		if (this.interval == 0) {
			// Time to spawn new pipe
			
			// Ensure the hole is 50px away from both top and bottom of the window
			int delta = 50;
			int holeSize = 120;
			int pos = (int) (Math.round(Math.random() * (this.height - delta * 2 - holeSize)) + delta);
			
			// Create the pipes
			Pipe p = new Pipe(this.width, 0, pos, true);
			this.pipes.add(p);
			
			p = new Pipe(this.width, pos + holeSize, this.height, false);
			this.pipes.add(p);
		}
		
		this.interval = (this.interval + 1) % this.spawnInterval;
		
		this.score++;
		
		if (this.score > this.maxScore) {
			this.maxScore = this.score;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		tick();
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.clearRect(0, 0, this.width, this.height);
		
		// Loop through number of background images needed to cover entire window
		for (int i = 0; i < Math.ceil(this.width / this.background.getWidth()) + 2; i++) {
			// Draw background at offset x position
			g.drawImage(this.background, 
						(int)(i * this.background.getWidth() - Math.floor(this.backgroundx)), 
						0, this.background.getWidth(), this.height, null);
		}
		
		this.backgroundx = (this.backgroundx + this.backgroundSpeed) % this.background.getWidth();
		
		// Loop through all birds
		for (int i = 0; i < this.birds.size(); i++) {
			Bird b = this.birds.get(i);
					
			// Consider only alive birds
			if (b.isAlive()) {
				// Rotate the bird depending on its heading
				AffineTransform tx = AffineTransform.getRotateInstance(Math.PI / 2 * (b.getGravity() / 20), 
																	   b.getWidth() / 2, b.getHeight() / 2);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
						
				// Draw the bird
				g.drawImage(op.filter(b.getImage(), null), b.getX(), b.getY(), 
						    b.getWidth(), b.getHeight(), null);
			}
		}
		
		// Loop through all pipes
		for (int i = 0; i < this.pipes.size(); i++) {
			Pipe p = this.pipes.get(i);
							
			if (!p.isOut()) {
				// Draw the pipe
				BufferedImage img;
				if (p.getHeight() > p.getImage().getHeight()) {
					img = p.getImage();
				} else {
					// Crop image from the top rather than the bottom
					img = p.getImage().getSubimage(0, p.getImage().getHeight() - p.getHeight(), 
							                       p.getWidth(), p.getHeight());
				}
				
				g.drawImage(img, p.getX(), p.getY(), 
						    p.getWidth(), p.getHeight(), null);
			}
		}
		
		g.setColor(new Color(255,255,255));
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		g.drawString("Max Score: " + this.maxScore, 20, 20);
		g.drawString("Score: " + this.score, 20, 20 + g.getFontMetrics().getHeight());
		g.drawString("Generation: " + this.genNumber, 20, 20 + g.getFontMetrics().getHeight() * 2);
		g.drawString("Alive: " + this.birdsAlive, 20, 20 + g.getFontMetrics().getHeight() * 3);
	}
	
	@Override
	public void update(Graphics g) {
		paintComponent(g);
	}
	
	private void restart() {
		if (this.maxScore < this.score) this.maxScore = this.score;
		this.score = 0;
		
		this.backgroundx = 0;
		
		this.pipes.clear();
		this.interval = 0;
		
		this.neuro.nextGeneration();
		this.genNumber++;
		
		this.birds.clear();
		
		for (int i = 0; i < this.neuro.generationSize(); i++) {
			Bird b = new Bird(this.width / 5, this.height / 2);
			this.birds.add(b);
		}
		
		this.birdsAlive = this.birds.size();
		
	}

}
