package catan.settlers.network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.network.server.commands.ClientToServerCommand;

public class Session extends Thread {

	private Server host;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private boolean sessionActive;

	/**
	 * A session is a thread that waits commands from a specific client (not
	 * player, as a client does not necessarily maps to a player)
	 * 
	 * @param socket
	 * @param host
	 * @throws IOException
	 *             if the session was not instantiated for some reason
	 */
	public Session(Socket socket, Server host) throws IOException {
		this.host = host;
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream(socket.getInputStream());
		this.sessionActive = true;

		start();
	}

	@Override
	public void run() {
		while (sessionActive) {
			try {
				ClientToServerCommand cmd = (ClientToServerCommand) in.readObject();
				cmd.execute(this, host);
			} catch (Exception e) {
				// Ignore
			}
		}
	}

	public void sendCommand(ServerToClientCommand cmd) throws IOException {
		out.writeObject(cmd);
	}

	public void close() {
		sessionActive = false;
		host.removeSession(this);
	}

}