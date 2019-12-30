/**
 * 
 * @author Jonathon Schnell
 * @version 1.0
 * @since 12-12-2019
 * COM S 227
 * homework4
 *
 */
package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import projectiles.Bomb;
import projectiles.DefenderProjectile;
import projectiles.Projectile;
import ships.BomberShip;
import ships.DefenderShip;
import ships.InvaderShip;
import ships.MultiShooterShip;
import ships.ShooterShip;
import ships.SpaceShip;
import ships.TsarBombaShip;
import ui.SpaceInvaders;

public class Tableau {
	/**
	 * The speed at which the invaders' fleet moves horizontally.
	 */
	public static final double FLEET_MOVE_X = 4;
	/**
	 * The speed at which the invaders' fleet moves vertically.
	 */
	public static final double FLEET_MOVE_Y = 10;

	private InvaderShip[][] enemyFleet;
	private DefenderShip defender;
	private ArrayList<Projectile> projectiles;
	private Random r;
	private boolean fleetMovePositiveX;
	private int score;

	public Tableau() {
		fleetMovePositiveX = true;
		r = new Random();
		enemyFleet = new InvaderShip[InvaderShip.SHIPS_Y][InvaderShip.SHIPS_X];
		for (int y = 0; y < InvaderShip.SHIPS_Y; y++) {
			for (int x = 0; x < InvaderShip.SHIPS_X; x++) {
				Position pos = new Position(((SpaceInvaders.WIDTH / 4) + (x * InvaderShip.SHIP_SPACING)),
						((y * InvaderShip.SHIP_SPACING)));
				switch (r.nextInt(InvaderShip.NUM_INVADER_SHIPS)) {
				case 0:
					enemyFleet[y][x] = new ShooterShip(pos, r.nextInt(SpaceShip.MAX_ARMOR) + 1);
					break;
				case 1:
					enemyFleet[y][x] = new BomberShip(pos, r.nextInt(SpaceShip.MAX_ARMOR) + 1);
					break;
				case 2:
					enemyFleet[y][x] = new MultiShooterShip(pos, r.nextInt(SpaceShip.MAX_ARMOR) + 1);
					break;
				case 3:
					enemyFleet[y][x] = new TsarBombaShip(pos, r.nextInt(SpaceShip.MAX_ARMOR) + 1);
					break;
				}
			}
		}
		projectiles = new ArrayList<>();
		defender = new DefenderShip();
		score = 0;
	}

	/**
	 * Checks all enemy ships to see if they are hit by d. If ship is hit, calls
	 * takeHit(). If ships armor falls to zero or below, score is updated and ship
	 * is destroyed.
	 * 
	 * @param d A projectile
	 * @return true if and only if a ship is hit
	 */
	private boolean checkForEnemyHit(DefenderProjectile d) {
		// for loops to check all of the ships in the enemy fleet array
		for (int x = 0; x < enemyFleet.length; x++) {
			for (int y = 0; y < enemyFleet[0].length; y++) {
				// ensures the there is a ship in the array at that location
				if (enemyFleet[y][x] != null) {
					// test if the projectile object is within the bounds/hit-box of the ship; the
					// ship takes damage
					if (d.hit(enemyFleet[y][x].getBounds()) == true) {
						enemyFleet[y][x].takeHit();
						// if the armor of the ship is zero the player is awarded a point and the ship
						// is set to null
						if (enemyFleet[y][x].getArmor() == 0) {
							score += enemyFleet[y][x].getPoints();
							enemyFleet[y][x] = null;
						}
						// this indicates a hit and the projectile is removed
						return true;
					}
				}
			}
		}
		// no hit
		return false;
	}

