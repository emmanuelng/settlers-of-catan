package catan.settlers.network.client.commands;

import java.util.ArrayList;

import catan.settlers.client.view.ClientWindow;

public class PlayerListResponseCommand implements ServerToClientCommand {
	
	private static final long serialVersionUID = 1L;
	private ArrayList<String> players;
	
	public PlayerListResponseCommand(ArrayList<String> playerList){
		this.players=playerList;
	}
	@Override
	public void execute() {
		ClientWindow.getInstance().getGameWindow().setListOfPlayers(players);
	}

}
