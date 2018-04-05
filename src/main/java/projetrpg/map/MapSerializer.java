package projetrpg.map;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MapSerializer implements  JsonSerializer<MainMap>, JsonDeserializer<MainMap> {

    @Override
    public JsonElement serialize(MainMap mainMap, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject map = new JsonObject();
        map.addProperty("name", mainMap.getName());
        map.addProperty("spawnpoint", mainMap.getSpawnPoint().getId());
        map.addProperty("humancount", mainMap.getHumanCount());

        JsonArray regions = jsonSerializationContext.serialize(mainMap.getRegions()).getAsJsonArray();
        map.add("regions", regions);

        return map;
    }

    @Override
    public MainMap deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext deserializationContext) throws JsonParseException {
        MainMap map = new MainMap("Map");
        Map<Integer, Map<Direction, Integer>> directions = new HashMap<>();

        JsonArray regions = jsonElement.getAsJsonObject().get("regions").getAsJsonArray();
        for(JsonElement region : regions) {
            map.addRegion(deserializationContext.deserialize(region, Region.class));

            Type directionsMapType = new TypeToken<Map<Direction, String>>(){}.getType();
            Map<Direction, Integer> regionDirections = deserializationContext.deserialize(
                    region.getAsJsonObject().get("directions"),
                    directionsMapType
            );

            directions.put(region.getAsJsonObject().get("id").getAsInt(), regionDirections);
        }



        //Not associating regions to regions using map directions
        //Associate spawnpoint
        throw new NotImplementedException();

        //return map;
    }

}