	public void handleCollisions() {
		Projectile p;

		for (int i = projectiles.size() - 1; i >= 0; i--) {
			p = projectiles.get(i);

			if (p.isOutOfBounds()) {
				if (p instanceof Bomb) {
					if (defender != null && ((Bomb) p).blowUp(defender.getBounds())) {
						defender.takeHit();
					}
				}
				projectiles.remove(i);
				i--;
			} else if (p instanceof DefenderProjectile) {
				if (checkForEnemyHit((DefenderProjectile) p)) {
					projectiles.remove(i);
					i--;
				}
			} else if (defender != null && p.hit(defender.getBounds())) {
				defender.takeHit();
				projectiles.remove(i);
				i--;
				if (defender.getArmor() <= 0) {
					defender = null;
					return;
				}
			}

			p.nextPosition();
		}
	}

	/**
	 * Returns a reference to the right-most ship in the invaders' fleet, or null if
	 * there are no ships.
	 * 
	 * @return a reference to the right-most ship
	 */
	private SpaceShip getRightMostEnemy() {
		// loops through all ships in the array from the bottom right
		for (int x = enemyFleet[0].length - 1; x > -1; x--) {
			for (int y = enemyFleet.length - 1; y > -1; y--) {
				// the first ship that the loop encounters is the one we are looking for so it
				// is returned
				if (enemyFleet[y][x] != null) {
					return enemyFleet[y][x];
				}
			}
		}
		// there are no more invade ships
		return null;
	}

	/**
	 * Returns a reference to the left-most ship in the invaders' fleet, or null if
	 * there are no ships.
	 * 
	 * @return a reference to the left-most ship
	 */
	private SpaceShip getLeftMostEnemy() {
		// loops through all ships in the array from the top left
		for (int x = 0; x < enemyFleet.length; x++) {
			for (int y = 0; y < enemyFleet[0].length; y++) {
				// the first ship the loop encounters is the one we are looking for so it is
				// returned
				if (enemyFleet[y][x] != null) {
					return enemyFleet[y][x];
				}
			}
		}
		// there are no more invade ships
		return null;
	}

	/**
	 * Returns a reference to the bottom-most ship in the invaders' fleet, or null
	 * if there are no ships.
	 * 
	 * @return a reference to the bottom-most ship
	 */
	private SpaceShip getLowestEnemy() {
		// loops through all ships in the array from the bottom right
		// this loops internal perameter is x unlike getRightMost()
		// we need to do this because we need to check all of the x values right to left
		// to find the furthest bottom ship that is also the rightmost
		for (int y = enemyFleet[0].length - 1; y > -1; y--) {
			for (int x = enemyFleet.length - 1; x > -1; x--) {
				if (enemyFleet[y][x] != null) {
					return enemyFleet[y][x];
				}
			}
		}
		return null;

	}

	private void translateEnemyFleet(double deltaX, double deltaY) {
		for (int y = 0; y < InvaderShip.SHIPS_Y; y++) {
			for (int x = 0; x < InvaderShip.SHIPS_X; x++) {
				if (enemyFleet[y][x] != null) {
					enemyFleet[y][x].translate(deltaX, deltaY);

					if (defender.getBounds().collide(enemyFleet[y][x].getBounds())) {
						defender = null;
						return;
					}
					/* Overloading to handle firing here, too */
					if (r.nextDouble() < InvaderShip.FIRING_PROBABILITY) {
						Projectile[] p = enemyFleet[y][x].fire();
						if (p != null)
							projectiles.addAll(Arrays.asList(p));
					}
				}
			}
		}
	}

