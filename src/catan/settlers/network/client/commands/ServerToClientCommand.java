package catan.settlers.network.client.commands;

import java.io.Serializable;

public interface ServerToClientCommand extends Serializable {
	public void execute();
}
