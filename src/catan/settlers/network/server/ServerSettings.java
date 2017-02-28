package catan.settlers.network.server;

import java.io.Serializable;

public class ServerSettings implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_PORT = 3000;

	private int port;

	/**
	 * Class that represents the server's settings
	 */
	public ServerSettings() {
		this.port = DEFAULT_PORT;
	}

	public ServerSettings(ServerSettings toCopy) {
		this.port = toCopy.port;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