	/**
	 * Does nothing if gameIsOver(), otherwise calculates the deltas to the invaders
	 * next position and calls translateEnemyFleet() on those deltas.
	 * fleetMovePositiveX gives the direction of fleet movement in the horizontal;
	 * if true, the fleet moves in the positive X direction, else it moves in the
	 * negative X direction. The entire fleet moves until it would go out of bounds
	 * (0 to SpaceInvaders.WIDTH), at which time, rather than go out of bounds, its
	 * direction changes and it moves down. Magnitude of the deltas are given by 0,
	 * FLEET_MOVE_X, and FLEET_MOVE_Y.
	 */
	public void moveEnemyFleet() {
		// if the game is over the fleet cannot be moved
		if (gameIsOver()) {
			return;
		}
		// fleetMovePositiveX is true the fleet should be moved to the right
		// the fleet should be moved until the right most enemy hits a wall
		if (fleetMovePositiveX && getRightMostEnemy().getPosition().getX() < SpaceInvaders.WIDTH) {
			translateEnemyFleet(FLEET_MOVE_X, 0);
		}
		// one the fleet hits the wall to the right it should be translated down one
		// the fleet fleetMovePositiveX should be set false because the fleet needs now
		// to be
		// translated to the left
		// the fleet can only move down once because the fleet move boolean is updated
		// and the if statement is exited
		else if (fleetMovePositiveX && getRightMostEnemy().getPosition().getX() >= SpaceInvaders.WIDTH - 1) {
			translateEnemyFleet(0, FLEET_MOVE_Y);
			fleetMovePositiveX = false;
		}
		// since fleetMovePositiveX is now false and the fleet is on the far right the
		// fleet should be translated to the left until the leftmost ship in the fleet
		// reaches the left side if the window
		else if (!fleetMovePositiveX && getLeftMostEnemy().getPosition().getX() > 0) {
			translateEnemyFleet(-FLEET_MOVE_X, 0);
		}
		// when the fleet hits the wall it needs to be translated down again
		// again we set the fleetMovePositiveX back to true so that the first if
		// statement can be entered
		else if (!fleetMovePositiveX && getLeftMostEnemy().getPosition().getX() <= 0) {
			translateEnemyFleet(0, FLEET_MOVE_Y);
			fleetMovePositiveX = true;
		}

	}

	public void moveDefender(double xAmount, double yAmount) {
		double newX, newY;
		Position pos;

		pos = defender.getPosition();
		newX = pos.getX() + xAmount;
		if (newX > SpaceInvaders.WIDTH - DefenderShip.SHIP_WIDTH / 2) {
			newX = SpaceInvaders.WIDTH - DefenderShip.SHIP_WIDTH / 2;
		}
		if (newX < 0) {
			newX = 0;
		}

		newY = pos.getY() + yAmount;
		if (newY > SpaceInvaders.HEIGHT - DefenderShip.SHIP_HEIGHT / 2) {
			newY = SpaceInvaders.HEIGHT - DefenderShip.SHIP_HEIGHT / 2;
		}
		if (newY < 0) {
			newY = 0;
		}

		defender.setPosition(newX, newY);
	}

	public boolean defenderDestroyed() {
		return defender == null;
	}

	public boolean enemyFleetDestroyed() {
		return getLowestEnemy() == null;
	}

	public boolean gameIsOver() {
		return defenderDestroyed() || enemyFleetDestroyed()
				|| getLowestEnemy().getPosition().getY() > SpaceInvaders.HEIGHT;
	}

	public void moveDefender(boolean left, boolean right, boolean up, boolean down, boolean fire) {
		if (gameIsOver())
			return;

		if (fire) {
			Projectile[] projectiles = defender.fire();
			if (projectiles != null)
				this.projectiles.addAll(Arrays.asList(projectiles));
		}

		if (right) {
			moveDefender(DefenderShip.MOVE_DELTA, 0);
		} else if (left) {
			moveDefender(-1 * DefenderShip.MOVE_DELTA, 0);
		} else if (up) {
			moveDefender(0, -1 * DefenderShip.MOVE_DELTA);
		} else if (down) {
			moveDefender(0, DefenderShip.MOVE_DELTA);
		}

	}

	public InvaderShip[][] getEnemyFleet() {
		return enemyFleet;
	}

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	public DefenderShip getDefenderShip() {
		return defender;
	}

	public int getScore() {
		return score;
	}
}
