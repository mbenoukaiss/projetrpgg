package projetrpg.entities.items;

import projetrpg.Describable;

import java.util.ArrayList;

/**
 * Created by mhevin on 29/03/18.
 */
public class Inventory implements Describable {
    private int maxCapacity;
    private ArrayList<Item> items = new ArrayList<>();

    public Inventory(int maxCapacity, ArrayList<Item> items) {
        this.maxCapacity = maxCapacity;
        this.items = items;
    }

    @Override
    public String describe() {
        String d = "";
        for(Item i: this.items) {
            d+=i.getName() +", ";
        }
        String r = d.substring(0, d.length()-2);
        return r;
    }

    public void transferContent(Inventory destination) {
        destination.items.addAll(this.items);
        this.items.clear();
    }

    public Item get(int i) {
        return items.get(i);
    }

    public ArrayList<Item> getAll() {
        return this.items;
    }

    public void add(Item i) {
        items.add(i);
    }

    public void remove(Item i) {
        items.remove(i);
    }

    public void clear() {
        items.clear();
    }

    public int availableSize() {
        return maxCapacity-items.size();
    }

    public void increaseCapacity(int n) {
        this.maxCapacity += n;
    }
}
