package catan.settlers.network.client.commands;

import java.awt.BorderLayout;

import javax.swing.JOptionPane;

import catan.settlers.client.view.setup.MainFrame;
import catan.settlers.client.view.setup.MainMenu;

public class AuthResultCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private boolean success;
	private String user;
	
	public AuthResultCommand(String user, boolean success) {
		this.success = success;
		this.user=user;
	}

	@Override
	public void execute() {
		// TODO Manage authentication success/failure on client side
		if (success) {
			MainMenu menu = new MainMenu(user);
			MainFrame.getInstance().removeAll();
			MainFrame.getInstance().add(menu.getPanel(), BorderLayout.CENTER);
			MainFrame.getInstance().setContentPane(menu.getPanel());
		} else {
			System.out.println("enter valid username & password");
			JOptionPane.showMessageDialog(null, "Incorrect login/password", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

}
