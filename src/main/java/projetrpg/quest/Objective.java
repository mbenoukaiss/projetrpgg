package projetrpg.quest;

import com.google.gson.*;
import projetrpg.entities.NPC;
import projetrpg.map.MapSerializer;
import projetrpg.observer.Observable;

import java.lang.reflect.Type;
import java.util.Objects;

public class Objective<T> extends Observable {

    /**
     * If the objective is finished.
     */
    protected boolean finished;

    /**
     * The description of this objective.
     */
    protected String description;

    /**
     * The type of the objective.
     */
    protected ObjectiveType type;

    /**
     * The concerned object.
     */
    protected T concernedObject;

    Objective() {

    }

    public Objective(String description, ObjectiveType type) {
        this.description = description;
        this.finished = false;
        this.type = type;
    }

    /**
     * Tells if this objective is finished.
     *
     * @return True if the objective is finished.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Consider this objective as finished.
     */
    public void finish() {
        this.finished = true;
        this.notifyObservers();
    }

    /**
     * Getter for the concerned object.
     *
     * @return The concerned object.
     */
    public T getConcernedObject() {
        return concernedObject;
    }

    /**
     * Setter for the concerned object.
     *
     * @param concernedObject The new object.
     */
    public void setConcernedObject(T concernedObject) {
        this.concernedObject = concernedObject;
    }

    /**
     * Getter for the description.
     *
     * @return The description of this objective.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for the type of objective.
     *
     * @return The type of this objective.
     */
    public ObjectiveType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Objective<?> objective = (Objective<?>) o;
        return finished == objective.finished &&
                Objects.equals(description, objective.description) &&
                type == objective.type &&
                Objects.equals(concernedObject, objective.concernedObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(finished, description, type, concernedObject);
    }



    /**
     * Class permitting to serialize and deserialize Objective.
     */
    public static class TypeAdapter implements JsonSerializer<Objective>, JsonDeserializer<Objective> {

        @Override
        public JsonElement serialize(Objective objective, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObjective = new JsonObject();

            jsonObjective.addProperty("finished", objective.finished);
            jsonObjective.addProperty("description", objective.description);
            jsonObjective.add("type", jsonSerializationContext.serialize(objective.type));

            JsonObject concerned = new JsonObject();
            concerned.addProperty("class", objective.concernedObject.getClass().getCanonicalName());

            if(objective.concernedObject.getClass() == NPC.class) {
                concerned.addProperty("object", ((NPC) objective.getConcernedObject()).getId());
            } else {
                concerned.add("object", jsonSerializationContext.serialize(objective.concernedObject));
            }

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

}
