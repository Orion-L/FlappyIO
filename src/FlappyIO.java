import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyIO {

	public static void main(String[] args) {
		ImageIcon backgroundImage = new ImageIcon("img/background.png");
		
		int windowWidth = backgroundImage.getIconWidth() + 200;
		int windowHeight = backgroundImage.getIconHeight();
		
		JFrame gameWindow =  new JFrame();
		gameWindow.setTitle("FlappyIO");
		gameWindow.setSize(windowWidth, windowHeight);
		gameWindow.setResizable(false);
		gameWindow.setVisible(true);
		
		Game g = new Game(windowWidth, windowHeight, gameWindow.getGraphics());
		g.tick();
		
		int tickRate = 1000 / 60;
		
		ActionListener tick = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				g.tick();
			}
		};
		
		
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
		
		gameWindow.addKeyListener(flap);
		new Timer(tickRate, tick).start();
		
		/**
		JLabel backLabel = new JLabel(backgroundImage);
		gameWindow.add(backLabel);
		gameWindow.setVisible(true);
		**/
	}

}
