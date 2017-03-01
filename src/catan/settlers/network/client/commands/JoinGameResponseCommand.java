package catan.settlers.network.client.commands;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import catan.settlers.client.view.setup.MainFrame;

public class JoinGameResponseCommand implements ServerToClientCommand {
	
	private boolean success;
	
	public JoinGameResponseCommand(boolean success) {
		this.success = success;
	}
	
	@Override
	public void execute() {
		// TODO Handle Join game result here
		if(success){
		new WaitingRoom(ArrayList<Player> participants);
		MainFrame.getInstance().remove(MainFrame.getInstance().getContentPane());
		MainFrame.getInstance().add(WaitingRoom.getPanel());
		MainFrame.getInstance().setContentPane(WaitingRoom.getPanel());
		MainFrame.getInstance().revalidate();
		MainFrame.getInstance().repaint();
		}else{
			JOptionPane.showMessageDialog(new JLabel(), "Room full");
		}
	}
}
