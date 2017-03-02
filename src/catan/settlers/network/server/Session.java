package catan.settlers.network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Player;

public class Session extends Thread {

	private Server host;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private boolean sessionActive;
	private Player authenticatedPlayer;

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
				// Close the session (e.g. when the client closes the connection)
				close();
				e.printStackTrace();
			}
		}
	}

	public void sendCommand(ServerToClientCommand cmd) throws IOException {
		out.writeObject(cmd);
	}

	public void close() {
		host.writeToConsole("Closing session...");
		sessionActive = false;
		host.removeSession(this);
	}
	
	public void setPlayer(Player player) {
		authenticatedPlayer = player;
	}
	
	public Player getPlayer() {
		return authenticatedPlayer;
	}
}
