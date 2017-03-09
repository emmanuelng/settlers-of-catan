package catan.settlers.client.view.game.handlers;

import java.util.ArrayList;

import org.minueto.handlers.MinuetoMouseHandler;

import catan.settlers.client.view.game.Clickable;

public class BoardMouseHandler implements MinuetoMouseHandler {

	private ArrayList<Clickable> clickableElmts;

	public BoardMouseHandler() {
		clickableElmts = new ArrayList<>();
	}

	public void handleMousePress(int x, int y, int button) {
		for (int i = clickableElmts.size() - 1; i >= 0; i--) {
			Clickable c = clickableElmts.get(i);
			if (c.isClicked(x, y)) {
				c.onclick();
				return;
			}
		}
	}

	public void register(Clickable c) {
		System.out.println("registering: " + c);
		clickableElmts.add(c);
	}

	public void unregister(Clickable c) {
		clickableElmts.remove(c);
	}

	@Override
	public void handleMouseRelease(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void handleMouseMove(int x, int y) {
		// Not going to print on this event.
	}
}