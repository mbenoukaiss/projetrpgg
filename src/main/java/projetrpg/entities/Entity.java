package projetrpg.entities;

import projetrpg.map.Region;

/**
 * Created by mhevin on 28/03/18.
 */
public abstract class Entity {

    protected final String name;
    protected Region location;

    public Entity(String name, Region location) {
        this.name = name;
        this.location = location;
    }


    public String getName() {
        return name;
    }

    public Region getLocation() {
        return location;
    }

    public void move(Region r) {
        this.location = r;
    }

    public boolean isInTheSameRegion(Entity other) {
        return (other.location.equals(this.location));
    }
}
