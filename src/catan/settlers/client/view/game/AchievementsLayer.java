package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.GameStateManager;

public class AchievementsLayer  extends ImageLayer{

	private static final MinuetoFont amt_font = new MinuetoFont("arial", 17 , false, false);
	
	public AchievementsLayer(){
		super();
	}
	
	@Override
	public void compose(GameStateManager gsm) {
		String largestArmy = gsm.getLargestArmy();
		String longestRoad = gsm.getLongestRoad();
		
		MinuetoText largestArmyText = new MinuetoText("Largest Army: " + largestArmy , amt_font , MinuetoColor.BLACK, true);
		MinuetoText longestRoadText = new MinuetoText("Longest Road: " + longestRoad, amt_font, MinuetoColor.BLACK, true);
		
		draw(largestArmyText, 10, 30);
		draw(longestRoadText, 10 ,largestArmyText.getHeight() + 30);
	}

}
