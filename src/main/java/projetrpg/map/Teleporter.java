package projetrpg.map;

import projetrpg.entities.items.Item;
import projetrpg.entities.player.Player;

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
public class Teleporter {

    private static int currentId = 0;

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
    private Region location;

    /**
     * The items required to repair this teleporter.
     */
    private ArrayList<Item> itemsNeededToRepair = new ArrayList<>();

    /**
     * The constructor for a teleporter.
     *
     * @param name   the name of the teleporter
     * @param location the location of the teleporter
     */
    public Teleporter(String name, Region location) {
        this.name = name;
        this.location = location;
        this.repaired = false;

        id = currentId++;
    }

    public Teleporter(int id, String name, Region location, boolean repaired,
                      ArrayList<Item> requiredItems) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.repaired = repaired;
        this.itemsNeededToRepair = requiredItems;

        if(id >= currentId) currentId = id+1;
    }

    public String describe() {
        String items = "";
        for(Item i : this.itemsNeededToRepair) {
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
     * Adds an item to the list of required items
     * to repair this teleporter.
     *
     * @param item The item to add.
     */
    public void addRequiredItem(Item item) {
        itemsNeededToRepair.add(item);
    }

    public void repair() {
        this.repaired = true;
    }

    public boolean isRepaired() {
        return repaired;
    }

    public void addItemToRepair(Item item) {
        if (!this.getItemsNeededToRepair().contains(item)) {
            this.itemsNeededToRepair.add(item);
        }
    }

    public ArrayList<Item> getItemsNeededToRepair() {
        return itemsNeededToRepair;
    }

    public void setLocation(Region location) {
        this.location = location;
    }

    public static void setCurrentId(int currentId) {
        Teleporter.currentId = currentId;
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
                Objects.equals(itemsNeededToRepair, that.itemsNeededToRepair);
    }

}
