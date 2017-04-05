package catan.settlers.network.client.commands.game;

import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.server.model.units.Port.PortKind;

public class OwnedPortsChangedCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 8873428035082445039L;
	private HashMap<PortKind, Boolean> ownedPorts;

	public OwnedPortsChangedCommand(HashMap<PortKind, Boolean> ownedPorts) {
		this.ownedPorts = ownedPorts;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setOwnedPorts(ownedPorts);
	}

}
