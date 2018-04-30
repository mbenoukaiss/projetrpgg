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
        int id = jsonElement.getAsJsonObject().get("id").getAsInt();
        String name = jsonElement.getAsJsonObject().get("name").getAsString();

        Region region = new Region(id, name, null);

        for(JsonElement element : jsonElement.getAsJsonObject().get("entities").getAsJsonArray()) {
            NPC npc = deserializationContext.deserialize(element, NPC.class);
            region.addEntity(npc);
        }

        int highestId = 0;

        for(JsonElement element : jsonElement.getAsJsonObject().get("teleporters").getAsJsonArray()) {
            Teleporter t = deserializationContext.deserialize(element, Teleporter.class);
            int linkId = element.getAsJsonObject().get("link").getAsInt();

            if(t.getId() >= highestId) {
                highestId = t.getId()+1;
            }

            MapSerializer.addTeleporter(linkId, t);
            region.addTeleporter(t);
        }

        Teleporter.setCurrentId(highestId);

        for(JsonElement element : jsonElement.getAsJsonObject().get("childregions").getAsJsonArray()) {
            Region r = deserializationContext.deserialize(element, Region.class);
            region.addContainedRegion(r);
        }

        Inventory inv = deserializationContext.deserialize(jsonElement.getAsJsonObject().get("inventory"), Inventory.class);
        inv.transferContent(region.getInventory());

        return region;
    }

}
