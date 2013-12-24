import java.awt.Color;

/**
 * This class defines the enemy ship type "Destroyer". There are no necessarily
 * specific attributes of a destroyer, only that right now it is acting as an
 * enemy for testing purposes.
 * 
 * @author Andrew
 * 
 */
public class Destroyer extends Ship {

	/**
	 * This constructor takes no parameters itself, but it does send both the
	 * image URL, and a random XY coordinate in which this unit will spawn. All
	 * necessary fields are defined in this constructor to create an enemy
	 * destroyer object.
	 */
	public Destroyer() {
		// Calls the superclass constructor to use a specific image, and to
		// spawn the unit at a random area in the visble field
		super("Green_Ship_Small",
				GameEngine.getRNG() * GameEngine.displayWidth,
				GameEngine.displayHeight * GameEngine.getRNG());
		// Boolean to determine whether this unit is a player. This boolean is
		// used for specific algorithms to detect player input
		isPlayer = false;
		// Effectively the "life" of this unit
		hullIntegrity = 30;
		// Whether this unit is hostile to the player
		isHostile = true;
		// Whether this unit is an enemy. TODO Seems redundant when consider an
		// enemy is hostile....
		isEnemy = true;
		// The current magnitude of velocity of this unit. This number is sent
		// to the Vector2D class to determine X and Y velocities based on a
		// theta value.
		currentSpeed = 2;
		// The speed of the projectiles this unit shoots
		projectileSpeed = 25;
		// The health bar associated with this unit. The health bar is currently
		// displayed below the unit.
		healthBar = new StatBar(hullIntegrity, false, Color.green, this);
		// Sets the entity that the health bar is attached to to this instance.
		healthBar.setAttachedEnt(this);
		// Creates a new hardpoint at the given position and adds it to the
		// hardpoints array.
		hardpoints.add(new HardPoint(this, .5, .5));
		// Declares a new ShipTurret object, and attaches it to this ship
		ShipTurret turret1 = new ShipTurret(this);
		// Sets the fire rate of the turret.
		turret1.setFireRate(45);
		// Sets the speed of the projectiles that this unit shoots.
		turret1.setProjectileSpeed(projectileSpeed);
		// Sets the target of this units turret to the player
		turret1.setTarget(GameEngine.player);
		// Does the actual addition or installation of the turret to the created
		// hardpoint for this unit
		hardpoints.get(0).installComponent(turret1);
		// Sets the turning rate for this unit. Not currently used.
		this.turningRate = .01;
		// Creates a movement vector for this unit at the units current
		// destination. This vector also takes a destination coordinate, and
		// TODO in the future will adjust its theta to match a destination
		// theta, thus simulating a turning rate
		this.movementVector = new Vector2D(this.x, this.y, destX, destY,
				currentSpeed, this);
	}
}
