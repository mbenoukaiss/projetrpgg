package projetrpg.entities.player;

import projetrpg.*;
import projetrpg.entities.Attacker;
import projetrpg.entities.Damageable;
import projetrpg.entities.Entity;
import projetrpg.entities.items.Inventory;
import projetrpg.entities.items.Item;
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
     * The health of the player.
     */
    private int hp;

    /**
     * The damage it deals without any weapon.
     */
    private int baseDamage;

    public Player(String name, int experience, Region location, Inventory inventory, Item itemInHand, HashSet<Ability> abilities, int hp, int baseDamage) {
        super(name, location);
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

    @Override
    public boolean attack(Damageable target) {
        return target.damage(this.baseDamage + itemInHand.getDamage());
    }

    private void levelUp() {
        this.hp+=50;
        this.baseDamage++;
    }

    public void pickUp(Item i) {
        this.inventory.add(i);
    }

}
