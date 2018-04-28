package projetrpg.game;

import projetrpg.commands.*;
import projetrpg.entities.Entity;
import projetrpg.entities.items.Item;
import projetrpg.map.MainMap;
import projetrpg.map.Region;
import projetrpg.map.Teleporter;

import java.lang.reflect.Field;
import java.util.*;

import java.util.Scanner;

public class Game {

    /**
     * The map.
     */
    private MainMap mainMap;

    /**
     * The list with a description of
     * each command
     */
    private String manuel;

    private CommandParser parser = new CommandParser();

    public Game(MainMap m) {
        this.mainMap = m;
        manuel = "User guide : \n" +
                "-attack : on an entity \n" +
                "-talk : on an non hostile entity \n" +
                "-fleefrom : on an entity you are fighting with \n" +
                "-move : on a region thats connected to the one you're in \n" +
                "-pickup : on an item present int the map you're in \n" +
                "-equip : on an item present in your inventory \n" +
                "-ditch : on an item thats present in your inventory \n" +
                "-unequip : on an item you have in head \n" +
                "-use : on an item you have in your inventory \n" +
                "-see : on an attribute you wish to see the status" + "\n" +
                "-describe location : to see your locations's infos\n" +
                "-describe : on an entity, an item, or a teleporter in order to see its infos\n" +
                "-help me : to see the user guide.\n";

        commandRegisterer();
    }

    public String getManuel() {
        return manuel;
    }

    public void commandRegisterer() {

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
            return null;
        });

        parser.registerCommand("teleport", Region.class, (player, arg) -> {
            for (Region r : this.mainMap.getRegions()) {
                if (r.getName().equalsIgnoreCase(arg)) {
                    return r;
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

        parser.registerCommand("use", Item.class, (player, arg)-> {
            for (Item i : player.getInventory().getAll()) {
                if (i.getName().toLowerCase().equals(arg.toLowerCase())) {
                    return i;
                }
            }
            return null;
        });

        parser.registerCommand("see", Field.class, (player, arg)-> {
            List<Field> fields = new ArrayList<>();
            fields.addAll(Arrays.asList(player.getClass().getDeclaredFields()));
            fields.addAll(Arrays.asList(player.getClass().getSuperclass().getDeclaredFields()));
            for (Field field : fields) {
                if (field.getName().equalsIgnoreCase(arg)) {
                    return field;
                }
            }
            return null;
        });

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

        parser.registerCommand("help", String.class, (player, arg)-> {
            if (arg.equalsIgnoreCase("me")) return arg;
            return null;
        });

        try {
            parser.registerListener(new CommandListener(mainMap.getMainCharacter(), this), CommandListener.class);
        } catch (InvalidAnnotationException e) {
            e.printStackTrace();
        }
    }

    public MainMap getMainMap() {
        return mainMap;
    }

    public CommandParser getParser() {
        return parser;
    }
}