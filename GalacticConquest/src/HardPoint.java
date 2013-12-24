
/**
 * This class defines a hardpoint on an entity, usually a ship of some sort. A
 * hardpoint is used to link a ship or entity to a component, which gives that
 * ship functionality. Primarily, it updates the position of the component, as
 * the update() method of the component itself is called in the entitys update()
 * method. A hardpoint is a convenient way to pre-position areas on a ship where
 * eventual components can be added, and to know that those components will have
 * their positions updated automatically.
 * 
 * @author Andrew
 */
public class HardPoint {

	// The entity that this hardpoint is connected to
	protected Entity attachedEnt;
	// The component that this hardpoint links to the entity
	protected Component component;
	// The XY coordinates of the last cycle in the viewable window. Used for
	// interpolation
	private double lastX;
	private double lastY;
	double deltaX ; 
	double deltaY ; 
	// The scalable position of this hardpoint using the X position and sprite
	// width of the attached entity
	private double percentAcross;
	// The scalable position of this hardpoint using the Y position and sprite
	// height of the attached entity
	private double percentUp;
	// The XY position of this hardpoint in the current display window
	protected double x;
	protected double y;

	/**
	 * This constructor is called if you want to create a hardpoint but not
	 * attach a component to it.
	 * 
	 * @param attachedEnt
	 *            The entity that this hardpoint should be attached to
	 * @param percentAcross
	 *            The percentage of the attached entitys sprite width, used to
	 *            define the X coordinate using the entity as a reference frame.
	 * @param percentUp
	 *            The percentage of the attached entitys sprite height, used to
	 *            define the Y coordinate using the entity as a reference frame.
	 */
	public HardPoint(Entity attachedEnt, double percentAcross, double percentUp) {
		this.percentAcross = percentAcross;
		this.percentUp = percentUp;
		this.attachedEnt = attachedEnt;
		//Sets the initial X and Y
		this.x = attachedEnt.getX() + attachedEnt.getWidth() * percentAcross ;
		this.y = attachedEnt.getY() + attachedEnt.getHeight() * percentUp ; 
	}

	/**
	 * To use this constructor, you must attach a component upon creation of
	 * this hardpoint. Refer to previous constructor remarks for exact
	 * implementation
	 * 
	 * @param attachedEnt
	 *            The entity this hardpoint is associated with
	 * @param percentAcross
	 *            How far across the entity this hardpoint is
	 * @param percentUp
	 *            How far this hardpoint is in the vertical
	 * @param attachedComponent
	 *            The component attached to this hardpoint
	 */
	public HardPoint(Entity attachedEnt, double percentAcross,
			double percentUp, Component attachedComponent) {
		this.percentAcross = percentAcross;
		this.percentUp = percentUp;
		this.attachedEnt = attachedEnt;
		this.component = attachedComponent;
		this.x = attachedEnt.getX() + attachedEnt.getWidth() * percentAcross ;
		this.y = attachedEnt.getY() + attachedEnt.getHeight() * percentUp ; 
		this.deltaX = this.x - (attachedEnt.getX() + attachedEnt.getWidth() / 2);
		this.deltaY = this.y - (attachedEnt.getY() + attachedEnt.getHeight() / 2);
	}

	/**
	 * Returns the entity associated with this hardpoint.
	 * 
	 * @return the attachedEnt
	 */
	public Entity getAttachedEnt() {
		return attachedEnt;
	}

	/**
	 * Returns the component associated with this hardpoint.
	 * 
	 * @return the attachedComponent
	 */
	public Component getComponent() {
		return component;
	}

	/**
	 * Returns the X coordinate of this hardpoint the last update cycle. Used
	 * for interpolation purposes.
	 * 
	 * @return the lastX
	 */
	public double getLastX() {
		return lastX;
	}

	/**
	 * Returns the last Y coordinate of this hardpoint the last update cycle.
	 * Used for interpolation purposes.
	 * 
	 * @return the lastY
	 */
	public double getLastY() {
		return lastY;
	}

