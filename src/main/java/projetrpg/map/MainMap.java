package projetrpg.map;

import com.google.gson.annotations.JsonAdapter;
import projetrpg.entities.player.Player;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents the whole map used in the game.
 *
 * @author mhevin
 * @author mbenoukaiss
 */
@JsonAdapter(MapSerializer.class)
public class MainMap {

    /**
     * The name of the map.
     */
    private String name;

    /**
     * The player.
     */
    private Player mainCharacter;

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

    /**
     * Used whenever we want to add a region
     * @return the region
     */
    public void addRegion(Region r) {
        regions.add(r);
    }

    /**
     * Accessor for the name of the main map
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name of this map
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Accessor for the main character
     * @return the name
     */
    public Player getMainCharacter() {
        return mainCharacter;
    }

    /**
     * Setter for the main character
     * @param mainCharacter the main character
     */
    public void setMainCharacter(Player mainCharacter) {
        this.mainCharacter = mainCharacter;
    }

    /**
     * Accessor for the regions of this main map.
     * @return the regions
     */
    public Collection<Region> getRegions() {
        return regions;
    }

    /**
     * Accessor for the human count
     * @return the count
     */
    public int getHumanCount() {
        return humanCount;
    }

    /**
     * Setter for the human count of this map.
     * @return the human count.
     */
    public void setHumanCount(int humanCount) {
        this.humanCount = humanCount;
    }

    /**
     * Accessor for the spawn point of this map.
     * @return the spawn point.
     */
    public Region getSpawnPoint() {
        return spawnPoint;
    }

    /**
     * Setter for the spawn point of this map.
     * @return the spawn point.
     */
    public void setSpawnPoint(Region spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

}
