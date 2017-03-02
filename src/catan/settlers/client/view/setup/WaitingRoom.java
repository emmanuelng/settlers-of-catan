package catan.settlers.client.view.setup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.GameBoard;
import catan.settlers.network.server.commands.CancelJoinGameCommand;

public class WaitingRoom extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton goButton, backButton;
	private int gameId;
	

	public WaitingRoom(ArrayList<String> participants, int gameId) {
		this.gameId = gameId;

		JLabel curGameIdLabel = new JLabel("" + gameId);
		goButton = new JButton("Go");
		backButton = new JButton("Back");

		for (String s : participants) {
			add(new JLabel(s));
		}

		add(goButton);
		add(backButton);
		add(curGameIdLabel);

		goButton.addActionListener(this);
		backButton.addActionListener(this);

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == goButton) {
		
        	
        	Thread workerThread = new Thread(new Worker());
        	workerThread.start();
        	
        	ClientWindow.getInstance().setWindowVisible(false);
		} else if (arg0.getSource() == backButton) {
			ClientModel.instance.sendCommand(new CancelJoinGameCommand(gameId));
		}
	}
}

class Worker implements Runnable {
	private boolean isWaiting = true;

	public void run() {
		while (isWaiting) {
	         try {
	        	GameBoard gameWindow = new GameBoard();		
	     		gameWindow.start();
	            Thread.sleep(100);
	         } catch (InterruptedException e) {
	            // Can ignore, we'll just try again
	         }
	      }
	}
	
}
