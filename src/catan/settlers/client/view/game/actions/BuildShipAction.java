package catan.settlers.client.view.game.actions;

public class BuildShipAction implements GameAction {

	@Override
	public boolean isPossible() {
		return false;
	}

	@Override
	public String getDescription() {
		return "Build ship";
	}

	@Override
	public void perform() {
		// TODO
		
	}

	@Override
	public String getSuccessMessage() {
		return "";
	}

	@Override
	public String getFailureMessage() {
		return "Not yet implemented";
	}

}
