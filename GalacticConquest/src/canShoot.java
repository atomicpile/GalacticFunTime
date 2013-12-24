/**
 * This interface ensures that all objects that implement it have both methods
 * required in order to fire a weapon. This is necessary if you want an object
 * to shoot using the methods outlined in this program. An object will not be
 * able to shoot unless it implements this interface.
 * 
 * @author Andrew
 * 
 */
public interface canShoot {

	/**
	 * This method acts as a counter for the entity which determines how often it shoots.  It is implemented in its own way
	 * 
	 * @param target
	 */
	public void tryToShoot(Entity target);

	/**
	 * This method holds the logic for the actual firing of the weapon. This is
	 * implemented in various ways for different kinds of turrets.
	 * 
	 * @param target
	 */
	public void fireWeapon(Entity target);
}
