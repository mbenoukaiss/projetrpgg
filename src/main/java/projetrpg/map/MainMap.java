package projetrpg.map;

import java.util.ArrayList;

/**
 * Represents the whole map used in the game.
 */
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
    private ArrayList<Region> regions = new ArrayList<>();

    /**
     * The amount of humans left on this map.
     */
    private int humanCount;

    public MainMap(String name, Region spawnPoint, ArrayList<Region> regions, int humanCount) {
        this.name = name;
        this.spawnPoint = spawnPoint;
        this.regions = regions;
        this.humanCount = humanCount;
    }

    public String getName() {
        return name;
    }

    public Region getSpawnPoint() {
        return spawnPoint;
    }

    public ArrayList<Region> getRegions() {
        return regions;
    }

    public int getHumanCount() {
        return humanCount;
    }
}
