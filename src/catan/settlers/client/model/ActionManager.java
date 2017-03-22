package catan.settlers.client.model;

import java.util.ArrayList;

import catan.settlers.client.view.game.actions.Action;
import catan.settlers.client.view.game.actions.PlaceSettlementAction;

public class ActionManager {

	private ArrayList<Action> actions;

	public ActionManager() {
		this.actions = new ArrayList<Action>();
		init();
	}

	public void init() {
		actions.add(new PlaceSettlementAction());
	}

	public ArrayList<Action> getPossibleActions() {
		ArrayList<Action> possibleActions = new ArrayList<Action>();
		for (Action a : actions) {
			if (a.isPossible()) {
				possibleActions.add(a);
			}
		}
		return possibleActions;

	}

}
