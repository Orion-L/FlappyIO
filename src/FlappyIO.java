
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import flappybird.Game;
import linegraph.LineGraph;

public class FlappyIO {
	private static final int NORMAL_RATE = 1000 / 60;
	private static final int DOUBLE_RATE = 1000 / 120;
	private static final int MAX_RATE = 1000 / 300;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				initGUI();
			}
		});
	}
	
	public static void initGUI() {
		// Background image, window width and height
		ImageIcon backgroundImage = new ImageIcon("img/background.png");
		int windowWidth = backgroundImage.getIconWidth() + 250;
		int windowHeight = backgroundImage.getIconHeight();
				
		// Set up game window
		JFrame gameWindow =  new JFrame();
		gameWindow.setTitle("FlappyIO");
		gameWindow.setSize(windowWidth, windowHeight);
		gameWindow.setResizable(false);
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameWindow.setVisible(true);			
		
		// Initialise a new game and add to window
		Game g = new Game(windowWidth, windowHeight);
		gameWindow.add(g);
			
		// Set the tick timer
		final Timer t = new Timer(NORMAL_RATE, g);
		t.start();
		
		// Create speed buttons
		JButton normalSpeed = new JButton("1x");
		normalSpeed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				t.setDelay(NORMAL_RATE);
			}
		});
		
		JButton doubleSpeed = new JButton("2x");
		doubleSpeed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				t.setDelay(DOUBLE_RATE);
			}
		});
		
		
		JButton maxSpeed = new JButton("5x");
		maxSpeed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				t.setDelay(MAX_RATE);
				
			}
		});

		/*
		// Create learning mode/reset buttons
		JButton genetic = new JButton("genetic");
		genetic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				g.reset(false);
			}
			
		});

		// Doesn't work, disabled for now
		JButton backprop = new JButton("backprop");
		backprop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				g.reset(true);
			}
			
		});
		*/
		
		JButton graph = new JButton("graph");
		graph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] scores = g.getScoreHistory();
				if (scores.length < 2) return;
				
				t.stop();
				
				JFrame graphWindow =  new JFrame();
				graphWindow.setTitle("FlappyGraph");
				graphWindow.setSize(windowWidth, windowHeight);
				graphWindow.setResizable(false);				
				graphWindow.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(java.awt.event.WindowEvent windowEvent) {
						t.start();
					}
				});
				graphWindow.setVisible(true);	
				
				LineGraph lg = new LineGraph(windowWidth, windowHeight, "scores over generations", 
						                     1, 1, "generation", "score", scores);
				graphWindow.add(lg);
			}
		});
		
		JButton nextGen = new JButton("next gen");
		nextGen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				t.stop();
				g.nextGen();
				t.start();
			}
		});
		
		JButton reset = new JButton("reset");
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				g.reset(false);
			}
		});
		
		// Create bottom panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		// Create grid for buttons
		JPanel buttonGrid = new JPanel();
		buttonGrid.setOpaque(false);
		buttonGrid.setLayout(new GridLayout(1, 5, 5, 5));
		
		// Add buttons to grid
		buttonGrid.add(normalSpeed);
		buttonGrid.add(doubleSpeed);
		buttonGrid.add(maxSpeed);
		//buttonGrid.add(genetic);
		//buttonGrid.add(backprop);
		buttonGrid.add(graph);
		buttonGrid.add(nextGen);
		buttonGrid.add(reset);
		
		// Add grid to bottom panel
		buttonPanel.add(buttonGrid, BorderLayout.WEST);
		
		// Add bottom panel to game window
		g.add(buttonPanel, BorderLayout.SOUTH);
	}
}
