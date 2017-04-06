package catan.settlers.client.model;

import java.util.ArrayList;
import java.util.Collection;

import catan.settlers.client.view.game.actions.ActivateKnightAction;
import catan.settlers.client.view.game.actions.BuildKnightAction;
import catan.settlers.client.view.game.actions.BuildRoadAction;
import catan.settlers.client.view.game.actions.DisplaceKnightAction;
import catan.settlers.client.view.game.actions.ExitMoveKnightMode;
import catan.settlers.client.view.game.actions.GameAction;
import catan.settlers.client.view.game.actions.MoveKnightAction;
import catan.settlers.client.view.game.actions.MoveRobberAction;
import catan.settlers.client.view.game.actions.PlaceSettlementAction;
import catan.settlers.client.view.game.actions.UpgradeKnightAction;
import catan.settlers.client.view.game.actions.UpgradeToCityAction;

public class ActionManager {

	private ArrayList<GameAction> tradeActions;
	private ArrayList<GameAction> politicActions;
	private ArrayList<GameAction> scienceActions;
	private ArrayList<GameAction> miscActions;
	private ArrayList<GameAction> moveKnightActions;

	public ActionManager() {
		this.tradeActions = new ArrayList<>();
		this.politicActions = new ArrayList<>();
		this.scienceActions = new ArrayList<>();
		this.miscActions = new ArrayList<>();
		this.moveKnightActions = new ArrayList<>();

		init();
	}

	public void init() {
		// Build settlement, road and ships
		tradeActions.add(new BuildRoadAction());
		tradeActions.add(new PlaceSettlementAction());

		// Build city and city walls
		politicActions.add(new UpgradeToCityAction());

		// Hire knight, promote, activate
		scienceActions.add(new BuildKnightAction());
		scienceActions.add(new UpgradeKnightAction());
		scienceActions.add(new ActivateKnightAction());

		// Other actions
		miscActions.add(new MoveKnightAction());

		// Move knight actions
		moveKnightActions.add(new DisplaceKnightAction());
		moveKnightActions.add(new ExitMoveKnightMode());
	}

	public ArrayList<GameAction> getTradeActions() {
		ArrayList<GameAction> ret = new ArrayList<>();
		ret.addAll(tradeActions);
		return ret;
	}

	public ArrayList<GameAction> getPoliticActions() {
		ArrayList<GameAction> ret = new ArrayList<>();
		ret.addAll(politicActions);
		return ret;
	}

	public ArrayList<GameAction> getScienceActions() {
		ArrayList<GameAction> ret = new ArrayList<>();
		ret.addAll(scienceActions);
		return ret;
	}

	public ArrayList<GameAction> getMiscActions() {
		ArrayList<GameAction> ret = new ArrayList<>();
		ret.addAll(miscActions);
		return ret;
	}

	public ArrayList<GameAction> getMoveKnightActions() {
		ArrayList<GameAction> ret = new ArrayList<>();
		ret.addAll(moveKnightActions);
		return ret;
	}

}
