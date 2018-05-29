package projetrpg.quest;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import projetrpg.entities.items.Item;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Set;

public class QuestSerializer implements JsonSerializer<Quest>, JsonDeserializer<Quest> {

    @Override
    public JsonElement serialize(Quest quest, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonQuest = new JsonObject();

        jsonQuest.addProperty("name", quest.name);
        jsonQuest.addProperty("description", quest.description);
        jsonQuest.addProperty("lrequired", quest.levelRequired);
        jsonQuest.addProperty("ereward", quest.expRewarded);

        JsonArray objectives = jsonSerializationContext.serialize(quest.objectives).getAsJsonArray();
        jsonQuest.add("objectives", objectives);

        JsonArray reward = jsonSerializationContext.serialize(quest.reward).getAsJsonArray();
        jsonQuest.add("reward", reward);

        return jsonQuest;
    }

    @Override
    public Quest deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Quest quest = new Quest();
        JsonObject jsonQuest = jsonElement.getAsJsonObject();

        quest.name = jsonQuest.get("name").getAsString();
        quest.levelRequired = jsonQuest.get("lrequired").getAsInt();
        quest.expRewarded = jsonQuest.get("ereward").getAsInt();
        quest.description = jsonQuest.get("description").getAsString();

        Type objectivesType = new TypeToken<Set<Objective>>() {}.getType();
        quest.objectives = jsonDeserializationContext.deserialize(jsonQuest.get("objectives"), objectivesType);
        quest.objectives.forEach(o -> o.addObserver(quest));

        Type rewardType = new TypeToken<ArrayList<Item>>() {}.getType();
        quest.reward = jsonDeserializationContext.deserialize(jsonQuest.get("reward"), rewardType);

        return quest;
    }

}
