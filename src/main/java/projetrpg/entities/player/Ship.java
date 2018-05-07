package projetrpg.entities.player;

import projetrpg.map.Region;

/**
 * Represents a ship.
 * Created by mhevin on 07/05/18.
 */
public class Ship extends Region {

    /**
     * The base fuel of the ship
     */
    private int baseFuel;

    /**
     * The actuel fuel of the ship
     */
    private int actualFuel;

    /**
     * The level of the ship
     */
    private int level;

    public Ship(int id, String name, Region parent, int baseFuel) {
        super(id, name, parent);
        this.baseFuel = baseFuel;
        this.level = 1;
    }

    /**
     * Accessor for the base fuel of the ship
     * @return the base fuel
     */
    public int getBaseFuel() {
        return baseFuel;
    }

    /**
     * Accessor for the actual fuel of the ship
     * @return the actuel fuel
     */
    public int getActualFuel() {
        return actualFuel;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
