package catan.settlers.server.model.game.handlers.set;

import catan.settlers.network.client.commands.game.CurrentPlayerChangedCommand;
import catan.settlers.network.client.commands.game.UpdateGameBoardCommand;
import catan.settlers.network.client.commands.game.cards.DeserterCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Knight;

public class DeserterSetHandler extends SetOfOpponentMove {

	private static final long serialVersionUID = 5395876342915355998L;

	@Override
	public void handle(Game game, Player sender, TurnData data) {
		if (!contains(sender))
			return;

		if (data.getIntersectionSelection() != null) {
			int intersectionId = data.getIntersectionSelection().getId();
			Intersection selected = game.getGameBoardManager().getBoard().getIntersectionById(intersectionId);

			if (selected != null) {
				IntersectionUnit unit = selected.getUnit();
				if (unit instanceof Knight && unit.getOwner() != sender) {
					unit.setOwner(sender);
					game.sendToAllPlayers(new UpdateGameBoardCommand(game.getGameBoardManager().getBoardDeepCopy()));

					game.setCurSetOfOpponentMove(null);
					game.sendToAllPlayers(new CurrentPlayerChangedCommand(game.getCurrentPlayer().getUsername()));
					return;
				}
			}
		}

		sender.sendCommand(new DeserterCommand(sender.getUsername()));
	}

}
