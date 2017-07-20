import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class FlappyIO {

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
				
		// Initialise a new game and add to window
		Game g = new Game(windowWidth, windowHeight);
		gameWindow.add(g);
		
		gameWindow.setVisible(true);
				
		// Set the tick timer
		int tickRate = 1000 / 60;

		new Timer(tickRate, g).start();
	}

}
