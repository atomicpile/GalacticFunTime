import java.awt.Color;

/**
 * This class defines what the player ship is, and how it functions. It is an
 * extension of the abstract Ship class, therefore behaves very similarly to
 * other ships. Contains the methods required for the player to interact with
 * it.
 * 
 * @author Andrew
 * 
 */
public class PlayerShip extends Ship {
	// Drag coefficient to slow down the player ship, not currently used.
	double drag = 0.05;
	// TODO Eventually change so that the player ship shoots from set turret
	// positions as opposed to a hard-coded value
	// Used so that the player ship shoots from both sides
	boolean shootLeft = true;

	/**
	 * The public constructor of the player ship. Takes no arguments, as they
	 * are all defined in the constructor
	 */
	public PlayerShip() {
		// Sets the sprite image and the starting position in pixels of the
		// player
		super("playerCruiser", GameEngine.displayWidth / 2,
				GameEngine.displayHeight / 2);
		// A player, obviously
		isPlayer = true;
		// The "life" of this player
		hullIntegrity = 100;
		// How much to increment the speed by each cycle if engine is thrusted
		// TODO eventually relocate this to an engine class, because it would be
		// cooler
		thrust = .5;
		// The speed at which to start the player
		currentSpeed = 1;
		// The fastest speed this player can achieve
		maxSpeed = 8;
		// How quickly theta can change in the movement vector
		turningRate = .02;
		// The starting theta of the movement vector
		// theta = 0;
		// Generates the health bar for this player
		healthBar = new StatBar(hullIntegrity, false, Color.GREEN, this);
		healthBar.setAttachedEnt(this);
		// Creates the movement vector so the player can control the ship
		movementVector = new Vector2D(this.x + sprite.getSpriteWidth() / 2,
				this.y + sprite.getSpriteWidth() / 2, this.x
						+ sprite.getSpriteWidth(), this.y
						+ sprite.getSpriteHeight() / 2, currentSpeed, this);
		movementVector.setPlayerVector(true);
		projectileSpeed = 25;
		fireRate = 5;

		// Creates hardpoints and adds turrets to them
		ShipTurret turret1 = new ShipTurret(this);
		turret1.setFireRate(fireRate);
		turret1.setProjectileSpeed(projectileSpeed);
		turret1.setPlayer(true);
		hardpoints.add(new HardPoint(this, .75, .5, turret1));
		ShipTurret turret2 = new ShipTurret(this);
		turret2.setFireRate(fireRate);
		turret2.setProjectileSpeed(projectileSpeed);
		turret2.setPlayer(true);
		hardpoints.add(new HardPoint(this, .5, .5, turret2));
	}

	public void thrust(boolean W_pressed, boolean S_pressed, boolean A_pressed,
			boolean D_pressed) {
		if (W_pressed && currentSpeed <= maxSpeed) {
			currentSpeed += thrust;
		}
		if (S_pressed && currentSpeed >= 0) {
			currentSpeed -= thrust * .5;
		}
		if (A_pressed)
			theta -= turningRate;
		if (D_pressed)
			theta += turningRate;
		this.movementVector.setTheta(theta);
		this.movementVector.setSpeed(currentSpeed);
	}
}