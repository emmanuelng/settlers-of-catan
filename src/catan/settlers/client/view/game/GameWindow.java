package catan.settlers.client.view.game;

import java.util.ArrayList;
import java.util.HashMap;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoEventQueue;
import org.minueto.image.MinuetoImage;
import org.minueto.window.MinuetoFrame;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.handlers.BoardWindowHandler;
import catan.settlers.client.view.game.handlers.Clickable;
import catan.settlers.client.view.game.handlers.KeyboardHandler;
import catan.settlers.client.view.game.handlers.MouseHandler;

public class GameWindow extends MinuetoFrame {

	private boolean open;

	private MinuetoEventQueue eventQueue;
	private MouseHandler mouseHandler;
	private KeyboardHandler keyboardHandler;

	private TopBarImage topBar;
	private GameBoardImage board;
	private PlayerListImage playersList;
	private ActionBoxImage actionBox;
	private DialogBox dbox;

	private HashMap<MinuetoImage, Clickable> imageClickableMap;

	public GameWindow() {
		super(ClientWindow.WINDOW_WIDTH, ClientWindow.WINDOW_HEIGHT, true);
	}

	public void start() {
		initialize();
		open = true;

		// Game loop
		while (open) {
			while (eventQueue.hasNext())
				eventQueue.handle();

			updateWindow();
			Thread.yield();
		}

		close();
	}

	private void initialize() {
		// Window setup
		setTitle("Settlers of Catan");

		// Register handlers
		this.eventQueue = new MinuetoEventQueue();
		this.mouseHandler = new MouseHandler();
		this.keyboardHandler = new KeyboardHandler();

		registerWindowHandler(new BoardWindowHandler(), eventQueue);
		registerMouseHandler(mouseHandler, eventQueue);
		registerKeyboardHandler(keyboardHandler, eventQueue);

		// Initialize layers
		this.topBar = new TopBarImage();
		this.board = new GameBoardImage();
		this.playersList = new PlayerListImage();
		this.actionBox = new ActionBoxImage();
		this.dbox = new DialogBox();

		this.imageClickableMap = new HashMap<>();
	}

	public void updateWindow() {
		clear();

		drawLayer(board, 0, 100);
		drawLayer(playersList, 0, 100);
		drawLayer(actionBox, 0, 100);
		drawLayer(dbox, 0, 100);
		drawLayer(topBar, 0, 0);
		render();
	}

	private void drawLayer(ImageLayer layer, int x, int y) {
		layer.compose(ClientModel.instance.getGameStateManager());

		// Draw images
		for (MinuetoImage image : layer) {
			int window_x_coord = layer.getCoordinates(image).getX() + x;
			int window_y_coord = layer.getCoordinates(image).getY() + y;
			draw(image, window_x_coord, window_y_coord);

			if (layer.isClickable(image)) {
				Clickable clickable = new Clickable() {

					@Override
					public boolean isClicked(int x, int y) {
						boolean isInXrange = x > window_x_coord && x < window_x_coord + image.getWidth();
						boolean isInYRange = y > window_y_coord && y < window_y_coord + image.getHeight();
						return isInXrange && isInYRange;
					}

					@Override
					public String getName() {
						return image.hashCode() + "";
					}

					@Override
					public void onclick() {
						layer.getClickListener(image).onClick();
					}
				};

				if (imageClickableMap.get(image) == null) {
					imageClickableMap.put(image, clickable);
					mouseHandler.register(clickable);
				}
			}
		}
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

	public void clearLayerClickables(ImageLayer layer) {
		for (MinuetoImage image : layer) {
			if (layer.isClickable(image)) {
				Clickable clickable = imageClickableMap.get(image);
				if (image != null)
					mouseHandler.unregister(clickable);
			}
		}
	}
}
