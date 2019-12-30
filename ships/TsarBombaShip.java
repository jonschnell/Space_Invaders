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

import utils.Position;

public class TsarBombaShip extends BomberShip {
	public static final double EXPLOSION_RADIUS = 50;

  /**
   * Constructs a TsarBombaShip
   * @param p The initial position
   * @param armor The initial armor level
   */
        public TsarBombaShip(Position p, int armor) {
        	super(p, armor);
	}
	
	@Override
	public String imgPath() {
		return "res/monster4.png";
	}
}

