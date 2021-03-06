package projetrpg.game;

import projetrpg.commands.CommandParser;
import projetrpg.commands.InvalidAnnotationException;
import projetrpg.entities.Entity;
import projetrpg.entities.NPC;
import projetrpg.entities.items.Item;
import projetrpg.entities.player.Ability;
import projetrpg.entities.player.ShipAmelioration;
import projetrpg.map.MainMap;
import projetrpg.map.Region;
import projetrpg.map.Teleporter;
import projetrpg.menu.save.Save;
import projetrpg.quest.Quest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * A game.
 */
public class Game {

    /**
     * The map.
     */
    Save save;

    /**
     * The list with a description of
     * each command
     */
    final String manual;

    /**
     * The command parser.
     */
    CommandParser parser = new CommandParser();

    public Game(Save save) {
        this.save = save;
        manual = "User guide : \n" +
                "-attack : on an entity \n" +
                "-talk : on an non hostile entity \n" +
                "-fleefrom : on an entity you are fighting with \n" +
                "-move : on a region thats connected to the one you're in or on your ship \n" +
                "-pickup : on an item present int the map you're in \n" +
                "-equip : on an item present in your inventory \n" +
                "-ditch : on an item thats present in your inventory \n" +
                "-unequip : on an item you have in head \n" +
                "-use : on an item you have in your inventory or an ability you learned\n" +
                "-see : on an attribute of yours or of your ship you wish to see the status :\n" +
                "       -health : your current hps\n" +
                "       -xp : your experience\n" +
                "       -item : your item equipped\n" +
                "       -mana : your current mana\n" +
                "       -location : your current location\n" +
                "       -ship level : your ship level\n" +
                "       -fuel : your ship fuel\n" +
                "       -ship improvements : your available ship improvements" + "\n" +
                "-describe location : to see your locations's infos\n" +
                "-describe : on an entity, an item, or a teleporter in order to see its infos\n" +
                "-take : on a teleporter you repaired\n" +
                "-repair : on a teleporter you wish to repair\n" +
                "-start : on a quest in order to start it\n" +
                "-giveup : in order to giveup your current quest\n" +
                "-learn : on an ability\n" +
                "-travel : on a region\n" +
                "-improve : on a possible amelioration of your ship (radar, engine, reactors)\n" +
                "-rest : in order to regain your hps and mana\n" +
                "-help : to see the user guide.\n\n";
        commandRegisterer();
    }

