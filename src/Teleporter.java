import java.util.ArrayList;

/**
 * Created by mhevin on 29/03/18.
 */
public class Teleporter {
    private String name;
    private Teleporter linkedTeleporter;
    private boolean isRepaired;
    private Region region;
    private ArrayList<Item> itemsNeededToRepair = new ArrayList<>();

    public Teleporter(String name, Region region) {
        this.name = name;
        this.region = region;
        this.isRepaired = false;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Item> getItemsNeededToRepair() {
        return this.itemsNeededToRepair;
    }

    public Teleporter getLinkedTeleporter() {
        return this.linkedTeleporter;
    }

    public boolean isRepaired() {
        return this.isRepaired;
    }

    public Region getRegion() {
        return this.region;
    }

    public boolean repair() {
        if (!this.isRepaired) {
            this.isRepaired = true;
            return true;
        } return false;
    }

    public void lier(Teleporter other) {
        this.linkedTeleporter = other;
        other.linkedTeleporter = this;
    }

    public Region getLinkedRegion() {
        return this.linkedTeleporter.region;
    }
}
