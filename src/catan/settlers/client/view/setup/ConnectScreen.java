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

public class ConnectScreen extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JFrame frame;

	private JButton connectButton;
	private JTextField ipAddressTextField, portNumberTextField;
	private JLabel ipAdressLabel, portNumberLabel;

	public ConnectScreen() {
		frame = MainFrame.getInstance();
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(1920, 1080);

		ipAdressLabel = new JLabel("Server Address");
		ipAddressTextField = new JTextField(50);

		portNumberLabel = new JLabel("Port number");
		portNumberTextField = new JTextField(5);

		connectButton = new JButton("Connect");

		add(ipAdressLabel);
		add(ipAddressTextField);
		add(portNumberLabel);
		add(portNumberTextField);
		add(connectButton);
		
		setVisible(true);

		frame.add(this, BorderLayout.CENTER);
		connectButton.addActionListener(this);
		frame.setTitle("Server Configuration");
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String i = ipAddressTextField.getText();
		int p = Integer.parseInt(portNumberTextField.getText());
		try {
			// Connect to the server
			ClientModel.instance.connect(i, p);
			Login login = new Login();
			MainFrame.getInstance().switchScreen(login);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error: Cannot connect to the server");
		}
	}
}
