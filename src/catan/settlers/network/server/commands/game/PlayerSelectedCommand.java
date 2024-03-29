package catan.settlers.network.server.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.GameStateManager.SelectionReason;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.TurnData.TurnAction;

public class PlayerSelectedCommand implements ClientToServerCommand {

	private static final long serialVersionUID = -4364802968992063390L;
	private int gameId;
	private TurnData data;

	public PlayerSelectedCommand(String username, SelectionReason reason) {
		this.gameId = ClientModel.instance.getGameStateManager().getGameId();
		this.data = new TurnData(TurnAction.PLAYER_SELECTED);
		data.setSelectedPlayer(username);
		data.setSelectionReason(reason);
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setShowSelectPlayerMenu(false);
	}

	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);
		game.receiveResponse(sender.getCredentials(), data);
	}

}
