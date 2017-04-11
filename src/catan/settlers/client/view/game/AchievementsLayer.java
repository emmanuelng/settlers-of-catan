package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.view.ClientWindow;

public class AchievementsLayer extends ImageLayer {

	private static final MinuetoFont amt_font = new MinuetoFont("arial", 17, false, false);
	private String largestArmy;
	private String longestRoad;

	public AchievementsLayer() {
		super();
	}

	@Override
	public void compose(GameStateManager gsm) {

		if (this.largestArmy != gsm.getLargestArmy() || this.longestRoad != gsm.getLongestRoad()) {
			clear();
		}

		largestArmy = (gsm.getLargestArmy() == null) ? "" : gsm.getLargestArmy();
		longestRoad = (gsm.getLongestRoad() == null) ? "" : gsm.getLongestRoad();

		MinuetoText largestArmyText = new MinuetoText("Largest Army: " + largestArmy, amt_font, MinuetoColor.BLACK,
				true);
		MinuetoText longestRoadText = new MinuetoText("Longest Road: " + longestRoad, amt_font, MinuetoColor.BLACK,
				true);
		
		int y_offset = ClientWindow.WINDOW_HEIGHT - longestRoadText.getHeight() - 30 - largestArmyText.getHeight() - 30;
		
		draw(largestArmyText, 10, y_offset);
		y_offset += largestArmyText.getHeight() + 30;
		
		draw(longestRoadText, 10, y_offset);
	}

}