	/**
	 * Returns the current X coordinate of this hardpoint on this cycle.
	 * 
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the current Y coordinate of this hardpoint this cycle.
	 * 
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * This method "installs" a component onto this hardpoint. The component
	 * will then have its position automatically updated as if it was actually
	 * attached to the ship at this hardpoints coordinates.
	 * 
	 * @param component
	 *            The attachedComponent to set
	 */
	public void installComponent(Component component) {
		this.component = component;
	}

	/**
	 * Changes the entity that this hardpoint is attached to. The component will
	 * remain installed on this hardpoint. This does not change where on the
	 * entity the hardpoint is attached. It still retains its percentageAcross
	 * and percentageUp values.Not really sure what situation this would be used
	 * in, perhaps as a ship salvager?
	 * 
	 * @param attachedEnt
	 *            The entity to which this hardpoint is associated.
	 */
	public void setAttachedEnt(Entity attachedEnt) {
		this.attachedEnt = attachedEnt;
	}

	/**
	 * This method sets the X coordinate of the last cycle.
	 * 
	 * @param lastX
	 *            the lastX to set
	 */
	public void setLastX(double lastX) {
		this.lastX = lastX;
	}

	/**
	 * This method sets the Y coordinate of the last cycle.
	 * 
	 * @param lastY
	 *            the lastY to set
	 */
	public void setLastY(double lastY) {
		this.lastY = lastY;
	}

	/**
	 * This method sets the current X coordinate of this hardpoint this cycle.
	 * 
	 * @param x
	 *            The X value to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * This method sets the current Y coordinate of this hardpoint this cycle.
	 * 
	 * @param y
	 *            The Y value to set.
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Returns a string representation of this hardpoint, which contains
	 * information including which entity it is attached to, and which component
	 * is attached to it.
	 * 
	 * @return the String representation of this object.
	 */
	@Override
	public String toString() {
		return "Associated Ship: " + attachedEnt + "\n"
				+ "Associated Component: " + component;
	}

	/**
	 * This method completely updates both the position of this hardpoint and
	 * the position of the component attached to this hardpoint, so that the
	 * component update() method does not have to worry about updating its
	 * position. Currently does not properly update component positions upon
	 * rotation of the associated entity.
	 */
	public void update() {
		// Gets the theta value associated with the rotation of the attached
		// entity
		double theta = attachedEnt.getTheta();
		// Sets the values of the previous cycle before updating them, used for
		// interpolation.
		lastX = x;
		lastY = y;
		// Calculates the XY coordinates of the center of the attached entity
		// using its XY position at the top left corner, along with the width
		// and height of the sprite. This will eventually be used to calculate
		// how to rotate components about this center, as sprite rotation
		// occurs about the center.
		double entityCenterX = attachedEnt.getX() + attachedEnt.getWidth()
				/ 2;
		double entityCenterY = attachedEnt.getY() + attachedEnt.getHeight()
				/ 2;
		
		// Sets the X and Y coordinates of this hardpoint using the
		// percentageAcross and percentageUp values defined in the constructor.
		// Makes for easy placement of hardpoints.
//		this.x = attachedEnt.getX() + attachedEnt.getWidth() * percentAcross;
//		this.y = attachedEnt.getY() + attachedEnt.getHeight() * percentUp;
		
		this.x = entityCenterX + deltaX * Math.cos(theta);
		this.y = entityCenterY + deltaY * Math.sin(theta);
		
		
		// Finds the difference beween the current X or Y position and the
		// center of of the entity sprite (effectively the center of the ship)

		// Takes the difference information and finds a theta value which
		// represents the offset from the X-axis (the X-axis being the
		// centerline of the ship lengthwise), and adds the rotation theta to
		// this.
		@SuppressWarnings("unused")
		double positionTheta = Math.atan2(deltaY, deltaX) + theta;
		// Sets the lastX and lastY variables for the component for
		// interpolation this cycle
		component.setLastX(lastX);
		component.setLastY(lastY);
		// Sets the current x and y coordinates for the component this cycle
		component.setX(this.x);
		component.setY(this.y);
		// Updates the component by calling its unique method (check for weapon
		// fire, thrust of engine, shield ping etc)
		component.update();
	}

}
