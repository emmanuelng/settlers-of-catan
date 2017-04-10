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
import catan.settlers.client.view.game.handlers.ClickListener;
import catan.settlers.client.view.game.handlers.Clickable;
import catan.settlers.client.view.game.handlers.KeyboardHandler;
import catan.settlers.client.view.game.handlers.MouseHandler;
import catan.settlers.server.model.Player.ResourceType;

public class GameWindow extends MinuetoFrame {

	private static final int FPS = 10;
	private boolean open;

	private MinuetoEventQueue eventQueue;
	private MouseHandler mouseHandler;
	private KeyboardHandler keyboardHandler;
	private HashMap<MinuetoImage, Clickable> imageClickableMap;

	private TopBarLayer topBar;
	private GameBoardImage board;
	private PlayerListLayer playersList;
	private ActionBoxLayer actionBox;
	private DialogBoxLayer dbox;
	private TradeMenuLayer tradeMenu;
	private TradeOfferMenuLayer receiveTradeMenu;
	private SevenDiscardMenuLayer sevenDiscardMenu;
	private SelectPlayerLayer selectPlayerLayer;
	private ProgressCardMenuLayer progressCardsLayer;
	private SelectProgressCardTypeLayer selectProgressCardTypeLayer;
	private SelectResourceMenuLayer selectResourceMenuLayer;
	private SelectCommodityLayer selectCommodityMenuLayer;
	private SelectHexLayer selectHexLayer;
	private BarbarianCounterLayer barbarianCounterLayer;
	private FlipchartLayer flipchartLayer;
	private AchievementsLayer achievementsLayer;

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

			try {
				Thread.sleep(1000 / FPS);
			} catch (InterruptedException e) {
				// Ignore
			}
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
		this.topBar = new TopBarLayer();
		this.board = new GameBoardImage();
		this.playersList = new PlayerListLayer();
		this.actionBox = new ActionBoxLayer();
		this.dbox = new DialogBoxLayer();
		this.tradeMenu = new TradeMenuLayer();
		this.receiveTradeMenu = new TradeOfferMenuLayer();
		this.sevenDiscardMenu = new SevenDiscardMenuLayer();
		this.selectPlayerLayer = new SelectPlayerLayer();
		this.progressCardsLayer = new ProgressCardMenuLayer();
		this.selectResourceMenuLayer = new SelectResourceMenuLayer();
		this.selectProgressCardTypeLayer = new SelectProgressCardTypeLayer();
		this.selectCommodityMenuLayer = new SelectCommodityLayer();
		this.selectHexLayer = new SelectHexLayer();
		this.flipchartLayer = new FlipchartLayer();
		this.barbarianCounterLayer = new BarbarianCounterLayer();
		this.achievementsLayer = new AchievementsLayer();

		this.imageClickableMap = new HashMap<>();
	}

	private void updateWindow() {
		clear(MinuetoColor.WHITE);

		drawLayer(board, 0, 100);
		drawLayer(playersList, 0, 100);
		drawLayer(actionBox, 0, 100);
		drawLayer(dbox, 0, 105);
		drawLayer(tradeMenu, 0, 0);
		drawLayer(receiveTradeMenu, 0, 0);
		drawLayer(sevenDiscardMenu, 0, 0);
		drawLayer(selectPlayerLayer, 0, 0);
		drawLayer(progressCardsLayer, 0, 0);
		drawLayer(selectResourceMenuLayer, 0, 0);
		drawLayer(selectProgressCardTypeLayer, 0, 0);
		drawLayer(selectCommodityMenuLayer, 0, 0);
		drawLayer(selectHexLayer, 0, 0);
		drawLayer(topBar, 0, 0);
		drawLayer(flipchartLayer,0,0);
		drawLayer(barbarianCounterLayer,0,100);
		drawLayer(achievementsLayer,0,100);

		render();
	}

	private void drawLayer(ImageLayer layer, int x, int y) {
		layer.compose(ClientModel.instance.getGameStateManager());
		ArrayList<MinuetoImage> images = layer.getImages();

		for (int i = 0; i < images.size(); i++) {
			MinuetoImage image = images.get(i);

			int window_x_coord = layer.getCoordinatesAt(i).getX() + x;
			int window_y_coord = layer.getCoordinatesAt(i).getY() + y;
			draw(image, window_x_coord, window_y_coord);

			if (layer.isClickable(image)) {
				ClickListener listener = layer.getClickListener(image);
				Clickable clickable = new Clickable() {

					@Override
					public boolean isInteracted(int x, int y) {
						boolean isInXrange = x > window_x_coord && x < window_x_coord + image.getWidth();
						boolean isInYRange = y > window_y_coord && y < window_y_coord + image.getHeight();
						return isInXrange && isInYRange;
					}

					@Override
					public String getName() {
						return image.toString();
					}

					@Override
					public void onclick() {
						if (listener != null) {
							listener.onClick();
						} else {
							System.out.println("WARNING: The layer " + layer
									+ " is not cleared correctly.\n\tMake sure that you call clearLayerClickables() BEFORE clear()");
						}
					}
				};

				if (imageClickableMap.get(image) == null) {
					imageClickableMap.put(image, clickable);
					mouseHandler.registerClickable(clickable);
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
				if (clickable != null) {
					mouseHandler.unregister(clickable);
					imageClickableMap.remove(image);
				}
			}
		}
	}

	public static MinuetoColor getTradeColor() {
		return new MinuetoColor(255, 221, 85);
	}

	public static MinuetoColor getPoliticsColor() {
		return new MinuetoColor(95, 188, 211);
	}

	public static MinuetoColor getScienceColor() {
		return new MinuetoColor(95, 211, 95);
	}

	public static MinuetoColor getColorByResource(ResourceType rtype) {
		if (TopBarLayer.resourceColors.get(rtype) == null) {
			MinuetoColor color;
			switch (rtype) {
			case GRAIN:
				color = new MinuetoColor(198, 233, 175);
				break;
			case LUMBER:
				color = new MinuetoColor(222, 170, 135);
				break;
			case ORE:
				color = new MinuetoColor(200, 190, 183);
				break;
			case WOOL:
				color = new MinuetoColor(255, 246, 213);
				break;
			case BRICK:
				color = new MinuetoColor(255, 153, 85);
				break;
			case CLOTH:
				color = new MinuetoColor(255, 230, 213);
				break;
			case PAPER:
				color = new MinuetoColor(246, 255, 213);
				break;
			default:
				color = new MinuetoColor(255, 215, 0);
				break;
			}
			TopBarLayer.resourceColors.put(rtype, color);
		}
		return TopBarLayer.resourceColors.get(rtype);
	}
}
