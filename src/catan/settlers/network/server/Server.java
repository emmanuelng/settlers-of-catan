package catan.settlers.network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

import catan.settlers.common.utils.File;
import catan.settlers.server.view.ServerWindow;

public class Server extends Thread {

	/**
	 * Name of the file where the server settings are saved
	 */
	public static final String SETTINGS_FILE = "settings.dat";
	public static Server instance;

	public static Server getInstance() {
		if (instance == null) {
			try {
				instance = new Server();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}

		return instance;
	}

	public static void resetInstance() {
		try {
			instance = new Server();
		} catch (IOException e) {
			System.exit(0);
		}
	}

	private ServerSettings settings;
	private File settingsFile;
	private boolean isServerRunning;
	private ServerSocket listener;

	private ServerWindow gui;
	private AuthenticationManager playerManager;
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
	public Server() throws IOException {
		this.playerManager = new AuthenticationManager();
		this.gameManager = new GameManager();

		settingsFile = new File(SETTINGS_FILE);
		loadSettings();
		isServerRunning = false;
	}

	public void setGui(ServerWindow gui) {
		this.gui = gui;
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
		writeToConsole("Server is running on port " + settings.getPort() + ". Your ip is " + getIp());
		while (isServerRunning) {
			try {
				Socket socket = listener.accept();
				gui.writeToLog("New client connected");
				new Session(socket, this);
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

	public ServerSettings getSettings() {
		return new ServerSettings(settings);
	}

	public void setSettings(ServerSettings settings) {
		this.settings = settings;
		saveSettings();
		loadSettings();

		writeToConsole("Server settings updated");
	}

	public AuthenticationManager getAuthManager() {
		return playerManager;
	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public void writeToConsole(String string) {
		gui.writeToLog(string);
	}

	public String getIp() {
		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			return in.readLine();
		} catch (Exception e) {
			return "";
		}
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
