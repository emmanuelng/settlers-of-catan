package catan.settlers.network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.network.server.commands.ClientToServerCommand;

public class Session extends Thread {

	private Server host;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private boolean sessionActive;

	private ArrayList<SessionObserver> observers;
	private Socket socket;

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
		this.socket = socket;
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
			obs.sessionWasClosed(getCredentials());
		}

		host.getAuthManager().removeSession(this);

		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Credentials getCredentials() {
		return host.getAuthManager().getCredentialsBySession(this);
	}
}
