package catan.settlers.server.model.game.handlers;

import java.io.Serializable;
import java.util.ArrayList;

import catan.settlers.network.client.commands.game.ChooseProgressCardCommand;
import catan.settlers.network.client.commands.game.FishResourceCommand;
import catan.settlers.network.client.commands.game.UpdateFishCommand;
import catan.settlers.network.client.commands.game.UpdateGameBoardCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.client.commands.game.fish.FishStealResourceCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.game.handlers.set.FishDrawResourceSetHandler;
import catan.settlers.server.model.game.handlers.set.FishStealResourceSetHandler;

public class FishHandler implements Serializable {

	private static final long serialVersionUID = 2035231735693549521L;
	private Game game;

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
			stealResource(sender);
			break;
		case DRAWRESOURCE:
			drawResource(sender);
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

	private void drawResource(Player sender) {
		FishDrawResourceSetHandler set = new FishDrawResourceSetHandler();
		set.waitForPlayer(sender);
		game.setCurSetOfOpponentMove(set);
		
		game.sendToAllPlayers(new FishResourceCommand(sender.getUsername()));
		
		sender.removeFish(4);
		sender.sendCommand(new UpdateFishCommand(sender.getNumFish()));
	}

	private void stealResource(Player sender) {
		FishStealResourceSetHandler set = new FishStealResourceSetHandler();
		set.waitForPlayer(sender);
		game.setCurSetOfOpponentMove(set);

		ArrayList<String> choosablePlayers = game.getPlayersManager().getParticipantsUsernames();
		choosablePlayers.remove(sender.getUsername());

		sender.sendCommand(new FishStealResourceCommand(sender.getUsername(), choosablePlayers));
		sender.removeFish(3);
		sender.sendCommand(new UpdateFishCommand(sender.getNumFish()));
	}
}
