package catan.settlers.client.view.setup;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.client.Client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

public class ConnectScreen extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private Client newClient;
	private JPanel panel;
	private JButton connect;
	private JTextField IP, portNumber;
	private JLabel label1, label2;

	public ConnectScreen() {
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		label1 = new JLabel("Enter the Server Address");
		IP = new JTextField(100);

		label2 = new JLabel("Enter the port number");
		portNumber = new JTextField(5);

		connect = new JButton("connect to server");

		panel = new JPanel(new GridLayout(3, 1));
		panel.add(label1);
		panel.add(IP);
		panel.add(label2);
		panel.add(portNumber);
		panel.add(connect);

		add(panel, BorderLayout.CENTER);
		connect.addActionListener(this);
		setTitle("Server Configuration");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String i = IP.getText();
		int p = Integer.parseInt(portNumber.getText());
		try {
			ClientModel.instance.connect(i, p);
			System.out.print("Connected to server " + i + " at port " + p);
			this.dispose();
			Login login = new Login();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());

		}
	}
}
