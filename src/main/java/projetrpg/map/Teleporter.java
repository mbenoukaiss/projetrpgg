package projetrpg.map;

import projetrpg.entities.items.Item;

import java.util.ArrayList;

/**
 * Created by mhevin on 29/03/18.
 */
public class Teleporter {

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

    public Teleporter(String name, Region region) {
        this.name = name;
        this.region = region;
        this.repaired = false;
        region.addTeleporter(this);
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Item> getItemsNeededToRepair() {
        return this.itemsNeededToRepair;
    }

    public Teleporter getLinkedTeleporter() {
        return this.linkedTeleporter;
    }

    public boolean isRepaired() {
        return this.repaired;
    }

    public void setRepaired(boolean r) {
        repaired = r;
    }

    public Region getRegion() {
        return this.region;
    }

    /**
     * Links this teleported to another teleporter.
     *
     * @param other The other teleporter
     */
    void link(Teleporter other) {
        this.linkedTeleporter = other;
        other.linkedTeleporter = this;
    }

    public Region getLinkedRegion() {
        return this.linkedTeleporter.region;
    }
}
