package catan.settlers.client.view.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.minueto.image.MinuetoImage;

import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.view.game.handlers.ClickListener;

public abstract class ImageLayer implements Iterable<MinuetoImage> {

	private ArrayList<MinuetoImage> images;
	private ArrayList<WindowCoordinates> coordinates;
	private HashMap<MinuetoImage, ClickListener> listeners;

	public ImageLayer() {
		this.images = new ArrayList<>();
		this.coordinates = new ArrayList<>();
		this.listeners = new HashMap<>();
	}

	public abstract void compose(GameStateManager gsm);

	public void draw(MinuetoImage image, int x, int y) {
		images.add(image);
		coordinates.add(new WindowCoordinates(x, y));
	}

	public boolean isClickable(MinuetoImage image) {
		return listeners.get(image) != null;
	}

	public void registerClickable(MinuetoImage image, ClickListener listener) {
		if (listeners.get(image) == null) {
			listeners.put(image, listener);
		}
	}

	public void clear() {
		images = new ArrayList<>();
		coordinates = new ArrayList<>();
		listeners = new HashMap<>();
	}

	public MinuetoImage getImageAt(int i) {
		return images.get(i);
	}

	public WindowCoordinates getCoordinatesAt(int i) {
		return coordinates.get(i);
	}

	public ClickListener getClickListener(MinuetoImage image) {
		return listeners.get(image);
	}

	public ArrayList<MinuetoImage> getImages() {
		ArrayList<MinuetoImage> ret = new ArrayList<>();
		ret.addAll(images);
		return ret;
	}

	@Override
	public Iterator<MinuetoImage> iterator() {
		return images.iterator();
	}
}
