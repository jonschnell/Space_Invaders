package ships;

import projectiles.DefenderProjectile;
import projectiles.Projectile;
import utils.Position;

public class ShooterShip extends InvaderShip {
	/**
	 * Constructs a ShooterShip
	 * 
	 * @param p     The initial position
	 * @param armor The initial armor level
	 */
	public ShooterShip(Position p, int armor) {
		super(p, armor);
	}

	/**
	 * fires a standard projectile
	 * @return out an array of projectiles 
	 */
	public Projectile[] fire() {
		if (!canFire()) {
			return null;
		}

		lastShotTime = System.currentTimeMillis();
		Projectile[] out = new Projectile[1];
		Position p = new Position(pos.getX() + DefenderShip.SHIP_WIDTH / 2, pos.getY() - DefenderShip.SHIP_HEIGHT / 2);
		out[0] = new Projectile(p, 0, -PROJECTILE_SPEED, Projectile.GRAVITY);

		return out;
	}

	@Override
	public String imgPath() {
		return "res/monster.png";

	}

	@Override
	public int getPoints() {
		return 50;
	}
}
