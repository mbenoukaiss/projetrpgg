package projetrpg.commands;

import projetrpg.entities.Attacker;
import projetrpg.entities.Damageable;
import projetrpg.entities.Entity;
import projetrpg.entities.NPC;
import projetrpg.entities.items.Inventory;
import projetrpg.entities.items.Item;
import projetrpg.entities.player.Player;
import projetrpg.map.MainMap;
import projetrpg.map.Region;

import java.lang.reflect.Field;
import java.sql.SQLOutput;

public class CommandListener {

    private Player player;
    private MainMap map;

    public CommandListener(Player player, MainMap map) {
        this.map = map;
        this.player = player;
    }

    @Listener({"attack"})
    public void combat(Damageable e) {
        if (e != null) {
            System.out.println(((Entity) e).getName() + " : " + ((NPC) e).getDialogue());
            System.out.println("Hps of the ennemy before the assault : " + e.getHp());
            System.out.println("Your Hps before the attack : " + player.getHp());
            System.out.println("......\nHes attacking you!");
            if (this.player.attack(e)) {
                System.out.println("Congrats, you killed : " + ((Entity) e).getName() + ", you won " +
                        ((Entity) e).getType().getExperienceRewarded() + " exp! You are level " + this.player.getLevel());
            } else if (((NPC) e).attack(this.player)) {
                ((NPC) e).setHps(((NPC) e).getBaseHps());
                System.out.println("You died, you have been redirected to the spawn point : " + map.getSpawnPoint()
                        .getName()+ ".");
                this.player.setLocation(map.getSpawnPoint());
                System.out.println("Vous can now go to :" + this.player.getLocation().getRegionNamesOnDirection());
                ((NPC) e).setInFight(false);
                this.player.setInFight(false);
                this.player.setHps(this.player.getBaseHps());
            } else {
                System.out.println("Hps of the ennemy after the assault : " + e.getHp());
                System.out.println("Your Hps after the attack : " + player.getHp());
                System.out.println("Vous may now flee or keep on fighting.");
            }
        } else {
            System.out.println("Error : check if this entity is in your location.");
        }
    }

    /**
     * This method is called whenever the user wishes to see a stat
     */
    @Listener({"see"})
    public void see(String arg) {
        if (arg != null) { // If the attribute was found
            for (Field field : player.getClass().getDeclaredFields()) { // go through all the player class attributes
                String name = field.getName();
                if (name.equalsIgnoreCase(arg)) {
                    field.setAccessible(true);
                    try {
                        if (field.getType() == Inventory.class) {
                            System.out.println(this.player.getInventory().describe());
                        } else if (field.getType() == Item.class) {
                            System.out.println(this.player.getItemInHand().getName());
                        } else {
                            System.out.println(field.get(player));
                        }
                    } catch (IllegalAccessException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
            for (Field f : player.getClass().getSuperclass().getDeclaredFields()) { // go through all the entity class attributes
                String name = f.getName();
                if (name.equalsIgnoreCase(arg)) {
                    f.setAccessible(true);
                    try {
                        if (f.getType() == Region.class) {
                            System.out.println(this.player.getLocation().getName());
                        } else {
                            System.out.println(f.get(player));
                        }
                    } catch (IllegalAccessException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
        } else { // If the attribute was not found
            System.out.println("Error : make sure you typed the right attribute to see.");
        }
    }

    @Listener({"talk"})
    public void talk(NPC e) {
        if (!this.player.isInFight()) {
            if (e != null) {
                System.out.println(e.getDialogue());
            }
            else {
                System.out.println("Error : check if this entity is in your location.");
            }
        } else {
            System.out.println("Error : You can only fight or flee.");
        }
    }

    @Listener({"fleefrom"})
    public void fleeFrom(NPC e) {
        if (e != null && e.isInFight()) {
            e.setHps(e.getBaseHps());
            System.out.println("You fled from " + e.getName() + ".");
            e.setInFight(false);
            this.player.setInFight(false);
        } else {
            System.out.println("Error : check if you are in fight with this entity.");
        }
    }

    @Listener({"use"})
    public void use(Item e) {
        if (!this.player.isInFight()) {
            if (e != null) {
                switch (e.getType()) {
                    case DMG:
                        System.out.println("You cant use an item without a target, attack something instead ! ");
                        break;
                    case FOOD:
                        System.out.println("You ate : " + e.getName() + ", + " + e.getType().getHpGiven() + " HPS");
                        this.player.setHps((this.player.getHp() + e.getType().getHpGiven() > this.player.getBaseHps()) ? 100 :
                                this.player.getHp() + e.getType().getHpGiven());
                        this.player.consume(e);
                        System.out.println("You now have " + this.player.getHp() + " HPS.");
                        break;
                    case UTILS:
                        System.out.println("You used : " + e.getName());
                        break;
                    default:
                        break;
                }
            } else {
                System.out.println("Error : check if you have this item in your inventory.");
            }
        } else {
            System.out.println("Error : You can only fight or flee.");
        }
    }

    @Listener({"move"})
    public void move(Region r) {
        if (!this.player.isInFight()) {
            if (r != null) {
                System.out.println("Initial region : " + this.player.getLocation().getName());
                this.player.move(r);
                System.out.println("Actual region : " + this.player.getLocation().getName() + ". "
                        + r.describe());
                System.out.println("Vous can now go to :" + this.player.getLocation().getRegionNamesOnDirection());
            } else {
                System.out.println("Error : check if this location is connected to your location.");
            }
        } else {
            System.out.println("Error : You can only fight or flee.");
        }
    }

    @Listener({"pickup"})
    public void pick(Item i) {
        if (!this.player.isInFight()) {
            if (i != null) {
                System.out.println("You picked up " + i.getName() + ".");
                this.player.pickUp(i);
            } else {
                System.out.println("Error : check if this item is in your location.");
            }
        } else {
            System.out.println("Error : You can only fight or flee.");
        }
    }

    @Listener({"equip"})
    public void equip(Item i) {
        if (!this.player.isInFight()) {
            if (i != null) {
                System.out.println("Previously equipped item : " + ((this.player.getItemInHand() == null) ? "Nothing" : this.player.getItemInHand().getName()));
                this.player.equip(i);
                System.out.println("Now equipped item : " + ((this.player.getItemInHand() == null) ? "Nothing" : this.player.getItemInHand().getName()));
            } else {
                System.out.println("Error : check if you have this item in your inventory.");
            }
        } else {
            System.out.println("Error : You can only fight or flee.");
        }
    }

    @Listener({"ditch"})
    public void ditch(Item i) {
        if (!this.player.isInFight()) {
            if (i != null) {
                System.out.println("You ditched " + i.getName() + ".");
                this.player.ditch(i);
            } else {
                System.out.println("Error : check if you have this item in your inventory.");
            }
        } else {
            System.out.println("Error : You can only fight or flee.");
        }
    }

    @Listener({"unequip"})
    public void unequip(Item i) {
        if (!this.player.isInFight()) {
            if (i != null) {
                System.out.println("You unequipped " + i.getName() + ".");
                this.player.unequip(i);
            } else {
                System.out.println("Error : check if you have the correct item equipped.");
            }
        } else {
            System.out.println("Error : You can only fight or flee.");
        }
    }
}

