package projetrpg.entities;

import com.google.gson.annotations.Expose;
import projetrpg.map.Region;

/**
 * Created by mhevin on 28/03/18.
 */
public abstract class Entity {

    protected final String name;

    protected Region location;

    protected EntityType type;

    protected int hps;
    
    private int baseHps;

    protected boolean isHostile;

    public Entity(String name, Region location, EntityType type, Boolean isHostile, int hps) {
        this.name = name;
        this.location = location;
        this.type = type;
        this.isHostile = isHostile;
        this.hps = hps;
        this.baseHps = hps;
    }

    public void setHps(int hps) {
        this.hps = hps;
    }

    public int getBaseHps() {
        return baseHps;
    }

    public String getName() {
        return name;
    }

    public Region getLocation() {
        return location;
    }

    public EntityType getType() {
        return type;
    }

    public void move(Region r) {
        this.location = r;
    }

    public boolean isInTheSameRegion(Entity other) {
        return (other.location.equals(this.location));
    }

}
