package projetrpg.game;

import projetrpg.Describable;
import projetrpg.commands.Listener;
import projetrpg.entities.player.Ability;
import projetrpg.game.Game;
import projetrpg.entities.Damageable;
import projetrpg.entities.Entity;
import projetrpg.entities.NPC;
import projetrpg.entities.items.Inventory;
import projetrpg.entities.items.Item;
import projetrpg.entities.player.Player;
import projetrpg.map.MainMap;
import projetrpg.map.Region;
import projetrpg.map.Teleporter;
import projetrpg.quest.Objective;
import projetrpg.quest.ObjectiveType;
import projetrpg.quest.Quest;

import java.lang.reflect.Field;


/**
 * Represents the listener of the different methods.
 *
 * @author mhevin
 * @author mbenoukaiss
 */
public class CommandListener {

    /**
     * The player.
     */
    private Player player;

    /**
     * The game.
     */
    private Game game;

    public CommandListener(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    /**
     * This method is called whenever the user wishes to see the user guide
     */
    @Listener({"help"})
    public String help(String arg) {
        return game.getManuel();
    }


    /**
     * This method is called whenever the user wishes to see a stat
     */
    @Listener({"see"})
    public String see(Field field) {
        if (field != null) { // If the attribute was found
            field.setAccessible(true);
            try {
                Object value = field.get(this.player);
                if (value == null) {
                    return "You have nothing equipped.";
                } else if (value instanceof Region) {
                    return((((Region) value).getName()));
                } else if (value instanceof Item) {
                    return(((Item) value).getName());
                } else if (value instanceof Inventory) {
                    return(((Inventory) value).describe());
                } else {
                    return(String.valueOf(value));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else { // If the attribute was not found
            return("Error : make sure you typed the right attribute to see.");
        }
        return null;
    }

    /**
     * This method is called whenever the user wishes to have infos on something.
     */
    @Listener({"describe"})
    public String describe(Object o) {
        if (o != null) {
            if (o instanceof Region) {
                return(this.player.getLocation().describe());
            } else if (o instanceof Entity) {
                return(((Describable) o).describe());
            } else if (o instanceof Item) {
                return(((Item) o).describe());
            } else if (o instanceof Teleporter) {
                return (((Teleporter) o).describe());
            }
        } else {
            return("Error : Check if this entity is present in your area, by typing 'describe location'.");
        }
        return null;
    }

    /**
     * This method is called whenever the user wishes to attack a damageable.
     */
    @Listener({"attack"})
    public String combat(Damageable e) {
        String message = "";
        if (e != null) { // If the damageable targeted can be attacked
            message += (((Entity) e).getName() + " : " + ((NPC) e).getDialogue())
            + ("\nHps of the ennemy before the assault : " + e.getHp())
            + ("\nYour Hps before the attack : " + player.getHp())
            + ("\n......\nHes attacking you!");
            if (this.player.attack(e, (this.player.getItemInHand() == null )? this.player.baseDamage() : this.player.gettrueDamage())) { // If the attack kills the target
                Objective objectiveFound = null;
                boolean isConcerned = false;
                if (this.player.getCurrentQuest() != null) {
                    for (Objective o : this.player.getCurrentQuest().getObjectives()) {
                        if (o.getType() == ObjectiveType.KILL && o.getConcernedNPC() == e) {
                            objectiveFound = o;
                            isConcerned = true;
                        }
                    }
                }
                if (isConcerned) {
                    Quest savedQuest = this.player.getCurrentQuest();
                    objectiveFound.finish();
                    if (this.player.getCurrentQuest() == null) {
                        if (this.player.canLevelUp(((NPC) e).getType().getExperienceRewarded() + savedQuest.getExpRewarded())) {
                            message += " Congrats, you leveled up ! You can now learn :";
                            for (Ability a : this.player.abilitiesAbleToLearn()) {
                                message += a.getName() + ", ";
                            }
                            message = message.substring(0, message.length()-2) + ". ";
                        }
                        message += ("Congrats, you killed : " + ((Entity) e).getName() + ", you won " +
                                ((Entity) e).getType().getExperienceRewarded() + " exp! You are level " + this.player.getLevel())
                                + (". You finished this objectiv : " + objectiveFound.getDescription() +
                                ". You have now finished this quest : " + savedQuest.getDescription() + "!" +
                                " And gained " + savedQuest.getExpRewarded() + " exp!");
                        return message;
                    } else {
                        if (this.player.canLevelUp(((NPC) e).getType().getExperienceRewarded())) {
                            message += " Congrats, you leveled up ! You can now learn :";
                            for (Ability a : this.player.abilitiesAbleToLearn()) {
                                message += a.getName() + ", ";
                            }
                            message = message.substring(0, message.length()-2) + ". ";
                        }
                        message += ("Congrats, you killed : " + ((Entity) e).getName() + ", you won " +
                                ((Entity) e).getType().getExperienceRewarded() + " exp! You are level " + this.player.getLevel())
                                + (". You finished this objectiv : " + objectiveFound.getDescription() + ".");
                        return message;
                    }
                } else {
                    if (this.player.canLevelUp(((NPC) e).getType().getExperienceRewarded())) {
                        message += " Congrats, you leveled up ! You can now learn :";
                        for (Ability a : this.player.abilitiesAbleToLearn()) {
                            message += a.getName() + ", ";
                        }
                        message = message.substring(0, message.length()-2) + ". ";
                    }
                    message += ("Congrats, you killed : " + ((Entity) e).getName() + ", you won " +
                            ((Entity) e).getType().getExperienceRewarded() + " exp! You are level " + this.player.getLevel());
                    return message;
                }
            } else if (((NPC) e).attack(this.player, 0)) { // If the target kills the player
                ((NPC) e).setHps(((NPC) e).getBaseHps());
                message += ("You died, you have been redirected to the spawn point : " + game.getMainMap().getSpawnPoint()
                        .getName()+ ".");
                this.player.setLocation(game.getMainMap().getSpawnPoint());
                message += ("You can now go to :" + this.player.getLocation().getRegionNamesOnDirection());
                ((NPC) e).setInFight(false);
                this.player.setInFight(false);
                this.player.setHps(this.player.getBaseHps());
                return message;
            } else { // The fight is still going on
                message += ("Hps of the ennemy after the assault : " + e.getHp())
                 + ("\nYour Hps after the attack : " + player.getHp())
                 + ("\nYou may now flee,keep on fighting or equip an item.");
                return message;
            }
        } else { // If the damageable targeted cannot be attacked
            return("Error : check if this entity is in your location.");
        }
    }

    @Listener({"start"})
    public String start(Quest q) {
        if (!this.player.isInFight()) {
            if (q != null) {
                if (this.player.getCurrentQuest() == null) {
                    if (this.player.getLevel() >= q.getLevelRequired()) {
                        this.player.setCurrentQuest(q);
                        return "You started this quest :" + q.getName() + "!";
                    } else {
                        return "You dont meet the required level in order to start this quest.";
                    }
                } else {
                    return "You may only start one quest at a time, finish the one you started.";
                }
            } else {
                return "Check if you may start this quest or if it even exists.";
            }
        } else {
            return("Error : You can only fight or flee.");
        }
    }

    /**
     * This method is called whenever the user wishes to talk to an NPC.
     */
    @Listener({"talk"})
    public String talk(NPC e) {
        if (!this.player.isInFight()) { // If the player isnt fighting
            if (e != null) {// If the target can be talked to
                Objective objectiveFound = null;
                boolean isConcerned = false;
                if (this.player.getCurrentQuest() != null) {
                    for (Objective o : this.player.getCurrentQuest().getObjectives()) {
                        if (o.getType() == ObjectiveType.TALK && o.getConcernedNPC() == e) {
                            objectiveFound = o;
                            isConcerned = true;
                        }
                    }
                }
                if (isConcerned) {
                    Quest savedQuest = this.player.getCurrentQuest();
                    objectiveFound.finish();
                    if (this.player.getCurrentQuest() == null) {
                        String message = "";
                        if (this.player.canLevelUp(savedQuest.getExpRewarded())) {
                            message += " Congrats, you leveled up ! You can now learn :";
                            for (Ability a : this.player.abilitiesAbleToLearn()) {
                                message += a.getName() + ", ";
                            }
                            message = message.substring(0, message.length()-2) + ". ";
                        }
                        message += e.getDialogue() + " You finished this objective : " + objectiveFound.getDescription() +
                                ". You have now finished this quest : " + savedQuest.getDescription() + "!";
                        return message;
                    }
                    return e.getDialogue() + " You finished this objective : " + objectiveFound.getDescription() + ".";
                }
                return(e.getDialogue());
            }
            else { // If the target cannot be talked to
                return("Error : check if this NON-Hostile entity is in your location.");
            }
        } else { // If the player is still fighting
            return("Error : You can only fight or flee.");
        }
    }

    /**
     * This methode is called whenever the user wishes to leave a fight he is engaged in.
     */
    @Listener({"fleefrom"})
    public String fleeFrom(NPC e) {
        if (e != null && e.isInFight()) { // If the NPC exists and is in fight with the player
            e.setHps(e.getBaseHps());
            e.setInFight(false);
            this.player.setInFight(false);
            return("You fled from " + e.getName() + ".");
        } else { // If the player is not fighting this entity
            return("Error : check if you are in fight with this entity.");
        }
    }

    /**
     * This method is called whenever the user wishes to use an item.
     */
    @Listener({"use"})
    public String use(Object o) {
        if (o != null){ // If the player is not engaged in a fight
            if (!this.player.isInFight() && o instanceof Item) { // If the item is in the player's inventory
                Item e = (Item) o;
                Objective objectiveFound = null;
                boolean isConcerned = false;
                if (this.player.getCurrentQuest() != null) {
                    for (Objective obj : this.player.getCurrentQuest().getObjectives()) {
                        if (obj.getType() == ObjectiveType.USE && obj.getConcernedItem() == e) {
                            objectiveFound = obj;
                            isConcerned = true;
                        }
                    }
                }
                switch (e.getType()) { // Different actions concerning the item"s type
                    case DMG:
                        return("You cant use this item without a target, attack something instead ! ");
                    case FOOD:
                        this.player.setHps((this.player.getHp() + e.getType().getHpGiven() > this.player.getBaseHps()) ? 100 :
                                this.player.getHp() + e.getType().getHpGiven());
                        this.player.consume(e);
                        if (isConcerned) {
                            String message= "";
                            Quest savedQuest = this.player.getCurrentQuest();
                            objectiveFound.finish();
                            if (this.player.getCurrentQuest() == null) {
                                if (this.player.canLevelUp(savedQuest.getExpRewarded())) {
                                    message += " Congrats, you leveled up ! You can now learn :";
                                    for (Ability a : this.player.abilitiesAbleToLearn()) {
                                        message += a.getName() + ", ";
                                    }
                                    message = message.substring(0, message.length()-2) + ". ";
                                }
                                message += ("You ate : " + e.getName() + ", + " + e.getType().getHpGiven() + " HPS") +
                                        ("\nYou now have " + this.player.getHp() + " HPS.") +
                                        ("You finished this objective : " + objectiveFound.getDescription() +
                                                ". You have now finished this quest : " + savedQuest.getDescription() + "!");
                                return message;
                            }
                            return("You ate : " + e.getName() + ", + " + e.getType().getHpGiven() + " HPS") +
                                    ("\nYou now have " + this.player.getHp() + " HPS.") +
                                    ("You finished this objective : " + objectiveFound.getDescription() + ".");
                        }
                        return("You ate : " + e.getName() + ", + " + e.getType().getHpGiven() + " HPS") +
                        ("\nYou now have " + this.player.getHp() + " HPS.");
                    case UTILS:
                        if (isConcerned) {
                            Quest savedQuest = this.player.getCurrentQuest();
                            objectiveFound.finish();
                            if (this.player.getCurrentQuest() == null) {
                                String message = "";
                                if (this.player.canLevelUp(savedQuest.getExpRewarded())) {
                                    message += " Congrats, you leveled up ! You can now learn :";
                                    for (Ability a : this.player.abilitiesAbleToLearn()) {
                                        message += a.getName() + ", ";
                                    }
                                    message = message.substring(0, message.length()-2) + ". ";
                                }
                                message += ("You used : " + e.getName() +
                                        ("You finished this objective : " + objectiveFound.getDescription() +
                                                (". You have now finished this quest : " + savedQuest.getDescription() + "!")));
                                return message;
                            }
                            return "You used : " + e.getName() + ("You finished this objective : "
                                    + objectiveFound.getDescription());
                        }
                        return("You used : " + e.getName());
                    default:
                        return "";
                }
            } else if (this.player.isInFight() && o instanceof Ability){ // If the item is not in the player's inventory
                Damageable e = this.player.getEnnemy();
                Objective objectiveFound = null;
                Ability a = (Ability) o;
                String message = "You used " + a.getName() + " on " + this.player.getEnnemy().getName() + "."
                        + (player.getEnnemy().getName() + " : " + this.player.getEnnemy().getDialogue())
                        + ("\nHps of the ennemy before the assault : " + player.getEnnemy().getHp()
                        + ("\nYour Hps before the attack : " + player.getHp())
                        + ("\n......\nHes attacking you!"));
                if (this.player.attack(player.getEnnemy(), (int)a.getDamage())) {
                    boolean isConcerned = false;
                    if (this.player.getCurrentQuest() != null) {
                        for (Objective obj : this.player.getCurrentQuest().getObjectives()) {
                            if (obj.getType() == ObjectiveType.KILL && obj.getConcernedNPC() == e) {
                                objectiveFound = obj;
                                isConcerned = true;
                            }
                        }
                    }
                    if (isConcerned) {
                        Quest savedQuest = this.player.getCurrentQuest();
                        objectiveFound.finish();
                        if (this.player.getCurrentQuest() == null) {
                            if (this.player.canLevelUp(((NPC) e).getType().getExperienceRewarded() + savedQuest.getExpRewarded())) {
                                message += " Congrats, you leveled up ! You can now learn :";
                                for (Ability ab : this.player.abilitiesAbleToLearn()) {
                                    message += ab.getName() + ", ";
                                }
                                message = message.substring(0, message.length()-2) + ". ";
                            }
                            message += ("Congrats, you killed : " + ((Entity) e).getName() + ", you won " +
                                    ((Entity) e).getType().getExperienceRewarded() + " exp! You are level " + this.player.getLevel())
                                    + (". You finished this objectiv : " + objectiveFound.getDescription() +
                                    ". You have now finished this quest : " + savedQuest.getDescription() + "!" +
                                    " And gained " + savedQuest.getExpRewarded() + " exp!");
                        } else {
                            if (this.player.canLevelUp(((NPC) e).getType().getExperienceRewarded())) {
                                message += " Congrats, you leveled up ! You can now learn :";
                                for (Ability ab : this.player.abilitiesAbleToLearn()) {
                                    message += ab.getName() + ", ";
                                }
                                message = message.substring(0, message.length()-2) + ". ";
                            }
                            message += ("Congrats, you killed : " + ((Entity) e).getName() + ", you won " +
                                    ((Entity) e).getType().getExperienceRewarded() + " exp! You are level " + this.player.getLevel())
                                    + (". You finished this objectiv : " + objectiveFound.getDescription() + ".");
                        }
                    } else {
                        if (this.player.canLevelUp(((NPC) e).getType().getExperienceRewarded())) {
                            message += " Congrats, you leveled up ! You can now learn :";
                            for (Ability ab : this.player.abilitiesAbleToLearn()) {
                                message += ab.getName() + ", ";
                            }
                            message = message.substring(0, message.length()-2) + ". ";
                        }
                        message += ("Congrats, you killed : " + ((Entity) e).getName() + ", you won " +
                                ((Entity) e).getType().getExperienceRewarded() + " exp! You are level " + this.player.getLevel());
                    }
                } else if (((NPC) e).attack(this.player, 0)) { // If the target kills the player
                    ((NPC) e).setHps(((NPC) e).getBaseHps());
                    message += ("You died, you have been redirected to the spawn point : " + game.getMainMap().getSpawnPoint()
                            .getName()+ ".");
                    this.player.setLocation(game.getMainMap().getSpawnPoint());
                    message += ("You can now go to :" + this.player.getLocation().getRegionNamesOnDirection());
                    ((NPC) e).setInFight(false);
                    this.player.setInFight(false);
                    this.player.setHps(this.player.getBaseHps());
                } else { // The fight is still going on
                    message += ("Hps of the ennemy after the assault : " + e.getHp())
                            + ("\nYour Hps after the attack : " + player.getHp())
                            + ("\nYou may now flee,keep on fighting or equip an item.");
                }
                return message;
            } else if (!this.player.isInFight() && o instanceof Ability) {
                this.player.setMana(this.player.getMana()-((Ability) o).getCost());
                return "You used " + ((Ability) o).getName() + " on nothing.";
            } else {
                return("Error : You can only fight or flee.");
            }
        } else { // If the player is engaged in a fight
            return("Error : check if you have this item in your inventory or if you learned this ability.");
        }
    }

    /**
     * This method is called whenever the user wishes to use an ability on an entity
     */
    @Listener({"use", "on"})
    public String useOn(Ability a, NPC e) {
        if (a != null && e != null) {
            if (this.player.getMana() >= a.getCost()) {
                this.player.setMana(this.player.getMana()-a.getCost());
                Objective objectiveFound = null;
                String message = "You used " + a.getName() + " on " + e.getName() + "."
                        + (e.getName() + " : " + e.getDialogue())
                        + ("\nHps of the ennemy before the assault : " + e.getHp()
                        + ("\nYour Hps before the attack : " + player.getHp())
                        + ("\n......\nHes attacking you!"));
                if (this.player.attack(e, (int) a.getDamage())) {
                    boolean isConcerned = false;
                    if (this.player.getCurrentQuest() != null) {
                        for (Objective obj : this.player.getCurrentQuest().getObjectives()) {
                            if (obj.getType() == ObjectiveType.KILL && obj.getConcernedNPC() == e) {
                                objectiveFound = obj;
                                isConcerned = true;
                            }
                        }
                    }
                    if (isConcerned) {
                        Quest savedQuest = this.player.getCurrentQuest();
                        objectiveFound.finish();
                        if (this.player.getCurrentQuest() == null) {
                            if (this.player.canLevelUp(((NPC) e).getType().getExperienceRewarded() + savedQuest.getExpRewarded())) {
                                message += " Congrats, you leveled up ! You can now learn :";
                                for (Ability ab : this.player.abilitiesAbleToLearn()) {
                                    message += ab.getName() + ", ";
                                }
                                message = message.substring(0, message.length() - 2) + ". ";
                            }
                            message += ("Congrats, you killed : " + ((Entity) e).getName() + ", you won " +
                                    ((Entity) e).getType().getExperienceRewarded() + " exp! You are level " + this.player.getLevel())
                                    + (". You finished this objectiv : " + objectiveFound.getDescription() +
                                    ". You have now finished this quest : " + savedQuest.getDescription() + "!" +
                                    " And gained " + savedQuest.getExpRewarded() + " exp!");
                        } else {
                            if (this.player.canLevelUp(((NPC) e).getType().getExperienceRewarded())) {
                                message += " Congrats, you leveled up ! You can now learn :";
                                for (Ability ab : this.player.abilitiesAbleToLearn()) {
                                    message += ab.getName() + ", ";
                                }
                                message = message.substring(0, message.length() - 2) + ". ";
                            }
                            message += ("Congrats, you killed : " + ((Entity) e).getName() + ", you won " +
                                    ((Entity) e).getType().getExperienceRewarded() + " exp! You are level " + this.player.getLevel())
                                    + (". You finished this objectiv : " + objectiveFound.getDescription() + ".");
                        }
                    } else {
                        if (this.player.canLevelUp(((NPC) e).getType().getExperienceRewarded())) {
                            message += " Congrats, you leveled up ! You can now learn :";
                            for (Ability ab : this.player.abilitiesAbleToLearn()) {
                                message += ab.getName() + ", ";
                            }
                            message = message.substring(0, message.length() - 2) + ". ";
                        }
                        message += ("Congrats, you killed : " + ((Entity) e).getName() + ", you won " +
                                ((Entity) e).getType().getExperienceRewarded() + " exp! You are level " + this.player.getLevel());
                    }
                } else if (((NPC) e).attack(this.player, 0)) { // If the target kills the player
                    ((NPC) e).setHps(((NPC) e).getBaseHps());
                    message += ("You died, you have been redirected to the spawn point : " + game.getMainMap().getSpawnPoint()
                            .getName() + ".");
                    this.player.setLocation(game.getMainMap().getSpawnPoint());
                    message += ("You can now go to :" + this.player.getLocation().getRegionNamesOnDirection());
                    ((NPC) e).setInFight(false);
                    this.player.setInFight(false);
                    this.player.setHps(this.player.getBaseHps());
                } else { // The fight is still going on
                    message += ("Hps of the ennemy after the assault : " + e.getHp())
                            + ("\nYour Hps after the attack : " + player.getHp())
                            + ("\nYou may now flee,keep on fighting or equip an item.");
                }
                return message;
            } else {
                return "You dont have enough mana in order to cast this spell";
            }
        } else {
            return "Check if you have learned this ability of if the entity is nearby";
        }
    }

    /**
     * This method is called whenever the user wishes to learn an ability
     */
    @Listener({"learn"})
    public String learn(Ability a) {
        if (a != null) {
            return this.player.learn(a);
        } else {
            return "Error : Check if you can learn this ability or if it even exists.";
        }
    }

    /**
     * This method is called whenever the player wants to move the another region.
     */
    @Listener({"move"})
    public String move(Region r) {
        if (!this.player.isInFight()) { // If the player is not engaged in a fight
            if (r != null) { // If the region exists and is connected to the player's location
                this.player.move(r);
                return("You moved to the " + r.describe());
            } else { // If the region doesnt exists and/or is not connected to the player's location
                return("Error : check if this location is connected to your location.");
            }
        } else { // If the player is engaged in a fight
            return("Error : You can only fight or flee.");
        }
    }

    /**
     * This method is called whenever the player wants to teleport to another region.
     */
    @Listener({"take"})
    public String teleport(Teleporter teleporter) {
        if (!this.player.isInFight()) { // If the player is not engaged in a fight
            if (teleporter != null) { // If the region exists and is connected to the player's location
                boolean canTeleportTo = false;
                if (teleporter.isRepaired() && teleporter.getLinkedTeleporter().isRepaired()) {
                    canTeleportTo = true;
                }
                if (canTeleportTo) {
                    this.player.move(teleporter.getLinkedTeleporter().getLocation());
                    return("You teleported to the : " + teleporter.getLinkedTeleporter().getLocation().describe());
                }
                return("You cant take to this teleporter" + "." + " Check if you have repaired it AND the linked teleporter");
            } else { // If the region doesnt exists and/or is not connected to the player's location
                return("Error : check if this location exists.");
            }
        } else { // If the player is engaged in a fight
            return("Error : You can only fight or flee.");
        }
    }

    /**
     * This method is called whenever the player wants to repair a teleporter.
     */
    @Listener({"repair"})
    public String repair(Teleporter t) {
        if (!this.player.isInFight()) {
            if (t != null) {
                boolean canRepair;
                for (Item i : t.getItemsNeededToRepair()) {
                    canRepair = false;
                    for (Item pi : this.player.getInventory().getAll()) {
                        if (pi.getName().equalsIgnoreCase(i.getName())) {
                            canRepair = true;
                        }
                    }
                    if (!canRepair) return("You do not have the required items in order to repair this teleporter.");
                }
                t.repair();
                return("You repaired this teleporter : " + t.getName());
            } else {
                return("Error : Check if this teleporter is in your region.");
            }
        } else {
            return("Error : You can only fight or flee.");
        }
    }

    /**
     * This method is called whenever the player wants to pickup an item.
     */
    @Listener({"pickup"})
    public String pick(Item i) {
        if (!this.player.isInFight()) { // If the player is not engaged in a fight
            if (i != null) { // If the item is in the location of the player
                Objective objectiveFound = null;
                boolean isConcerned = false;
                if (this.player.getCurrentQuest() != null) {
                    for (Objective o : this.player.getCurrentQuest().getObjectives()) {
                        if (o.getType() == ObjectiveType.PICKUP && o.getConcernedItem() == i) {
                            objectiveFound = o;
                            isConcerned = true;
                        }
                    }
                }
                if (isConcerned) {
                    Quest savedQuest = this.player.getCurrentQuest();
                    this.player.pickUp(i);
                    objectiveFound.finish();
                    if (this.player.getCurrentQuest() == null) {
                        String message = "";
                        if (this.player.canLevelUp(savedQuest.getExpRewarded())) {
                            message += " Congrats, you leveled up ! You can now learn :";
                            for (Ability a : this.player.abilitiesAbleToLearn()) {
                                message += a.getName() + ", ";
                            }
                            message = message.substring(0, message.length()-2) + ". ";
                        }
                        message += ("You picked up " + i.getName() + ".") +
                                ("You finished this objective : " + objectiveFound.getDescription() +
                                        ". You have now finished this quest : " + savedQuest.getDescription() + "!");
                        return message;
                    }
                    return("You picked up " + i.getName() + ".") +
                            ("You finished this objective : " + objectiveFound.getDescription() + ".");
                }
                this.player.pickUp(i);
                return("You picked up " + i.getName() + ".");
            } else { // If the item cannot be picked up
                return("Error : check if this item is in your location.");
            }
        } else { // If the player is engaged in a fight
            return("Error : You can only fight or flee.");
        }
    }

    /**
     * This method is called whenever the player wants to equip an item.
     */
    @Listener({"equip"})
    public String equip(Item i) {
        if (i != null) { // If the item can be equipped
            String message = ("Previously equipped item : " + ((this.player.getItemInHand() == null) ? "Nothing" : this.player.getItemInHand().getName()));
            this.player.equip(i);
            message+= ("\nNow equipped item : " + ((this.player.getItemInHand() == null) ? "Nothing" : this.player.getItemInHand().getName()));
            return message;
        } else { // If the item can not be equipped
            return("Error : check if you have this item in your inventory.");
        }
    }

    /**
     * This method is called whenever the player wants to ditch an item.
     */
    @Listener({"ditch"})
    public String ditch(Item i) {
        if (!this.player.isInFight()) { // If the player is not in a fight
            if (i != null) { // If the item can be ditched
                this.player.ditch(i);
                return("You ditched " + i.getName() + ".");
            } else { // If the item can not be ditched
                return("Error : check if you have this item in your inventory.");
            }
        } else { // If the player is in a fight
            return("Error : You can only fight or flee.");
        }
    }

    /**
     * This method is called whenever the player wants to unequip an item.
     */
    @Listener({"unequip"})
    public String unequip(Item i) {
        if (!this.player.isInFight()) { // If the player is not in a fight
            if (i != null) { // If the item can be unequipped
                this.player.unequip(i);
                return("You unequipped " + i.getName() + ".");
            } else { // If the item can not be unequipped
                return("Error : check if you have the correct item equipped.");
            }
        } else { // If the player is in a fight
            return("Error : You can only fight or flee.");
        }
    }

    @Listener({"giveup"})
    public String abandon(String s) {
        if (!this.player.isInFight()) {
            Quest sq = this.player.getCurrentQuest();
            if (sq != null) this.player.getCurrentQuest().abandon();
            this.player.setCurrentQuest(null);
            if (sq != null) return "You gave up on this quest : " + sq.getName() + ".";
            return "No quest to give up";
        } else { // If the player is in a fight
            return("Error : You can only fight or flee.");
        }
    }
}
