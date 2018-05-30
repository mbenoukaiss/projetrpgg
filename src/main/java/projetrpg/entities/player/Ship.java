package projetrpg.entities.player;

import projetrpg.Describable;
import projetrpg.game.Expose;
import projetrpg.map.Region;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a ship.
 * Created by mhevin on 07/05/18.
 */
public class Ship extends Region implements Describable{

    private static final int TRAVEL_COST = 10;

    /**
     * The base fuel of the ship
     */
    protected int baseFuel;

    /**
     * The actuel fuel of the ship
     */
    @Expose("fuel")
    protected int actualFuel;

    /**
     * The level of the ship
     */
    @Expose("ship level")
    protected int level;

    /**
     * Possible ameliorations of the ship;
     */
    @Expose("ship improvements")
    protected Set<ShipAmelioration> ameliorations;

    public Ship(int id, String name, Region parent, int baseFuel) {
        super(id, name, "", parent, 0);
        this.baseFuel = baseFuel;
        this.actualFuel = this.baseFuel;
        this.level = 0;
        this.ameliorations = new HashSet<>();
        this.ameliorations.add(ShipAmelioration.ENGINE_AMELIORATION);
        this.ameliorations.add(ShipAmelioration.RADAR_AMELIORATION);
        this.ameliorations.add(ShipAmelioration.REACTORS_AMELIORATION);
    }

    /**
     * Create a ship out of a region.
     * @param base The region
     * @param baseFuel The base fuel of the ship
     */
    public Ship(Region base, int baseFuel) {
        super(base);
        this.baseFuel = baseFuel;
        this.actualFuel = this.baseFuel;
        this.level = 1;
        this.ameliorations = new HashSet<>();
        this.ameliorations.add(ShipAmelioration.ENGINE_AMELIORATION);
        this.ameliorations.add(ShipAmelioration.RADAR_AMELIORATION);
        this.ameliorations.add(ShipAmelioration.REACTORS_AMELIORATION);
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

    public void setActualFuel(int actualFuel) {
        this.actualFuel = actualFuel;
    }

    public int getTravelCost() {
        return TRAVEL_COST;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void improve(ShipAmelioration amelioration) {
        this.level += 1;
        finishAmelioration(amelioration);
    }

    public Set<ShipAmelioration> getAmeliorations() {
        return ameliorations;
    }

    private void finishAmelioration(ShipAmelioration a) {
        this.ameliorations.remove(a);
    }

    @Override
    public String describe() {
        return "Hervé's ship : \n -Level : " + this.level;
    }
}
