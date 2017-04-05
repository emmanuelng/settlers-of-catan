package catan.settlers.network.client.commands;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class RegistrationResultCommand implements ServerToClientCommand {

	private static final long serialVersionUID = -4187356844265663743L;
	private boolean success;
	private JLabel label1,label2;
	
	public RegistrationResultCommand(boolean success) {
		this.success = success;
	}

	@Override
	public void execute() {
		// TODO Manage registration success/failure on client side
		if(success){
			JOptionPane.showMessageDialog(label1, "Register Success");
		}else{
			JOptionPane.showMessageDialog(label2, "Invalid Username or Password");
		}
	}

}
