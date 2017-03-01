package catan.settlers.client.view.setup;

import javax.swing.*;

import org.omg.PortableInterceptor.ClientRequestInfo;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.commands.RegisterCommand;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register implements ActionListener {
	
	private JButton RegisterButton;
	private JButton backToLogin;
	private JPanel registerPanel;
	private JLabel label1, label2, label3, label4;
	private JTextField username;
	private JPasswordField password, cpassword;

	Register() {
		registerPanel = new JPanel();

		label1 = new JLabel();
		label1.setText("Username: ");
		username = new JTextField(15);

		label2 = new JLabel();
		label2.setText("Password: ");
		password = new JPasswordField(15);

		label3 = new JLabel();
		label3.setText("Confirm Password: ");
		cpassword = new JPasswordField(15);

		RegisterButton = new JButton("Register");
		backToLogin = new JButton("Go back");

		registerPanel = new JPanel();
		registerPanel.add(label1);
		registerPanel.add(username);
		registerPanel.add(label2);
		registerPanel.add(password);
		registerPanel.add(label3);
		registerPanel.add(cpassword);

		registerPanel.add(RegisterButton);
		registerPanel.add(backToLogin);

		RegisterButton.addActionListener(this);
		backToLogin.addActionListener(this);
	}

	public JPanel getPanel() {
		return registerPanel;
	}

	public void actionPerformed(ActionEvent arg0) {
		JFrame topFrame = MainFrame.getInstance();

		if (arg0.getSource() == RegisterButton) {
			String u = username.getText();
			String p = String.valueOf(password.getPassword());
			
			// send a register query to server
			ClientModel.instance.sendCommand(new RegisterCommand(u, p));
			
		} else if (arg0.getSource() == backToLogin) {
			topFrame.remove(registerPanel);
			topFrame.dispose();
			Login login = new Login();
		}
	}
}
