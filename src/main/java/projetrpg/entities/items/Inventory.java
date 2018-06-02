package projetrpg.entities.items;

import projetrpg.Describable;

import java.util.ArrayList;

/**
 * Contains the items of an entity, a region
 * or anything else.
 *
 * @author mhevin
 * @author mbenoukaiss
 */
public class Inventory implements Describable {

    /**
     * Maximum capacity
     */
    private int maxCapacity;

    /**
     * Les items contenus
     */
    private ArrayList<Item> items;

    /**
     * Create an inventory.
     *
     * @param maxCapacity The capacity of this inventory
     */
    public Inventory(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.items = new ArrayList<>();
    }

    /**
     * Ask this object to describe itself.
     *
     * @return the description as a string.
     */
    @Override
    public String describe() {
        if(items.isEmpty()) return "";

        StringBuilder d = new StringBuilder();

        for(Item i: this.items) {
            d.append(i.getName()).append(", ");
        }

        return d.substring(0, d.length() - 2);
    }

    /**
     * Returns the nth item
     *
     * @param n The index of the item
     * @return Corresponding item.
     */
    public Item get(int n) {
        return items.get(n);
    }

    /**
     * Returns the list of the items.
     *
     * @return All the items.
     */
    public ArrayList<Item> getAll() {
        return (ArrayList<Item>) this.items.clone();
    }

    /**
     * Adds an item.
     *
     * @param i The item
     */
    public void add(Item i) {
        if(items.size() < maxCapacity) {
            items.add(i);
        }
    }

    /**
     * Removes an item.
     *
     * @param i The item
     */
    public void remove(Item i) {
        items.remove(i);
    }

}