    /**
     * Registers all the commands available
     * in this game.
     */
    private void commandRegisterer() {

        Scanner sc = new Scanner(System.in);

        // Registering all the commands used.
        parser.registerCommand("attack", Entity.class, (player, arg)-> {
            for (Entity e : player.getLocation().getEntities()) {
                if (e.getName().toLowerCase().equals(arg.toLowerCase())) {
                    return e;
                }
            }
            return null;
        });

        parser.registerCommand("talk", Entity.class, (player, arg)-> {
            for (Entity e : player.getLocation().getEntities()) {
                if (e.getName().toLowerCase().equals(arg.toLowerCase()) && !e.isHostile()) {
                    return e;
                }
            }
            return null;
        });

        parser.registerCommand("fleefrom", Entity.class, (player, arg)-> {
            for (Entity e : player.getLocation().getEntities()) {
                if (e.getName().toLowerCase().equals(arg.toLowerCase())) {
                    return e;
                }
            }
            return null;
        });

        parser.registerCommand("move", Region.class, (player, arg)-> {
            if (player.getLocation().getName().equalsIgnoreCase(arg) || (player.getLocation() == player.getShip() &&
            arg.equalsIgnoreCase("ship"))) {
                return null;
            }
            if (player.getLocation() == player.getShip() && arg.equalsIgnoreCase(player.getShip().getParent().getName())) {
                return player.getShip().getParent();
            }
            for (Region r : player.getLocation().getRegionOnDirection().values()) {
                if (r.getName().toLowerCase().equals(arg.toLowerCase())) {
                    return r;
                }
            }
            for (Region r : player.getLocation().getContainedRegions()) {
                if (r.getName().equalsIgnoreCase(arg)) {
                    return r;
                }
            }
            if (player.getLocation().getParent() != null &&
                    player.getLocation().getParent().getName().equalsIgnoreCase(arg))
                return player.getLocation().getParent();
            if (arg.equalsIgnoreCase("ship")) {
                return player.getShip();
            }
            return null;
        });

        parser.registerCommand("take", Teleporter.class, (player, arg) -> {
            for (Teleporter teleporter : save.getMap().getMainCharacter().getLocation().getTeleporters()) {
                if (teleporter.getName().equalsIgnoreCase(arg)) {
                    return teleporter;
                }
            }
            return null;
        });

        parser.registerCommand("repair", Teleporter.class, (player, arg) -> {
            for (Teleporter t : player.getLocation().getTeleporters()) {
                if (t.getName().equalsIgnoreCase(arg)) {
                    return t;
                }
            }
            return null;
        });

        parser.registerCommand("pickup", Item.class, (player, arg)-> {
            for (Item i : player.getLocation().getInventory().getAll()) {
                if (i.getName().toLowerCase().equals(arg.toLowerCase())) {
                    return i;
                }
            }
            return null;
        });

        parser.registerCommand("equip", Item.class, (player, arg)-> {
            for (Item i : player.getInventory().getAll()) {
                if (i.getName().toLowerCase().equals(arg.toLowerCase())) {
                    return i;
                }
            }
            return null;
        });

        parser.registerCommand("ditch", Item.class, (player, arg)-> {
            for (Item i : player.getInventory().getAll()) {
                if (i.getName().toLowerCase().equals(arg.toLowerCase())) {
                    return i;
                }
            }
            return null;
        });

        parser.registerCommand("unequip", Item.class, (player, arg)-> {
            if (player.getItemInHand() != null && player.getItemInHand().getName().equalsIgnoreCase(arg)) {
                return player.getItemInHand();
            }
            return null;
        });

        parser.registerCommand("use", Object.class, (player, arg)-> {
            for (Item i : player.getInventory().getAll()) {
                if (i.getName().toLowerCase().equals(arg.toLowerCase())) {
                    return i;
                }
            }
            for (Ability a : player.getAbilities()) {
                if (a.getName().equalsIgnoreCase(arg)) {
                    return a;
                }
            }
            return null;
        });

        parser.registerCommand("on", NPC.class, (player, arg) -> {
            for (NPC e : player.getLocation().getEntities()) {
                if (e.getName().equalsIgnoreCase(arg)) {
                    return e;
                }
            }
            return null;
        });

        parser.registerCommand("see", Object.class, (player, arg)-> {
            for (Method m : player.getClass().getDeclaredMethods()) {
                if (m.isAnnotationPresent(Expose.class)) {
                    if (m.getAnnotation(Expose.class).value().equalsIgnoreCase(arg)) {
                        return m;
                    }
                }
            }
            List<Field> fields = new ArrayList<>();
            fields.addAll(Arrays.asList(player.getClass().getDeclaredFields()));
            fields.addAll(Arrays.asList(player.getClass().getSuperclass().getDeclaredFields()));
            fields.addAll(Arrays.asList(player.getShip().getClass().getDeclaredFields()));
            for (Field field : fields) {
                if (field.isAnnotationPresent(Expose.class)) {
                    if (field.getAnnotation(Expose.class).value().equalsIgnoreCase(arg)) {
                        return field;
                    }
                }
            }
            return null;
        });

        parser.registerCommand("learn", Ability.class, (player, arg) -> {
            for (Ability a : player.learnableAbilities()) {
                if (a.getName().equalsIgnoreCase(arg)) {
                    return a;
                }
            }
            return null;
        });

        parser.registerCommand("improve", ShipAmelioration.class, (player, s) -> {
            switch(s.toLowerCase()) {
                case "radar":
                    return ShipAmelioration.RADAR_AMELIORATION;
                case "engine":
                    return ShipAmelioration.ENGINE_AMELIORATION;
                case "reactors":
                    return ShipAmelioration.REACTORS_AMELIORATION;
                default:
                    return null;
            }
        });

        parser.registerCommand("travel", Region.class, ((player, s) -> {
            for (Region region : save.getMap().getRegions()) {
                if (findRegion(region, s) != null) {
                    return findRegion(region, s);
                }
            }
            return null;
        }));

        parser.registerCommand("describe", Object.class, (player, arg) -> {
            for (Entity e : player.getLocation().getEntities()) {
                if (e.getName().equalsIgnoreCase(arg)) {
                    return e;
                }
            }
            for (Item i : player.getLocation().getInventory().getAll()) {
                if (i.getName().equalsIgnoreCase(arg)) {
                    return i;
                }
            }
            for (Item i : player.getInventory().getAll()) {
                if (i.getName().equalsIgnoreCase(arg)) {
                    return i;
                }
            }
            for (Teleporter t : player.getLocation().getTeleporters()) {
                if (t.getName().equalsIgnoreCase(arg)) {
                    return t;
                }
            }
            if (arg.equalsIgnoreCase("location")) {
                return player.getLocation();
            }
            return null;
        });

        parser.registerCommand("start", Quest.class, (player, arg) -> {
            for (Quest quest : save.getMap().getQuests()) {
                if (quest.getName().equalsIgnoreCase(arg)) {
                    return quest;
                }
            }
            return null;
        });

        parser.registerCommand("giveup", String.class);

        parser.registerCommand("help", String.class);

        parser.registerCommand("rest", String.class);

        try {
            parser.registerListener(new CommandListener(save.getMap().getMainCharacter(), this), CommandListener.class);
        } catch (InvalidAnnotationException e) {
            e.printStackTrace();
        }
    }

    public MainMap getMap() {
        return save.getMap();
    }

    public Region findRegion(Region region, String regionName) {
        if (region.getName().equalsIgnoreCase(regionName)) {
            return region;
        }
        for (Region region1 : region.getContainedRegions()) {
            if (findRegion(region1, regionName) != null) {
                return findRegion(region1, regionName);
            }
        }
        return null;
    }
}
