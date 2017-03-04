package catan.settlers.client.view.setup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.commands.CreateGameCommand;
import catan.settlers.network.server.commands.GetListOfGamesCommand;
import catan.settlers.network.server.commands.JoinGameCommand;
import catan.settlers.server.model.Game;

public class Lobby extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton backButton, createGameButton;
	private JLabel publicGamesLabel;

	public Lobby(ArrayList<Game> games) {
		backButton = new JButton("Back");
		createGameButton = new JButton("Create new game");
		publicGamesLabel = new JLabel("Public Games");

		add(backButton);
		add(createGameButton);
		add(publicGamesLabel);

		for (int i = 0; i < games.size(); i++) {
			JButton gameButton = new JButton("Game" + i);
			gameButton.addActionListener(new JoinGameActionListener(games.get(i)));
			add(gameButton); // TODO: add some layout later
		}

		backButton.addActionListener(this);
		createGameButton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == backButton) {
			ClientWindow.getInstance().setScreen(new MainMenu());
		} else if (arg0.getSource() == createGameButton) {
			ClientModel.instance.getNetworkManager().sendCommand(new CreateGameCommand());
			ClientModel.instance.getNetworkManager().sendCommand(new GetListOfGamesCommand());
		}
	}
}

/**
 * Custom action listener that sends a Join game command to the server. Allows
 * to pass the wanted game through the constructor (impossible with anonymous
 * classes)
 */
class JoinGameActionListener implements ActionListener {

	private Game game;

	public JoinGameActionListener(Game game) {
		this.game = game;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ClientModel.instance.getNetworkManager().sendCommand(new JoinGameCommand(game.getGameId()));
	}

}
