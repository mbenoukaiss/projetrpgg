package projetrpg.map;

import com.google.gson.*;
import projetrpg.entities.Entity;
import projetrpg.entities.NPC;
import projetrpg.entities.items.Inventory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegionSerializer implements JsonSerializer<Region>, JsonDeserializer<Region> {

    @Override
    public JsonElement serialize(Region region, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject reg = new JsonObject();
        reg.addProperty("id", region.getId());
        reg.addProperty("name", region.getName());

        if(region.getParent() != null) {
            reg.addProperty("parent", region.getParent().getId());
        } else {
            reg.addProperty("parent", -1);
        }

        JsonObject directions = new JsonObject();
        for(Map.Entry<Direction, Region> directionRegionEntry : region.getRegionOnDirection().entrySet()) {
            directions.addProperty(directionRegionEntry.getKey().name(), directionRegionEntry.getValue().getId());
        }
        reg.add("directions", directions);

        List<Integer> childregionsId = new ArrayList<>();
        for(Region r : region.getContainedRegions()) {
            childregionsId.add(r.getId());
        }
        JsonArray childregions = jsonSerializationContext.serialize(childregionsId).getAsJsonArray();
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
            entities.add(deserializationContext.deserialize(element, Entity.class));
        }

        List<Teleporter> teleporters = new ArrayList<>();
        for(JsonElement element : jsonElement.getAsJsonObject().get("teleporters").getAsJsonArray()) {
            teleporters.add(deserializationContext.deserialize(element, Teleporter.class));
        }

        Inventory inv = deserializationContext.deserialize(jsonElement.getAsJsonObject().get("inventory"), Inventory.class);

        return new Region(regionId, regionName, null, null, entities, teleporters, inv);
    }

}
