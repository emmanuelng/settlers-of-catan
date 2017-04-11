package catan.settlers.network.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import catan.settlers.server.model.Game;

public class GameManager {

	private ArrayList<Game> publicGames, savedGames;
	private int lastId;

	public GameManager() {
		lastId = loadLastId();
		this.publicGames = new ArrayList<>();
		this.savedGames = new ArrayList<>();
	}

	public synchronized Game createGame(Credentials owner) {
		Game game = new Game(lastId, owner);
		publicGames.add(game);
		lastId++;
		saveLastId();
		return game;
	}

	public synchronized ArrayList<Game> getListOfPublicGames() {
		ArrayList<Game> list = new ArrayList<>();
		list.addAll(publicGames);
		return list;
	}

	public synchronized Game getGameById(int id) {
		for (Game g : publicGames) {
			if (g.getGameId() == id) {
				return g;
			}
		}

		for (Game g : savedGames) {
			if (g.getGameId() == id) {
				return g;
			}
		}

		return null;
	}

	public synchronized void removeGame(Game game) {
		publicGames.remove(game);
	}

	public ArrayList<Game> getSavedGames(Credentials credentials) {
		loadSavedGames();
		ArrayList<Game> ret = new ArrayList<>();
		ret.addAll(savedGames);
		return ret;
	}

	private void loadSavedGames() {
		savedGames = new ArrayList<>();

		try {
			File folder = new File("saves/");

			for (File file : folder.listFiles()) {
				if (file.isFile()) {
					FileInputStream fis = new FileInputStream(file);
					ObjectInputStream ois = new ObjectInputStream(fis);
					Object obj = ois.readObject();

					if (obj instanceof Game)
						savedGames.add((Game) obj);

					ois.close();
				}
			}
		} catch (Exception e) {
			// Ignore
		}
	}

	private int loadLastId() {
		try {
			FileInputStream fis = new FileInputStream("config.catan.dat");
			ObjectInputStream ois = new ObjectInputStream(fis);
			lastId = ois.readInt();
			System.out.println("lastId is " + lastId);
			ois.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			lastId = 0;
		}
		return 0;
	}

	private void saveLastId() {
		try {
			FileOutputStream fos = new FileOutputStream("config.catan.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeInt(lastId);
			oos.close();
		} catch (Exception e) {
			// ignore
		}

	}
}
