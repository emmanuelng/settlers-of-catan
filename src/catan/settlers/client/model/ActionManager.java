package catan.settlers.client.model;

import java.util.ArrayList;

import catan.settlers.client.view.game.actions.Action;
import catan.settlers.client.view.game.actions.PlaceSettlementAction;

public class ActionManager {
	private ArrayList<Action> buildingActions;
	private ArrayList<Action> tradingActions;
	private ArrayList<Action> cardActions;
	
	public ActionManager(){
		buildingActions = new ArrayList<Action>();
		tradingActions = new ArrayList<Action>();
		cardActions = new ArrayList<Action>();
		init();
	}
	
	public void init(){
		buildingActions.add(new PlaceSettlementAction());
	}
	
	public ArrayList<Action> getPossibleActions(){
		ArrayList<Action> possibleActions = new ArrayList<Action>();
		for(Action a : buildingActions){
			if(a.isPossible()){
				possibleActions.add(a);
			}
		}
		return possibleActions;
		
	}
	
}
