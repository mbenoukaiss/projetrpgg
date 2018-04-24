package projetrpg.entities.player;

import com.google.gson.*;

import java.lang.reflect.Type;

public class PlayerSerializer implements JsonSerializer<Player>, JsonDeserializer<Player> {

    @Override
    public JsonElement serialize(Player player, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        
        obj.addProperty("name", player.getName());
        obj.addProperty("health", player.getHp());
        obj.addProperty("basehealth", player.getBaseHps());
        obj.addProperty("experience", player.getExperience());

        JsonElement inventory = jsonSerializationContext.serialize(player.getInventory());
        obj.add("inventory", inventory);

        JsonElement itemInHand = jsonSerializationContext.serialize(player.getItemInHand());
        obj.add("iteminhand", itemInHand);

        JsonArray abilities = jsonSerializationContext.serialize(player.getAbilities()).getAsJsonArray();
        obj.add("abilities", abilities);

        obj.addProperty("basedamage", player.baseDamage());
        
        return obj;
    }

    @Override
    public Player deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }

}
