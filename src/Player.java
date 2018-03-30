import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by mhevin on 28/03/18.
 */
public class Player extends Entity implements Describable, Damageable, Attacker{
    private String name;
    private int experience;
    private Region location;
    private ArrayList<Entity> nearbyEntities = new ArrayList<>();
    private Inventory inventory;
    private Item itemInHand;
    private HashSet<Ability> abilities = new HashSet<>();
    private int hp;
    private int baseDamage;
    private int trueDamage = baseDamage + itemInHand.getDamage();

    public Player(String name, int experience, Region location, Inventory inventory, Item itemInHand, HashSet<Ability> abilities, int hp, int baseDamage) {
        super(location);
        this.name = name;
        this.experience = experience;
        this.inventory = inventory;
        this.itemInHand = itemInHand;
        this.abilities = abilities;
        this.hp = hp;
        this.baseDamage = baseDamage;
    }

    public int getExperience() {
        return this.experience;
    }

    public int getLevel() {
        return (int) ((this.experience*this.experience)*0.04);
    }

    public Item getItemInHand() {
        return this.itemInHand;
    }

    @Override
    public int getHp() {
        return this.hp;
    }

    @Override
    public Region getLocation() {
        return this.location;
    }

    @Override
    public String describe() {
        String d = this.name + " : possède ces items : ";
        d+=inventory.describe();
        d+=", se trouve dans la région de : " + this.location.getName();
        return d;
    }

    @Override
    public int baseDamage() {
        return this.baseDamage;
    }

    @Override
    public boolean damage(int value) {
        this.hp-=value;
        return (this.hp <= 0);
    }

    public HashSet<Ability> getAbilities() {
        return abilities;
    }

    public ArrayList<Entity> getNearbyEntities() {
        return this.nearbyEntities;
    }

    @Override
    public boolean attack(Damageable target) {
        return (target.damage(this.trueDamage));
    }

    public void levelUp() {
        this.hp+=50;
    }

    public void pickUp(Item i) {
        this.inventory.add(i);
    }

}
