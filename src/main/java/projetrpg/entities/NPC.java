package projetrpg.entities;

import projetrpg.Describable;
import projetrpg.map.Region;
import projetrpg.observer.IObservable;
import projetrpg.observer.IObserver;
import projetrpg.utils.SerializationIgnore;

import java.util.Set;

/**
 * An entity which can interact with the player.
 *
 * @author mhevin
 * @author mbenoukaiss
 */
public class NPC extends Entity implements Describable, Damageable, Attacker, IObservable {

    /**
     * The highest ID for the NPC.
     * Necessary for deserialization.
     */
    private static int currentId = 0;

    /**
     * The id of this NPC.
     */
    private int id;

    /**
     * The damage.
     */
    private int baseDamage;

    /**
     * The dialogue.
     */
    private String dialogue;

    /**
     * Whether or not this NPC is fighting.
     */
    @SerializationIgnore
    private boolean inFight;

    /**
     * Observers for the observer pattern.
     */
    @SerializationIgnore
    private Set<IObserver> observers;

    public NPC(String name, Region location, EntityType type, boolean isHostile,
               int hps, int baseDamage, String dialogue) {
        super(name, location, type, isHostile, hps);
        this.baseDamage = baseDamage;
        this.dialogue = dialogue;
        location.addEntity(this);

        id = currentId++;
    }

    /**
     * Accessor for the id of the NPC.
     * @return the id.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for the id of the npc.
     * @param id the id.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Accessor for the dialogue of the NPC.
     * @return the dialogue.
     */
    public String getDialogue() { return this.dialogue; }

    /**
     * Accessor for the hp of the NPC.
     * @return the hp.
     */
    @Override
    public int getHp() {
        return this.hps;
    }

    /**
     * Accessor for the location.
     * @return the location.
     */
    @Override
    public Region getLocation() {
        return this.location;
    }

    /**
     * Ask this object to describe itself.
     * @return the description as a string.
     */
    @Override
    public String describe() {
        String d = this.name;
        d+=", can be found in the " + this.location.getName() + " region";
        d+=". If you kill him you'll gain " + this.type.getExperienceRewarded() + " exp.";
        d+=" This NPC is a : " + this.type + ".";
        d+= ((this.isHostile)? " Beware, this NPC is hostile !" : " This NPC is not hostile at all.");
        return d;
    }

    /**
     * Accessor for the base damage of the NPC.
     * @return the base damage.
     */
    @Override
    public int baseDamage() {
        return this.baseDamage;
    }

    /**
     * Whenever an NPC takes damage from outside.
     * @param value the damage took.
     * @return if the player died from the attack.
     */
    @Override
    public boolean damage(int value) {
        if (this.type.isKillable()) {
            this.hps -= value;
            this.inFight = true;
            if (this.hps <= 0) {
                this.location.deleteEntity(this);
                return true;
            } return false;
        } else {
            return false;
        }
    }

    /**
     * Accessor for the status on whether or not the NPC is fighting.
     * @return the status.
     */
    public boolean isInFight() {
        return inFight;
    }

    /**
     * Setter for the status on whether or not the NPC is fighting.
     * @param inFight the status.
     */
    public void setInFight(boolean inFight) {
        this.inFight = inFight;
    }

    /**
     * Whenever the NPC attacks a Damageable.
     * @return true if the attack killed the target, false if not.
     */
    @Override
    public boolean attack(Damageable target, int damage) {
        return target.damage(this.baseDamage);
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

    /**
     * Changes the highest ID.
     *
     * @param currentId The new highest ID.
     */
    public static void setCurrentId(int currentId) {
        NPC.currentId = currentId;
    }
}
