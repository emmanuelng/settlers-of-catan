package catan.settlers.network.server;

import catan.settlers.server.model.Player;

public interface SessionObserver {

	void sessionWasClosed(Player player);

}
