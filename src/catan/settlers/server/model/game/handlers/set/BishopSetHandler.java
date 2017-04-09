package catan.settlers.server.model.game.handlers.set;

import catan.settlers.network.client.commands.game.CurrentPlayerChangedCommand;
import catan.settlers.network.client.commands.game.UpdateGameBoardCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.client.commands.game.cards.BishopCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.map.GameBoard;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Hexagon.TerrainType;

public class BishopSetHandler extends SetOfOpponentMove {

	private static final long serialVersionUID = 434541760184447827L;

	@Override
	public void handle(Game game, Player sender, TurnData data) {
		if (contains(sender)) {
			GameBoard board = game.getGameBoardManager().getBoard();
			Hexagon selectedHex = data.getSelectedHex(board);

			if (selectedHex.getType() == TerrainType.SEA) {
				game.sendToAllPlayers(new BishopCommand(sender.getUsername()));
				return;
			}

			board.setRobberHex(selectedHex);
			game.sendToAllPlayers(new UpdateGameBoardCommand(game.getGameBoardManager().getBoardDeepCopy()));

			for (Player player : selectedHex.getPlayersOnHex()) {
				if (player == sender)
					continue;

				ResourceType stolen = player.drawRandomResource();
				player.removeResource(stolen, 1);
				sender.giveResource(stolen, 1);
				player.sendCommand(new UpdateResourcesCommand(player.getResources()));
			}

			sender.sendCommand(new UpdateResourcesCommand(sender.getResources()));
			game.setCurSetOfOpponentMove(null);
			game.sendToAllPlayers(new CurrentPlayerChangedCommand(game.getCurrentPlayer().getUsername()));
		}
	}

}
