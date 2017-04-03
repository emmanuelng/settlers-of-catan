package catan.settlers.client.view.game.actions;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Knight;

public class MoveKnightAction implements Action {

	@Override
	public boolean isPossible() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		Intersection selectedIntersec = gsm.getSelectedIntersection();

		if (selectedIntersec != null) {
			IntersectionUnit unit = selectedIntersec.getUnit();
			if (unit != null) {
				return unit instanceof Knight;
			}
		}

		return false;
	}

	@Override
	public String getDescription() {
		return "Move knight";
	}

	@Override
	public void perform() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setMoveKnightMode(true);
		
		Intersection selectedIntersec = gsm.getSelectedIntersection();
		Knight knight = (Knight) selectedIntersec.getUnit();
		gsm.setCanMoveKnightIntersecIds(knight.canCanMoveIntersecIds());
	}

}
