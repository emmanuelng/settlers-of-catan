package catan.settlers.client.model;

import java.util.HashMap;

import org.minueto.image.MinuetoImageFile;

public class ImageFileManager {
	private final HashMap<String, MinuetoImageFile> images = new HashMap<>();

	public MinuetoImageFile load(String path) {
		if (images.get(path) == null) {
			try {
				MinuetoImageFile img = new MinuetoImageFile(path);
				images.put(path, img);
			} catch (Exception e) {
				System.out.println("Image " + path + " cannot be loaded!");
				images.put(path, null);
			}
		}

		return images.get(path);
	}
}
