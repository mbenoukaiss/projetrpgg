package projetrpg.entities;

import com.google.gson.*;
import projetrpg.entities.player.Player;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Type;

public class NPCSerializer implements JsonSerializer<NPC>, JsonDeserializer<NPC> {

    @Override
    public JsonElement serialize(NPC entity, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();

        obj.addProperty("name", entity.getName());
        obj.addProperty("type", entity.type.name());
        obj.addProperty("health", entity.hps);
        obj.addProperty("basehealth", entity.baseHps);
        obj.addProperty("hostile", entity.isHostile);
        obj.addProperty("basedamage", entity.baseDamage());
        obj.addProperty("dialogue", entity.getDialogue());

        return obj;
    }

    @Override
    public NPC deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }

}
