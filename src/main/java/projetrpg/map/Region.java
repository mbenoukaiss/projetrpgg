package projetrpg.map;

import com.google.gson.annotations.JsonAdapter;
import projetrpg.Describable;
import projetrpg.entities.NPC;
import projetrpg.entities.items.Inventory;
import projetrpg.entities.items.Item;

import java.util.*;

/**
 * Represents any kind of location, a solar system,
 * a planet, an area on a planet, a spaceship...
 */
@JsonAdapter(RegionSerializer.class)
public class Region implements Describable {

    private static final int DEFAULT_ROOM_ITEM_CAPACITY = 10;

    /**
     * Id of the region
     */
    private int id;

    /**
     * Name of the region
     */
    private String name;
    
    /**
     * The parent region, containing this region.
     */
    private Region parent;

    /**
     * The adjacent regions.
     */
    private Map<Direction, Region> regionOnDirection;

    /**
     * The regions DIRECTLY contained in this region.
     */
    private Set<Region> containedRegions;

    /**
     * All the entities DIRECTLY contained in this region.
     */
    private List<NPC> entities;

    /**
     * All the teleporters DIRECTLY contained in this region.
     */
    private List<Teleporter> teleporters;

    /**
     * The inventory of this region : all the items DIRECTLY
     * containe in this region..
     */
    private Inventory inventory;

    public Region(int id, String name, Region parent) {
        this.id = id;
        this.name = name;
        this.regionOnDirection = new HashMap<>();
        this.containedRegions = new HashSet<>();
        this.entities = new ArrayList<>();
        this.teleporters = new ArrayList<>();
        this.inventory = new Inventory(DEFAULT_ROOM_ITEM_CAPACITY);
    }

    public Region(int id, String name, Region parent,
                  Set<Region> containedRegions, List<NPC> entities,
                  List<Teleporter> teleporters, Inventory inventory) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.regionOnDirection = new HashMap<>();
        this.containedRegions = containedRegions;
        this.entities = entities;
        this.teleporters = teleporters;
        this.inventory = inventory;
    }

    /**
     * Ask this object to describe itself.
     * @return the description as a string.
     */
    @Override
    public String describe() {
        String d = "region : " + this.name + ", contains those items : ";
        for(Item i: this.inventory.getAll()) {
            d+=i.getName() +", ";
        }
        d = d.substring(0, d.length()-2);
        d+=". And those entities :";
        for(NPC e: this.entities) {
            d+=e.getName() +", ";
        }
        d = d.substring(0, d.length()-2) + ". From there you can go to : " + (this.getRegionNamesOnDirection()) + ".";
        return d;
    }

    /**
     * Accessor for the names of the linked regions as a String.
     * @return the names.
     */
    public String getRegionNamesOnDirection() {
        String r ="";
        for(Region i : this.regionOnDirection.values()) {
            r += i.name + ",";
        }
        r = r.substring(0, r.length() -1);
        return r;
    }

    /**
     * Accessor for the regions contained.
     * @return the regions.
     */
    public Map<Direction, Region> getRegionOnDirection() {
        return regionOnDirection;
    }

    /**
     * Accessor for the entities contained.
     * @return the entities.
     */
    public List<NPC> getEntities() {
        return entities;
    }

    /**
     * Accessor for the inventory.
     * @return the inventory.
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Accessor for the name.
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Add an entity to this region.
     * @param e the entity.
     */
    public void addEntity(NPC e) {
        if (!this.entities.contains(e)) {
            this.entities.add(e);
        }
    }

    /**
     * Deletes an entity to this region.
     * @param e the entity.
     */
    public void deleteEntity(NPC e) {
        if (this.entities.contains(e)) {
            this.entities.remove(e);
        }
    }

    /**
     * Add a teleporter to the region.
     */
    public void addTeleporter(Teleporter t) {
        if (!this.teleporters.contains(t)) {
            this.teleporters.add(t);
        }
    }

    /**
     * Add an item to this region.
     * @param item the item.
     */
    public void addItemToInventory(Item item) {
        this.inventory.add(item);
    }

    /**
     * Link regions to another region.
     * @param r the region that you wish to associate.
     * @param direction the direction where the @param r is.
     */
    public void linkToDirection(Region r, Direction direction) {
        switch (direction) {
            case EST:
                this.regionOnDirection.put(Direction.EST, r);
                r.regionOnDirection.put(Direction.WEST, this);
                break;
            case WEST:
                this.regionOnDirection.put(Direction.WEST, r);
                r.regionOnDirection.put(Direction.EST, this);
                break;
            case NORTH:
                this.regionOnDirection.put(Direction.NORTH, r);
                r.regionOnDirection.put(Direction.SOUTH, this);
                break;
            case SOUTH:
                this.regionOnDirection.put(Direction.SOUTH, r);
                r.regionOnDirection.put(Direction.NORTH, this);
                break;
        }
    }
    
}
