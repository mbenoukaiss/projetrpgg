package projetrpg.entities.player;

import projetrpg.entities.items.Item;

import java.util.ArrayList;
import java.util.List;

public enum ShipAmelioration {

    ENGINE_AMELIORATION("Improve the engine", 2, 0),
    RADAR_AMELIORATION("Improve the radar", 3, 1),
    REACTORS_AMELIORATION("Improve the reactors", 4, 2);

    /**
     * The description of the amùelioration.
     */
    private String description;

    /**
     * The associated level
     */
    private int levelAssociated;

    /**
     * The ship level required.
     */
    private int shipLevelNeeded;

    /**
     * The items needed to apply this upgrade.
     */
    private List<Item> itemsNeeded;

    ShipAmelioration(String description, int levelAssociated, int levelNeeded) {
        this.description = description;
        this.levelAssociated = levelAssociated;
        this.shipLevelNeeded = levelNeeded;
        this.itemsNeeded = new ArrayList<>();
    }

    /**
     * Getter for the description.
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for the items needed
     * @return The items needed
     */
    public List<Item> getItemsNeeded() {
        return itemsNeeded;
    }


    /**
     * Getter for the ship level needed
     * @return The ship level needed
     */
    public int getShipLevelNeeded() {
        return shipLevelNeeded;
    }

}
