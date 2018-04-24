package projetrpg.map;

import com.google.gson.annotations.JsonAdapter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents the whole map used in the game.
 */
@JsonAdapter(MapSerializer.class)
public class MainMap {

    /**
     * The name of the map.
     */
    private String name;

    /**
     * The region where the player spawns in.
     */
    private Region spawnPoint;

    /**
     * All the regions at the top of the tree.
     * In our case, solar systems.
     */
    private Collection<Region> regions;

    /**
     * The amount of humans left on this map.
     */
    private int humanCount;

    public MainMap(String name) {
        this.name = name;
        this.regions = new ArrayList<>();
    }

    public MainMap(String name, Region spawnPoint, Collection<Region> regions, int humanCount) {
        this.name = name;
        this.spawnPoint = spawnPoint;
        this.regions = regions;
        this.humanCount = humanCount;
    }

    public String getName() {
        return name;
    }

    public Collection<Region> getRegions() {
        return regions;
    }

    public int getHumanCount() {
        return humanCount;
    }

    public void addRegion(Region r) {
        regions.add(r);
    }

    /**
     * Accessor for the spawn point of this map.
     * @return the spawn point.
     */
    public Region getSpawnPoint() {
        return spawnPoint;
    }

    public void setSpawnPoint(Region spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    public void setHumanCount(int humanCount) {
        this.humanCount = humanCount;
    }
}
