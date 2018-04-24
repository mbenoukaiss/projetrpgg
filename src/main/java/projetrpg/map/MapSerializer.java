package projetrpg.map;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Type;
import java.util.Collection;
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

        //Each region associated with its id
        Map<Integer, Region> regionWithId = new HashMap<>();
        map.getRegions().forEach(r -> associateIDwRegions(regionWithId, r));

        //Associate regions to regions using map directions
        directions.forEach((id, dirMap) -> {
            dirMap.forEach((dir, otherId) -> {
                regionWithId.get(id).addRegionTowards(dir, regionWithId.get(otherId));
            });
        });

        //Let children know who is their parent
        map.getRegions().forEach(r -> findRegionParent(null, r.getContainedRegions()));

        //Find spawnpoint
        map.setSpawnPoint(regionWithId.get(
                jsonElement.getAsJsonObject().get("spawnpoint").getAsInt()
        ));

        return map;
    }

    private void associateIDwRegions(Map<Integer, Region> regionWithId, Region r) {
        r.getContainedRegions().forEach(sr -> regionWithId.put(sr.getId(), sr));
    }

    private void findRegionParent(Region parent, Collection<Region> regions) {
        for(Region region : regions) {
            region.setParent(parent);
            findRegionParent(region, region.getContainedRegions());
        }
    }

}
