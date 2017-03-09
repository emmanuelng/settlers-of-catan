package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoRectangle;
import org.minueto.image.MinuetoText;

public class DialogBox extends MinuetoRectangle implements Clickable {
	
	private int relativeX;
	private int relativeY;
	
	public DialogBox(int relativeX, int relativeY, int sizex,int sizey,String prompt){
		super(sizex, sizey, MinuetoColor.BLACK, false);
		this.relativeX = relativeX;
		this.relativeY = relativeY;
		
		MinuetoText promptText = new MinuetoText(prompt,new MinuetoFont("arial",9,false,false),MinuetoColor.BLACK);
		draw(promptText, 1, 1);
	}

	@Override
	public boolean isClicked(int x, int y) {
		return x > relativeX && x < relativeX + getWidth() && y > relativeY + 100 && y < relativeY + 100 + getHeight();
	}

	@Override
	public void onclick() {
		System.out.println("dbox was clicked!");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
