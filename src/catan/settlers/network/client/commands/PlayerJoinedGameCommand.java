package catan.settlers.network.client.commands;

import java.util.ArrayList;

import catan.settlers.client.view.setup.MainFrame;
import catan.settlers.client.view.setup.WaitingRoom;

public class PlayerJoinedGameCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;

	private ArrayList<String> currentListOfPlayers;
	private int gameID;

	public PlayerJoinedGameCommand(ArrayList<String> currentListOfPlayers, int gameID) {
		this.currentListOfPlayers = currentListOfPlayers;
		this.gameID = gameID;
	}

	@Override
	public void execute() {
		System.out.println("PlayerJoinedGameCommand.execute()");
		WaitingRoom room = new WaitingRoom(currentListOfPlayers, gameID);
		MainFrame.getInstance().switchScreen(room);
	}

}
