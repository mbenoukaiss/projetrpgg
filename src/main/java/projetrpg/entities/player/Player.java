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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    /**
     * The amount of mana the player has.
     */
    @Expose("mana")
    private int mana;

    /**
     * The amount of mana the player gets by
     * default (e.g. when he dies, he get this amount
     * of mana)
     */
    private int baseMana;

    /**
     * The abilities the player could learn (or has
     * learned), value tells if he learned them or not.
     */
    private Map<Ability, Boolean> abilities;

    /**
     * The damage it deals without any weapon.
     */
    private int baseDamage;

    /**
     * The quest this player is currently doing.
     */
    private Quest currentQuest;

    /**
     * Observers for the observer pattern.
     */
    private Set<IObserver> observers;

    /**
     * The enemy this player is fighting.
     */
    @SerializationIgnore
    private NPC enemy;

    public Player(String name, int experience, Region location, Item itemInHand, int hp, int baseDamage,
                  EntityType type, boolean isHostile, int maxCapacity, int mana) {
        super(name, location, type, isHostile, hp);
        this.experience = experience;
        this.itemInHand = itemInHand;
        this.baseDamage = baseDamage;
        abilities = new HashMap<>();
        inventory = new Inventory(maxCapacity);
        observers = new HashSet<>();
        enemy = null;
        this.mana = mana;
        this.baseMana = mana;
    }

    /**
     * Returns the current amount of mana.
     *
     * @return The mana
     */
    public int getMana() {
        return mana;
    }

    /**
     * Set the amount of mana.
     *
     * @param mana The new mana.
     */
    public void setMana(int mana) {
        this.mana = mana;
    }

    /**
     * Getter for the current enemy.
     *
     * @return The enemy.
     */
    public NPC getEnemy() {
        return enemy;
    }

    /**
     * Tells if this player is fighting or not.
     *
     * @return True if the player is fighting.
     */
    public boolean isInFight() {
        return enemy != null;
    }

    /**
     * Getter for the level of this player based
     * on its experience.
     *
     * @return The level.
     */
    public int getLevel() { return (int) (0.5 * Math.sqrt((double) experience)); }

    /**
     * Calculate the level based on the given
     * experience.
     *
     * @param exp The experience
     * @return The level.
     */
    private int getLevel(int exp) { return (int) (0.5 * Math.sqrt((double) exp)); }

    /**
     * Tells if the player will level up.
     *
     * @param otherXP The future amount of XP.
     * @return True if the player will level up
     */
    public boolean canLevelUp(int otherXP) {
        if (this.getLevel(this.experience+otherXP) > this.getLevel()) {
            this.experience+=otherXP;
            return true;
        }
        this.experience+=otherXP;
        return false;
    }

    /**
     * Getter for the player's inventory.
     *
     * @return The inventory.
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Getter for the item in the item in
     * the player's hand.
     *
     * @return The item in its hand.
     */
    public Item getItemInHand() {
        return this.itemInHand;
    }

    /**
     * Getter for the amount of damage this player
     * deals (base damage + item in hand).
     *
     * @return The true damage
     */
    public int getTrueDamage() {
        return this.baseDamage+this.itemInHand.getDamage();
    }

    /**
     * Getter for the current quest.
     *
     * @return The current quest.
     */
    public Quest getCurrentQuest() {
        return currentQuest;
    }

    /**
     * Changes the current quest.
     *
     * @param currentQuest The new quest.
     */
    public void setCurrentQuest(Quest currentQuest) {
        this.currentQuest = currentQuest;
        if (currentQuest != null) this.currentQuest.start();
    }

    @Override
    public String describe() {
        String d = this.name + " : possède ces items : ";
        d+=inventory.describe();
        d+=", se trouve dans la région de : " + this.location.getName();
        return d;
    }

    @Override
    public boolean damage(int value) {
        this.hps -= value;
        return (this.hps <= 0);
    }

    @Override
    public int getHp() {
        return this.hps;
    }

    @Override
    public boolean attack(Damageable target, int damage) {
        this.enemy = (NPC) target;

        if (target.damage(damage)) {
            this.enemy = null;
            return true;
        }
        return false;
    }

    @Override
    public int baseDamage() {
        return this.baseDamage;
    }

    /**
     * Adds an ability to the abilities.
     * It is not considered as "learned" by default.
     *
     * @param a The new ability
     */
    public void addAbbility(Ability a) {
        abilities.put(a, false);
    }

    /**
     * Getter for all the abilities this player
     * knows/learned.
     *
     * @return All the known abilities.
     */
    public Set<Ability> getAbilities() {
        return abilities.keySet().stream()
                .filter(a -> abilities.get(a))
                .collect(Collectors.toSet());
    }

    /**
     * Makes the player learn an ability.
     *
     * @param a The ability to try to learn
     * @return The message
     */
    //TODO: Change the return type?
    public String learn(Ability a) {
        if (!abilities.get(a)) {
            abilities.put(a, true);
            return "You learned this ability " + a.getName() + "!";
        }

        return "You have already learned this spell";
    }

    /**
     * Getter for all the abilities the player can
     * learn now, meaning he has the required level.
     *
     * @return All the learnable abilities.
     */
    public Set<Ability> learnableAbilities() {
        Set<Ability> ab = new HashSet<>();

        for (Ability a : this.abilities.keySet()) {
            if(!a.isLocked(getLevel()) && !abilities.get(a)) {
                ab.add(a);
            }
        }

        return ab;
    }

    /**
     * Pick up an item.
     *
     * @param i The item.
     */
    public void pickUp(Item i) {
        this.inventory.add(i);
        this.location.getInventory().remove(i);
    }

    /**
     * Ditch an item.
     *
     * @param i The item.
     */
    public void ditch(Item i) {
        this.location.getInventory().add(i);
        this.inventory.remove(i);
        if (this.itemInHand == i) this.itemInHand = null;
    }

    /**
     * Consume an item.
     *
     * @param i The item.
     */
    public void consume(Item i) {
        if (i.getType() == ItemType.FOOD) this.inventory.remove(i);
    }

    /**
     * Equip an item.
     *
     * @param i The item.
     */
    public void equip(Item i) {
        if (this.inventory.getAll().contains(i)) {
            this.itemInHand = i;
        }
    }

    /**
     * Unequip an item.
     *
     * @param i The item.
     */
    public void unequip(Item i) {
        if (this.itemInHand == i) {
            this.itemInHand = null;
        }
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

}
