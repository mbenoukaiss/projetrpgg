package projetrpg.entities.player;

import projetrpg.*;
import projetrpg.entities.*;
import projetrpg.entities.items.Inventory;
import projetrpg.entities.items.Item;
import projetrpg.entities.items.ItemType;
import projetrpg.game.Expose;
import projetrpg.map.Region;
import projetrpg.observer.IObservable;
import projetrpg.observer.IObserver;
import projetrpg.quest.Quest;
import projetrpg.utils.SerializationIgnore;

import java.util.HashSet;
import java.util.Set;

/**
 * The main character.
 *
 * @author mhevin
 * @author mbenoukaiss
 */
public class Player extends Entity implements Describable, Damageable, Attacker, IObserver, IObservable {

    /**
     * The experience of the player.
     */
    @Expose("xp")
    private int experience;

    /**
     * The inventory of the player.
     */
    private Inventory inventory;

    /**
     * The item in their main hand.
     */
    @Expose("item")
    private Item itemInHand;

    @Expose("mana")
    private int mana;

    private int baseMana;

    /**
     * Their abilities.
     */
    private HashSet<Ability> abilities;

    private Set<Ability> AllAbilities;


    /**
     * The damage it deals without any weapon.
     */
    private int baseDamage;

    /**
     * Whether or not this Player is fighting.
     */
    @SerializationIgnore
    private boolean isInFight;

    private Quest currentQuest;

    private Set<IObserver> observers;

    private NPC ennemy;

    public Player(String name, int experience, Region location, Item itemInHand, int hp, int baseDamage,
                  EntityType type, boolean isHostile, int maxCapacity, int mana) {
        super(name, location, type, isHostile, hp);
        this.experience = experience;
        this.itemInHand = itemInHand;
        this.baseDamage = baseDamage;
        abilities = new HashSet<>();
        inventory = new Inventory(maxCapacity);
        observers = new HashSet<>();
        this.AllAbilities = new HashSet<>();
        ennemy = null;
        this.mana = mana;
        this.baseMana = mana;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getExperience() {
        return this.experience;
    }

    public int getLevel() { return (int) (0.5 * Math.sqrt((double) experience)); }

    private int getLevel(int exp) { return (int) (0.5 * Math.sqrt((double) exp)); }

    public Inventory getInventory() {
        return inventory;
    }

    public Item getItemInHand() {
        return this.itemInHand;
    }

    public Quest getCurrentQuest() {
        return currentQuest;
    }

    public void setCurrentQuest(Quest currentQuest) {
        this.currentQuest = currentQuest;
        if (currentQuest != null) this.currentQuest.start();
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
        return (HashSet<Ability>) abilities.clone();
    }

    @Override
    public boolean attack(Damageable target, int damage) {
        this.isInFight = true;
        this.ennemy = (NPC) target;
        if (target.damage(damage)) {
                this.isInFight = false;
                this.ennemy = null;
                return true;
        }
        return false;
    }

    public NPC getEnnemy() {
        return ennemy;
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

    @Override
    public void update(IObservable a) {
        ((Quest) a).getReward().forEach(item -> this.inventory.add(item));
        this.currentQuest = null;
    }

    @Override
    public void addObserver(IObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(IObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        notifyObservers(null);
    }

    @Override
    public void notifyObservers(Object arg) {
        for (IObserver observer : observers) {
            observer.update(this);
        }
    }

    @Override
    public void clearObservers() {
        observers.clear();
    }

    public Boolean canLevelUp(int otherXP) {
        if (this.getLevel(this.experience+otherXP) > this.getLevel()) {
            this.experience+=otherXP;
            for (Ability a : this.AllAbilities) {
                if (a.getMinLevelRequired() <= this.getLevel()) {
                    a.setLocked(false);
                }
            }
            return true;
        }
        this.experience+=otherXP;
        return false;
    }
    public void addAbbility(Ability a) {
        this.AllAbilities.add(a);
    }

    public String learn(Ability a) {
        if (!this.abilities.contains(a)) {
            this.abilities.add(a);
            return "You learned this ability " + a.getName() + "!";
        }
        return "You have already learned this spell";
    }

    public Set<Ability> abilitiesAbleToLearn() {
        Set<Ability> ab = new HashSet<>();
        for (Ability a : this.AllAbilities) {
            if (!a.isLocked() && !this.abilities.contains(a)) {
                ab.add(a);
            }
        }
        return ab;
    }
}
