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

            JsonArray ameliorations = jsonSerializationContext.serialize(ship.ameliorations).getAsJsonArray();
            regionPart.add("ameliorations", ameliorations);

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
            ship.ameliorations = jsonDeserializationContext.deserialize(jsonShip.get("ameliorations"), ameliorationsType);

            MapSerializer.setShip(ship);
            return ship;
        }

    }

}
