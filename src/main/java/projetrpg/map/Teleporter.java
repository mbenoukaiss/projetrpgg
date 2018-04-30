package projetrpg.map;

import projetrpg.entities.items.Item;

import java.util.ArrayList;

/**
 * A teleporter which allows a player to move
 * from a region to another even though they're
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
    private Region region;

    /**
     * The items required to repair this teleporter.
     */
    private ArrayList<Item> itemsNeededToRepair = new ArrayList<>();

    /**
     * The constructor for a teleporter.
     *
     * @param name   the name of the teleporter
     * @param region the region of the teleporter
     */
    public Teleporter(String name, Region region) {
        this.name = name;
        this.region = region;
        this.repaired = false;

        id = currentId++;
    }

    public Teleporter(int id, String name, Region region, boolean repaired,
                      ArrayList<Item> requiredItems) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.repaired = repaired;
        this.itemsNeededToRepair = requiredItems;
    }

    public String describe() {
        String items = "";
        for(Item i : this.itemsNeededToRepair) {
            items+=i.getName() + ", ";
        }
        if (items.length() > 0) items = items.substring(0, items.length()-2);
        return this.name + ", you'll need those items in order to repair it : " + items + ".";
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
     * Accessor for the region of the teleporter.
     *
     * @return the region
     */
    public Region getRegion() {
        return this.region;
    }

    /**
     * Links this teleported to another teleporter.
     *
     * @param other The other teleporter
     */
    public void link(Teleporter other) {
        this.linkedTeleporter = other;
        other.linkedTeleporter = this;
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

    public void setRegion(Region region) {
        this.region = region;
    }

    public static void setCurrentId(int currentId) {
        Teleporter.currentId = currentId;
    }
}
