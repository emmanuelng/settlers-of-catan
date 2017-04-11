package catan.settlers.server.model.game.handlers.set;

import catan.settlers.network.client.commands.game.CurrentPlayerChangedCommand;
import catan.settlers.network.client.commands.game.UpdateGameBoardCommand;
import catan.settlers.network.client.commands.game.cards.DiplomatCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.map.Edge;

public class DiplomatSetHandler extends SetOfOpponentMove {

	private static final long serialVersionUID = -2347192342781689201L;
	private Edge previousPos;

	@Override
	public void handle(Game game, Player sender, TurnData data) {
		if (!contains(sender))
			return;

		if (data.getEdgeSelection() != null) {
			int selectedId = data.getEdgeSelection().getId();
			Edge selected = game.getGameBoardManager().getBoard().getEdgeById(selectedId);

			if (previousPos != null) {
				previousPos.setOwner(null);
				selected.setOwner(sender);

				game.sendToAllPlayers(new UpdateGameBoardCommand(game.getGameBoardManager().getBoardDeepCopy()));
				game.setCurSetOfOpponentMove(null);
				game.sendToAllPlayers(new CurrentPlayerChangedCommand(game.getCurrentPlayer().getUsername()));
				return;
			}

			if (selected.getOwner() != null) {
				if (selected.getOwner() == sender) {
					this.previousPos = selected;
					sender.sendCommand(new DiplomatCommand(sender.getUsername(), true));
					return;
				} else {
					selected.setOwner(null);
					game.setCurSetOfOpponentMove(null);
					game.sendToAllPlayers(new CurrentPlayerChangedCommand(game.getCurrentPlayer().getUsername()));
				}
			}
		}

		sender.sendCommand(new DiplomatCommand(sender.getUsername(), false));
	}

}
