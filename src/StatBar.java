import java.awt.Color;
import java.awt.Graphics2D;

/**
 * This class defines a statbar, which is a visual representation on the UI of
 * an amount of a stat that an entity has. A stat bar can represent health,
 * shields, etc.
 * 
 * @author Andrew
 * 
 */
public class StatBar {

	// The entity this statbar is associated with
	private Entity attachedEnt;
	// The color these bars should render as
	private Color barColor;
	// The height of each unit in pixels
	private int barWidth = 5;
	private int barSpacing = barWidth;
	// The width of each unit in pixels
	private int barHeight = 5;
	// Sets the current number of bars to render
	private int currentSize;
	private int maxSize;
	// Whether the bar runs up and down, or left and right
	private boolean isPlacedUp = false;
	// The XY coordinates this bar is located at in the viewable area, generally
	// linked to the entitys position itself
	private double x;
	private double y;

	public StatBar(int maxSize, boolean isPlacedUp, Color color, Ship ent) {
		// Sets the entity that this stat bar is attached to
		this.maxSize = maxSize / 2;
		this.currentSize = maxSize;
		this.barColor = color;
		this.isPlacedUp = isPlacedUp;
		// This section is called if the statbar is meant for the player
		// TODO will eventually have to be adapted for multiple kinds of stat
		// bars for the player
		if (ent.isPlayer) {
			this.x = GameEngine.displayWidth * .09;
			this.y = GameEngine.displayHeight * .95;
			this.barHeight = 10;
			this.barWidth = 5;
			this.barSpacing = barWidth;
		}
	}

	public void draw(Graphics2D g2d, float interp) {
		// Stores the current color of the graphics context so that it can be
		// reset later after the bar is rendered.
		Color tempColor = g2d.getColor();
		// Sets the color of the stat bar
		g2d.setColor(barColor);
		// This for loop draws the stat bar based on size.
		for (int i = 0; i < currentSize; i++) {
			if (isPlacedUp) {
				g2d.fill3DRect((int) x, (int) y - i * barSpacing, barWidth,
						barHeight, true);
			} else if (!isPlacedUp) {
				g2d.fill3DRect((int) x + i * barSpacing, (int) y, barWidth,
						barHeight, true);
			}
		}
		// Resets the color of the graphics context to whatever it was before
		// this method was called
		g2d.setColor(tempColor);
	}

	/**
	 * Returns the entity that this statbar is attached to.
	 * 
	 * @return the attachedEnt
	 */
	public Entity getAttachedEnt() {
		return attachedEnt;
	}

	/**
	 * Sets the entity at this stat bar should be attached to.
	 * 
	 * @param attachedEnt
	 *            the attachedEnt to set
	 */
	public void setAttachedEnt(Entity attachedEnt) {
		this.attachedEnt = attachedEnt;
	}

	/**
	 * Sets the color of this bar
	 * 
	 * @param barColor
	 *            the barColor to set
	 */
	public void setBarColor(Color barColor) {
		this.barColor = barColor;
	}

	/**
	 * Sets the height of this bar
	 * 
	 * @param barHeight
	 *            the barHeight to set
	 */
	public void setBarHeight(int barHeight) {
		this.barWidth = barHeight;
	}

	/**
	 * Sets the spacing in pixels between each bar
	 * 
	 * @param barSpacing
	 *            the barSpacing to set
	 */
	public void setBarSpacing(int barSpacing) {
		this.barSpacing = barSpacing;
	}

	/**
	 * Sets the width of each bar in pixels
	 * 
	 * @param barWidth
	 *            the barWidth to set
	 */
	public void setBarWidth(int barWidth) {
		this.barHeight = barWidth;
	}

	/**
	 * Sets the current size of this bar. Generally the current health/shield of
	 * whatever entity this is attached to.
	 * 
	 * @param currentSize
	 *            the currentSize to set
	 */
	public void setCurrentSize(int currentSize) {
		this.currentSize = currentSize;
	}

	/**
	 * Sets whether this statbar is vertically oriented or horizontally
	 * oriented.
	 * 
	 * @param isPlacedUp
	 *            the isPlacedUp to set
	 */
	public void setPlacedUp(boolean isPlacedUp) {
		this.isPlacedUp = isPlacedUp;
	}

	/**
	 * This method updates the stat bar. Each cycle this method is called and it
	 * adjusts the size of the bar based on the amount of the current stat.
	 */
	public void update() {
		// Divides the total health of the attached entity by 2, then renders
		// the stat bar. This way, each unit is representative of 2 units of
		// health on the entity
		currentSize = ((Ship) attachedEnt).getHullIntegrity() / 2;
		// Changes the color to yellow once the entitys stat drops below half.
		if (currentSize <= maxSize / 2 && currentSize > maxSize / 4) {
			barColor = Color.YELLOW;
		}
		// Changes the color to Red once the entitys stat drops below a quarter
		else if (currentSize <= maxSize / 4) {
			barColor = Color.RED;
		}
		// This is run only if the stat bar is attached to a non-player entity,
		// in order that the health bar moves with the entity as opposed to
		// being static.
		if (!attachedEnt.isPlayer) {
			this.x = attachedEnt.getX();
			this.y = attachedEnt.getY() + attachedEnt.getHeight() + 2;
		}
	}
}
