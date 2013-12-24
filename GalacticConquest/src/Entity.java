import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

/**
 * This abstract class defines what an entity is. An entity is any object that
 * moves around the screen, and can be collided with. Most commonly, an entity
 * is a form of projectile, or a ship.
 * 
 * @author Andrew
 * 
 */
public abstract class Entity implements EntitySuiteInterface {

	// Whether the entity has arrived at its destination
	private boolean arrived;
	// TODO The bounding box associated with this entity, may eventually change
	// to something more accurate
	private Rectangle2D.Double boundingBox = new Rectangle2D.Double();
	// The current speed of this entity, subject to change especially for the
	// player.
	protected double currentSpeed;
	// The current XY coordinate destination of this entity
	protected double destX = 0;
	protected double destY = 0;
	// The vector list of hardpoints associated with this entity. This list is
	// called in update methods, as each hardpoint knows its attached component,
	// and how to update it.
	protected Vector<HardPoint> hardpoints;
	// The health bar associated with this entity, this is drawn to the screen
	// below the entity
	protected StatBar healthBar;
	// Whether the logic of this entity is updated, will be deleted and
	// garbage-collected if this is false at the end of a game loop cycle
	protected boolean isEnabled = true;
	// Whether this entity is a player and needs to be treated as such in
	// updating game logic
	protected boolean isPlayer;
	// True if this entity is a projectile and be treated as such in updating
	// game logic
	protected boolean isProjectile = false;
	// Rendering method only renders this object if this boolean is true
	protected boolean isVisible = true;
	// Coordinates of this entity, lastXPos and lastYPos are for interpolation
	protected double lastX;
	protected double lastY;
	// The maximum speed of this entity
	protected double maxSpeed;
	// The vector defining movement for this entity
	protected Vector2D movementVector;
	// Graphical sprite associated w/ this entity
	protected Sprite sprite;
	// The theta value for the movement vector, subject to change depending on
	// the entity
	protected double theta;
	// Current XY cartesian coordinate positioning
	protected double x;
	protected double y;

	/**
	 * The constructor this entity. Takes a filename for the image of the
	 * sprite, and the starting x and y coordinates in the viewable area
	 * 
	 * @param ref
	 *            The name of the image in the ~/sprites folder, without the
	 *            .png extension
	 * @param x
	 *            The starting X coordinate for this entity
	 * @param y
	 *            The starting Y coordinate for this entity
	 */
	public Entity(String ref, double x, double y) {
		// Sets the starting position in the XY plane
		this.x = x;
		this.y = y;
		// Sets to true initially and the entity sets a destination
		this.arrived = true;
		// The reference to the sprite picture file
		// Gets the sprite from the ~src/sprites and puts it in the cache
		this.sprite = SpriteStore.get().getSprite(ref);
		sprite.setAttachedEnt(this);
		hardpoints = new Vector<HardPoint>();
		// The initial theta to set for the movement vector for this entity
		// Creates the bounding box for this entity, based on the size of the
		// sprite
		boundingBox.setRect(x, y, sprite.getSpriteWidth(),
				sprite.getSpriteHeight());

	}

	@Override
	/**
	 * Method to detect the collision of this entity with another entity.  This
	 *  method is called in the GameEngine class by the detectCollisions() method, 
	 *  and is given an entity to compare to.  If there is a collision detected,
	 *  determined by whether the boundingBox of this entity, then this method
	 *  runs the ifCollided() methods of both entities.
	 */
	public void detectCollision(Entity entB) {
		// A collision counter to check how many times this method was called
		GameEngine.collisionsChecked++;
		// This entire block is run only if the bounding boxes for entA and entB
		// intersect TODO hard code intersection calculations instead?
		if (this.boundingBox.intersects(entB.boundingBox)) {
			if (this.isPlayer && entB.isProjectile) {
				if (!((Projectile) entB).isPlayerProjectile) {
					this.ifCollided(entB);
					entB.ifCollided(this);
				}
			} else if (((Ship) this).isEnemy && entB.isProjectile) {
				if (((Projectile) entB).isPlayerProjectile) {
					this.ifCollided(entB);
					entB.ifCollided(this);
				}
			}
		}
	}

