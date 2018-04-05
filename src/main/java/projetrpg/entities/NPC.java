package projetrpg.entities;

import projetrpg.Describable;
import projetrpg.entities.items.Inventory;
import projetrpg.map.Region;

public class NPC extends Entity implements Describable, Damageable, Attacker {

    private Inventory inventory;

    private int baseDamage;
    
    private String dialogue;
    
    private boolean inFight;

    public NPC(String name, Region location, EntityType type, boolean isHostile, 
               int hps, int baseDamage, String dialogue) {
        super(name, location, type, isHostile, hps);
        this.baseDamage = baseDamage;
        this.dialogue = dialogue;
        location.addEntity(this);
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
        d+=". Si vous le tuer, vous obtiendrez " + this.type.getExperienceRewarded() + " exp";
        return d;
    }

    @Override
    public int baseDamage() {
        return this.baseDamage;
    }

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

    public boolean isInFight() {
        return inFight;
    }

    public void setInFight(boolean inFight) {
        this.inFight = inFight;
    }

    @Override
    public boolean attack(Damageable target) {
        return target.damage(this.baseDamage);
    }

    public void talk() {
        System.out.println(this.dialogue);
    }

}
