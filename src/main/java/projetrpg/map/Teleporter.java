package projetrpg.map;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import projetrpg.Describable;
import projetrpg.entities.items.Item;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A teleporter which allows a player to move
 * from a location to another even though they're
 * not next to eachother.
 *
 * @author mhevin
 * @author mbenoukaiss
 */
public class Teleporter implements Describable {

    /**
     * The highest ID for the Teleporter.
     * Necessary for deserialization.
     */
    static int currentId = 0;

    /**
     * Id of the teleporter.
     */
    private int id;

    /**
     * Name of the teleporter if the player decides
     * to rename it, else, null.
     */
    private String name;

    /**
     * The teleporter linked to this teleporter.
     */
    private Teleporter linkedTeleporter;

    /**
     * State of the teleporter (broken or not).
     */
    private boolean repaired;

    /**
     * Region where the teleporter is.
     */
    Region location;

    /**
     * The items required to repair this teleporter.
     */
    private ArrayList<Item> repairRequirements = new ArrayList<>();

    /**
     * Constructor for the type adapter.
     */
    private Teleporter() {

    }

    /**
     * The constructor for a teleporter.
     *
     * @param name   the name of the teleporter
     * @param location the location of the teleporter
     */
    Teleporter(String name, Region location) {
        this.name = name;
        this.location = location;
        this.repaired = false;

        id = currentId++;
    }

    @Override
    public String describe() {
        String items = "";
        for(Item i : this.repairRequirements) {
            items+=i.getName() + ", ";
        }
        if (items.length() > 0) items = items.substring(0, items.length()-2);
        return this.name + ", you'll need those items in order to repair it : " + items + "." +
                " This teleporter will take you to the region : " + this.getLinkedTeleporter().getLocation().getName() + ".";
    }

    /**
     * Accessor for the id of the teleporter.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Accessor for the name of the teleporter.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Accessor for the linked teleporter.
     *
     * @return the linked teleporter
     */
    public Teleporter getLinkedTeleporter() {
        return linkedTeleporter;
    }

    /**
     * Accessor for the location of the teleporter.
     *
     * @return the location
     */
    public Region getLocation() {
        return this.location;
    }

    /**
     * Links this teleported to another teleporter.
     *
     * @param other The other teleporter
     */
    public void link(Teleporter other) {
        //Undoing possible previous linking
        if(linkedTeleporter != null)
            this.linkedTeleporter.linkedTeleporter = null;

        if(other.linkedTeleporter != null)
            other.linkedTeleporter.linkedTeleporter = null;

        //Doing the actual link
        this.linkedTeleporter = other;
        other.linkedTeleporter = this;
    }

    /**
     * Repairs the teleporter.
     */
    public void repair() {
        this.repaired = true;
    }

    /**
     * Tells if the teleporter is repaired.
     *
     * @return True if the teleporter is repaired.
     */
    public boolean isRepaired() {
        return repaired;
    }

    /**
     * Adds an item to the list of items required
     * to repair the teleporter.
     *
     * @param item The item.
     */
    public void addItemToRepair(Item item) {
        this.repairRequirements.add(item);
    }

    /**
     * Getter for the items required to repair
     * the teleporter.
     *
     * @return List of items required.
     */
    public ArrayList<Item> getRepairRequirements() {
        return (ArrayList<Item>) repairRequirements.clone();
    }

    @Override
    public String toString() {
        return "Teleporter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", linkedTeleporter=" + ((linkedTeleporter != null)? linkedTeleporter.getId() : -1) +
                ", repaired=" + repaired +
                ", location=" + location +
                ", repairRequirements=" + repairRequirements +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Teleporter that = (Teleporter) o;
        return id == that.id &&
                repaired == that.repaired &&
                Objects.equals(name, that.name) &&
                (linkedTeleporter == null || that.linkedTeleporter == null ||
                        linkedTeleporter.getId() == that.linkedTeleporter.getId()) &&
                Objects.equals(location, that.location) &&
                Objects.equals(repairRequirements, that.repairRequirements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, repaired, location, repairRequirements);
    }



    /**
     * Class permitting to serialize and deserialize Teleporter.
     */
    public static class TypeAdapter implements JsonSerializer<Teleporter>, JsonDeserializer<Teleporter> {

        @Override
        public JsonElement serialize(Teleporter teleporter, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject o = new JsonObject();

            o.addProperty("id", teleporter.id);
            o.addProperty("name", teleporter.name);
            o.addProperty("repaired", teleporter.repaired);

            if(teleporter.getLinkedTeleporter() != null) {
                o.addProperty("link", teleporter.linkedTeleporter.id);
            } else {
                o.addProperty("link", -1);
            }

            JsonElement requirements = jsonSerializationContext.serialize(teleporter.repairRequirements);
            o.add("requirements", requirements);

            return o;
        }

        @Override
        public Teleporter deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            Teleporter teleporter = new Teleporter();
            JsonObject jsonTeleporter = jsonElement.getAsJsonObject();

            teleporter.id = jsonTeleporter.get("id").getAsInt();
            if(teleporter.id >= currentId) currentId = teleporter.id+1;

            teleporter.name = jsonTeleporter.get("name").getAsString();
            teleporter.repaired = jsonTeleporter.get("repaired").getAsBoolean();

            Type requirementsType = new TypeToken<ArrayList<Item>>() {}.getType();
            teleporter.repairRequirements = context.deserialize(jsonTeleporter.get("requirements"), requirementsType);

            return teleporter;
        }

    }

}
