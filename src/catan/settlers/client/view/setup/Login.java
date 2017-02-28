package catan.settlers.client.view.setup;

import javax.swing.*;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.commands.AuthenticationCommand;

import java.awt.*;
import java.awt.event.*;

public class Login implements ActionListener {

	private JFrame frame;
	private JButton Login;
	private JButton Register;
	private JPanel loginPanel;
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

		loginPanel = new JPanel();
		loginPanel.add(label1);
		loginPanel.add(username);
		loginPanel.add(label2);
		loginPanel.add(password);
		loginPanel.add(Login);
		loginPanel.add(Register);
		frame.add(loginPanel, BorderLayout.CENTER);

		Login.addActionListener(this);
		Register.addActionListener(this);

		frame.setTitle("Settlers Of Catan Login");
		frame.setContentPane(loginPanel);
		frame.setSize(1920, 1080);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == Login) {
			String u = username.getText();
			String p = String.valueOf(password.getPassword());
			MainFrame.getInstance().remove(loginPanel);
			ClientModel.instance.sendCommand(new AuthenticationCommand(u,p));

		} else if (arg0.getSource() == Register) {
			Register register = new Register();
			frame.remove(loginPanel);
			frame.add(register.getPanel(), BorderLayout.CENTER);
			frame.setTitle("Settlers Of Catan - Register");
			frame.setContentPane(register.getPanel());
		}
		frame.validate();
		frame.repaint();
	}

	public JPanel getPanel() {
		return loginPanel;
	}
}

class LoginInit {
	public static void main(String arg[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Login();
			}
		});
	}
}
