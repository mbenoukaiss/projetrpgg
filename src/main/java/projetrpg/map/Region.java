package projetrpg.map;

import com.google.gson.annotations.JsonAdapter;
import projetrpg.Describable;
import projetrpg.entities.NPC;
import projetrpg.entities.items.Inventory;
import projetrpg.entities.items.Item;
import projetrpg.entities.player.Ship;

import java.util.*;

/**
 * Represents any kind of location, a solar system,
 * a planet, an area on a planet, a spaceship...
 *
 * @author mhevin
 * @author mbenoukaiss
 */
@JsonAdapter(RegionSerializer.class)
public class Region implements Describable {

    /**
     * The default capacity of a room.
     */
    private static final int DEFAULT_ROOM_ITEM_CAPACITY = 10;

    /**
     * Id of the region
     */
    protected int id;

    /**
     * Name of the region
     */
    protected String name;
    
    /**
     * The parent region, containing this region.
     */
    protected Region parent;

    /**
     * The adjacent regions.
     */
    protected Map<Direction, Region> regionOnDirection;

    /**
     * The regions DIRECTLY contained in this region.
     */
    protected Set<Region> containedRegions;

    /**
     * All the entities DIRECTLY contained in this region.
     */
    protected Set<NPC> entities;

    /**
     * All the teleporters DIRECTLY contained in this region.
     */
    protected Set<Teleporter> teleporters;

    /**
     * The inventory of this region : all the items DIRECTLY
     * containe in this region..
     */
    protected Inventory inventory;

    /**
     *  The items needed in order to enter this region.
     */
    protected List<Item> itemsNeeded;

    /**
     * The player's ship's level needed
     */
    protected int shipLevelRequired;

    Region() {
        this.regionOnDirection = new HashMap<>();
        this.containedRegions = new HashSet<>();
        this.entities = new HashSet<>();
        this.teleporters = new HashSet<>();
        this.inventory = new Inventory(DEFAULT_ROOM_ITEM_CAPACITY);
        this.itemsNeeded = new ArrayList<>();
    }

    public Region(int id, String name, Region parent, int shipLevelRequired) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.regionOnDirection = new HashMap<>();
        this.containedRegions = new HashSet<>();
        this.entities = new HashSet<>();
        this.teleporters = new HashSet<>();
        this.inventory = new Inventory(DEFAULT_ROOM_ITEM_CAPACITY);
        this.itemsNeeded = new ArrayList<>();
        this.shipLevelRequired = shipLevelRequired;

        if(parent != null)
            parent.addContainedRegion(this);
    }

    public Region(Region other) {
        System.out.println(other.containedRegions);
        this.id = other.id;
        this.name = other.name;
        this.parent = other.parent;
        this.regionOnDirection = other.regionOnDirection;
        this.containedRegions = other.containedRegions;
        this.entities = other.entities;
        this.teleporters = other.teleporters;
        this.inventory = other.inventory;
        this.itemsNeeded = other.itemsNeeded;
        this.shipLevelRequired = other.shipLevelRequired;
    }

    /**
     * Accessor for the id of the region.
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Accessor for the contained regions.
     * @return the regions
     */
    public Set<Region> getContainedRegions() {
        return new HashSet<>(containedRegions);
    }

    /**
     * Accessor for the parent
     * @return the parent
     */
    public Region getParent() {
        return parent;
    }

    /**
     * Accessor for the contained teleporters.
     * @return the teleporters
     */
    public Set<Teleporter> getTeleporters() {
        return new HashSet<>(teleporters);
    }

    /**
     * Ask this object to describe itself.
     * @return the description as a string.
     */
    @Override
    public String describe() {
        String d = "Region : " + this.name;
        if (!this.inventory.getAll().isEmpty()) {
            d+=", contains those items : ";
            for(Item i: this.inventory.getAll()) {
                d+=i.getName() +", ";
            }
            d = d.substring(0, d.length()-2);
        }
        if (!this.entities.isEmpty()) {
            d += ". Contains those entities :";
            for (NPC e : this.entities) {
                d += e.getName() + ", ";
            }
            d = d.substring(0, d.length()-2);
        }
        if (!this.getRegionNamesOnDirection().isEmpty()) {
            d += ". From there you can go to : " + (this.getRegionNamesOnDirection())
                    + ((this.getContainedRegions().isEmpty()) ? "" : "," + this.getContainedRegionsNames()) + "."
                    + " And your ship aswell.";
        } else if (this.parent != null && (this.parent instanceof Planet && ((Planet) this.parent).getLandingRegion() != this)){
            d += ". From there you can go to : "
                    + this.parent.getName()
                    + ((this.getContainedRegions().isEmpty()) ? "" : "," + this.getContainedRegionsNames()) + "."
                    + " And your ship aswell.";
        }
        if (!this.teleporters.isEmpty()) {
            d += " Contains those teleporters :";
            for (Teleporter t : this.teleporters) {
                d += t.getName() + ", ";
            }
            d = d.substring(0, d.length()-2);
        }
        if (!itemsNeeded.isEmpty()) {
            d+=" You'll need those items in order to go there : ";
            for (Item i : this.itemsNeeded) {
                d+= i.getName() + ",";
            }
            d = d.substring(0, d.length()-1);
        }
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
        if (r.length() > 0) r = r.substring(0, r.length() -1);
        return r;
    }

    /**
     * Accessor for the names of the linked regions as a String.
     * @return the names.
     */
    public String getContainedRegionsNames() {
        String r ="";
        for(Region i : this.containedRegions) {
            r += i.name + ",";
        }
        if (r.length() > 0) r = r.substring(0, r.length() -1);
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
    public Set<NPC> getEntities() {
        return new HashSet<>(entities);
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
     * Used to add a region into the contained ones.
     * @param r the region
     */
    public void addContainedRegion(Region r) {
        if(r == this) throw new IllegalArgumentException("A region can't contain itself");
        r.parent = this;
        containedRegions.add(r);
    }

    /**
     * Used to add a region with a direction
     * @param r the region
     */
    public void addRegionTowards(Direction d, Region r) {
        regionOnDirection.put(d, r);
    }

    /**
     * Add an entity to this region.
     * @param e the entity.
     */
    public void addEntity(NPC e) {
        this.entities.add(e);
        e.setLocation(this);
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
        this.teleporters.add(t);
        t.location = this;
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

    public List<Item> getItemsNeeded() {
        return itemsNeeded;
    }

    public void addItemNeeded(Item item) {
        if (!this.itemsNeeded.contains(item)) this.itemsNeeded.add(item);
    }

    public int getShipLevelRequired() {
        return shipLevelRequired;
    }


    public Region getRoot() {
        Region reg = this;
        while(reg.getParent() != null) {
            reg = reg.getParent();
            if (reg.getParent() == null) {
                return reg;
            }
        }
        return null;
    }
}
