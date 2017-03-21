package catan.settlers.client.view.game.actions;

import catan.settlers.client.model.ClientModel;

public class PlaceSettlementAction implements Action {

	public PlaceSettlementAction(){
		
	}
	
	@Override
	public boolean isPossible() {
		if(ClientModel.instance.getGameStateManager().getSelectedIntersection()== null){
			return false;
		}else{
			return ClientModel.instance.getGameStateManager().getSelectedIntersection().canBuild();
		}
	}

	@Override
	public String getDescription() {
		
		return "Build Settlement here";
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Build Settlement";
	}

	@Override
	public void sendCommand() {
		// TODO Auto-generated method stub
		System.out.println(getDescription());
	}

}
