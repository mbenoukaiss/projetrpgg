import java.util.ArrayList;

/**
 * Created by mhevin on 29/03/18.
 */
public class mainMap {
    private String name;
    private Region spawnPoint;
    private ArrayList<Region> regions = new ArrayList<>();
    private int humanCount;

    public mainMap(String name, Region spawnPoint, ArrayList<Region> regions, int humanCount) {
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
}
