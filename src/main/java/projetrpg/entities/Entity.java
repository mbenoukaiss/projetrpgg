package projetrpg.entities;
import projetrpg.game.Expose;
import projetrpg.utils.SerializationIgnore;
import projetrpg.map.Region;

/**
 * An entity.
 *
 * @author mhevin
 * @author mbenoukaiss
 */
public abstract class Entity {

    /**
     * The name of the entity.
     */
    protected String name;

    /**
     * The location of the entity.
     */
    @SerializationIgnore
    @Expose("location")
    protected Region location;

    /**
     * The type of the entity.
     */
    protected EntityType type;

    /**
     * The hps of the entity.
     */
    @Expose("health")
    protected int hps;

    /**
     * The baseHps of the entity.
     */
    protected int baseHps;

    /**
     * Whether or not this entity is hostile.
     */
    protected boolean isHostile;

    public Entity(String name, Region location, EntityType type, Boolean isHostile, int hps) {
        this.name = name;
        this.location = location;
        this.type = type;
        this.isHostile = isHostile;
        this.hps = hps;
        this.baseHps = hps;
    }

    /**
     *
     * @param location
     */
    public void setLocation(Region location) {
        this.location = location;
    }

    /**
     * Setter for the hps of an entity.
     * @param hps the entity hps.
     */
    public void setHps(int hps) {
        this.hps = hps;
    }

    /**
     * Accessor for the base hp of an entity.
     * @return the base hps.
     */
    public int getBaseHps() {
        return baseHps;
    }

    /**
     * Accessor for the name of the Entity.
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Accessor for the location of the Entity.
     * @return  the location.
     */
    public Region getLocation() {
        return location;
    }

    /**
     * Accessor for the type of the Entity.
     * @return the type.
     */
    public EntityType getType() {
        return type;
    }

    /**
     * The player moves to another region.
     * @param r the targeted region.
     */
    public void move(Region r) {
        this.location = r;
    }

    public boolean isHostile() {
        return isHostile;
    }
}
