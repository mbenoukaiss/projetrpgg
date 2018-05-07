package projetrpg.game;

import projetrpg.commands.*;
import projetrpg.entities.Entity;
import projetrpg.entities.NPC;
import projetrpg.entities.items.Item;
import projetrpg.entities.player.Ability;
import projetrpg.map.MainMap;
import projetrpg.map.Region;
import projetrpg.map.Teleporter;
import projetrpg.quest.Quest;

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
                "-use : on an item you have in your inventory or an ability you learned\n" +
                "-see : on an attribute you wish to see the status" + "\n" +
                "-describe location : to see your locations's infos\n" +
                "-describe : on an entity, an item, or a teleporter in order to see its infos\n" +
                "-take : on a teleporter you repaired\n" +
                "-repair : on a teleporter you wish to repair\n" +
                "-start : on a quest in order to start it\n" +
                "-giveup : in order to abandon your current quest\n" +
                "-learn : on an ability\n" +
                "-travel : on a region\n" +
                "-help : to see the user guide.\n\n";
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

        parser.registerCommand("take", Teleporter.class, (player, arg) -> {
            for (Teleporter teleporter : this.mainMap.getMainCharacter().getLocation().getTeleporters()) {
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

        parser.registerCommand("see", Field.class, (player, arg)-> {
            List<Field> fields = new ArrayList<>();
            fields.addAll(Arrays.asList(player.getClass().getDeclaredFields()));
            fields.addAll(Arrays.asList(player.getClass().getSuperclass().getDeclaredFields()));
            for (Field field : fields) {
                if (field.isAnnotationPresent(Expose.class)) {
                    if (field.getAnnotation(Expose.class).value().equalsIgnoreCase(arg)) return field;
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
            for (Quest quest : this.mainMap.getQuests()) {
                if (quest.getName().equalsIgnoreCase(arg)) {
                    return quest;
                }
            }
            return null;
        });

        parser.registerCommand("giveup", String.class);

        parser.registerCommand("help", String.class);

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

    public Region findRegion(Region region, String regionName) {
        if (region.getName().equalsIgnoreCase(regionName)) return region;
        for (Region region1 : region.getContainedRegions()) {
            return findRegion(region1, regionName);
        }
        return null;
    }
}
