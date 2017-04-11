package catan.settlers.server.model.game.handlers;

import java.io.Serializable;

import catan.settlers.network.client.commands.game.ChooseProgressCardCommand;
import catan.settlers.network.client.commands.game.FishResourceCommand;
import catan.settlers.network.client.commands.game.SelectPlayerToStealFromCommand;
import catan.settlers.network.client.commands.game.UpdateFishCommand;
import catan.settlers.network.client.commands.game.UpdateGameBoardCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;

public class FishHandler implements Serializable {

	private static final long serialVersionUID = 2035231735693549521L;
	private Game game;
	private Player currentPlayer;

	public FishHandler(Game game) {
		this.game = game;
	}

	public enum FishAction {
		REMOVEROBBER, STEALRESOURCE, DRAWRESOURCE, BUILDROAD, PROGRESSCARD
	}

	public void handle(Player sender, FishAction action) {
		switch (action) {
		case REMOVEROBBER:
			game.getGameBoardManager().getBoard().setRobberHex(null);
			sender.removeFish(2);
			break;
		case STEALRESOURCE:
			sender.sendCommand(new SelectPlayerToStealFromCommand());
			sender.removeFish(3);
			break;
		case DRAWRESOURCE:
			sender.sendCommand(new FishResourceCommand());
			sender.removeFish(4);
			break;
		case BUILDROAD:
			sender.addFreeRoad();
			sender.removeFish(5);
			break;
		case PROGRESSCARD:
			sender.sendCommand(new ChooseProgressCardCommand());
			sender.removeFish(7);
			break;
		default:
			break;
		}
		sender.sendCommand(new UpdateFishCommand(sender.getNumFish()));
		for (Player p : game.getParticipants()) {
			p.sendCommand(new UpdateResourcesCommand(p.getResources()));
			p.sendCommand(new UpdateGameBoardCommand(game.getGameBoardManager().getBoardDeepCopy()));
		}
	}
}
