package catan.settlers.server.model.game.handlers.set;

import catan.settlers.network.client.commands.game.CurrentPlayerChangedCommand;
import catan.settlers.network.client.commands.game.UpdateGameBoardCommand;
import catan.settlers.network.client.commands.game.cards.InventorCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.map.Hexagon;

public class InventorSetHandler extends SetOfOpponentMove {

	private static final long serialVersionUID = 3395994052301019095L;
	private Hexagon hex1, hex2;

	@Override
	public void handle(Game game, Player sender, TurnData data) {
		if (!contains(sender))
			return;

		Hexagon selectedHex = data.getSelectedHex(game.getGameBoardManager().getBoard());
		if (selectedHex != null) {
			if (hex1 == null) {
				hex1 = selectedHex;
			} else if (hex2 == null) {
				hex2 = selectedHex;

				int hex2Nb = hex2.getNumber();
				hex2.setNumber(hex1.getNumber());
				hex1.setNumber(hex2Nb);

				game.sendToAllPlayers(new UpdateGameBoardCommand(game.getGameBoardManager().getBoardDeepCopy()));
				game.setCurSetOfOpponentMove(null);
				game.sendToAllPlayers(new CurrentPlayerChangedCommand(game.getCurrentPlayer().getUsername()));
				return;
			}
		}

		sender.sendCommand(new InventorCommand(sender.getUsername()));
	}

}
