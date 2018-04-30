package projetrpg.map;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import projetrpg.entities.player.Player;
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

        JsonElement mainCharacter = jsonSerializationContext.serialize(mainMap.getMainCharacter());
        map.add("maincharacter", mainCharacter);

        JsonArray regions = jsonSerializationContext.serialize(mainMap.getRegions()).getAsJsonArray();
        map.add("regions", regions);

        return map;
    }

    @Override
    public MainMap deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext deserializationContext) throws JsonParseException {
        JsonObject jsonMap = jsonElement.getAsJsonObject();
        MainMap map = new MainMap("Map");

        map.setName(jsonMap.get("name").getAsString());
        map.setHumanCount(jsonMap.get("humancount").getAsInt());
        map.setMainCharacter(deserializationContext.deserialize(
                jsonMap.get("maincharacter"),
                Player.class
        ));

        Map<Integer, Map<Direction, Integer>> directions = new HashMap<>();

        JsonArray regions = jsonMap.get("regions").getAsJsonArray();
        for(JsonElement region : regions) {
            map.addRegion(deserializationContext.deserialize(region, Region.class));

            Type directionsMapType = new TypeToken<Map<Direction, Integer>>(){}.getType();
            Map<Direction, Integer> regionDirections = deserializationContext.deserialize(
                    region.getAsJsonObject().get("directions"),
                    directionsMapType
            );

            directions.put(region.getAsJsonObject().get("id").getAsInt(), regionDirections);
        }

        //Each region associated with its id
        Map<Integer, Region> regionWithId = new HashMap<>();
        for(Region region : map.getRegions()) {
            regionWithId.put(region.getId(), region);
            associateIDwRegions(regionWithId, region);
        }

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
                jsonMap.get("spawnpoint").getAsInt()
        ));

        //Link teleporters
        TeleporterSerializer.linkTeleporters();

        return map;
    }

    private void associateIDwRegions(Map<Integer, Region> regionWithId, Region region) {
        for(Region sr : region.getContainedRegions()) {
            regionWithId.put(sr.getId(), sr);
            associateIDwRegions(regionWithId, sr);
        }
    }

    private void findRegionParent(Region parent, Collection<Region> regions) {
        for(Region region : regions) {
            region.setParent(parent);
            findRegionParent(region, region.getContainedRegions());
        }
    }

}
