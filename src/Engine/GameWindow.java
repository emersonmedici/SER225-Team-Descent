package Engine;

import javax.swing.*;
//import Screens.*;
/*
 * The JFrame that holds the GamePanel
 * Just does some setup and exposes the gamePanel's screenManager to allow an external class to setup their own content and attach it to this engine.
 */
public class GameWindow {
	private static GameWindow mg;
	private JFrame gameWindow;
	private GamePanel gamePanel;
	private boolean isMinigameOpen = false;

	public GameWindow() {
		mg = this;
		gameWindow = new JFrame("Game");
		gamePanel = new GamePanel();
		gamePanel.setFocusable(true);
		gamePanel.requestFocusInWindow();
		gameWindow.setContentPane(gamePanel);
		gameWindow.setResizable(false);
		gameWindow.setSize(Config.GAME_WINDOW_WIDTH, Config.GAME_WINDOW_HEIGHT);
		gameWindow.setLocationRelativeTo(null);
		gameWindow.setVisible(true);
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // it'd be nice if this actually worked more than 1/3rd of the time
		gamePanel.setupGame();
	}

	public void displayMinigame(JPanel panel) {		
		if (checkIfMgOpen() == true) {
			return;
		}
			isMinigameOpen = true;
			gamePanel.add(panel);
			gamePanel.revalidate();
			gamePanel.repaint();	
	}

	public boolean checkIfMgOpen() {
		return isMinigameOpen;
	}

	public void minigameCloser(JPanel panel) {
		gamePanel.remove(panel);
		isMinigameOpen = false;
		gamePanel.revalidate();
		gamePanel.repaint();
	}

	public static GameWindow getGameWindow() {
		return mg;
	}
	// triggers the game loop to start as defined in the GamePanel class
	public void startGame() {
		gamePanel.startGame();
	}

	public ScreenManager getScreenManager() {
		return gamePanel.getScreenManager();
	}
}
