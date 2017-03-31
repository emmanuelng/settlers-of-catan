package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.server.model.Game.GamePhase;

public class GamePhaseChangedCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private GamePhase currentPhase;

	public GamePhaseChangedCommand(GamePhase currentPhase) {
		this.currentPhase = currentPhase;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setCurrentPhase(currentPhase);
	}

}
