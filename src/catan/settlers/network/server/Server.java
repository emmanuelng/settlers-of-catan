package catan.settlers.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import catan.settlers.common.utils.File;
import catan.settlers.server.model.GameManager;
import catan.settlers.server.model.PlayerManager;
import catan.settlers.server.view.ServerGUI;

public class Server extends Thread {

	/**
	 * Name of the file where the server settings are saved
	 */
	public static final String SETTINGS_FILE = "settings.dat";

	private ServerSettings settings;
	private File settingsFile;
	private boolean isServerRunning;
	private ServerSocket listener;

	private ArrayList<Session> activeSessions;
	private ServerGUI gui;
	private PlayerManager playerManager;
	private GameManager gameManager;

	/**
	 * A Server is a thread that blocks until a client connects. When a new
	 * server is created, it is not started automatically. In order to do so,
	 * use the launch() method.
	 * 
	 * @param gui
	 *            The GUI that will display the server's log
	 * @throws IOException
	 *             Throws an error if the server was not instantiated for some
	 *             reason
	 */
	public Server(ServerGUI gui) throws IOException {
		this.gui = gui;
		this.playerManager = new PlayerManager();
		this.gameManager = new GameManager();
		this.activeSessions = new ArrayList<>();

		settingsFile = new File(SETTINGS_FILE);
		loadSettings();
		isServerRunning = false;
	}

	/**
	 * Launches the server (i.e. starts the server thread)
	 */
	public void launch() {
		try {
			listener = new ServerSocket(settings.getPort());
			isServerRunning = true;
			start();
		} catch (IOException e) {
			writeToConsole("Error: Failed to launch server. Port " + settings.getPort() + " is already used.");
		}
	}

	/**
	 * Closes the server. It is not recommended to restart the server. If a
	 * restart is needed, use a new instance of Server.
	 */
	public void close() {
		writeToConsole("Closing server...");
		try {
			listener.close();
			isServerRunning = false;
		} catch (Exception e) {
			// Ignore
		}
	}

	@Override
	public void run() {
		writeToConsole("Server is running on port " + settings.getPort());
		while (isServerRunning) {
			try {
				Socket socket = listener.accept();
				gui.writeToLog("Detected new client");
				Session session = new Session(socket, this);
				activeSessions.add(session);
			} catch (IOException e) {
				// Ignore
			}
		}
		writeToConsole("Server is now closed");
	}

	/**
	 * Indicates whether the server is currently running or not.
	 * 
	 * @return True if the server is alive, false otherwise
	 */
	public boolean isRunning() {
		return isServerRunning;
	}

	/**
	 * Closes a session
	 * 
	 * @param session
	 *            the session to close
	 */
	public void removeSession(Session session) {
		activeSessions.remove(session);
	}

	public ServerSettings getSettings() {
		return new ServerSettings(settings);
	}

	public void setSettings(ServerSettings settings) {
		this.settings = settings;
		saveSettings();
		loadSettings();

		writeToConsole("Server settings updated");
	}
	
	public PlayerManager getPlayerManager() {
		return playerManager;
	}
	
	public GameManager getGameManager() {
		return gameManager;
	}

	public void writeToConsole(String string) {
		gui.writeToLog(string);
	}

	private void loadSettings() {
		settings = (ServerSettings) settingsFile.read();

		if (settings == null) {
			settings = new ServerSettings();
			saveSettings();
		}
	}

	private void saveSettings() {
		settingsFile.write(settings);
	}
}