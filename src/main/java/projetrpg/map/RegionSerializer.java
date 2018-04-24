package projetrpg.map;

import com.google.gson.*;
import projetrpg.entities.Entity;
import projetrpg.entities.NPC;
import projetrpg.entities.items.Inventory;

import java.lang.reflect.Type;
import java.util.*;

public class RegionSerializer implements JsonSerializer<Region>, JsonDeserializer<Region> {

    @Override
    public JsonElement serialize(Region region, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject reg = new JsonObject();
        reg.addProperty("id", region.getId());
        reg.addProperty("name", region.getName());

        JsonObject directions = new JsonObject();
        for(Map.Entry<Direction, Region> directionRegionEntry : region.getRegionOnDirection().entrySet()) {
            directions.addProperty(directionRegionEntry.getKey().name(), directionRegionEntry.getValue().getId());
        }
        reg.add("directions", directions);

        JsonArray childregions = jsonSerializationContext.serialize(region.getContainedRegions()).getAsJsonArray();
        reg.add("childregions", childregions);

        JsonArray entities = jsonSerializationContext.serialize(region.getEntities()).getAsJsonArray();
        reg.add("entities", entities);

        JsonArray teleporters = jsonSerializationContext.serialize(region.getTeleporters()).getAsJsonArray();
        reg.add("teleporters", teleporters);

        JsonElement inventory = jsonSerializationContext.serialize(region.getInventory());
        reg.add("inventory", inventory);

        return reg;
    }

    @Override
    public Region deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext deserializationContext) throws JsonParseException {
        int regionId = jsonElement.getAsJsonObject().get("id").getAsInt();
        String regionName = jsonElement.getAsJsonObject().get("name").getAsString();

        List<NPC> entities = new ArrayList<>();
        for(JsonElement element : jsonElement.getAsJsonObject().get("entities").getAsJsonArray()) {
            entities.add(deserializationContext.deserialize(element, NPC.class));
        }

        List<Teleporter> teleporters = new ArrayList<>();
        for(JsonElement element : jsonElement.getAsJsonObject().get("teleporters").getAsJsonArray()) {
            teleporters.add(deserializationContext.deserialize(element, Teleporter.class));
        }

        Set<Region> subregions = new HashSet<>();
        for(JsonElement element : jsonElement.getAsJsonObject().get("childregions").getAsJsonArray()) {
            subregions.add(deserializationContext.deserialize(element, Region.class));
        }

        Inventory inv = deserializationContext.deserialize(jsonElement.getAsJsonObject().get("inventory"), Inventory.class);

        return new Region(regionId, regionName, null, subregions, entities, teleporters, inv);
    }

}
