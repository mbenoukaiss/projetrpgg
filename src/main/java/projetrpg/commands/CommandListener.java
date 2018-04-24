package projetrpg.commands;

import projetrpg.Describable;
import projetrpg.Partie;
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


/**
 * Created by mhevin + mbnoukaiss
 *
 * Represents the listener of the different methods.
 */
public class CommandListener {

    /**
     * The player.
     */
    private Player player;

    /**
     * The map.
     */
    private MainMap map;

    private Partie partie;

    public CommandListener(Player player, MainMap map, Partie partie) {
        this.player = player;
        this.map = map;
        this.partie = partie;
    }

    /**
     * This method is called whenever the user wishes to see the user guide
     */
    @Listener({"help"})
    public void help(String arg) {
        if (arg != null) { // If the user typed the right argument
            System.out.println(partie.getManuel());
        } else { // If the user typed the wrong argument
            System.out.println("Type \"Help me\" to see the user guide.");
        }
    }


    /**
     * This method is called whenever the user wishes to see a stat
     */
    @Listener({"see"})
    public void see(Field field) {
        if (field != null) { // If the attribute was found
            field.setAccessible(true);
            try {
                Object value = field.get(this.player);
                if (value == null) {
                    System.out.println("You have nothing equipped.");
                } else if (value instanceof Region) {
                    System.out.println((((Region) value).getName()));
                } else if (value instanceof Item) {
                    System.out.println(((Item) value).getName());
                } else if (value instanceof Inventory) {
                    System.out.println(((Inventory) value).describe());
                } else {
                    System.out.println(value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else { // If the attribute was not found
            System.out.println("Error : make sure you typed the right attribute to see.");
        }
    }

    /**
     * This method is called whenever the user wishes to have infos on something.
     */
    @Listener({"describe"})
    public void describe(Object o) {
        if (o != null) {
            if (o instanceof Region) {
                System.out.println(this.player.getLocation().describe());
            } else if (o instanceof Entity) {
                System.out.println(((Describable) o).describe());
            } else if (o instanceof Item) {
                System.out.println(((Item) o).describe());
            }
        } else {
            System.out.println("Error : Check if this entity is present in your area, by typing 'describe location'.");
        }
    }

    /**
     * This method is called whenever the user wishes to attack a damageable.
     */
    @Listener({"attack"})
    public void combat(Damageable e) {
        if (e != null) { // If the damageable targeted can be attacked
            System.out.println(((Entity) e).getName() + " : " + ((NPC) e).getDialogue());
            System.out.println("Hps of the ennemy before the assault : " + e.getHp());
            System.out.println("Your Hps before the attack : " + player.getHp());
            System.out.println("......\nHes attacking you!");
            if (this.player.attack(e)) { // If the attack kills the target
                System.out.println("Congrats, you killed : " + ((Entity) e).getName() + ", you won " +
                        ((Entity) e).getType().getExperienceRewarded() + " exp! You are level " + this.player.getLevel());
            } else if (((NPC) e).attack(this.player)) { // If the target kills the player
                ((NPC) e).setHps(((NPC) e).getBaseHps());
                System.out.println("You died, you have been redirected to the spawn point : " + map.getSpawnPoint()
                        .getName()+ ".");
                this.player.setLocation(map.getSpawnPoint());
                System.out.println("You can now go to :" + this.player.getLocation().getRegionNamesOnDirection());
                ((NPC) e).setInFight(false);
                this.player.setInFight(false);
                this.player.setHps(this.player.getBaseHps());
            } else { // The fight is still going on
                System.out.println("Hps of the ennemy after the assault : " + e.getHp());
                System.out.println("Your Hps after the attack : " + player.getHp());
                System.out.println("You may now flee,keep on fighting or equip an item.");
            }
        } else { // If the damageable targeted cannot be attacked
            System.out.println("Error : check if this entity is in your location.");
        }
    }

    /**
     * This method is called whenever the user wishes to talk to an NPC.
     */
    @Listener({"talk"})
    public void talk(NPC e) {
        if (!this.player.isInFight()) { // If the player isnt fighting
            if (e != null) {// If the target can be talked to
                System.out.println(e.getDialogue());
            }
            else { // If the target cannot be talked to
                System.out.println("Error : check if this NON-Hostile entity is in your location.");
            }
        } else { // If the player is still fighting
            System.out.println("Error : You can only fight or flee.");
        }
    }

    /**
     * This methode is called whenever the user wishes to leave a fight he is engaged in.
     */
    @Listener({"fleefrom"})
    public void fleeFrom(NPC e) {
        if (e != null && e.isInFight()) { // If the NPC exists and is in fight with the player
            e.setHps(e.getBaseHps());
            System.out.println("You fled from " + e.getName() + ".");
            e.setInFight(false);
            this.player.setInFight(false);
        } else { // If the player is not fighting this entity
            System.out.println("Error : check if you are in fight with this entity.");
        }
    }

    /**
     * This method is called whenever the user wishes to use an item.
     */
    @Listener({"use"})
    public void use(Item e) {
        if (!this.player.isInFight()) { // If the player is not engaged in a fight
            if (e != null) { // If the item is in the player's inventory
                switch (e.getType()) { // Different actions concerning the item"s type
                    case DMG:
                        System.out.println("You cant use this item without a target, attack something instead ! ");
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
            } else { // If the item is not in the player's inventory
                System.out.println("Error : check if you have this item in your inventory.");
            }
        } else { // If the player is engaged in a fight
            System.out.println("Error : You can only fight or flee.");
        }
    }

    /**
     * This method is called whenever the player wants to move the another region.
     */
    @Listener({"move"})
    public void move(Region r) {
        if (!this.player.isInFight()) { // If the player is not engaged in a fight
            if (r != null) { // If the region exists and is connected to the player's location
                this.player.move(r);
                System.out.println("You moved to the " + r.describe());
            } else { // If the region doesnt exists and/or is not connected to the player's location
                System.out.println("Error : check if this location is connected to your location.");
            }
        } else { // If the player is engaged in a fight
            System.out.println("Error : You can only fight or flee.");
        }
    }

    /**
     * This method is called whenever the player wants to pickup an item.
     */
    @Listener({"pickup"})
    public void pick(Item i) {
        if (!this.player.isInFight()) { // If the player is not engaged in a fight
            if (i != null) { // If the item is in the location of the player
                System.out.println("You picked up " + i.getName() + ".");
                this.player.pickUp(i);
            } else { // If the item cannot be picked up
                System.out.println("Error : check if this item is in your location.");
            }
        } else { // If the player is engaged in a fight
            System.out.println("Error : You can only fight or flee.");
        }
    }

    /**
     * This method is called whenever the player wants to equip an item.
     */
    @Listener({"equip"})
    public void equip(Item i) {
        if (i != null) { // If the item can be equipped
            System.out.println("Previously equipped item : " + ((this.player.getItemInHand() == null) ? "Nothing" : this.player.getItemInHand().getName()));
            this.player.equip(i);
            System.out.println("Now equipped item : " + ((this.player.getItemInHand() == null) ? "Nothing" : this.player.getItemInHand().getName()));
        } else { // If the item can not be equipped
            System.out.println("Error : check if you have this item in your inventory.");
        }
    }

    /**
     * This method is called whenever the player wants to ditch an item.
     */
    @Listener({"ditch"})
    public void ditch(Item i) {
        if (!this.player.isInFight()) { // If the player is not in a fight
            if (i != null) { // If the item can be ditched
                System.out.println("You ditched " + i.getName() + ".");
                this.player.ditch(i);
            } else { // If the item can not be ditched
                System.out.println("Error : check if you have this item in your inventory.");
            }
        } else { // If the player is in a fight
            System.out.println("Error : You can only fight or flee.");
        }
    }

    /**
     * This method is called whenever the player wants to unequip an item.
     */
    @Listener({"unequip"})
    public void unequip(Item i) {
        if (!this.player.isInFight()) { // If the player is not in a fight
            if (i != null) { // If the item can be unequipped
                System.out.println("You unequipped " + i.getName() + ".");
                this.player.unequip(i);
            } else { // If the item can not be unequipped
                System.out.println("Error : check if you have the correct item equipped.");
            }
        } else { // If the player is in a fight
            System.out.println("Error : You can only fight or flee.");
        }
    }
}

