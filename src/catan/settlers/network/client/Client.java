package catan.settlers.network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.network.server.commands.ClientToServerCommand;

public class Client extends Thread {

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	private String host;
	private int port;

	/**
	 * A Client is a thread that waits for commands from the server. When it is
	 * created, it does not connect directly to the server. In order to do so,
	 * use the connect() method.
	 * 
	 * @param host
	 * @param port
	 */
	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void connect() throws IOException {
		try {
			socket = new Socket(host, port);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			start();
		} catch (Exception e) {
			throw new IOException();
		}
	}

	@Override
	public void run() {
		LinkedList<ServerToClientCommand> cmdList = new LinkedList<>();

		while (true) {
			try {
				ServerToClientCommand cmd = (ServerToClientCommand) in.readObject();
				cmdList.add(cmd);

				while (!cmdList.isEmpty())
					cmdList.remove().execute();

			} catch (ClassNotFoundException e) {
				// Ignore
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	public void sendCommand(ClientToServerCommand cmd) throws IOException {
		out.writeObject(cmd);

	}
}
