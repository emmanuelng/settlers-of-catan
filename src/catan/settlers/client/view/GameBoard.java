package catan.settlers.client.view;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.minueto.image.MinuetoImage;
import org.minueto.window.MinuetoFrame;
import org.minueto.window.MinuetoPanel;
import org.minueto.window.MinuetoWindow;

import catan.settlers.client.model.HexagonMap;
import catan.settlers.client.view.setup.Login;

public class GameBoard implements Runnable{
	private GameFrame gameFrame;
	private MinuetoWindow boardPanel;
	private MinuetoPanel resourcePanel;
	private MinuetoPanel playerPanel;
	private MinuetoPanel controlPanel;
	
	private MinuetoImage board;
	private boolean isWaiting =true;
	
	public GameBoard(){
		//just the board and its elements
		//boardPanel = new MinuetoWindow(0,100);
		//boardPanel.setVisible(true);
		board = new HexagonMap().DrawHexagonMap(100,100);
		boardPanel.draw(board, 0, 0);
		boardPanel.render();
		
		
		//game entire frame
		gameFrame = GameFrame.getInstance();
		//add the elements to the gameFrame
		//gameFrame.add(boardPanel);
		gameFrame.setVisible(true);
	}
	
	public MinuetoWindow getBoardPanel(){
		return boardPanel;
	}
	public static void main(String[] args){
		 Thread ourGame = new Thread(new GameBoard());
	      // Start
	      ourGame.start();
	}

	@Override
	public void run() {
		 JFrame menu = new (this);
	      menu.setVisible(true);

	      // Wait for signal to start game
	      while (isWaiting) {
	         try {
	            Thread.sleep(100);
	         } catch (InterruptedException e) {
	            // Can ignore, we'll just try again
	         }
	      }
	}
}
