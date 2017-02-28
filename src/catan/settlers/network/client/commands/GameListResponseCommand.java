package catan.settlers.network.client.commands;

import java.util.ArrayList;

import javax.swing.JButton;

import catan.settlers.client.view.setup.MainFrame;
import catan.settlers.server.model.Game;

public class GameListResponseCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private ArrayList<Game> games;

	public GameListResponseCommand(ArrayList<Game> pGames) {
		//this.games = games;
		this.games= new ArrayList<Game>();
		for(Game g: pGames){
			this.games.add(g);
		}
	}

	@Override
	public void execute() {
		// TODO Display the list of games
		System.out.println(games);
		for (int i=0;i<games.size();i++) {
			JButton gameButton = new JButton("Game" + i);
			MainFrame.getInstance().getContentPane().add(gameButton); // add some layout later
			MainFrame.getInstance().getContentPane().validate();
			MainFrame.getInstance().getContentPane().repaint();
		}
	}
}
