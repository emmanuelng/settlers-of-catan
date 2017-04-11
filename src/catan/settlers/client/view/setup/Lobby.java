package catan.settlers.client.view.setup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameRepresentation;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.network.server.commands.CreateGameCommand;
import catan.settlers.network.server.commands.JoinGameCommand;

public class Lobby extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton backButton, createGameButton;
	private JLabel publicGamesLabel;
	private JLabel savedGamesLabel;

	public Lobby(ArrayList<GameRepresentation> publicGames, ArrayList<GameRepresentation> savedGames) {
		backButton = new JButton("Back");
		createGameButton = new JButton("Create new game");
		publicGamesLabel = new JLabel("Public Games");

		savedGamesLabel = new JLabel("Saved games");

		add(backButton);
		add(createGameButton);

		add(publicGamesLabel);
		for (int i = 0; i < publicGames.size(); i++) {
			GameRepresentation curGame = publicGames.get(i);
			JButton gameButton = new JButton("Game " + curGame.getGameId());
			gameButton.addActionListener(new JoinGameActionListener(curGame));
			add(gameButton); // TODO: add some layout later
		}

		add(savedGamesLabel);
		for (int i = 0; i < savedGames.size(); i++) {
			GameRepresentation curGame = savedGames.get(i);
			JButton gameButton = new JButton("Game " + curGame.getGameId());
			gameButton.addActionListener(new JoinGameActionListener(curGame));
			add(gameButton); // TODO: add some layout later
		}

		backButton.addActionListener(this);
		createGameButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == backButton) {
			ClientWindow.getInstance().getSetupWindow().setScreen(new MainMenu());
		} else if (arg0.getSource() == createGameButton) {
			ClientModel.instance.getNetworkManager().sendCommand(new CreateGameCommand());
		}
	}
}

/**
 * Custom action listener that sends a Join game command to the server. Allows
 * to pass the wanted game through the constructor (impossible with anonymous
 * classes)
 */
class JoinGameActionListener implements ActionListener {

	private GameRepresentation game;

	public JoinGameActionListener(GameRepresentation gameRepresentation) {
		this.game = gameRepresentation;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ClientModel.instance.getNetworkManager().sendCommand(new JoinGameCommand(game.getGameId()));
	}

}
