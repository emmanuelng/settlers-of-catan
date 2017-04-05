package catan.settlers.network.client.commands;

import java.util.ArrayList;

import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.setup.WaitingRoom;
import catan.settlers.server.model.Game;

public class PlayerJoinedGameCommand implements ServerToClientCommand {

	private static final long serialVersionUID = -602502603302757569L;
	private ArrayList<String> currentListOfPlayers;
	private int gameID;
	private int readyPlayers;
	private int maxPlayers;

	public PlayerJoinedGameCommand(Game game) {
		this.currentListOfPlayers = game.getPlayersManager().getParticipantsUsernames();
		this.gameID = game.getGameId();
		this.readyPlayers = game.getPlayersManager().getNbOfReadyPlayers();
		this.maxPlayers = Game.MAX_NB_OF_PLAYERS;
	}

	@Override
	public void execute() {
		WaitingRoom room = new WaitingRoom(currentListOfPlayers, gameID, readyPlayers, maxPlayers);
		ClientWindow.getInstance().getSetupWindow().setScreen(room);
	}

}
