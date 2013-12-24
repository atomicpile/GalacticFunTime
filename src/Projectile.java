/**
 * This class describes a projectile and it is planned to change this class to
 * abstract as new projectile and weapon types are generated.
 * 
 * @author Andrew
 * 
 */
public class Projectile extends Entity {

	// This boolean is used for certain logic in collision detection to
	// differentiate this projectile from either player or hostile sources.
	public boolean isPlayerProjectile = false;

	/**
	 * @return the isPlayerProjectile
	 */
	public boolean isPlayerProjectile() {
		return isPlayerProjectile;
	}

	/**
	 * Sets whether this is a player projectile. Used to differentiate
	 * projectiles from player vs. hostile sources for collision detection.
	 * 
	 * @param isPlayerProjectile
	 *            the isPlayerProjectile to set
	 */
	public void setPlayerProjectile(boolean isPlayerProjectile) {
		this.isPlayerProjectile = isPlayerProjectile;
	}

	/**
	 * This constructor is used to generate a projectile based on a source
	 * location and a destination, both in XY coordinates
	 * 
	 * @param xPos
	 *            the X coordinate of the source
	 * @param yPos
	 *            The Y coordinate of the source
	 * @param xDest
	 *            The X coordinate of the destination
	 * @param yDest
	 *            The Y coordinate of the destination
	 * @param speed
	 *            The speed of this projectile
	 * @param isPlayerProjectile
	 *            Whether this is a player projectile or not
	 */
	public Projectile(double xPos, double yPos, double xDest, double yDest,
			double speed, boolean isPlayerProjectile) {
		super("red_shot", xPos, yPos);
		this.isPlayerProjectile = isPlayerProjectile;
		isProjectile = true;
		// Given that all projectiles are also entities, there is a
		// movementVector associated with it. This movement vector actually
		// moves the projectile, as it is an entity.
		movementVector = new Vector2D(xPos, yPos, xDest, yDest, speed, this);
	}

	/**
	 * What to do if this projectile is collided.. Currently only disables the
	 * projectile.
	 */
	@Override
	public void ifCollided(Entity ent) {
		disable();
	}
}
