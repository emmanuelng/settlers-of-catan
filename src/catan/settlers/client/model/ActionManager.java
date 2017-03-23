package catan.settlers.client.model;

import java.util.ArrayList;

import catan.settlers.client.view.game.actions.Action;
import catan.settlers.client.view.game.actions.BuildRoadAction;
import catan.settlers.client.view.game.actions.MoveRobberAction;
import catan.settlers.client.view.game.actions.PlaceSettlementAction;
import catan.settlers.client.view.game.actions.UpgradeToCityAction;

public class ActionManager {

	private ArrayList<Action> actions;

	public ActionManager() {
		this.actions = new ArrayList<Action>();
		init();
	}

	public void init() {
		actions.add(new PlaceSettlementAction());
		actions.add(new UpgradeToCityAction());
		actions.add(new BuildRoadAction());
		actions.add(new MoveRobberAction());
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
