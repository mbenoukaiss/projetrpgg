package projetrpg.map;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import projetrpg.entities.items.Item;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeleporterSerializer implements JsonSerializer<Teleporter>, JsonDeserializer<Teleporter> {

    private static Map<Integer, Teleporter> linkIdTeleporter = new HashMap<>();

    public static void addTeleporter(int linkId, Teleporter t) {
        linkIdTeleporter.put(linkId, t);
    }

    public static void linkTeleporters() {
        linkIdTeleporter.forEach((k, v) -> {
            v.link(linkIdTeleporter.get(v.getId()));
        });

        //Avoid memory leaks
        linkIdTeleporter = null;
    }

    @Override
    public JsonElement serialize(Teleporter teleporter, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject o = new JsonObject();

        o.addProperty("id", teleporter.getId());
        o.addProperty("name", teleporter.getName());
        o.addProperty("repaired", teleporter.isRepaired());

        if(teleporter.getLinkedTeleporter() != null) {
            o.addProperty("link", teleporter.getLinkedTeleporter().getId());
        } else {
            o.addProperty("link", -1);
        }

        JsonElement requirements = jsonSerializationContext.serialize(teleporter.getItemsNeededToRepair());
        o.add("requirements", requirements);

        return o;
    }

    @Override
    public Teleporter deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject teleporter = jsonElement.getAsJsonObject();

        int id = teleporter.get("id").getAsInt();
        String name = teleporter.get("name").getAsString();
        boolean repaired = teleporter.get("repaired").getAsBoolean();

        Type requirementsType = new TypeToken<ArrayList<Item>>() {}.getType();
        ArrayList<Item> requirements = context.deserialize(teleporter.get("requirements"), requirementsType);

        return new Teleporter(id, name, null, repaired, requirements);
    }
}
