package projetrpg.entities.player;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import projetrpg.Describable;
import projetrpg.game.Expose;
import projetrpg.map.MapSerializer;
import projetrpg.map.Region;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a ship.
 * Created by mhevin on 07/05/18.
 */
public class Ship extends Region implements Describable{

    /**
     * The cost in humans of a travel.
     */
    public static final int TRAVEL_COST = 10;

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
     * Possible upgrades of the ship;
     */
    @Expose("ship improvements")
    private Set<ShipAmelioration> upgrades;

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
        this.upgrades = new HashSet<>();
        this.upgrades.add(ShipAmelioration.ENGINE_AMELIORATION);
        this.upgrades.add(ShipAmelioration.RADAR_AMELIORATION);
        this.upgrades.add(ShipAmelioration.REACTORS_AMELIORATION);
    }

    /**
     * Accessor for the actual fuel of the ship
     * @return the actuel fuel
     */
    public int getActualFuel() {
        return actualFuel;
    }

    /**
     * Getter for the level of the ship.
     * @return The level of the ship
     */
    public int getLevel() {
        return level;
    }

    /**
     * Upgrades a ship
     * @param amelioration The upgrade to apply.
     */
    public void improve(ShipAmelioration amelioration) {
        this.level += 1;
        finishImprovement(amelioration);
    }

    /**
     * Getter for the upgrades.
     * @return Set of upgrades
     */
    public Set<ShipAmelioration> getUpgrades() {
        return new HashSet<>(upgrades);
    }

    /**
     * Finishes an improvement.
     * @param a The upgrade.
     */
    private void finishImprovement(ShipAmelioration a) {
        this.upgrades.remove(a);
    }

    @Override
    public String describe() {
        return "Hervé's ship : \n -Level : " + this.level;
    }



    /**
     * Class permitting to serialize and deserialize Ship.
     */
    public static class TypeAdapter implements JsonSerializer<Ship>, JsonDeserializer<Ship> {

        @Override
        public JsonElement serialize(Ship ship, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject regionPart = jsonSerializationContext.serialize(ship, Region.class).getAsJsonObject();

            regionPart.addProperty("type", "ship");
            regionPart.addProperty("basefuel", ship.baseFuel);
            regionPart.addProperty("actualfuel", ship.actualFuel);
            regionPart.addProperty("level", ship.level);

            JsonArray ameliorations = jsonSerializationContext.serialize(ship.upgrades).getAsJsonArray();
            regionPart.add("upgrades", ameliorations);

            return regionPart;
        }

        @Override
        public Ship deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonShip = jsonElement.getAsJsonObject();
            Region regionPart = jsonDeserializationContext.deserialize(jsonElement, Region.class);
            Ship ship = new Ship(regionPart, -1);

            ship.baseFuel = jsonShip.get("basefuel").getAsInt();
            ship.actualFuel = jsonShip.get("actualfuel").getAsInt();
            ship.level = jsonShip.get("level").getAsInt();

            Type ameliorationsType = new TypeToken<Set<ShipAmelioration>>(){}.getType();
            ship.upgrades = jsonDeserializationContext.deserialize(jsonShip.get("upgrades"), ameliorationsType);

            MapSerializer.setShip(ship);
            return ship;
        }

    }

}
