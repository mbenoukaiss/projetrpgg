package projetrpg.map;

import com.google.gson.annotations.JsonAdapter;

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

    public MainMap(String name, Region spawnPoint, Collection<Region> regions) {
        this.name = name;
        this.spawnPoint = spawnPoint;
        this.regions = regions;
    }

    /**
     * Accessor for the spawn point of this map.
     * @return the spawn point.
     */
    public Region getSpawnPoint() {
        return spawnPoint;
    }
}
