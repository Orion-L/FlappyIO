
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import neuroevolver.Neuroevolver;


public class Game extends JPanel implements ActionListener {
	private static final long serialVersionUID = 5676566905984384798L;
	
	// Game constants
	private static final int PIPE_RATE = 90;
	private static final int HOLE_SIZE = 120;
	private static final int PIPE_MIN_LENGTH = 50;
	private static final double BACKGROUND_SPEED = 0.5;
	
	
	// Neural evolver constants
	private static final int GEN_SIZE = 50;
	private static final int GEN_CHILDREN = 1;
	private static final double CROSSOVER = 0.5;
	private static final double MUTATE_RATE = 0.1;
	private static final double MUTATE_RANGE = 0.2;
	private static final double ELITISM = 0.2;
	private static final double RANDOM_RATE = 0.25;
	private static final int INPUT_SIZE = 2;
	private static final int[] HIDDEN = {2};
	private static final int OUTPUT_SIZE = 1;
	private static final double FLAP_THRESHOLD = 0.5;
	
	private ArrayList<Bird> birds;
	private ArrayList<Pipe> pipes;
	private int score, tickCount, 
		birdsAlive, genNumber, maxScore, 
		width, height;
	private double backgroundOffset;
	private BufferedImage background;
	private Neuroevolver neuro;
	
	/**
	 * Initialise a new game
	 * @param width The width of the game window
	 * @param height The height of the game window
	 */
	public Game(int width, int height) {
		// Birds and pipes lists
		this.birds = new ArrayList<Bird>();
		this.pipes = new ArrayList<Pipe>();
		
		this.score = 0;
		this.maxScore = 0;
		
		// Current tick number
		this.tickCount = 0;
		
		// Current background offset
		this.backgroundOffset = 0;
		
		// Game width, height and frame
		this.width = width;
		this.height = height;
		
		// Background image
		try {
			this.background = ImageIO.read(new File("img/background.png"));
		} catch (Exception e) {
			System.out.println(e);
		}
		
		// Initalise the neuroevolver
		this.neuro = new Neuroevolver(GEN_SIZE, GEN_CHILDREN, CROSSOVER, 
			MUTATE_RATE, MUTATE_RANGE, ELITISM, RANDOM_RATE, INPUT_SIZE, 
			OUTPUT_SIZE, HIDDEN);
		
		// Generate the first generation and corresponding birds
		this.neuro.nextGeneration();
		this.genNumber = 1;
		
		for (int i = 0; i < this.neuro.generationSize(); i++) {
			Bird b = new Bird(width / 5, height / 2);
			this.birds.add(b);
		}
		
		this.birdsAlive = this.birds.size();
		
		// Set the game window layout to border layout
		this.setLayout(new BorderLayout());
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
						if (p.getX() + p.getWidth() > b.getX()) {
							nextHole = p.getHeight() + (HOLE_SIZE / 2);
						}
						
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
				
				double[] inputs = {((double) (this.height - b.getY())) / this.height, 
					(this.height - nextHole) / this.height};
				
				if (this.neuro.evaluateGenome(i, inputs)[0] > FLAP_THRESHOLD) {
					b.flap();
				}
				
				if (b.getY() >= this.height || b.getY() + b.getHeight() <= 0) {
					// Bird is above or below game screen, kill it
					this.birdsAlive--;
					this.neuro.updateScore(i, this.score);
					b.kill();
				} else {
					for (int j = 0; j < this.pipes.size(); j++) {
						Pipe p = this.pipes.get(j);
                        if (b.getX() < p.getX() + p.getWidth() 
                         && b.getX() + b.getWidth() > p.getX()
                         && b.getY() < p.getY() + p.getHeight() 
                         && b.getY() + b.getHeight() > p.getY()) {
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
	
		if (this.tickCount % PIPE_RATE == 0) {
			// Time to spawn new pipe
			// Generate hole position
			int pos = (int) (Math.round(Math.random() 
				* (this.height - PIPE_MIN_LENGTH * 2 - HOLE_SIZE)) 
				+ PIPE_MIN_LENGTH);
			
			// Create the pipes
			Pipe p = new Pipe(this.width, 0, pos, true);
			this.pipes.add(p);
			
			p = new Pipe(this.width, pos + HOLE_SIZE, this.height, false);
			this.pipes.add(p);
		}
		
		this.tickCount++;
		this.score++;
		
		if (this.score > this.maxScore) this.maxScore = this.score;
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
						(int)(i * this.background.getWidth() - this.backgroundOffset), 
						0, this.background.getWidth(), this.height, null);
		}
		
		this.backgroundOffset = (this.backgroundOffset + BACKGROUND_SPEED) % this.background.getWidth();
		
		// Loop through all birds
		for (int i = 0; i < this.birds.size(); i++) {
			Bird b = this.birds.get(i);
					
			// Consider only alive birds
			if (b.isAlive()) {
				// Rotate the bird depending on its heading
				AffineTransform tx = AffineTransform.getRotateInstance(Math.PI / 2 * (b.getVelocity() / 20), 
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
		this.tickCount = 0;
		this.backgroundOffset = 0;
		this.pipes.clear();
		this.birds.clear();
		
		this.neuro.nextGeneration();
		this.genNumber++;
		
		for (int i = 0; i < this.neuro.generationSize(); i++) {
			Bird b = new Bird(this.width / 5, this.height / 2);
			this.birds.add(b);
		}
		
		this.birdsAlive = this.birds.size();
		
	}
}
