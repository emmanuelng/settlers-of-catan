package catan.settlers.client.view.setup;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class MainFrame extends JFrame {
	
	private static final int WINDOW_WIDTH = 1000;
	private static final int WINDOW_HEIGHT = 500;
	private static final String WINDOW_TITLE = "Settlers of Catan";
	
	private static final long serialVersionUID = 1L;
	private static MainFrame instance;
	private JPanel currentPanel;
	
	public static MainFrame getInstance() {
		if (instance == null) {
			instance = new MainFrame();
		}
		return instance;
	}

	private MainFrame() {
		// Setup look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// Ignore
		}
		
		// Setup the window
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setTitle(WINDOW_TITLE);
		
		// Initialize the frame with the connect screen
		setScreen(new ConnectScreen());
		setVisible(true);
	}

	public void setScreen(JPanel panel) {
		if (currentPanel != null) {
			remove(currentPanel);
		} else {
			remove(this.getContentPane());
		}
		currentPanel = panel;
		setContentPane(panel);
		revalidate();
		repaint();
	}
}
