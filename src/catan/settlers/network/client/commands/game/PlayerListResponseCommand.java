package catan.settlers.network.client.commands.game;

import java.util.ArrayList;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.network.client.commands.ServerToClientCommand;

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
