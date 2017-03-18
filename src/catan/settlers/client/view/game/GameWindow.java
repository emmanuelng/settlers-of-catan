package catan.settlers.client.view.game;

import java.util.ArrayList;
import java.util.HashMap;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoEventQueue;
import org.minueto.image.MinuetoImage;
import org.minueto.window.MinuetoFrame;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.handlers.BoardKeyboardHandler;
import catan.settlers.client.view.game.handlers.BoardMouseHandler;
import catan.settlers.client.view.game.handlers.BoardWindowHandler;
import catan.settlers.server.model.Player.ResourceType;

public class GameWindow extends MinuetoFrame {

	private boolean open;

	private MinuetoEventQueue eventQueue;
	private BoardMouseHandler mouseHandler;
	private BoardKeyboardHandler keyboardHandler;

	private GameBoardImage board;
	private TopBarImage topBar;
	private PlayerListImage playersList;
	private TradeMenu tradeMenu;
	private MinuetoImage dbox;

	private boolean boardChanged;
	private boolean playerChanged;

	public GameWindow() {
		super(ClientWindow.WINDOW_WIDTH, ClientWindow.WINDOW_HEIGHT, true);
	}

	public void start() {
		initialize();
		open = true;

		// Event loop
		while (open) {
			while (eventQueue.hasNext())
				eventQueue.handle();

			updateWindow();
			Thread.yield();
		}

		close();
	}

	private void initialize() {
		this.setTitle("Settlers of Catan");
		this.eventQueue = new MinuetoEventQueue();
		this.mouseHandler = new BoardMouseHandler();
		this.keyboardHandler = new BoardKeyboardHandler();
		this.boardChanged = true;

		this.board = new GameBoardImage();
		this.topBar = new TopBarImage();
		this.playersList = new PlayerListImage();

		registerWindowHandler(new BoardWindowHandler(), eventQueue);
		registerMouseHandler(mouseHandler, eventQueue);
	}

	public void updateWindow() {
		if (boardChanged) {
			board = new GameBoardImage();
			boardChanged = false;
		}
		
		if (playerChanged) {
			playersList = new PlayerListImage();
			playerChanged = false;
		}

		draw(board, 0, 100);
		draw(topBar, 0, 0);
		draw(playersList, 0, 100);

		if (dbox != null) {
			draw(dbox, 0, 110);
		}

		if (tradeMenu != null) {
			draw(tradeMenu, 0, 100);
		}

		render();
	}

	public int getPlayerNumber(String username) {
		ArrayList<String> participants = ClientModel.instance.getGameStateManager().getParticipants();
		return participants.indexOf(username) + 1;
	}

	public MinuetoColor getColorByUsername(String username) {
		ArrayList<String> participants = ClientModel.instance.getGameStateManager().getParticipants();
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

	public void updateResources(HashMap<ResourceType, Integer> resources) {
	}

	public BoardMouseHandler getMouseHandler() {
		return mouseHandler;
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

	public void notifyBoardHasChanged() {
		boardChanged = true;
	}
	
	public void notifyCurPlayerHasChanged() {
		playerChanged = true;
	}
}
