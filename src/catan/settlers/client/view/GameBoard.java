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
	
	private MinuetoImage board;
	private boolean isWaiting =true;
	
	public GameBoard(){
		gameFrame = GameFrame.getInstance());
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
