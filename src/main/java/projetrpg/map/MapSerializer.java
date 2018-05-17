package projetrpg.map;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import projetrpg.entities.NPC;
import projetrpg.entities.player.Player;
import projetrpg.entities.player.Ship;
import projetrpg.quest.Quest;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Type;
import java.util.*;

public class MapSerializer implements  JsonSerializer<MainMap>, JsonDeserializer<MainMap> {

    private static HashMap<Integer, NPC> idNPCMap = new HashMap<>();

    private static Map<Integer, Map<Direction, Integer>> directions = new HashMap<>();

    private static Ship ship;

    public static void addNPC(NPC npc) {
        idNPCMap.put(npc.getId(), npc);
    }

    public static NPC getNPC(int id) {
        return idNPCMap.get(id);
    }

    public static void addRegion(int source, Map<Direction, Integer> direction) {
        directions.put(source, direction);
    }

    public static void setShip(Ship ship) {
        MapSerializer.ship = ship;
    }

    @Override
    public JsonElement serialize(MainMap mainMap, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject map = new JsonObject();

        map.addProperty("name", mainMap.getName());
        map.addProperty("spawnpoint", mainMap.getSpawnPoint().getId());
        map.addProperty("humancount", mainMap.getHumanCount());

        JsonElement mainCharacter = jsonSerializationContext.serialize(mainMap.getMainCharacter());
        mainCharacter.getAsJsonObject().addProperty("location", mainMap.getMainCharacter().getLocation().getId());
        map.add("maincharacter", mainCharacter);

        JsonArray quests = jsonSerializationContext.serialize(mainMap.getQuests()).getAsJsonArray();
        map.add("quests", quests);

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

        //Deserialize the regions
        JsonArray regions = jsonMap.get("regions").getAsJsonArray();
        for(JsonElement region : regions) {
            map.addRegion(deserializationContext.deserialize(region, Region.class));

            Type directionsMapType = new TypeToken<Map<Direction, Integer>>(){}.getType();
            addRegion(region.getAsJsonObject().get("id").getAsInt(),
                    deserializationContext.deserialize(
                            region.getAsJsonObject().get("directions"),
                            directionsMapType)
            );
        }

        map.setMainCharacter(deserializationContext.deserialize(
                jsonMap.get("maincharacter"),
                Player.class
        ));

        JsonArray quests = jsonMap.get("quests").getAsJsonArray();
        for(JsonElement quest : quests) {
            map.addQuest(deserializationContext.deserialize(quest, Quest.class));
        }

        map.getQuests().forEach(q -> q.addObserver(map.getMainCharacter()));

        //Each region associated with its id
        Map<Integer, Region> regionWithId = new HashMap<>();
        for(Region region : map.getRegions()) {
            regionWithId.put(region.getId(), region);
            associateIDwRegions(regionWithId, region);
        }

        //Set player's location
        map.getMainCharacter().setLocation(regionWithId.get(
                jsonMap.get("maincharacter").getAsJsonObject()
                        .get("location").getAsInt()
        ));

        map.getMainCharacter().setShip(ship);

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

        //Avoid memory leaks
        idNPCMap = new HashMap<>();

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
            if(parent != null)
                parent.addContainedRegion(region);
            findRegionParent(region, region.getContainedRegions());
        }
    }

}
