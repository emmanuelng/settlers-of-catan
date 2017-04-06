package catan.settlers.client.view.game.actions;

public class ManageLevelsAction implements GameAction {

	@Override
	public boolean isPossible() {
		return true;
	}

	@Override
	public String getDescription() {
		return "Open flipchart";
	}

	@Override
	public void perform() {
		// TODO
	}

	@Override
	public String getSuccessMessage() {
		return "Manage your Trade, Science and Politics levels";
	}

	@Override
	public String getFailureMessage() {
		return "";
	}

}
