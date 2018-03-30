/**
 * Created by mhevin on 29/03/18.
 */
public class Item extends Entity implements Describable{
    private String name;
    private int damage;
    private ItemType type;
    private Region defaultLocation;

    public Item(String name, int damage, ItemType type, Region defaultLocation) {
        super(defaultLocation);
        this.name = name;
        this.damage = damage;
        this.type = type;
    }

    @Override
    public String describe() {
        return this.name + ", damages : " + this.damage + ", type : " + this.type;
    }

    public String getName() {
        return this.name;
    }

    public int getDamage() {
        return damage;
    }

}
