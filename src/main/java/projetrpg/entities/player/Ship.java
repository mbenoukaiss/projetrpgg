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

    private int TRAVEL_COST;

    /**
     * The base fuel of the ship
     */
    private int baseFuel;

    /**
     * The actuel fuel of the ship
     */
    @Expose("fuel")
    private int actualFuel;

    /**
     * The level of the ship
     */
    @Expose("ship level")
    private int level;

    /**
     * Possible ameliorations of the ship;
     */
    @Expose("ship improvements")
    private Set<ShipAmelioration> ameliorations;

    /**
     * The last region the player was on before entering his ship
     */
    private Region lastRegion;

    public Ship(int id, String name, Region parent, int baseFuel) {
        super(id, name, parent, 0);
        this.baseFuel = baseFuel;
        this.actualFuel = this.baseFuel;
        this.level = 1;
        this.ameliorations = new HashSet<>();
        this.ameliorations.add(ShipAmelioration.ENGINE_AMELIORATION);
        this.ameliorations.add(ShipAmelioration.RADAR_AMELIORATION);
        this.ameliorations.add(ShipAmelioration.REACTORS_AMELIORATION);
        lastRegion = null;
        this.TRAVEL_COST = 10;
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
        this .level += 1;
        finishAmelioration(amelioration);
    }

    public Set<ShipAmelioration> getAmeliorations() {
        return ameliorations;
    }

    private void finishAmelioration(ShipAmelioration a) {
        this.ameliorations.remove(a);
    }

    public void setLastRegion(Region lastRegion) {
        this.lastRegion = lastRegion;
    }

    public Region getLastRegion() {
        return lastRegion;
    }

    @Override
    public String describe() {
        return "Hervé's ship : \n -Level : " + this.level;
    }
}
