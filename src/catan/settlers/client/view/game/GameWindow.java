package catan.settlers.client.view.game;

import java.util.ArrayList;

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
import catan.settlers.server.model.map.GameBoard;

public class GameWindow extends MinuetoFrame {

	private MinuetoEventQueue eventQueue;
	private boolean open;
	private GameBoard curBoard;
	private BoardMouseHandler mouseHandler;
	private BoardKeyboardHandler keyboardHandler;
	private ArrayList<String> player;
	private DialogBox dbox;
	private TradeMenu tradeMenu;

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
			if (curBoard != null)
				updateWindow(curBoard);
			Thread.yield();
		}

		close();
	}

	public void updateWindow(GameBoard board) {
		GameBoardImage gameBoard = new GameBoardImage(board);
		ResourceBarImage resourceBar = new ResourceBarImage();

		draw(resourceBar, 0, 0);
		draw(gameBoard, 0, 100);
		printListOfPlayers(player, 1200, 300);

		if (dbox != null) {
			draw(dbox, 0, 100);
		}
		
		if (tradeMenu != null){
			draw(tradeMenu, 0, 100);
			printListOfPlayers(player, 1200, 300);
		}
		render();
	}

	private void initialize() {
		// Window settings
		this.setTitle("Settlers of Catan");

		// Build the event queue.
		eventQueue = new MinuetoEventQueue();

		// Register the window handler with the event queue.
		this.registerWindowHandler(new BoardWindowHandler(), eventQueue);
		this.registerMouseHandler(mouseHandler, eventQueue);
	}

	private void requestGameBoard() {
		GetGameBoardCommand req = new GetGameBoardCommand(ClientModel.instance.getCurGameId());
		ClientModel.instance.getNetworkManager().sendCommand(req);
	}

	private void requestPlayers() {
		GetListOfPlayersCommand req = new GetListOfPlayersCommand(ClientModel.instance.getCurGameId());
		ClientModel.instance.getNetworkManager().sendCommand(req);
	}

	public void updateGameBoard(GameBoard board) {
		curBoard = board;
	}

	public BoardMouseHandler getMouseHandler() {
		return mouseHandler;
	}

	public void setListOfPlayers(ArrayList<String> players) {
		this.player = players;
	}

	private void printListOfPlayers(ArrayList<String> players, int x, int y) {
		for (int i = 0; i < players.size(); i++) {
			draw(new MinuetoText(players.get(i), new MinuetoFont("arial", 30, false, false), MinuetoColor.BLACK), x,
					y + i * 200);
		}
	}

	public void setDialogBox(DialogBox dbox) {
		this.dbox = dbox;
	}
	
	public BoardKeyboardHandler getKeyBoardHandler() {
		// TODO Auto-generated method stub
		return keyboardHandler;
	}
	
	public MinuetoEventQueue getEventQueue(){
		return eventQueue;
	}
	
	public void setTradeMenu(TradeMenu tradeMenu){
		this.tradeMenu=tradeMenu;
	}
	
	public TradeMenu getTradeMenu(){
		return tradeMenu;
	}
}
