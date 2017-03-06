package catan.settlers.client.model;

import java.util.ArrayList;

public interface ClientModelObserver {
	public void gameListUpdated(ArrayList<GameRepresentation> games);
}
