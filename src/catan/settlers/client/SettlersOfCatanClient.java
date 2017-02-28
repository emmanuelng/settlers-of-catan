package catan.settlers.client;

import javax.swing.JOptionPane;

import catan.settlers.client.view.setup.ConnectScreen;

public class SettlersOfCatanClient {
	
	public static void main(String arg[]) {
		try {
			ConnectScreen frame = new ConnectScreen();
			frame.setSize(300, 100);
			frame.setVisible(true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}
