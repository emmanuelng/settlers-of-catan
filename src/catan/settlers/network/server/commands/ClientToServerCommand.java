package catan.settlers.network.server.commands;

import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;

public interface ClientToServerCommand {
	public void execute(Session sender, Server server);
}
