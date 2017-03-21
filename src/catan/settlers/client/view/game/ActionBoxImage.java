package catan.settlers.client.view.game;

import java.util.ArrayList;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoRectangle;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.actions.Action;

public class ActionBoxImage extends MinuetoImage {

	private static final int WIDTH = 300;
	private static final int PADDING_TOP = 30;
	private static final int PADDING_LEFT = 10;

	private MinuetoRectangle background;
	private MinuetoText title;

	private int box_x, box_y;
	
	private ArrayList<ActionButtons> allButtons;

	public ActionBoxImage() {
		super(ClientWindow.WINDOW_WIDTH, ClientWindow.WINDOW_HEIGHT);

		this.box_x = ClientWindow.WINDOW_WIDTH - WIDTH;
		this.box_y = 0;

		this.background = new MinuetoRectangle(WIDTH, ClientWindow.WINDOW_HEIGHT, new MinuetoColor(233, 221, 175),
				true);
		this.title = new MinuetoText("ACTIONS", new MinuetoFont("arial", 15, true, false),
				new MinuetoColor(72, 62, 55));

		draw(background, box_x, box_y);
		draw(title, box_x + PADDING_LEFT, box_y + PADDING_TOP);
		allButtons = new ArrayList<ActionButtons>();
		compose();
		
		
	}
	
	public void compose(){
		ArrayList<Action> list = ClientModel.instance.getActionManager().getPossibleActions();
		for(ActionButtons a: allButtons){
			ClientWindow.getInstance().getGameWindow().getMouseHandler().unregister(a);
			allButtons.remove(a);
		}
		String des;
		int bgbutt_x, bgbutt_y;
		for(int i = 0;i<list.size();i++){
			MinuetoRectangle buttBackground = new MinuetoRectangle(WIDTH-50, ClientWindow.WINDOW_HEIGHT/10, new MinuetoColor(233, 221, 175).lighten(100),
					true);
			des = list.get(i).getDescription();
			bgbutt_x = box_x+PADDING_LEFT;
			bgbutt_y = (box_y+ PADDING_TOP)+i*(buttBackground.getHeight()+10);
			MinuetoText description = new MinuetoText(des, new MinuetoFont("arial", 15, true, false),
				new MinuetoColor(72, 62, 55));
			
			ActionButtons actionButton = new ActionButtons(list.get(i), bgbutt_x, bgbutt_y , buttBackground.getWidth(), buttBackground.getHeight());
			allButtons.add(actionButton);
			
			draw(buttBackground,bgbutt_x,bgbutt_y);
			draw(description,bgbutt_x+10,bgbutt_y+10); //need to do some text wrapping
			
		}
	}
}
