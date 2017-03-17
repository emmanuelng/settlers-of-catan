package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.server.model.Player;

public class CurrentPlayerChangedCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private Player player;

	public CurrentPlayerChangedCommand(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		ClientModel.instance.getGameStateManager().setCurrentPlayer(player);
	}

}
