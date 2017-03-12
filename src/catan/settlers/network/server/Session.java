package catan.settlers.network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Player;

public class Session extends Thread {

	private Server host;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private boolean sessionActive;

	private ArrayList<SessionObserver> observers;

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
		this.observers = new ArrayList<>();

		start();
	}

	@Override
	public void run() {
		try {
			while (sessionActive) {
				ClientToServerCommand cmd = (ClientToServerCommand) in.readObject();
				cmd.execute(this, host);
			}
		} catch (Exception e) {
			// Ignore
		} finally {
			close();
		}
	}

	public void sendCommand(ServerToClientCommand cmd) throws IOException {
		out.writeObject(cmd);
	}

	public void registerObserver(SessionObserver obs) {
		observers.add(obs);
	}

	public void close() {
		host.writeToConsole("Closing session...");
		sessionActive = false;

		for (SessionObserver obs : observers) {
			obs.sessionWasClosed(getPlayer());
		}

		host.getPlayerManager().removeSession(this);
	}

	public Player getPlayer() {
		return host.getPlayerManager().getPlayerBySession(this);
	}
}
