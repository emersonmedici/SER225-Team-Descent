package Engine;

import GameObject.Rectangle;
import SpriteFont.SpriteFont;
import Utils.Colors;

import javax.swing.*;
import java.awt.*;

/*
 * This is where the game loop process and render back buffer is setup
 */
public class GamePanel extends JPanel {
	// loads Screens on to the JPanel
	// each screen has its own update and draw methods defined to handle a "section" of the game.
	private ScreenManager screenManager;

	// used to draw graphics to the panel
	private GraphicsHandler graphicsHandler;

	private boolean isGamePaused = false;
	private SpriteFont pauseLabel;

	private SpriteFont pauseLabel2;
	private SpriteFont pauseLabel3;
	private SpriteFont pauseLabel4;
	private SpriteFont pauseLabel5;
	private SpriteFont pauseLabel6;
	private SpriteFont pauseLabel7;

	private KeyLocker keyLocker = new KeyLocker();
	private final Key pauseKey = Key.P;
	private Thread gameLoopProcess;

	private Key showFPSKey = Key.G;
	private SpriteFont fpsDisplayLabel;
	private boolean showFPS = false;
	private int currentFPS;
	private boolean doPaint;

	// The JPanel and various important class instances are setup here
	public GamePanel() {
		super();
		this.setDoubleBuffered(true);

		// attaches Keyboard class's keyListener to this JPanel
		this.addKeyListener(Keyboard.getKeyListener());

		graphicsHandler = new GraphicsHandler();

		screenManager = new ScreenManager();

		pauseLabel = new SpriteFont("CONTROLS", 280, 180, "Arial", 40, Color.white);
		pauseLabel.setOutlineColor(Color.black);
		pauseLabel.setOutlineThickness(2.0f);

		pauseLabel2 = new SpriteFont("move player - WASD", 280, 250, "Arial", 24, Color.white);
		pauseLabel2.setOutlineColor(Color.black);
		pauseLabel2.setOutlineThickness(2.0f);

		pauseLabel3 = new SpriteFont("sprint - shift", 320, 280, "Arial", 24, Color.white);
		pauseLabel3.setOutlineColor(Color.black);
		pauseLabel3.setOutlineThickness(2.0f);

		pauseLabel4 = new SpriteFont("toggle flashlight - f", 290, 310, "Arial", 24, Color.white);
		pauseLabel4.setOutlineColor(Color.black);
		pauseLabel4.setOutlineThickness(2.0f);

		pauseLabel5 = new SpriteFont("interact, pick up / drop items - space", 200, 340, "Arial", 24, Color.white);
		pauseLabel5.setOutlineColor(Color.black);
		pauseLabel5.setOutlineThickness(2.0f);

		pauseLabel6 = new SpriteFont("1-9 - select inventory item", 260, 370, "Arial", 24, Color.white);
		pauseLabel6.setOutlineColor(Color.black);
		pauseLabel6.setOutlineThickness(2.0f);

		pauseLabel7 = new SpriteFont("0 - deselect inventory item", 260, 400, "Arial", 24, Color.white);
		pauseLabel7.setOutlineColor(Color.black);
		pauseLabel7.setOutlineThickness(2.0f);

		fpsDisplayLabel = new SpriteFont("FPS", 4, 3, "Arial", 12, Color.black);

		currentFPS = Config.TARGET_FPS;

		// this game loop code will run in a separate thread from the rest of the program
		// will continually update the game's logic and repaint the game's graphics
		GameLoop gameLoop = new GameLoop(this);
		gameLoopProcess = new Thread(gameLoop.getGameLoopProcess());
	}

	// this is called later after instantiation, and will initialize screenManager
	public void setupGame() {
		setBackground(Colors.CORNFLOWER_BLUE);
		screenManager.initialize(new Rectangle(getX(), getY(), getWidth(), getHeight()));
	}

	// this starts the timer (the game loop is started here)
	public void startGame() {
		gameLoopProcess.start();
	}

	public ScreenManager getScreenManager() {
		return screenManager;
	}

	public void setCurrentFPS(int currentFPS) {
		this.currentFPS = currentFPS;
	}

	public void setDoPaint(boolean doPaint) {
		this.doPaint = doPaint;
	}

	public void update() {
		updatePauseState();
		updateShowFPSState();

		if (!isGamePaused) {
			screenManager.update();
		}
	}

	private void updatePauseState() {
		if (Keyboard.isKeyDown(pauseKey) && !keyLocker.isKeyLocked(pauseKey)) {
			isGamePaused = !isGamePaused;
			keyLocker.lockKey(pauseKey);
		}

		if (Keyboard.isKeyUp(pauseKey)) {
			keyLocker.unlockKey(pauseKey);
		}
	}

	private void updateShowFPSState() {
		if (Keyboard.isKeyDown(showFPSKey) && !keyLocker.isKeyLocked(showFPSKey)) {
			showFPS = !showFPS;
			keyLocker.lockKey(showFPSKey);
		}

		if (Keyboard.isKeyUp(showFPSKey)) {
			keyLocker.unlockKey(showFPSKey);
		}

		fpsDisplayLabel.setText("FPS: " + currentFPS);
	}

	public void draw() {			
		// draw current game state
		screenManager.draw(graphicsHandler);

		// if game is paused, draw pause gfx over Screen gfx
		if (isGamePaused) {
			pauseLabel.draw(graphicsHandler);
			pauseLabel2.draw(graphicsHandler);
			pauseLabel3.draw(graphicsHandler);
			pauseLabel4.draw(graphicsHandler);
			pauseLabel5.draw(graphicsHandler);
			pauseLabel6.draw(graphicsHandler);
			pauseLabel7.draw(graphicsHandler);
			graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), new Color(0, 0, 0, 100));
		}

		if (showFPS) {
			fpsDisplayLabel.draw(graphicsHandler);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (doPaint) {
			// every repaint call will schedule this method to be called
			// when called, it will setup the graphics handler and then call this class's draw method
			graphicsHandler.setGraphics((Graphics2D) g);
			draw();
		}
	}
}