	/**
	 * Disables the entity, which is then deleted from the entity cache and
	 * garbage collected after one game loop cycle
	 */
	public void disable() {
		isEnabled = false;
	}

	/**
	 * The method for drawing this entity, draws both the sprite and the
	 * bounding boxes, if that portion of the code is not commented out
	 */
	@Override
	public void draw(Graphics2D g2D, float interp) {
		double interpX = (x - lastX) * interp + lastX;
		double interpY = (y - lastY) * interp + lastY;
		// Stores the incoming color of the graphics object
		Color temp = g2D.getColor();
		// Draws the sprite associated with this object
		// TODO add a counter and boolean to eventually animate sprites here?
		sprite.draw(g2D, interpX, interpY, movementVector.getTheta());
		if (!this.isProjectile)
			healthBar.draw(g2D, interp);
		// Currently, sets the color of the bounding boxes to be drawn
		g2D.setColor(Color.BLUE);
		// Draws any components attached to hardpoints on this ship
		for (HardPoint currPoint : hardpoints) {
			if (currPoint.getComponent() != null) {
				currPoint.getComponent().draw(g2D, interp);
			}
		}
		// TODO Comment or uncomment to hide or show bounding boxes for all
		// entities
		// g2D.draw3DRect((int) boundingBox.x, (int) boundingBox.y,
		// (int) boundingBox.width, (int) boundingBox.height, true);
		// Returns the graphics color to the original color so this draw method
		// does not affect entire game graphics
		g2D.setColor(temp);
	}

	/**
	 * Enables this entity, not currently used, as all entities are enabled by
	 * default
	 */
	public void enable() {
		isEnabled = true;
	}

	/**
	 * Gets the height of the bounding box for this entity. Used quite a lot for
	 * targeting specific parts of the ship etc, as the bounding box dimensions
	 * can be used to determine a position relative to the XY position of this
	 * entity
	 * 
	 * @return the height of the bounding box for this entity
	 */
	public double getHeight() {
		return sprite.getSpriteHeight();
	}

	/**
	 * Used for interpolation purposes
	 * 
	 * @return the lastXposition of this entity
	 */
	public double getLastX() {
		return this.lastX;
	}

	/**
	 * Used for interpolation purposes.
	 * 
	 * @return the last Y position of this entity
	 */
	public double getLastY() {
		return this.lastY;
	}

	/**
	 * Returns the current theta value for this entity. This corresponds to the
	 * total rotation of the object.
	 * 
	 * @return the theta value associated with this object
	 */
	public double getTheta() {
		return theta;
	}

	/**
	 * Gets the width of the sprite object associated with this entity.
	 * 
	 * @return the width of the sprite
	 */
	public double getWidth() {
		return sprite.getSpriteWidth();
	}

	/**
	 * Gets the X coordinate of this entity. The X coordinate is defined as the
	 * left side of the sprite.
	 * 
	 * @return the x coordinate of this entity
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * Gets the Y coordinate of this entity. The Y coordinate is defined as the
	 * top side of the sprite.
	 * 
	 * @return the y coordinate of this entity
	 */
	public double getY() {
		return this.y;
	}

	/**
	 * Hides this entity. All hidden entities are not rendered in the viewable
	 * area, but are not disabled. Likely a good implementation when a world map
	 * is introduced and the entity is off screen.
	 */
	public void hide() {
		isVisible = false;
	}

	/**
	 * Checks whether this entity is enabled. All enabled entities are updated
	 * when updateGameLogic() is called in the game engine.
	 * 
	 * @return isEnabled Whether this entity is enabled
	 */
	public boolean isEnabled() {
		return isEnabled;
	}

	/**
	 * Returns whether this entity is a projectile. Projectiles are checked
	 * differently for collision than are ships, which is why this boolean is
	 * used.
	 * 
	 * @return isProjectile Whether this entity is a projectile
	 */
	protected boolean isProjectile() {
		return this.isProjectile;
	}

