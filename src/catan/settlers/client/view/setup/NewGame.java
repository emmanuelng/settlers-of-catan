package catan.settlers.client.view.setup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import catan.settlers.server.model.Game;

public class NewGame implements ActionListener {
	private JButton back;
	private JButton gameBoard1;
	private JButton gameBoard2;
	private JButton gameBoard3;
	private JPanel newGamePanel;
	private JLabel label1;
	
	public NewGame(String user) {
		newGamePanel = new JPanel();
		
		back = new JButton("Back");
		gameBoard1 = new JButton("gameBoard1");
		gameBoard2 = new JButton("gameBoard2");
		gameBoard3 = new JButton("gameBoard3");
		label1 = new JLabel("Game Boards");
		
		newGamePanel.add(back);
		newGamePanel.add(label1);
		
		//populating the publicGame arraylist
		/*gameBoards = new ArrayList<JButton>();

		for(int i = 0;i<gameBoards.size();i++){
			gameBoards.get(i) = new JButton("Board " + i.getText());
			newGamePanel.add(i); //add some layout later
			i.addActionListener(this);
		}*/ //doesnt seem to work need some thought, gonna hard code for now
		
		newGamePanel.add(gameBoard1);
		newGamePanel.add(gameBoard2);
		newGamePanel.add(gameBoard3);
		
		back.addActionListener(this);
		gameBoard1.addActionListener(this);
		gameBoard2.addActionListener(this);
		gameBoard3.addActionListener(this);
	}

	public JPanel getPanel() {
		return newGamePanel;
	}
	
	public void actionPerformed(ActionEvent arg0){
		JFrame topFrame = MainFrame.getInstance();

		
		if(arg0.getSource()==back){
			Lobby backLobby = new Lobby(new ArrayList<Game>());
			topFrame.remove(newGamePanel);
			topFrame.add(backLobby.getPanel());
			topFrame.setContentPane(backLobby.getPanel());
		}
		/*for(int i =0;i<gameBoards.size();i++){
			if(arg0.getSource()==gameBoards.get(i)){
				//GameBoard gameBoard = new GameBoard(ArrayList<Player> players,i);
				
				topFrame.remove(newGamePanel);
				GameBoard gameBoard = new GameBoard();
			}
		}*/ //nonfunctional gonna hard code for now
		else if(arg0.getSource()==gameBoard1){
			System.out.println("triggered");
			topFrame.getContentPane().removeAll();
			topFrame.setTitle("Cattlers of Seten");
		}
		else if(arg0.getSource()==gameBoard2){
			topFrame.getContentPane().removeAll();
			topFrame.setTitle("Cattlers of Seten");
		}
		else if(arg0.getSource()==gameBoard3){
			topFrame.getContentPane().removeAll();
			topFrame.setTitle("Cattlers of Seten");
		}
		
		topFrame.validate();
		topFrame.repaint();
	}

}

