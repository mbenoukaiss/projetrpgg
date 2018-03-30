import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by mhevin on 28/03/18.
 */
public class Region implements Describable{

    private HashMap<Direction, Region> regionOnDirection = new HashMap<>();
    private HashSet<Region> containedRegions = new HashSet<>();
    private Region regionContainedIn;
    private String name;
    private ArrayList<Entity> entities = new ArrayList<>();
    private ArrayList<Teleporter> teleporters = new ArrayList<>();
    private Inventory inventory;

    public Region(Region regionContainedIn, String name, Inventory inventory) {
        this.regionContainedIn = regionContainedIn;
        this.name = name;
        this.inventory = inventory;
    }

    @Override
    public String describe() {
        String d = "Region : " + this.name + ", contains those items :";
        for(Item i: this.inventory.getAll()) {
            d+=i.getName() +", ";
        }
        String r = d.substring(0, d.length()-2);
        return r;
    }

    public Region getRegionTowards(Direction d) {
        return regionOnDirection.get(d);
    }

    public HashMap<Direction, Region> getRegionOnDirection() {
        return regionOnDirection;
    }

    public HashSet<Region> getContainedRegions() {
        return containedRegions;
    }

    public Region getRegionContainedIn() {
        return regionContainedIn;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public ArrayList<Teleporter> getTeleporters() {
        return teleporters;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
