package catan.settlers.client.view.setup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.ClientWindow;

public class ConnectScreen extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton connectButton;
	private JTextField ipAddressTextField, portNumberTextField;
	private JLabel ipAdressLabel, portNumberLabel;

	public ConnectScreen() {
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

		connectButton.addActionListener(this);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			// Get the data from the text fields
			String ip = ipAddressTextField.getText();
			int port = Integer.parseInt(portNumberTextField.getText());
			
			// Connect
			ClientModel.instance.getNetworkManager().connect(ip, port);
			ClientWindow.getInstance().getSetupWindow().setScreen(new Login());

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error: Cannot connect to the server");
		}
	}
}
