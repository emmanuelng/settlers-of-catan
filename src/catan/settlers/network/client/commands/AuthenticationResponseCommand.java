package catan.settlers.network.client.commands;

import javax.swing.JOptionPane;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.setup.MainMenu;

public class AuthenticationResponseCommand implements ServerToClientCommand {

	public static enum Status {
		SUCCESS, INVALID_CREDENTIALS, ALREADY_CONNECTED
	}

	private static final long serialVersionUID = 1L;
	private Status status;
	private String username;

	public AuthenticationResponseCommand(String username, Status success) {
		this.status = success;
		this.username = username;
	}

	@Override
	public void execute() {
		if (status == Status.SUCCESS) {
			ClientModel.instance.setUsername(username);
			ClientWindow.getInstance().getSetupWindow().setScreen(new MainMenu());
		} else if (status == Status.INVALID_CREDENTIALS) {
			JOptionPane.showMessageDialog(null, "Incorrect login/password", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "You cannot connect twice", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
