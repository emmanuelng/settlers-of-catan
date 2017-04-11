package catan.settlers.network.server.commands;

import java.io.Serializable;

import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;

public interface ClientToServerCommand extends Serializable {
	public void execute(Session sender, Server server);
}
