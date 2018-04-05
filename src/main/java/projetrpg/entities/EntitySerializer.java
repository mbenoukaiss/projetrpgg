package projetrpg.entities;

import com.google.gson.*;
import projetrpg.entities.player.Player;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Type;

public class EntitySerializer implements JsonSerializer<Entity>, JsonDeserializer<Entity> {

    @Override
    public JsonElement serialize(Entity entity, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();

        obj.addProperty("name", entity.getName());
        obj.addProperty("type", entity.type.name());
        obj.addProperty("health", entity.hps);
        obj.addProperty("hostile", entity.isHostile);

        if(entity instanceof NPC) {
            NPC npc = (NPC) entity;
            obj.addProperty("basedamage", npc.baseDamage());
        } else if(entity instanceof Player) {

        } else {
            throw new NotImplementedException();
        }

        return obj;
    }

    @Override
    public Entity deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }

}
