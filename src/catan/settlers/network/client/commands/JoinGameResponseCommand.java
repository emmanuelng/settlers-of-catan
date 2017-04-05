package catan.settlers.network.client.commands;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.setup.WaitingRoom;
import catan.settlers.server.model.Game;

public class JoinGameResponseCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1004798897285071471L;
	private boolean success;
	private ArrayList<String> currentListOfPlayers;
	private int gameID;
	private int readyPlayers;
	private int maxPlayers;
	private String message;

	public JoinGameResponseCommand(boolean success, String message, Game game) {
		if (success) {
			this.success = success;
			this.currentListOfPlayers = game.getPlayersManager().getParticipantsUsernames();
			this.gameID = game.getGameId();
			this.readyPlayers = game.getPlayersManager().getNbOfReadyPlayers();
			this.maxPlayers = Game.MAX_NB_OF_PLAYERS;
		} else {
			this.success = false;
			this.message = message;
		}
	}

	@Override
	public void execute() {
		if (success) {
			WaitingRoom room = new WaitingRoom(currentListOfPlayers, gameID, readyPlayers, maxPlayers);
			ClientWindow.getInstance().getSetupWindow().setScreen(room);
			ClientModel.instance.joinGameSuccess(gameID);
		} else {
			JOptionPane.showMessageDialog(new JLabel(), message);
		}
	}
}
