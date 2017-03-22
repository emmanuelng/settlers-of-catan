package catan.settlers.client.view.setup;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import catan.settlers.client.view.ClientWindow;

public class SetupWindow extends JFrame {

	private static final String WINDOW_TITLE = "Settlers of Catan";

	private static final long serialVersionUID = 1L;
	private JPanel currentPanel;

	public SetupWindow() {
		// Setup look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// Ignore
		}

		// Setup the window
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(ClientWindow.WINDOW_WIDTH, ClientWindow.WINDOW_HEIGHT);
		setResizable(false);
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

	public void setWindowVisible(boolean arg0) {
		setVisible(arg0);
	}
}