	/**
	 * Returns whether this entity is visible. Visible entities are rendered to
	 * the screen.
	 * 
	 * @return isVisible Whether this entity is visible on the screen
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * This method moves the entity using a Vector2D object. Furthermore, an
	 * AffineTransform object translates the entitys bounding box.
	 */
	public void move() {
		// Calculated for interpolation
		lastX = x;
		lastY = y;
		// Steps the vector, which also steps the entity itself
		movementVector.step();
		// Adjusts the bounding box to the new position of this entity
		boundingBox.x = x;
		boundingBox.y = y;
		// Picks a random destination for this entity if it is neither a player
		// nor a projectile.
		if (!this.isPlayer && !this.isProjectile) {
			pickDestination();
		}
	}

	/**
	 * This method is called every tick, and checks to see whether the entity
	 * has arrived at is destination, within a certain tolerance (in order to
	 * prevent mistakes, or circling around a destination). If the entity has
	 * arrived at its destination, then it randomly picks a new destination
	 * within the current boundary of the visible game window.
	 * 
	 */
	public void pickDestination() {
		// Checks to see whether the unit has arrived at its destination within
		// the tolerance of max speed so that an entity doesn't continue to try
		// and fly to its exact destination as it continually overshoots because
		// its speed updates its position over the destination.
		if ((this.x >= this.destX - maxSpeed)
				&& (this.x <= this.destX + maxSpeed)) {
			arrived = true;
		}
		// Runs this block to pick a new destination if the unit has indeed
		// arrived at its location
		if (arrived) {
			// Picks new random XY coordinates to fly to on the visible screen.
			destX = GameEngine.getRNG() * GameEngine.displayWidth;
			destY = GameEngine.getRNG() * GameEngine.displayHeight;
			// Sets the destination in the movement vector, which then
			// recalculates a heading.
			movementVector.setxDest(destX);
			movementVector.setyDest(destY);
			// Once a new destination is picked, the entity is no longer arrived
			// at its destination.
			arrived = false;
		}
	}

	/**
	 * A method to rotate the bounding box of this entity. Not currently
	 * implemented.
	 */
	public void rotateBoundingBox() {

	}

	/**
	 * Sets the current theta value for this entity. Currently it is used by the
	 * movement vector to determine which direction the entity should face, and
	 * is also used to determine at what angle the entity should be drawn.
	 * 
	 * @param theta
	 *            the theta to set
	 */
	public void setTheta(double theta) {
		this.theta = theta;
	}

	/**
	 * This method sets the X coordinate of this entity. This method is called
	 * by the Vector2D objects' step() method each cycle.
	 * 
	 * @param xPos
	 *            the xPos to set
	 */
	public void setXPos(double xPos) {
		this.x = xPos;
	}

	/**
	 * This method sets the Y coordinate of this entity. This method is called
	 * by the Vector2D objects' step() method each cycle.
	 * 
	 * @param yPos
	 *            the yPos to set
	 */
	public void setYPos(double yPos) {
		this.y = yPos;
	}

	/**
	 * Sets the visibility of this entity to true so that it can be rendered
	 */
	public void show() {
		isVisible = true;
	}

	@Override
	/**
	 * The string representation of this entity
	 */
	public String toString() {
		return "boundingBox=" + boundingBox + ", isVisible=" + isVisible
				+ "]\n";
	}

	/**
	 * The update method of this entity, currently all it does is call the
	 * move() method, and if the entity is not a player or a projectile,
	 * attempts to shoot (because this makes the assumption that the only other
	 * entities other than projectiles and player ships are enemy ships).
	 */
	@Override
	public void update() {
		// Moves this entity and picks the destination if need be
		move();
		// Rotates the bounding box so that collision detection still happens
		// accurately.
		rotateBoundingBox();
		// Updates all hardpoints attached to this entity. Each hardpoint has an
		// update() method, which calls the update() method of the component
		// attached to it, if there is one.
		for (HardPoint currPoint : hardpoints) {
			currPoint.update();
		}
		// Updates the healthbar associated with this entity, as long as its not
		// a projectile (projectiles don't have health!)
		if (!this.isProjectile) {
			healthBar.update();
		}
	}
}
