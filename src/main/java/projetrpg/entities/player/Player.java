package projetrpg.entities.player;

import projetrpg.*;
import projetrpg.Quest.Objectiv;
import projetrpg.Quest.Quest;
import projetrpg.entities.Attacker;
import projetrpg.entities.Damageable;
import projetrpg.entities.Entity;
import projetrpg.entities.EntityType;
import projetrpg.entities.items.Inventory;
import projetrpg.entities.items.Item;
import projetrpg.entities.items.ItemType;
import projetrpg.map.Region;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by mhevin on 28/03/18.
 */
public class Player extends Entity implements Describable, Damageable, Attacker {

    /**
     * The experience of the player.
     */
    private int experience;

    /**
     * The inventory of the player.
     */
    private Inventory inventory;

    /**
     * The item in their main hand.
     */
    private Item itemInHand;

    /**
     * Their abilities.
     */
    private HashSet<Ability> abilities;

    /**
     * The damage it deals without any weapon.
     */
    private int baseDamage;

    private ArrayList<Objectiv> currentObjectivs;

    private Quest quest;

    /**
     * Whether or not this Player is fighting.
     */
    @SerializationIgnore
    private boolean isInFight;

    public Player(String name, int experience, Region location, Item itemInHand, int hp, int baseDamage, EntityType type, boolean isHostile, int maxCapacity) {
        super(name, location, type, isHostile, hp);
        this.experience = experience;
        this.itemInHand = itemInHand;
        this.baseDamage = baseDamage;
        abilities = new HashSet<>();
        inventory = new Inventory(maxCapacity);
        currentObjectivs = new ArrayList<>();
    }

    public int getExperience() {
        return this.experience;
    }

    public void earnExperience(int e) {
        this.experience+=e;
    }

    public int getLevel() { return (int) ((this.experience*this.experience)*0.03); }

    public Inventory getInventory() {
        return inventory;
    }

    public Item getItemInHand() {
        return this.itemInHand;
    }

    public Quest getQuest() {
        return quest;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getHp() {
        return this.hps;
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

    public int gettrueDamage() {
        return this.baseDamage+this.itemInHand.getDamage();
    }

    @Override
    public boolean damage(int value) {
        this.isInFight = true;
        this.hps-=value;
        return (this.hps <= 0);
    }

    public boolean isInFight() {
        return isInFight;
    }

    public void setInFight(boolean inFight) {
        isInFight = inFight;
    }

    public HashSet<Ability> getAbilities() {
        return abilities;
    }

    @Override
    public boolean attack(Damageable target) {
        this.isInFight = true;
        if (target.damage(this.baseDamage + ((itemInHand == null)? 0 : itemInHand.getDamage()))) {
                this.experience+=((Entity) target).getType().getExperienceRewarded();
                this.isInFight = false;
                return true;
        }
        return false;
    }

    public void pickUp(Item i) {
        this.inventory.add(i);
        this.location.getInventory().remove(i);
    }

    public void ditch(Item i) {
        this.location.getInventory().add(i);
        this.inventory.remove(i);
        if (this.itemInHand == i) this.itemInHand = null;
    }

    public void consume(Item i) {
        if (i.getType() == ItemType.FOOD) this.inventory.remove(i);
    }

    public void equip(Item i) {
        if (this.inventory.getAll().contains(i)) {
            this.itemInHand = i;
        }
    }

    public void unequip(Item i) {
        if (this.itemInHand == i) {
            this.itemInHand = null;
        }
    }

    public void addItemToInventory(Item item) {
        this.inventory.add(item);
    }

    public void addAbilityToAbilities(Ability ability) {
        this.abilities.add(ability);
    }

    public ArrayList<Objectiv> getCurrentObjectivs() {
        return currentObjectivs;
    }

    public void setCurrentObjectivs(ArrayList<Objectiv> currentObjectivs) {
        this.currentObjectivs = currentObjectivs;
    }

    public void removeObjectiv(Objectiv o) {
        this.currentObjectivs.remove(o);
    }
}
