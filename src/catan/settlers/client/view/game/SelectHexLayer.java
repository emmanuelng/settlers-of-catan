package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.handlers.ClickListener;
import catan.settlers.network.server.commands.game.PlayerTradeConfirmCommand;
import catan.settlers.network.server.commands.game.cards.SelectHexFeedbackCommand;

public class SelectHexLayer extends ImageLayer{

	private static final int WIDTH = 1000, HEIGHT = ClientWindow.WINDOW_HEIGHT;
	private static final MinuetoColor confirm_btn_color = new MinuetoColor(255, 153, 85);
	private static final MinuetoColor refuse_btn_color = new MinuetoColor(55, 200, 113);
	private static final MinuetoFont description_font = new MinuetoFont("arial", 17, false, false);
	
	private boolean clear;
	private int box_x, box_y;
	private Button HexConfirmButton;
	private Button HexRefuseButton;
	private MinuetoText orText;

	public SelectHexLayer() {
		
		
		this.HexConfirmButton = new Button(this, "Confirm", confirm_btn_color, getConfirmListener());
		this.HexRefuseButton = new Button(this, "Cancel", refuse_btn_color, getRefuseListener());
		this.orText = new MinuetoText("or", description_font, MinuetoColor.BLACK);
		this.box_x = ClientWindow.WINDOW_WIDTH / 2 - 850  ;
		this.box_y = HEIGHT-HexConfirmButton.getImage().getHeight();
	}



	@Override
	public void compose(GameStateManager gsm) {
		if (!gsm.getSelectHexLayer()) {
			if (clear) {
				ClientWindow.getInstance().getGameWindow().clearLayerClickables(this);
				clear();
				clear = false;
			}
			return;
		} else {
			clear = true;
		}
		
		int y_offset =  box_y - 20;

		draw(HexConfirmButton.getImage(), (box_x + WIDTH - HexConfirmButton.getImage().getWidth() - 20)
				- HexRefuseButton.getImage().getWidth() - 10 - orText.getWidth() - 10, y_offset);
		draw(orText, box_x + WIDTH - HexRefuseButton.getImage().getWidth() - 20 - orText.getWidth() - 10,
				y_offset + HexRefuseButton.getImage().getHeight() / 2 - orText.getHeight() / 2);
		draw(HexRefuseButton.getImage(), box_x + WIDTH - HexRefuseButton.getImage().getWidth() - 20, y_offset);
	}
	
	private ClickListener getConfirmListener() {
		return new ClickListener() {
			@Override
			public void onClick() {
				GameStateManager gsm = ClientModel.instance.getGameStateManager();
				// confirm trade here
				ClientModel.instance.getNetworkManager().sendCommand(new SelectHexFeedbackCommand(gsm.getSelectedHex()));
				gsm.setSelectedHex(null);
				gsm.setdBox(null,null);
				gsm.doShowSelectHexLayer(false);
			}
		};	
	}

	private ClickListener getRefuseListener() {
		// TODO Auto-generated method stub
		return new ClickListener() {
			@Override
			public void onClick() {
				GameStateManager gsm = ClientModel.instance.getGameStateManager();
				// confirm trade here
				gsm.setSelectedHex(null);
				gsm.setdBox(null,null);
				gsm.doShowSelectHexLayer(false);
				
			}
		};	
	}

}
