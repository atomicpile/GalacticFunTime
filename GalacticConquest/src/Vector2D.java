public class Vector2D {

	/**
	 * This method provides functionality for adding two int[] arrays of
	 * vectors, and returns the resultant vector.
	 * 
	 * @param aVec
	 *            The first vector to add.
	 * @param bVec
	 *            The second vector to add.
	 * @return The vector resulting from this vector addition.
	 */
	public static int[] addVector(int[] aVec, int[] bVec) {
		// The array which will hold the resultant vector.
		int[] addedVectors = null;
		// Makes sure that the length of the first vector is equal to the length
		// of the second vector.
		if (aVec.length == bVec.length) {
			addedVectors = new int[aVec.length];
			// Adds each component of the two vector arrays together.
			for (int i = 0; i < aVec.length; i++) {
				addedVectors[i] = aVec[i] + bVec[i];
			}
		} else {
			System.out.println("Vector arrays not the proper size!");
			return null;
		}
		return addedVectors;
	}

	/**
	 * This method provides functionality for finding two int[] arrays of
	 * vectors, and returns the resultant dot product.
	 * 
	 * @param aVec
	 *            The first vector
	 * @param bVec
	 *            The second vector
	 * @return the dotProductSum of these two vectors.
	 */
	public static int dotProduct(int[] aVec, int[] bVec) {
		int dotProductSum = 0;
		if (aVec.length == bVec.length) {
			for (int i = 0; i < aVec.length; i++) {
				dotProductSum += aVec[i] * bVec[i];
			}
		} else {
			System.out.println("Vector arrays not the proper size!");
		}
		return dotProductSum;
	}

	/**
	 * This method takes a double array representing a vector and returns the
	 * normal as a double.
	 * 
	 * @param aVec
	 *            The vector to normalize.
	 * @return the normalized vector
	 */
	public static double norm(double[] aVec) {
		double norm = 0;
		for (int i = 0; i < aVec.length; i++) {
			norm += aVec[i] * aVec[i];
		}
		return Math.sqrt(norm);
	}

	/**
	 * This method takes an int[] array representing a vector and finds the
	 * normal of it and returns it as an int.
	 * 
	 * @param xVector
	 *            The vector to normalize.
	 * @return the normalized vector.
	 */
	public static double norm(int[] xVector) {
		int norm = 0;
		for (int i = 0; i < xVector.length; i++) {
			norm += xVector[i] * xVector[i];
		}
		return Math.sqrt(norm);
	}

	// ///////////////////////////////////////////////////////////////////////////////
	// All code below this is for instantiating a vector object
	//
	// //////////////////////////////////////////////////////////////////////////////

	// Whether this vector belongs to the player ship or not
	private boolean isPlayerVector;
	// The magnitude of this vector, this is multiplied by both sin(theta) and
	// cos(theta) to find x and y velocities for the entity this vector is
	// associated with
	private double speed;
	// The theta value associated with this vector
	private double theta;
	// Current x, y position of this vector
	private double x;
	private double y;
	// The XY destination set for this vector, is used to calculate a theta
	// value given a source XY position
	private double xDest;
	private double yDest;
	// Velocity components for this vector, calculated here so the vector is
	// automatically updating
	private double xVel = speed * Math.cos(theta);
	private double yVel = speed * Math.sin(theta);
	// The entity associated with this vector, used to get and set xy positions
	private Entity attachedEntity;

	/**
	 * This constructor is used to generate a vector given a theta value, source
	 * position, and speed. This constructor is primarily used for the player
	 * ship in which the destination is not known upon creation of the vector.
	 * 
	 * @param xSrc
	 *            The X coordinate of the anchor point.
	 * @param ySrc
	 *            The Y coordinate of the anchor point.
	 * @param theta
	 *            The angle away from the x axis in radians
	 * @param speed
	 *            The magnitude of velocity of this vector.
	 * @param attachedEntity
	 *            The entity this vector is attached to
	 */
	public Vector2D(double xSrc, double ySrc, double theta, double speed,
			Entity attachedEntity) {
		this.x = xSrc;
		this.y = ySrc;
		this.theta = theta;
		this.speed = speed;
		// Finds the X and Y velocity of this vector
		xVel = speed * Math.cos(theta);
		yVel = speed * Math.sin(theta);
		this.attachedEntity = attachedEntity;
	}

	/**
	 * This constructor is used to generate a vector at a given source position,
	 * and a given destination position. A theta value is automatically
	 * calculated given these data. This constructor is primarily used for
	 * vectors which do not change direction over time (ie. projectiles).
	 * 
	 * @param xOrig
	 *            the X origin of this vector
	 * @param yOrig
	 *            the Y origin of this vector
	 * @param xDest
	 *            the X destination of this vector
	 * @param yDest
	 *            the Y destination of this vector
	 * @param speed
	 *            the magnitude of this vector
	 * @param attachedEntity
	 *            the entity associated with this vector
	 */
	public Vector2D(double xOrig, double yOrig, double xDest, double yDest,
			double speed, Entity attachedEntity) {
		this.x = xOrig;
		this.y = yOrig;
		// The X and Y coordinates of the destination
		this.xDest = xDest;
		this.yDest = yDest;
		// The difference between the X coordinates and the Y coordinates
		double deltaX = xDest - xOrig;
		double deltaY = yDest - yOrig;
		// Takes the difference and finds the theta
		this.theta = Math.atan2(deltaY, deltaX);
		this.speed = speed;
		xVel = speed * Math.cos(theta);
		yVel = speed * Math.sin(theta);
		this.attachedEntity = attachedEntity;
	}

	/**
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * @return the theta
	 */
	public double getTheta() {
		return theta;
	}

	/**
	 * @return the xDest
	 */
	public double getxDest() {
		return xDest;
	}

	/**
	 * @return the xVel
	 */
	public double getxVel() {
		return xVel;
	}

	/**
	 * @return the yDest
	 */
	public double getyDest() {
		return yDest;
	}

	/**
	 * @return the yVel
	 */
	public double getyVel() {
		return yVel;
	}

	/**
	 * @return the isPlayerVector
	 */
	public boolean isPlayerVector() {
		return isPlayerVector;
	}

	/**
	 * @param isPlayerVector
	 *            the isPlayerVector to set
	 */
	public void setPlayerVector(boolean isPlayerVector) {
		this.isPlayerVector = isPlayerVector;
	}

	/**
	 * @param speed
	 *            the speed to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * @param theta
	 *            the theta to set
	 */
	public void setTheta(double theta) {
		this.theta = theta;
		xVel = speed * Math.cos(theta);
		yVel = speed * Math.sin(theta);
	}

	public void setTheta(double xDest, double yDest) {
		double deltaX = xDest - this.x;
		double deltaY = yDest - this.y;
		this.theta = Math.atan2(deltaY, deltaX);
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @param xDest
	 *            the xDest to set
	 */
	public void setxDest(double xDest) {
		this.xDest = xDest;
	}

	/**
	 * @param xVel
	 *            the xVel to set
	 */
	public void setxVel(double xVel) {
		this.xVel = xVel;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @param yDest
	 *            the yDest to set
	 */
	public void setyDest(double yDest) {
		this.yDest = yDest;
	}

	/**
	 * @param yVel
	 *            the yVel to set
	 */
	public void setyVel(double yVel) {
		this.yVel = yVel;
	}

	public void step() {
		this.x += this.speed * Math.cos(this.theta);
		this.y += this.speed * Math.sin(this.theta);
		attachedEntity.setXPos(this.x);
		attachedEntity.setYPos(this.y);
	}
}
