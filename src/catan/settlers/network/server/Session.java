package catan.settlers.network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.network.server.commands.ClientToServerCommand;

public class Session extends Thread {

	private Server host;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private boolean sessionActive;

	private LinkedList<ServerToClientCommand> cmdQueue;
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
		this.cmdQueue = new LinkedList<>();

		start();
	}

	@Override
	public void run() {
		try {
			LinkedList<ClientToServerCommand> cmdList = new LinkedList<>();

			while (sessionActive) {
				ClientToServerCommand cmd = (ClientToServerCommand) in.readObject();
				cmdList.add(cmd);

				while (!cmdList.isEmpty())
					cmdList.remove().execute(this, host);
			}
		} catch (Exception e) {
			// Ignore
			if (e.getMessage() != null) {
				if (!e.getMessage().equals("Connection reset"))
					System.out.println(e.getMessage());
			} else {
				e.printStackTrace();
			}
		} finally {
			close();
		}
	}

	public synchronized void sendCommand(ServerToClientCommand cmd) throws IOException {
		cmdQueue.add(cmd);

		while (!cmdQueue.isEmpty())
			out.writeObject(cmdQueue.remove());
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
