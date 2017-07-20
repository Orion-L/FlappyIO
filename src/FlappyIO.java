import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyIO {

	public static void main(String[] args) {
		// Background image, window width and height
		ImageIcon backgroundImage = new ImageIcon("img/background.png");
		int windowWidth = backgroundImage.getIconWidth() + 250;
		int windowHeight = backgroundImage.getIconHeight() + 100;
		
		// Set up game window
		JFrame gameWindow =  new JFrame();
		gameWindow.setTitle("FlappyIO");
		gameWindow.setSize(windowWidth, windowHeight);
		gameWindow.setResizable(false);
		gameWindow.setVisible(true);
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Start the game
		Game g = new Game(windowWidth, windowHeight, gameWindow.getGraphics());
		g.tick();
		
		/**
		KeyListener flap = new KeyListener() {
			public void keyPressed(KeyEvent e) {
				g.flap();
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					g.flap();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		
		gameWindow.addKeyListener(flap);**/
		
		// Set the tick timer
		int tickRate = 1000 / 60;
		ActionListener tick = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				g.tick();
			}
		};

		new Timer(tickRate, tick).start();
		
		tickRate = 1000 / 60;
		ActionListener drawfps = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				g.draw();
			}
		};

		new Timer(tickRate, drawfps).start();
	}

}
