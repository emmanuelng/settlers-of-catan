package catan.settlers.server.model.game.handlers;

import java.util.ArrayList;

import catan.settlers.network.client.commands.game.CurrentPlayerChangedCommand;
import catan.settlers.network.client.commands.game.MoveRobberCommand;
import catan.settlers.network.client.commands.game.SelectPlayerToStealFromCommand;
import catan.settlers.network.client.commands.game.UpdateGameBoardCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.client.commands.game.cards.BishopCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.game.handlers.set.SetOfOpponentMove;
import catan.settlers.server.model.map.GameBoard;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Hexagon.IntersectionLoc;
import catan.settlers.server.model.map.Hexagon.TerrainType;
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Village;

public class MoveRobberHandler extends SetOfOpponentMove{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1881812992580785787L;
	private boolean secondPhase;

	@Override
	public void handle(Game game, Player sender, TurnData data) {
		if (contains(sender)) {
			if(!secondPhase){
				GameBoard board = game.getGameBoardManager().getBoard();
				Hexagon selectedHex = data.getSelectedHex(board);
				if(selectedHex != null){
					if (selectedHex.getType() == TerrainType.SEA) {
						game.sendToAllPlayers(new MoveRobberCommand(false));
						return;
					}
		
					board.setRobberHex(selectedHex);
					game.sendToAllPlayers(new UpdateGameBoardCommand(game.getGameBoardManager().getBoardDeepCopy()));
				}
				
				secondPhase = true;
			}else{
				ArrayList<String> listOfStealable = new ArrayList<>();
				
				for(IntersectionLoc intloc: IntersectionLoc.values()){
					IntersectionUnit iu = game.getGameBoardManager().getBoard().getRobberHex().getIntersection(intloc).getUnit();
					if(iu instanceof Village){
						listOfStealable.add(iu.getOwner().getUsername());
					}
				}
				sender.sendCommand(new SelectPlayerToStealFromCommand(listOfStealable));
	//			for (selectedHex.getPlayersOnHex()) {
	//				if (player == sender)
	//					continue;
	//
	//				ResourceType stolen = player.drawRandomResource();
	//				player.removeResource(stolen, 1);
	//				sender.giveResource(stolen, 1);
	//				player.sendCommand(new UpdateResourcesCommand(player.getResources()));
	//			}
				
				sender.sendCommand(new UpdateResourcesCommand(sender.getResources()));
				game.setCurSetOfOpponentMove(null);
				game.sendToAllPlayers(new CurrentPlayerChangedCommand(game.getCurrentPlayer().getUsername()));
			}
		}
		
	}

}
