import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameEngine {

	public class GameWindow extends Canvas {
		private static final long serialVersionUID = 3235738755392294029L;
		private BufferStrategy strategy;

		public GameWindow(int displayWidth, int displayHeight) {
			// Creates the frame to hold the game
			JFrame displayFrame = new JFrame("Galactic Conquest!");
			// Get a hold of the content of the frame and sets up resolution of
			// the game
			JPanel panel = (JPanel) displayFrame.getContentPane();
			panel.setPreferredSize(new Dimension(displayWidth, displayHeight));
			panel.setLayout(null);
			// Sets up canvas size and puts into content of the frame
			setBounds(0, 0, displayWidth, displayHeight);
			panel.add(this);
			panel.setBackground(Color.BLACK);
			// Tells AWT not to bother repainting our canvas
			setIgnoreRepaint(true);
			// Makes the window visible
			displayFrame.pack();
			displayFrame.setResizable(false);
			displayFrame.setVisible(true);
			// Listener to respond to the user closing the window
			displayFrame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			addKeyListener(new KeyboardInputHandler());

			addMouseListener(new MouseControl()); // mouse control
			addMouseMotionListener(new MouseMove()); // mouse control 2

			requestFocus();
			createBufferStrategy(2);
			strategy = getBufferStrategy();
		}

		public BufferStrategy getStrategy() {
			return this.strategy;
		}
	}

	// Class for handling keyboard input
	class KeyboardInputHandler extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.VK_W) {
				W_pressed = true;
			}
			if (event.getKeyCode() == KeyEvent.VK_S) {
				S_pressed = true;
			}
			if (event.getKeyCode() == KeyEvent.VK_A) {
				A_pressed = true;
			}
			if (event.getKeyCode() == KeyEvent.VK_D) {
				D_pressed = true;
			}
			if (event.getKeyCode() == KeyEvent.VK_SPACE) {
				space_pressed = true;
			}
			if (event.getKeyCode() == KeyEvent.VK_P) {
				P_pressed = true;
			}
		}

		@Override
		public void keyReleased(KeyEvent event1) {
			if (event1.getKeyCode() == KeyEvent.VK_W) {
				W_pressed = false;
			}
			if (event1.getKeyCode() == KeyEvent.VK_S) {
				S_pressed = false;
			}
			if (event1.getKeyCode() == KeyEvent.VK_A) {
				A_pressed = false;
			}
			if (event1.getKeyCode() == KeyEvent.VK_D) {
				D_pressed = false;
			}
			if (event1.getKeyCode() == KeyEvent.VK_SPACE) {
				space_pressed = false;
			}
			if (event1.getKeyCode() == KeyEvent.VK_P) {
				P_pressed = false;
			}
		}

		@Override
		public void keyTyped(KeyEvent event2) {
			if (event2.getKeyChar() == 27) {
				System.exit(0);
			}

		}
	}

	class MouseControl extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			mouseDown = true;
			mouseX = e.getX();
			mouseY = e.getY();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			mouseDown = false;
			mouseX = e.getX();
			mouseY = e.getY();
		}
	}

	class MouseMove extends MouseMotionAdapter {
		@Override
		public void mouseDragged(MouseEvent e) {
			mouseDown = true;
			mouseX = e.getX();
			mouseY = e.getY();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mouseDown = false;
			mouseX = e.getX();
			mouseY = e.getY();
		}
	}

	// A useful stat for keeping track of the number of total collisions checked
	// per cycle.
	// This number is averaged against the total number of cycles
	public static double collisionsChecked = 0;
	// The height of the display in pixels
	public static int displayHeight;
	// The width of the display in pixels
	public static int displayWidth;
	// The Vector list which holds all entities in game
	public static Vector<Entity> entityCache;
	// The graphics object for the game...shouldn't need more than one
	public static Graphics2D g2D;
	public static float interpolation = 1;
	// The X position of the mouse
	public static double mouseX;
	// The Y position of the mouse
	public static double mouseY;
	private static final int NANOSECONDS_PER_SECOND = 1000000000;
	// Key to pause the game
	public static boolean P_pressed;
	// The player ship
	public static PlayerShip player;

	/**
	 * A quick random number generator for the game
	 * 
	 * @return a random double between 0 and 1
	 */
	public static double getRNG() {
		Random rng = new Random();
		double randDouble = rng.nextDouble();
		return randDouble;
	}

	// Key for leftwards movement
	public boolean A_pressed;
	// Key for rightwards movement
	public boolean D_pressed;
	// The game window which displays the game
	private GameWindow display;
	// True when the mouse button is depressed
	public static boolean mouseDown;
	// Refines which objects are checked against other objects in a 300 unit
	// distance
	double numberCycles = 0;
	// Whether the game loop is paused
	boolean paused = false;
	// The quadtree for storing entities in a 2D grid spanning the game space
	Quadtree quad;
	// Key for backwards movement
	public boolean S_pressed;
	// Key to fire weapons
	public boolean space_pressed;
	int thisSecond;
	// Key for forward movement
	public boolean W_pressed;

	/**
	 * Public constructor for starting the game engine
	 * 
	 */
	public GameEngine(int displayWidth, int displayHeight) {
		GameEngine.displayWidth = displayWidth;
		GameEngine.displayHeight = displayHeight;
		display = new GameWindow(displayWidth, displayHeight);
		quad = new Quadtree(0, new Rectangle(0, 0, displayWidth, displayHeight));
		entityCache = new Vector<Entity>();
		player = new PlayerShip();
		entityCache.add(player);
		initGameLoop();
	}

	public void addEntity(Entity ent) {
		entityCache.add(ent);
	}

	/**
	 * 
	 */
	private void checkCollisions() {
		// Clears the quadtree
		quad.clear();
		for (Entity currEntity : entityCache) {
			// Inserts the entity into the quadtree if it is enabled
			if (currEntity.isEnabled) {
				quad.insert(currEntity);
			}
		}
		for (Entity currEnt : entityCache) {
			// Pulls all entities near current non-projectile entity from the
			// quadtree and checks them
			if (!currEnt.isProjectile) {
				Vector<Entity> temp = quad.retrieve(currEnt);
				for (Entity ent : temp) {
					if (currEnt != ent) {
						// Calls the collisions detection method for current
						// entity against all eligible entities pulled from
						// quadtree
						currEnt.detectCollision(ent);
					}
				}
			}
		}
	}

	private void gameLoop() {
		final double GAME_HERTZ = 30.0;
		boolean running = true;

		final double TIME_BETWEEN_UPDATES = NANOSECONDS_PER_SECOND / GAME_HERTZ;
		// Maximum times a game will update before a new render
		// TODO Set to 1 if worried about graphical errors more than perfect
		// timing
		final int MAX_UPDATES_BEFORE_RENDER = 3;
		double lastUpdateTime = System.nanoTime();
		double lastRenderTime = System.nanoTime();
		final double TARGET_FPS = 60;
		final double TARGET_TIME_BETWEEN_RENDERS = NANOSECONDS_PER_SECOND
				/ TARGET_FPS;
		// Simple way to find the FPS
		int lastSecondTime = (int) (lastUpdateTime / NANOSECONDS_PER_SECOND);
		while (running) {
			long currentTime = System.nanoTime();
			int updateCount = 0;
			paused = P_pressed;
			if (!paused) {
				while (currentTime - lastUpdateTime > TIME_BETWEEN_UPDATES
						&& updateCount < MAX_UPDATES_BEFORE_RENDER) {
					// Updates the entire game logic
					updateGameLogic();
					lastUpdateTime += TIME_BETWEEN_UPDATES;
					updateCount++;
				}

				// If an update takes a long time, limits the number of catchup
				// cycles
				// If EXACT time is needed, this if statement should be removed
				if (currentTime - lastUpdateTime > TIME_BETWEEN_UPDATES) {
					lastUpdateTime = currentTime - TIME_BETWEEN_UPDATES;
				}
				// The rendering block, interpolation is calculated for a smooth
				// render of graphics
				interpolation = Math
						.min(1.0f,
								(float) ((currentTime - lastUpdateTime) / TIME_BETWEEN_UPDATES));
				// Renders all game objects
				render(g2D);
				lastRenderTime = currentTime;
				// Updates the number of frames we got
				thisSecond = (int) (lastUpdateTime / NANOSECONDS_PER_SECOND);
				if (thisSecond > lastSecondTime) {
					lastSecondTime = thisSecond;
				}

				// Garbage collects some objects that aren't being used

				System.gc();
				// Yields the threat until it has been the target time between
				// renders. Saves on CPU hogging.
				while ((currentTime - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && currentTime
						- lastUpdateTime < TIME_BETWEEN_UPDATES)
						&& !paused) {
					Thread.yield();

					// This stops the app from consuming the entire CPU.
					// Slightly
					// less accurate, but worth it.
					// Removing this line causes the game to work better, but
					// CPU
					// starts to work overtime
					// TODO Causes stuttering on some OS's
					try { Thread.sleep(1) ; } catch(Exception e) {}
					numberCycles++;
					currentTime = System.nanoTime();
				}
			}
		}
	}

	/**
	 * Initializes a new thread which runs the game loop
	 */
	public void initGameLoop() {
		Thread gameLoopThread = new Thread() {
			@Override
			public void run() {
				gameLoop();
			}
		};
		gameLoopThread.start();
	}

	public void render(Graphics2D g2D) {
		g2D = (Graphics2D) display.getBufferStrategy().getDrawGraphics();
		g2D.setColor(Color.BLACK);
		g2D.draw3DRect(0, 0, displayWidth, displayHeight, true);
		// Renders the UI of the game (currently not that good obviously
		renderUI(g2D);
		g2D.setColor(Color.BLACK);
		try {
			for (Entity currEntity : entityCache) {
				if (currEntity.isVisible()) {

					currEntity.draw(g2D, interpolation);
				}
			}
		} catch (NullPointerException e) {
			return;
		}
		g2D.dispose();
		display.getBufferStrategy().show();
	}

	/**
	 * @param g2D
	 */
	private void renderUI(Graphics2D g2D) {
		g2D.setFont(new Font("Impact", 1, 20));
		Sprite bkgd;
		bkgd = SpriteStore.get().getSprite("bkgd");
		bkgd.draw(g2D, 0, 0, 0);
		g2D.setColor(Color.WHITE);
		// UI for entities on screen and collision checks
		g2D.drawString("Entities on Screen: " + entityCache.size(),
				(int) (displayWidth * .01), (int) (displayHeight * .03));
		/*g2D.drawString("Average Collision Checks per cycle: "
				+ (collisionsChecked / numberCycles),
				(int) (displayWidth * .03), (int) (displayHeight * .05));*/
		g2D.drawString("Number of cycles so far: " + numberCycles,
				(int) (displayWidth * .01), (int) (displayHeight * .05));
		g2D.drawString( "Hull Integrity: ",(int)(displayWidth * .01), (int)(displayHeight * .96) );
		if (!player.isEnabled()) {
			g2D.drawString("You Lose!", displayWidth / 2, displayHeight / 2);
		}
		
		if (!checkVictory())
		{
			g2D.drawString("You Win!!", displayWidth / 2, displayHeight / 2);
			
		}
	}

	private void updateEntities() {
		// The tolerance for how far off screen an entity can go before it is
		// disabled
		for (int i = 0; i < entityCache.size(); i++) {
			Entity currEntity = entityCache.get(i);
			// Disables any entity that goes over the sides of the screen
			if (currEntity.getX() > displayWidth * 2
					|| currEntity.getY() > displayHeight * 2
					|| currEntity.getX() < 0 - displayWidth
					|| currEntity.getY() < -displayHeight) {
				//TODO currently commented out so entities aren't disabled when they go off screen 
				currEntity.disable();
			}
			if (currEntity.isEnabled()) {
				currEntity.update();
			}
			if (!currEntity.isEnabled()) {
				entityCache.removeElementAt(i);
				i--;
			}
		}
		// Run collision detection method
		checkCollisions();
	}

	/**
	 * The super method for updating all game logic
	 */
	private void updateGameLogic() {
		// Updates the player and checks for input etc
		updatePlayer();
		/*
		 * Updates all entities in the following manner: 1) Checks for
		 * collisions 2) Disables any entities that went off screen 3) Calls the
		 * update method on any entities that are not disabled 4) Removes all
		 * disabled entities
		 */
		updateEntities();
		checkVictory();
	}

	/**Checks victory conditions by looping through entity cache and seeing if all
	 * hostile entities were destroyed
	 * 
	 */
	private boolean checkVictory() {
		boolean found = false;
		for (Entity currEnt : entityCache)
		{
			if (!currEnt.isProjectile())
			{
				if (((Ship)currEnt).isHostile)
				{
					found = true;
				}
			}	
		}
		return found; 
	}

	private void updatePlayer() {
		//Entity target = null;
		try {
			player.thrust(W_pressed, S_pressed, A_pressed, D_pressed);

		} catch (NullPointerException e)
		{

		}
	}
}
