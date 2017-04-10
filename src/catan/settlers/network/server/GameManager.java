package catan.settlers.network.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import catan.settlers.server.model.Game;

public class GameManager {

	private ArrayList<Game> publicGames, savedGames;
	private int lastId;

	public GameManager() {
		lastId = 0;
		this.publicGames = new ArrayList<>();
		this.savedGames = new ArrayList<>();
	}

	public synchronized Game createGame(Credentials owner) {
		Game game = new Game(lastId, owner);
		publicGames.add(game);
		lastId++;
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
			e.printStackTrace();
		}
	}
}
