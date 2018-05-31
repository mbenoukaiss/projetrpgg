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

    private static Map<Integer, Teleporter> linkIdTeleporter = new HashMap<>();

    private static Map<Integer, Map<Direction, Integer>> directions = new HashMap<>();

    private static Map<Planet, Integer> planets = new HashMap<>();

    private static Ship ship;

    public static void addNPC(NPC npc) {
        idNPCMap.put(npc.getId(), npc);
    }

    public static NPC getNPC(int id) {
        return idNPCMap.get(id);
    }

    public static void addTeleporter(int linkId, Teleporter t) {
        linkIdTeleporter.put(linkId, t);
    }

    public static void linkTeleporters() {
        linkIdTeleporter.forEach((k, v) -> {
            v.link(linkIdTeleporter.get(v.getId()));
        });

        //Avoid memory leaks
        linkIdTeleporter = new HashMap<>();
    }

    public static void addRegion(int source, Map<Direction, Integer> direction) {
        directions.put(source, direction);
    }

    public static void addPlanet(Planet p, int landingRegionId) {
        planets.put(p, landingRegionId);
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
            map.addRegion(deserializeRegion(deserializationContext, region.getAsJsonObject()));

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

        //Associate planets with landing regions
        planets.forEach((p, landingId) -> {
            p.setLandingRegion(regionWithId.get(landingId));
        });

        //Let children know who is their parent
        map.getRegions().forEach(r -> findRegionParent(null, r.getContainedRegions()));

        //Find spawnpoint
        map.setSpawnPoint(regionWithId.get(
                jsonMap.get("spawnpoint").getAsInt()
        ));

        //Link teleporters
        linkTeleporters();

        //Avoid memory leaks
        idNPCMap = new HashMap<>();
        planets = new HashMap<>();

        return map;
    }

    private void associateIDwRegions(Map<Integer, Region> regionWithId, Region region) {
        for(Region sr : region.containedRegions) {
            regionWithId.put(sr.getId(), sr);
            associateIDwRegions(regionWithId, sr);
        }
    }

    private void findRegionParent(Region parent, Collection<Region> regions) {
        for(Region region : regions) {
            if(parent != null)
                parent.addContainedRegion(region);

            if(region.containedRegions != null)
            findRegionParent(region, region.containedRegions);
        }
    }

    public static Region deserializeRegion(JsonDeserializationContext jdc, JsonObject o) {
        if(o.has("type")) {
            if(o.get("type").getAsString().equals("ship")) {
                return jdc.deserialize(o, Ship.class);
            } else {
                return jdc.deserialize(o, Planet.class);
            }
        } else {
            return jdc.deserialize(o, Region.class);
        }
    }

}
