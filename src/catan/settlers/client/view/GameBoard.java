package catan.settlers.client.view;

import javax.swing.JFrame;

import org.minueto.image.MinuetoImage;
import org.minueto.window.MinuetoFrame;
import org.minueto.window.MinuetoPanel;

import catan.settlers.client.model.HexagonMap;

public class GameBoard {
	private JFrame gameFrame;
	private MinuetoPanel boardPanel;
	private MinuetoPanel resourcePanel;
	private MinuetoPanel playerPanel;
	private MinuetoPanel controlPanel;
	
	private MinuetoImage board;
	
	public GameBoard(){
		//just the board and its elements
		boardPanel = new MinuetoPanel(0,100);
		boardPanel.setVisible(true);
		board = new HexagonMap().DrawHexagonMap(100,100);
		boardPanel.draw(board, 0, 0);
		boardPanel.render();
		
		
		//game entire frame
		gameFrame = GameFrame.getInstance();
		//add the elements to the gameFrame
		gameFrame.add(boardPanel);
		gameFrame.setVisible(true);
		
}
