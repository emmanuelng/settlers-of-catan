package catan.settlers.client.view.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.minueto.image.MinuetoImage;

import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.view.game.handlers.ClickListener;

public abstract class ImageLayer implements Iterable<MinuetoImage> {

	ArrayList<MinuetoImage> images;
	HashMap<MinuetoImage, WindowCoordinates> imagesCoordsMap;
	HashMap<MinuetoImage, ClickListener> clickableElmts;

	public ImageLayer() {
		this.images = new ArrayList<>();
		this.imagesCoordsMap = new HashMap<>();
		this.clickableElmts = new HashMap<>();
	}

	public abstract void compose(GameStateManager gsm);

	public void draw(MinuetoImage image, int x, int y) {
		images.add(image);
		imagesCoordsMap.put(image, new WindowCoordinates(x, y));
	}

	public WindowCoordinates getCoordinates(MinuetoImage image) {
		return imagesCoordsMap.get(image);
	}

	public boolean isClickable(MinuetoImage image) {
		return clickableElmts.keySet().contains(image);
	}

	public void registerClickable(MinuetoImage image, ClickListener listener) {
		if (images.contains(image)) {
			clickableElmts.put(image, listener);
		}
	}

	public ClickListener getClickListener(MinuetoImage image) {
		return clickableElmts.get(image);
	}

	@Override
	public Iterator<MinuetoImage> iterator() {
		return images.iterator();
	}
}
