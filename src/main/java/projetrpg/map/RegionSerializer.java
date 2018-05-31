package projetrpg.map;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import projetrpg.entities.Entity;
import projetrpg.entities.NPC;
import projetrpg.entities.items.Inventory;
import projetrpg.entities.items.Item;
import projetrpg.entities.player.Ship;

import java.lang.reflect.Type;
import java.util.*;

public class RegionSerializer implements JsonSerializer<Region>, JsonDeserializer<Region> {

    @Override
    public JsonElement serialize(Region region, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject reg = new JsonObject();
        reg.addProperty("id", region.getId());
        reg.addProperty("name", region.getName());
        reg.addProperty("description", region.description);
        reg.addProperty("shiprequired", region.shipLevelRequired);

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

        JsonArray requireditems = jsonSerializationContext.serialize(region.itemsNeeded).getAsJsonArray();
        reg.add("requireditems", requireditems);

        return reg;
    }

    @Override
    public Region deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext deserializationContext) throws JsonParseException {
        JsonObject jsonRegion = jsonElement.getAsJsonObject();
        Region region = new Region();

        region.id = jsonRegion.get("id").getAsInt();
        region.name = jsonRegion.get("name").getAsString();
        region.description = jsonRegion.get("description").getAsString();
        region.shipLevelRequired = jsonRegion.get("shiprequired").getAsInt();

        Type directionsMapType = new TypeToken<Map<Direction, Integer>>(){}.getType();
        MapSerializer.addRegion(jsonRegion.get("id").getAsInt(),
                deserializationContext.deserialize(
                        jsonRegion.get("directions"),
                        directionsMapType)
        );

        int highestId = 0;

        for(JsonElement element : jsonRegion.get("entities").getAsJsonArray()) {
            NPC npc = deserializationContext.deserialize(element, NPC.class);
            region.addEntity(npc);

            MapSerializer.addNPC(npc);
            if(npc.getId() >= highestId) highestId = npc.getId()+1;
        }

        NPC.setCurrentId(highestId);

        for(JsonElement element : jsonRegion.get("teleporters").getAsJsonArray()) {
            Teleporter t = deserializationContext.deserialize(element, Teleporter.class);
            int linkId = element.getAsJsonObject().get("link").getAsInt();
            TeleporterSerializer.addTeleporter(linkId, t);
            region.addTeleporter(t);
        }

        for(JsonElement element : jsonRegion.get("childregions").getAsJsonArray()) {
            region.addContainedRegion(MapSerializer.deserializeRegion(deserializationContext, element.getAsJsonObject()));
        }

        Inventory inv = deserializationContext.deserialize(jsonRegion.get("inventory"), Inventory.class);
        inv.transferContent(region.getInventory());

        region.itemsNeeded = new ArrayList<>();
        for(JsonElement element : jsonRegion.get("requireditems").getAsJsonArray()) {
            region.itemsNeeded.add(deserializationContext.deserialize(element, Item.class));
        }

        return region;
    }

}
