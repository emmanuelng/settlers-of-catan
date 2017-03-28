package catan.settlers.client.view.game.handlers;

import java.util.ArrayList;
import java.util.HashSet;

import org.minueto.handlers.MinuetoMouseHandler;

public class MouseHandler implements MinuetoMouseHandler {

	private ArrayList<Clickable> clickableElmts;
	private HashSet<String> addedElements;

	public MouseHandler() {
		clickableElmts = new ArrayList<>();
		addedElements = new HashSet<>();
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
		if (!addedElements.contains(c.getName())) {
			clickableElmts.add(c);
			addedElements.add(c.getName());
		}
	}

	public void unregister(InteractiveElement c) {
		clickableElmts.remove(c);
		addedElements.remove(c.getName());
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