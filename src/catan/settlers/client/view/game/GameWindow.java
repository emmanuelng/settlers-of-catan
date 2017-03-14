package catan.settlers.client.view.game;

import java.util.ArrayList;
import java.util.HashMap;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoEventQueue;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoText;
import org.minueto.window.MinuetoFrame;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.game.handlers.BoardKeyboardHandler;
import catan.settlers.client.view.game.handlers.BoardMouseHandler;
import catan.settlers.client.view.game.handlers.BoardWindowHandler;
import catan.settlers.network.server.commands.game.GetGameBoardCommand;
import catan.settlers.network.server.commands.game.GetListOfPlayersCommand;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.map.GameBoard;

public class GameWindow extends MinuetoFrame {

	private boolean open;

	private MinuetoEventQueue eventQueue;
	private BoardMouseHandler mouseHandler;
	private BoardKeyboardHandler keyboardHandler;

	private DialogBox dbox;
	private TradeMenu tradeMenu;

	private ArrayList<String> participants;
	private GameBoard board;
	private HashMap<ResourceType, Integer> resources;

	public GameWindow() {
		super(ClientModel.WINDOW_WIDTH, ClientModel.WINDOW_HEIGHT, true);
		mouseHandler = new BoardMouseHandler();
		keyboardHandler = new BoardKeyboardHandler();
	}

	public void start() {
		initialize();
		requestGameBoard();
		requestPlayers();
		open = true;

		// Event loop
		while (open) {
			while (eventQueue.hasNext())
				eventQueue.handle();
			if (board != null)
				updateWindow(board);
			Thread.yield();
		}

		close();
	}

	private void initialize() {
		this.setTitle("Settlers of Catan");
		eventQueue = new MinuetoEventQueue();
		this.registerWindowHandler(new BoardWindowHandler(), eventQueue);
		this.registerMouseHandler(mouseHandler, eventQueue);

		this.participants = new ArrayList<>();
		this.resources = new HashMap<>();
	}

	public void updateWindow(GameBoard board) {
		GameBoardImage gameBoard = new GameBoardImage(board);
		TopBarImage topBar = new TopBarImage(resources);

		draw(topBar, 0, 0);
		draw(gameBoard, 0, 100);
		printListOfPlayers(participants, 1200, 300);

		if (dbox != null) {
			draw(dbox, 0, 100);
		}

		if (tradeMenu != null) {
			draw(tradeMenu, 0, 100);
			printListOfPlayers(participants, 1200, 300);
		}
		render();
	}

	public int getPlayerNumber(String username) {
		return participants.indexOf(username) + 1;
	}

	public MinuetoColor getColorByUsername(String username) {
		int index = participants.indexOf(username);

		if (index == 0) {
			return new MinuetoColor(200, 55, 55);
		} else if (index == 1) {
			return new MinuetoColor(44, 137, 160);
		} else if (index == 2) {
			return new MinuetoColor(200, 113, 55);
		}

		return MinuetoColor.BLACK;
	}

	public void updateGameBoard(GameBoard board) {
		this.board = board;
	}

	public void updateResources(HashMap<ResourceType, Integer> resources) {
		this.resources = resources;
	}

	public BoardMouseHandler getMouseHandler() {
		return mouseHandler;
	}

	public void setListOfPlayers(ArrayList<String> players) {
		this.participants = players;
	}

	public void setDialogBox(DialogBox dbox) {
		this.dbox = dbox;
	}

	public BoardKeyboardHandler getKeyBoardHandler() {
		return keyboardHandler;
	}

	public MinuetoEventQueue getEventQueue() {
		return eventQueue;
	}

	public void setTradeMenu(TradeMenu tradeMenu) {
		this.tradeMenu = tradeMenu;
	}

	public TradeMenu getTradeMenu() {
		return tradeMenu;
	}

	private void requestGameBoard() {
		GetGameBoardCommand req = new GetGameBoardCommand(ClientModel.instance.getCurGameId());
		ClientModel.instance.getNetworkManager().sendCommand(req);
	}

	private void requestPlayers() {
		GetListOfPlayersCommand req = new GetListOfPlayersCommand(ClientModel.instance.getCurGameId());
		ClientModel.instance.getNetworkManager().sendCommand(req);
	}

	private void printListOfPlayers(ArrayList<String> players, int x, int y) {
		for (int i = 0; i < players.size(); i++) {
			draw(new MinuetoText(players.get(i), new MinuetoFont("arial", 30, false, false), MinuetoColor.BLACK), x,
					y + i * 200);
		}
	}
}
