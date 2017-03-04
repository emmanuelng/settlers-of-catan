package catan.settlers.client.view.setup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.commands.AuthenticationCommand;

public class Login extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JButton Login;
	private JButton Register;
	private JLabel label1, label2;
	private JTextField username;
	private JPasswordField password;

	public Login() {
		label1 = new JLabel();
		label1.setText("Username: ");
		username = new JTextField(15);

		label2 = new JLabel();
		label2.setText("Password: ");
		password = new JPasswordField(15);

		Login = new JButton("Login");
		Register = new JButton("Register");

		add(label1);
		add(username);
		add(label2);
		add(password);
		add(Login);
		add(Register);

		Login.addActionListener(this);
		Register.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == Login) {
			String u = username.getText();
			String p = String.valueOf(password.getPassword());
			ClientModel.instance.getNetworkManager().sendCommand(new AuthenticationCommand(u, p));

		} else if (arg0.getSource() == Register) {
			Register register = new Register();
			ClientWindow.getInstance().setScreen(register);
		}
	}
}
