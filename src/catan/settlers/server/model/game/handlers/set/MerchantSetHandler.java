package catan.settlers.server.model.game.handlers.set;

import catan.settlers.network.client.commands.game.UpdateGameBoardCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.map.GameBoard;
import catan.settlers.server.model.map.Hexagon;

public class MerchantSetHandler extends SetOfOpponentMove{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7676871129265636416L;
	private Player playerWhoSelectsHex;
	private Hexagon selectedHex;
	
	public MerchantSetHandler(Game game, Player playerWhoSelectsHex){

		this.playerWhoSelectsHex = playerWhoSelectsHex;
	}
	
	@Override
	public void handle(Game game, Player sender, TurnData data) {
		GameBoard board = game.getGameBoardManager().getBoard();
		if (sender == playerWhoSelectsHex && selectedHex == null) {
			// Response from the person who gives resources
			this.selectedHex = data.getSelectedHex();
			board.setMerchantHex(selectedHex);
			game.setMerchantOwner(sender);
			
		}
		if (allPlayersResponded()) {
			// End of move
			game.setCurSetOfOpponentMove(null);
			game.sendToAllPlayers(new UpdateGameBoardCommand(game.getGameBoardManager().getBoardDeepCopy()));
		} 
		this.selectedHex = null;
	}

}
