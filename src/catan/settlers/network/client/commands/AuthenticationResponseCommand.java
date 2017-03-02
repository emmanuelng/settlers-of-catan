package catan.settlers.network.client.commands;

import javax.swing.JOptionPane;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.setup.ClientWindow;
import catan.settlers.client.view.setup.MainMenu;

public class AuthenticationResponseCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private boolean success;
	private String username;

	public AuthenticationResponseCommand(String username, boolean success) {
		this.success = success;
		this.username = username;
	}

	@Override
	public void execute() {
		if (success) {
			ClientModel.instance.setUsername(username);
			ClientWindow.getInstance().setScreen(new MainMenu());
		} else {
			JOptionPane.showMessageDialog(null, "Incorrect login/password", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
