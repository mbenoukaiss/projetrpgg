package projetrpg.entities;

import projetrpg.map.Region;

/**
 * Created by mhevin on 28/03/18.
 */
public abstract class Entity {

    private Region location;

    public Entity(Region location) {
        this.location = location;
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
