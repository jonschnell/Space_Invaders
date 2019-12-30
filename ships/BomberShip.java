/**
 * 
 * @author Jonathon Schnell
 * @version 1.0
 * @since 12-12-2019
 * COM S 227
 * homework4
 *
 */
package ships;

import projectiles.Bomb;
import projectiles.DefenderProjectile;
import projectiles.Projectile;
import utils.Position;

public class BomberShip extends InvaderShip {
	public static final double EXPLOSION_RADIUS = 10;

	/**
	 * Constructs a BomberShip
	 * 
	 * @param p     The initial position
	 * @param armor The initial armor level
	 */
	public BomberShip(Position p, int armor) {
		super(p, armor);
	}

	/**
	 * Drops a single bomb
	 * 
	 * @return An array containing a single bomb
	 */
	public Projectile[] fire() {
		if (!canFire()) {
			return null;
		}
		lastShotTime = System.currentTimeMillis();
		Bomb[] out = new Bomb[1];
		Position p = new Position(pos.getX() + DefenderShip.SHIP_WIDTH / 2, pos.getY() - DefenderShip.SHIP_HEIGHT / 2);
		out[0] = new Bomb(p, 0, -PROJECTILE_SPEED, Bomb.GRAVITY, EXPLOSION_RADIUS);

		return out;
	}

	@Override
	public String imgPath() {
		return "res/monster2.png";
	}

	@Override
	public int getPoints() {
		return 100;
	}
}
