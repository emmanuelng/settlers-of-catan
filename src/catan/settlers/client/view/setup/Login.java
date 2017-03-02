package catan.settlers.client.view.setup;

import javax.swing.*;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.commands.AuthenticationCommand;

import java.awt.*;
import java.awt.event.*;

public class Login extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JFrame frame;

	private JButton Login;
	private JButton Register;
	private JLabel label1, label2;
	private JTextField username;
	private JPasswordField password;

	public Login() {
		frame = MainFrame.getInstance();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
		
		frame.add(this, BorderLayout.CENTER);

		Login.addActionListener(this);
		Register.addActionListener(this);

		frame.setTitle("Settlers Of Catan Login");
		frame.setContentPane(this);
		frame.setSize(1920, 1080);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == Login) {
			String u = username.getText();
			String p = String.valueOf(password.getPassword());

			ClientModel.instance.sendCommand(new AuthenticationCommand(u, p));
			/*
			 * MainMenu menu = new MainMenu(u);
			 * MainFrame.getInstance().add(menu.getPanel(),
			 * BorderLayout.CENTER);
			 * MainFrame.getInstance().setContentPane(menu.getPanel());
			 */

		} else if (arg0.getSource() == Register) {
			Register register = new Register();
			MainFrame.getInstance().switchScreen(register);
		}
		frame.validate();
		frame.repaint();
	}
}
