public abstract class Ship extends Entity {

	
	//TODO candidates for making properties of ship components
	protected double thrust; //place in an engine component
	protected double maxSpeed; //place in an engine component
	protected double turningRate; //place in engine/thruster component
	protected double projectileSpeed = 25;
	protected int fireRate = 10;
	
	/**
	 * @return the turningRate
	 */
	public double getTurningRate() {
		return turningRate;
	}

	/**
	 * @param turningRate the turningRate to set
	 */
	public void setTurningRate(double turningRate) {
		this.turningRate = turningRate;
	}

	protected int hullIntegrity;
	/**
	 * @return the hullIntegrity
	 */
	public int getHullIntegrity() {
		return hullIntegrity;
	}

	/**
	 * @param hullIntegrity the hullIntegrity to set
	 */
	public void setHullIntegrity(int hullIntegrity) {
		this.hullIntegrity = hullIntegrity;
	}

	protected boolean isHostile = false;
	protected double mass;
	protected boolean isEnemy; 

	// The graphical representation of the current health of the player
	

	public Ship(String ref, double x, double y) {
		super(ref, x, y);
		
	}

	public boolean isHostile() {
		return isHostile;
	}

	@Override
	public void ifCollided(Entity ent) {
		hullIntegrity--;
		if (hullIntegrity <= 0) {
			disable();
		}
	}
}
