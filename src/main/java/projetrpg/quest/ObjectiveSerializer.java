package projetrpg.quest;

import com.google.gson.*;
import projetrpg.entities.items.Item;
import projetrpg.map.MapSerializer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ObjectiveSerializer implements JsonSerializer<Objective>, JsonDeserializer<Objective> {

    @Override
    public JsonElement serialize(Objective objective, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObjective = new JsonObject();

        jsonObjective.addProperty("finished", objective.finished);
        jsonObjective.addProperty("description", objective.description);
        jsonObjective.add("type", jsonSerializationContext.serialize(objective.type));
        jsonObjective.add("concerned", jsonSerializationContext.serialize(objective.concernedObject));

        return jsonObjective;
    }
    
    @Override
    public Objective deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Objective objective = new Objective();
        JsonObject jsonObjective = jsonElement.getAsJsonObject();

        objective.finished = jsonObjective.get("finished").getAsBoolean();
        objective.description = jsonObjective.get("description").getAsString();
        objective.type = jsonDeserializationContext.deserialize(jsonObjective.get("type"), ObjectiveType.class);
        /*
        if(jsonObjective.has("npc"))
            objective.concernedNPC = MapSerializer.getNPC(jsonObjective.get("npc").getAsInt());
        if(jsonObjective.has("item"))
            objective.concernedItem = jsonDeserializationContext.deserialize(jsonObjective.get("item"), Item.class);*/

        return objective;
    }

}
