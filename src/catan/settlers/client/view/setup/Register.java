package catan.settlers.client.view.setup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.commands.RegisterCommand;

public class Register extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton registerButton;
	private JButton backButton;
	private JLabel usernameLabel, passwordLabel, confirmLabel;
	private JTextField usernameTextField;
	private JPasswordField passwordTextField, confirmTextField;

	public Register() {
		usernameLabel = new JLabel("Username: ");
		usernameTextField = new JTextField(15);

		passwordLabel = new JLabel("Password");
		passwordTextField = new JPasswordField(15);

		confirmLabel = new JLabel("Confirm password");
		confirmTextField = new JPasswordField(15);

		registerButton = new JButton("Register");
		backButton = new JButton("Go back");

		add(usernameLabel);
		add(usernameTextField);
		add(passwordLabel);
		add(passwordTextField);
		add(confirmLabel);
		add(confirmTextField);

		add(registerButton);
		add(backButton);

		registerButton.addActionListener(this);
		backButton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent arg0) {
		JFrame topFrame = MainFrame.getInstance();

		if (arg0.getSource() == registerButton) {
			String u = usernameTextField.getText();
			String p = String.valueOf(passwordTextField.getPassword());

			// send a register query to server
			ClientModel.instance.sendCommand(new RegisterCommand(u, p));

		} else if (arg0.getSource() == backButton) {
			topFrame.remove(this);
			topFrame.dispose();
			new Login();
		}
	}
}
