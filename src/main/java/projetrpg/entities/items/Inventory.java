package projetrpg.entities.items;

import projetrpg.Describable;

import java.util.ArrayList;

/**
 * Contains the items of an entity, a region
 * or anything else.
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

    public Inventory(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        items = new ArrayList<>();
    }

    @Override
    public String describe() {
        StringBuilder d = new StringBuilder();

        for(Item i: this.items) {
            d.append(i.getName()).append(", ");
        }

        return d.substring(0, d.length() - 2);
    }

    /**
     * Moves all the items from this inventory to
     * the other inventory.
     *
     * @param destination The receiver
     */
    public void transferContent(Inventory destination) {
        destination.items.addAll(this.items);
        this.items.clear();
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
        return this.items; //Return a copy ?
    }

    /**
     * Adds an item.
     *
     * @param i The item
     */
    public void add(Item i) {
        items.add(i);
    }


    /**
     * Removes an item.
     *
     * @param i The item
     */
    public void remove(Item i) {
        items.remove(i);
    }

    /**
     * Returns the amount of items that can be added.
     *
     * @return Available slots.
     */
    public int availableSlots() {
        return maxCapacity-items.size();
    }

    /**
     * Increase the capacity by n.
     *
     * @param n The amount of slots to add.
     */
    public void increaseCapacity(int n) {
        this.maxCapacity += n;
    }
}
