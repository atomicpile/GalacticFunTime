import java.awt.Graphics2D;

/**
 * The component class is an abstraction layer which defines exactly what a
 * component is. A component is a piece of "ship hardware" which gives a ship
 * some sort of capability, either engines, shields, weapons etc. A module
 * should be able to completely encapsulate a specific function of the ship.
 * 
 * @author Andrew
 * 
 */
public abstract class Component {

	// The ship this component is associated with through the hardpoint
	protected Ship attachedShip;
	// The XY coordinates of the component on the last cycle, used for
	// interpolation in smooth rendering.
	protected double lastX;
	protected double lastY;
	// The sprite associated with this component
	protected Sprite sprite;
	// The angle of rotation associated with this component. Likely implemented
	// by turrets and anything else that rotates independent of the ship. At
	// least that's how I'd like it implemented
	double theta;
	// XY coordinates of this component in the viewable area, which are set by
	// the hardpoint object to which this component is attached
	protected double x;
	protected double y;

	/**
	 * The constructor for this component. Takes in a ship this component is
	 * associated with, and the String ref, which is the image associated with
	 * the sprite representative of this component.
	 * 
	 * @param ship
	 *            The ship this component is associated with.
	 * @param ref
	 *            The String name of the image that represents the sprite
	 */
	public Component(Ship ship, String ref) {
		// The ship entity this component is associated with. Used for referring
		// to the position of the ship, amongst other things.
		this.attachedShip = ship;
		// Retrieves the sprite from the ~/sprites folder and places it in the
		// cache
		this.sprite = SpriteStore.get().getSprite(ref);
		// Sets the loaded sprite to this component. This line allows the
		// component to be translated using interpolated values in the
		// sprite.draw() method in the sprite class
		this.sprite.setAttachedComponent(this);
	}

	/**
	 * This method is responsible for drawing the representative sprite of this
	 * component at an interpolated XY position. It takes the Graphics2D g2D
	 * object, which is used to draw the image to the graphics context. It also
	 * takes the double interpolation value, which is the value used to
	 * interpolate the actual or likely position this object should be rendered
	 * at to achieve a smooth graphics rendering.
	 * 
	 * @param g2D
	 * @param interp
	 */
	public void draw(Graphics2D g2D, double interp) {
		// The interpolated X and Y coordinates the image should be drawn at.
		double interpX = (x - lastX) * interp + lastX;
		double interpY = (y - lastY) * interp + lastY;
		// Does the actual drawing of the sprite to the g2D context at the
		// interpolated values, with any associated theta rotation.
		// TODO likely will not behave as expected, given that an
		// AffineTransform object will likely need to be defined in order to
		// properly rotate this object. Refer to entity affine transformation
		// for more info on this.
		this.sprite.draw(g2D, interpX, interpY, theta);
	}

	/**
	 * Returns the x coordinate on the viewable display of this component.
	 * 
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the y coordinate on the viewable display of this component.
	 * 
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sets the value of the last X value of this component. This method is used
	 * by the hardpoint this component is attached to to make sure this is
	 * interpolated and drawn correctly.
	 * 
	 * @param lastX
	 *            The X position of this component on the previous cycle.
	 */
	public void setLastX(double lastX) {
		this.lastX = lastX;
	}

	/**
	 * Sets the value of the last Y value of this component. This method is used
	 * by the hardpoint this component is attached to to make sure this is
	 * interpolated and drawn correctly.
	 * 
	 * @param lastY
	 *            The Y position of this component on the previous cycle.
	 */
	public void setLastY(double lastY) {
		this.lastY = lastY;
	}

	/**
	 * This method is used by the hardpoint step() method in order to update the
	 * current position of this component at the hardpoint.
	 * 
	 * @param x
	 *            The current X position of this component this cycle.
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * This method is called by the hardpoint step() method in order to set this
	 * component.
	 * 
	 * @param y
	 *            The current Y position of this component at the hardpoint.
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * This method declaration, given that this is an abstract class, requires
	 * each component inheriting from this class to implement an update method,
	 * which in principle will be unique to that type of component (called to
	 * determine how turrets fire weapons, how shields shield, how engines
	 * thrust etc). This method is ultimately called by the entity itself, as
	 * each component is stored in a "component cache," which is iterated
	 * through to call the update() method of each unique component.
	 */
	public abstract void update();
}
