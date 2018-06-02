package projetrpg.quest;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import projetrpg.entities.items.Item;
import projetrpg.observer.IObservable;
import projetrpg.observer.IObserver;
import projetrpg.observer.Observable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Quest extends Observable implements IObserver, Comparable<Quest> {

    /**
     * The name of the quest.
     */
    private String name;

    /**
     * The description of the quest.
     */
    private String description;

    /**
     * The required level to start this quest.
     */
    private int levelRequired;

    /**
     * The experience reward.
     */
    private int expRewarded;

    /**
     * The items rewarded
     */
    private ArrayList<Item> reward;

    /**
     * The objectives.
     */
    private Set<Objective> objectives;

    /**
     * Constructor for the type adapter.
     */
    private Quest() {

    }

    /**
     * The getter for the name of this quest.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the description of the quest.
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for the experience reward.
     *
     * @return The experience reward.
     */
    public int getExpRewarded() {
        return expRewarded;
    }

    /**
     * Getter for the objectives.
     *
     * @return The objectives.
     */
    public Set<Objective> getObjectives() {
        return new HashSet<>(objectives);
    }

    /**
     * Getter for the items reward.
     *
     * @return The items.
     */
    public ArrayList<Item> getReward() {
        return (ArrayList<Item>) reward.clone();
    }

    /**
     * Getter for the level required to start
     * this quest.
     *
     * @return The level required.
     */
    public int getLevelRequired() {
        return levelRequired;
    }

    /**
     * Give up this quest.
     */
    public void giveUp() {
        for (Objective o : this.getObjectives()) {
            o.finished = false;
        }
    }

    @Override
    public void update(IObservable a) {
        boolean over = true;
        for(Objective o : this.objectives) {
            if (!o.isFinished()) {
                over = false;
            }
        }
        if (over) {
            notifyObservers();
        }
    }

    @Override
    public int compareTo(Quest quest) {
        return Integer.compare(levelRequired, quest.levelRequired);
    }



    /**
     * Class permitting to serialize and deserialize Quest.
     */
    public static class TypeAdapter implements JsonSerializer<Quest>, JsonDeserializer<Quest> {

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
}
