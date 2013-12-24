import java.awt.Graphics2D;

/**
 * This interface provides functionality commonly found in entities. This
 * includes the ability to draw the entity, a collision detection method, and an
 * ifCollided method, which is used by the entity subclasses to determine how
 * that unique entity is affected by collision.
 * 
 * @author Andrew
 * 
 */
public interface EntitySuiteInterface {

	/**
	 * This method draws the entity. Takes a graphics2D context and a
	 * interpolation value for smooth rendering.
	 * 
	 * @param g2D
	 * @param interpolation
	 */
	public abstract void draw(Graphics2D g2D, float interpolation);

	/**
	 * This method is called by the GameEngine object to detect a collison for
	 * each entity.
	 * 
	 * @param ent
	 *            the entity that is to be checked for collisions with other
	 *            entities.
	 */
	public void detectCollision(Entity ent);

	/**
	 * What to do if the entity collides with another entity
	 * 
	 * @param ent
	 *            The entity that this entity instance collides with
	 */
	public void ifCollided(Entity ent);

	/**
	 * Moves this entity, and updates hardpoints attached to it
	 */
	public void update();
}
