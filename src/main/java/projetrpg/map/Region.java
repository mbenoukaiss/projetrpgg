package projetrpg.map;

import com.google.gson.annotations.JsonAdapter;
import projetrpg.*;
import projetrpg.entities.Entity;
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
    private List<Entity> entities;

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
                  Set<Region> containedRegions, List<Entity> entities,
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

    @Override
    public String describe() {
        String d = "Region : " + this.name + ", contains those items :";
        for(Item i: this.inventory.getAll()) {
            d+=i.getName() +", ";
        }
        d = d.substring(0, d.length()-2);
        d+=". And those entities :";
        for(Entity e: this.entities) {
            d+=e.getName() +", ";
        }
        return d.substring(0, d.length()-2) + ".";
    }

    public Region getRegionTowards(Direction d) {
        return regionOnDirection.get(d);
    }

    public Map<Direction, Region> getRegionOnDirection() {
        return regionOnDirection;
    }

    public Set<Region> getContainedRegions() {
        return containedRegions;
    }

    public Region getParent() {
        return parent;
    }

    public void setParent(Region parent) {
        this.parent = parent;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Teleporter> getTeleporters() {
        return teleporters;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addContainedRegion(Region r) {
        if(r == this) throw new IllegalArgumentException("A region can't contain itself");
        r.parent = this;
        containedRegions.add(r);
    }

    public void addRegionTowards(Direction d, Region r) {
        regionOnDirection.put(d, r);
    }

    public void addTeleporter(Teleporter t) {
        if (!this.teleporters.contains(t)) {
            this.teleporters.add(t);
        }
    }

    public void addEntity(Entity e) {
        if (!this.entities.contains(e)) {
            this.entities.add(e);
        }

    }

    public boolean estAdjacente(Region re) {
        return this.regionOnDirection.containsValue(re);
    }

    public void addItemToInventory(Item item) {
        this.inventory.add(item);
        item.setLocation(this);
    }

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
