package projetrpg.quest;

import com.google.gson.*;
import projetrpg.entities.NPC;
import projetrpg.entities.items.Item;
import projetrpg.map.MapSerializer;
import projetrpg.map.Region;
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

        JsonObject concerned = new JsonObject();
        concerned.addProperty("class", objective.concernedObject.getClass().getCanonicalName());
        concerned.add("object", jsonSerializationContext.serialize(objective.concernedObject));

        jsonObjective.add("concerned", concerned);

        return jsonObjective;
    }
    
    @Override
    public Objective deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Objective objective = new Objective<>();
        JsonObject jsonObjective = jsonElement.getAsJsonObject();

        objective.finished = jsonObjective.get("finished").getAsBoolean();
        objective.description = jsonObjective.get("description").getAsString();
        objective.type = jsonDeserializationContext.deserialize(jsonObjective.get("type"), ObjectiveType.class);

        try {
            JsonObject jsonConcerned = jsonObjective.get("concerned").getAsJsonObject();

            Class clazz = Class.forName(jsonConcerned.get("class").getAsString());

            if(clazz == NPC.class) {
                objective.concernedObject = MapSerializer.getNPC(jsonConcerned.get("object").getAsInt());
            } else {
                objective.concernedObject = jsonDeserializationContext.deserialize(jsonConcerned.get("object"), clazz);
            }
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }

        return objective;
    }

}
