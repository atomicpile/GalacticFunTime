public class ShipTurret extends Component {

	protected int weaponCounter = 0; // placed in a turret/weapon component
	protected int fireRate = 10; // place in a turret/weapon component
	protected double projectileSpeed = 10; // place in a turret/weapon component
	protected boolean isPlayer = false;
	protected Entity target;

	/**
	 * @return the target
	 */
	public Entity getTarget() {
		return target;
	}

	/**
	 * @param target
	 *            the target to set
	 */
	public void setTarget(Entity target) {
		this.target = target;
	}

	/**
	 * @return the weaponCounter
	 */
	public int getWeaponCounter() {
		return weaponCounter;
	}

	/**
	 * @param weaponCounter
	 *            the weaponCounter to set
	 */
	public void setWeaponCounter(int weaponCounter) {
		this.weaponCounter = weaponCounter;
	}

	/**
	 * @return the fireRate
	 */
	public int getFireRate() {
		return fireRate;
	}

	/**
	 * @param fireRate
	 *            the fireRate to set
	 */
	public void setFireRate(int fireRate) {
		this.fireRate = fireRate;
	}

	/**
	 * @return the projectileSpeed
	 */
	public double getProjectileSpeed() {
		return projectileSpeed;
	}

	/**
	 * @param projectileSpeed
	 *            the projectileSpeed to set
	 */
	public void setProjectileSpeed(double projectileSpeed) {
		this.projectileSpeed = projectileSpeed;
	}

	/**
	 * @return the isPlayer
	 */
	public boolean isPlayer() {
		return isPlayer;
	}

	/**
	 * @param isPlayer
	 *            the isPlayer to set
	 */
	public void setPlayer(boolean isPlayer) {
		this.isPlayer = isPlayer;
	}

	public ShipTurret(Ship attachedShip) {
		super(attachedShip, "turretPlaceholder");
		fireRate = 5;
	}

	@Override
	public void update() {
		tryToShoot();
	}

	public void tryToShoot() {
		if (GameEngine.player == null)
			return;
		weaponCounter++;
		if (weaponCounter >= fireRate) {
			weaponCounter = 0;
			fireWeapon();
		}
	}

	public void fireWeapon() {
		// The speed of the projectile fired by this weapon
		double destY = 0;
		double destX = 0;
		if ( !isPlayer ) {
			destY = target.getY() + target.getHeight() / 2;
			destX = target.getX() + target.getWidth() / 2;
			GameEngine.entityCache.add(new Projectile(x, y, destX, destY,
					projectileSpeed, isPlayer));
		} else if (isPlayer && target == null) {
			if (GameEngine.mouseDown) {
				destY = GameEngine.mouseY;
				destX = GameEngine.mouseX;
				GameEngine.entityCache.add(new Projectile(x, y, destX, destY,
						projectileSpeed, isPlayer));
			}
		}
	}

}
