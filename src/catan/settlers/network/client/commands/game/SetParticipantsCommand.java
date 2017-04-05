package catan.settlers.network.client.commands.game;

import java.util.ArrayList;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class SetParticipantsCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 4774282974258390088L;
	private ArrayList<String> players;

	public SetParticipantsCommand(ArrayList<String> playerList) {
		this.players = playerList;
	}

	@Override
	public void execute() {
		ClientModel.instance.getGameStateManager().setParticipants(players);
	}

}
