package catan.settlers.client;

import javax.swing.JOptionPane;

import catan.settlers.client.view.setup.ConnectScreen;

public class SettlersOfCatanClient {
	
	public static void main(String arg[]) {
		try {
			new ConnectScreen();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}
