package projetrpg.entities.player;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import projetrpg.map.MapSerializer;
import projetrpg.map.Region;

import java.lang.reflect.Type;
import java.util.Set;

public class ShipSerializer implements JsonSerializer<Ship>, JsonDeserializer<Ship> {

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
