package catan.settlers.client.view.setup;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static MainFrame instance = null;
	private JPanel currentPanel;

	private MainFrame() {

	}

	public static MainFrame getInstance() {
		if (instance == null) {
			instance = new MainFrame();
		}
		return instance;
	}
	
	public void switchScreen(JPanel panel){
		if(currentPanel != null){
			remove(currentPanel);
			revalidate();
			repaint();
		}
		else{
			remove(this.getContentPane());
			revalidate();
			repaint();
		}
		currentPanel= panel;
		setContentPane(panel);
		revalidate();
		repaint();
	}
}
