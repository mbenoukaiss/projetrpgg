package projetrpg.entities;

import projetrpg.Describable;
import projetrpg.entities.items.Inventory;
import projetrpg.map.Region;

public class NPC extends Entity implements Describable, Damageable, Attacker {

    private int experienceRewarded;
    private Inventory inventory;
    private int baseDamage;
    private boolean isKillable;
    protected String dialogue;

    public NPC(String name, Region location, EntityType type, Boolean isHostile, int hps,
               int baseDamage, boolean isKillable, String dialogue) {
        super(name, location, type, isHostile, hps);
        this.baseDamage = baseDamage;
        this.isKillable = isKillable;
        location.addEntity(this);
        this.dialogue = dialogue;
    }

    public String getDialogue() { return this.dialogue; }



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
        d+=". Si vous le tuer, vous obtiendrez " + this.experienceRewarded + " exp";
        return d;
    }

    @Override
    public int baseDamage() {
        return this.baseDamage;
    }

    @Override
    public boolean damage(int value) {
        if (this.isKillable) {
            this.hps -= value;
            return (this.hps <= 0);
        } else {
            return false;
        }
    }

    @Override
    public boolean attack(Damageable target) {
        return target.damage(this.baseDamage);
    }

    public void talk() {
        System.out.println(this.dialogue);
    }

}
