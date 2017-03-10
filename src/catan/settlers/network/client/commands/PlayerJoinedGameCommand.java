package catan.settlers.network.client.commands;

import java.util.ArrayList;

import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.setup.WaitingRoom;

public class PlayerJoinedGameCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;

	private ArrayList<String> currentListOfPlayers;
	private int gameID;
	private int readyPlayers;
	private int maxPlayers;

	public PlayerJoinedGameCommand(ArrayList<String> currentListOfPlayers, int gameID, int readyPlayers,
			int maxPlayers) {
		this.currentListOfPlayers = currentListOfPlayers;
		this.gameID = gameID;
		this.readyPlayers = readyPlayers;
		this.maxPlayers = maxPlayers;
	}

	@Override
	public void execute() {
		WaitingRoom room = new WaitingRoom(currentListOfPlayers, gameID, readyPlayers, maxPlayers);
		ClientWindow.getInstance().getSetupWindow().setScreen(room);
	}

}
