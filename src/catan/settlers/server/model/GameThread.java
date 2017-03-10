package catan.settlers.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import catan.settlers.network.client.commands.StartGameCommand;

/**
 * TODO: Ideally this class should not be Serializable
 */
public class GameThread extends Thread implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Player> participants;
	private Lock lock;
	private Condition continueGame;

	public GameThread(ArrayList<Player> participants, Lock lock, Condition continueGame) {
		this.participants = participants;
		this.lock = lock;
		this.continueGame = continueGame;
	}

	@Override
	public void run() {
		try {
			lock.lock();
			System.out.println("Waiting");
			continueGame.await();
			
			for (Player p : participants) {
				p.sendCommand(new StartGameCommand());
			}

			System.out.println("Start game!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}
}
