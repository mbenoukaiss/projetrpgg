package projetrpg.entities.player;

import projetrpg.entities.items.Item;

import java.util.ArrayList;
import java.util.List;

public enum ShipAmelioration {

    ENGINE_AMELIORATION("Improve the engine", 2, 1),
    RADAR_AMELIORATION("Improve the radar", 3, 2),
    REACTORS_AMELIORATION("Improve the reactors", 4, 3);

    private String description;
    private int levelAssociated;
    private int shipLevelNeeded;
    private List<Item> itemsNeeded;

    ShipAmelioration(String description, int levelAssociated, int levelNeeded) {
        this.description = description;
        this.levelAssociated = levelAssociated;
        this.shipLevelNeeded = levelNeeded;
        this.itemsNeeded = new ArrayList<>();
    }

    public String getDescription() {
        return description;
    }

    public int getLevelAssociated() {
        return levelAssociated;
    }

    public List<Item> getItemsNeeded() {
        return itemsNeeded;
    }

    public int getShipLevelNeeded() {
        return shipLevelNeeded;
    }

    public void addItemNeeded(Item i) {
        this.itemsNeeded.add(i);
    }

}
