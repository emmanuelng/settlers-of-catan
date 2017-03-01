package catan.settlers.client.view.setup;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import catan.settlers.client.model.ClientModel;

public class ConnectScreen implements ActionListener {

	private JFrame frame;
	private JPanel panel;
	private JButton connect;
	private JTextField IP, portNumber;
	private JLabel label1, label2;

	public ConnectScreen() {
		frame = MainFrame.getInstance();
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(1920, 1080);
		
		
		label1 = new JLabel("Enter the Server Address");
		IP = new JTextField(100);

		label2 = new JLabel("Enter the port number");
		portNumber = new JTextField(5);

		connect = new JButton("connect to server");

		panel = new JPanel();
		panel.add(label1);
		panel.add(IP);
		panel.add(label2);
		panel.add(portNumber);
		panel.add(connect);
		panel.setVisible(true);

		frame.add(panel, BorderLayout.CENTER);
		connect.addActionListener(this);
		frame.setTitle("Server Configuration");
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String i = IP.getText();
		int p = Integer.parseInt(portNumber.getText());
		try {
			ClientModel.instance.connect(i, p);
			System.out.print("Connected to server " + i + " at port " + p);
			Login login = new Login();
			MainFrame.getInstance().switchScreen(login.getPanel());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());

		}
	}
}
