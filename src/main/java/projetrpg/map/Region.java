package projetrpg.map;

import projetrpg.*;
import projetrpg.entities.Entity;
import projetrpg.entities.items.Inventory;
import projetrpg.entities.items.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Represents any kind of location, a solar system,
 * a planet, an area on a planet, a spaceship...
 */
public class Region implements Describable {

    /**
     * The adjacent regions.
     */
    private HashMap<Direction, Region> regionOnDirection = new HashMap<>();

    /**
     * The parent region, containing this region.
     */
    private Region parent;

    /**
     * The regions DIRECTLY contained in this region.
     */
    private HashSet<Region> containedRegions = new HashSet<>();

    /**
     * Name of the region
     */
    private String name;

    /**
     * All the entities DIRECTLY contained in this region.
     */
    private ArrayList<Entity> entities = new ArrayList<>();

    /**
     * All the teleporters DIRECTLY contained in this region.
     */
    private ArrayList<Teleporter> teleporters = new ArrayList<>();

    /**
     * The inventory of this region : all the items DIRECTLY
     * containe in this region..
     */
    private Inventory inventory;

    public Region(Region parent, String name, Inventory inventory) {
        if (parent != null) parent.containedRegions.add(this);
        this.parent = parent;
        this.name = name;
        this.inventory = inventory;
    }

    @Override
    public String describe() {
        String d = "Region : " + this.name + ", contains those items :";
        for(Item i: this.inventory.getAll()) {
            d+=i.getName() +", ";
        }

        return d.substring(0, d.length()-2);
    }

    public Region getRegionTowards(Direction d) {
        return regionOnDirection.get(d);
    }

    public HashMap<Direction, Region> getRegionOnDirection() {
        return regionOnDirection;
    }

    public HashSet<Region> getContainedRegions() {
        return containedRegions;
    }

    public Region getParent() {
        return parent;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public ArrayList<Teleporter> getTeleporters() {
        return teleporters;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getName() {
        return name;
    }
}
