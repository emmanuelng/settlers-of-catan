package catan.settlers.server.model.game.handlers.set;

import catan.settlers.network.client.commands.game.CurrentPlayerChangedCommand;
import catan.settlers.network.client.commands.game.UpdateGameBoardCommand;
import catan.settlers.network.client.commands.game.cards.IntrigueCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.map.Edge;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Knight;

public class IntrigueSetHandler extends SetOfOpponentMove {

	private static final long serialVersionUID = 8586344636083436039L;
	private boolean isMoveKnightPhase;
	private Knight selectedKnight;

	public IntrigueSetHandler() {
		this.isMoveKnightPhase = false;
	}

	@Override
	public void handle(Game game, Player sender, TurnData data) {
		if (!contains(sender))
			return;

		if (!isMoveKnightPhase) {
			selectKnightPhase(game, sender, data);
		} else {
			moveKnightPhase(game, sender, data);
		}
	}

	private void selectKnightPhase(Game game, Player sender, TurnData data) {
		if (data.getIntersectionSelection() != null) {
			int selectedId = data.getIntersectionSelection().getId();
			Intersection selected = game.getGameBoardManager().getBoard().getIntersectionById(selectedId);

			if (selected != null) {
				IntersectionUnit unit = selected.getUnit();
				if (unit instanceof Knight) {
					if (unit.getOwner() != sender) {
						boolean isValid = false;
						for (Edge edge : selected.getEdges()) {
							if (edge.getOwner() == sender) {
								isValid = true;
								break;
							}
						}

						if (isValid) {
							// The selected knight is a valid one
							Knight knight = (Knight) unit;
							Player owner = knight.getOwner();
							this.selectedKnight = knight;

							playerResponded(sender);
							waitForPlayer(owner);

							game.sendToAllPlayers(new IntrigueCommand(owner.getUsername(), (Knight) unit));
							isMoveKnightPhase = true;
							return;
						}
					}
				}
			}
		}

		sender.sendCommand(new IntrigueCommand(sender.getUsername()));
	}

	private void moveKnightPhase(Game game, Player sender, TurnData data) {
		int selectedId = data.getIntersectionSelection().getId();
		Intersection selected = game.getGameBoardManager().getBoard().getIntersectionById(selectedId);

		if (selected != null) {
			if (selectedKnight.canCanMoveIntersecIds().contains(selectedId)) {
				Intersection prevKnightPos = selectedKnight.getLocatedAt();

				prevKnightPos.setUnit(null);
				selected.setUnit(selectedKnight);
				game.sendToAllPlayers(new UpdateGameBoardCommand(game.getGameBoardManager().getBoardDeepCopy()));

				game.sendToAllPlayers(new CurrentPlayerChangedCommand(game.getCurrentPlayer().getUsername()));
				game.setCurSetOfOpponentMove(null);
				return;
			}
		}

		sender.sendCommand(new IntrigueCommand(sender.getUsername()));
	}

}
