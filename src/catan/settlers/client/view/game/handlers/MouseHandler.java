package catan.settlers.client.view.game.handlers;

import java.util.ArrayList;
import java.util.HashSet;

import org.minueto.handlers.MinuetoMouseHandler;

public class MouseHandler implements MinuetoMouseHandler {

	private ArrayList<Clickable> clickableElmts;
	private HashSet<String> addedElements;
	private ArrayList<Hoverable> hoverableElmts;

	public MouseHandler() {
		clickableElmts = new ArrayList<>();
		hoverableElmts = new ArrayList<>();
		addedElements = new HashSet<>();
	}

	@Override
	public void handleMousePress(int x, int y, int button) {
		for (int i = clickableElmts.size() - 1; i >= 0; i--) {
			Clickable c = clickableElmts.get(i);
			if (c.isInteracted(x, y)) {
				c.onclick();
				return;
			}
		}
	}

	public void registerClickable(Clickable c) {
		if (!addedElements.contains(c.getName())) {
			clickableElmts.add(c);
			addedElements.add(c.getName());
			if (clickableElmts.size() > 100000)
				System.out.println("WARNING: Too many elements are registered!");
		}
	}

	public void registerHoverable(Hoverable h) {
		if (!addedElements.contains(h.getName())) {
			hoverableElmts.add(h);
			addedElements.add(h.getName());
			if (hoverableElmts.size() > 100000)
				System.out.println("WARNING: Too many elements are registered!");
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
		for (int i = hoverableElmts.size() - 1; i >= 0; i--) {
			Hoverable c = hoverableElmts.get(i);
			if (c.isInteracted(x, y)) {
				c.onHover();
				return;
			}
		}
	}
}