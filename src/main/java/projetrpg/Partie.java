package projetrpg;

import projetrpg.commands.*;
import projetrpg.entities.Entity;
import projetrpg.entities.NPC;
import projetrpg.entities.items.Item;
import projetrpg.entities.player.Player;
import projetrpg.map.MainMap;
import projetrpg.map.Region;

import java.lang.reflect.Field;
import java.util.*;

import java.util.Scanner;

public class Partie {
    
    private Player mainCharacter;
    private MainMap mainMap;

    public Partie(MainMap m, Player p) {
        this.mainMap = m;
        this.mainCharacter = p;
    }

    public void lauchGame() {

        Scanner sc = new Scanner(System.in);
        CommandParser parser = new CommandParser();

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
                if (e.getName().toLowerCase().equals(arg.toLowerCase())) {
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


        parser.registerCommand("see", String.class, (player, arg)-> {
            for (Field i : ((Entity)player).getClass().getDeclaredFields()) {
                String name = i.getName();
                if (name.equalsIgnoreCase(arg)) {
                    return i.getName();
                }
            }
            return null;
        });

        parser.registerCommand("see", String.class, (player, arg)-> {
            for (Field field : player.getClass().getDeclaredFields()) {
                String name = field.getName();
                if (name.equalsIgnoreCase(arg)) {
                    return name;
                }
            }
            for (Field f : player.getClass().getSuperclass().getDeclaredFields()) {
                String name = f.getName();
                if (name.equalsIgnoreCase(arg)) {
                    return name;
                }
            }
            return null;
        });

        try {
            parser.registerListener(new CommandListener(mainCharacter, mainMap), CommandListener.class);
        } catch (InvalidAnnotationException e) {
            e.printStackTrace();
        }

        String manuel = "User guide : \n";
        manuel += "-attack : on an entity \n" +
                "-talk : on an non hostile entity \n" +
                "-fleefrom : on an entity you are fighting with \n" +
                "-move : on a region thats connected to the one you're in \n" +
                "-pickup : on an item present int the map you're in \n" +
                "-equip : on an item present in your inventory \n" +
                "-ditch : on an item thats present in your inventory \n" +
                "-unequip : on an item you have in head \n" +
                "-use : on an item you have in your inventory \n" +
                "-see : on an attribute you wish to see the status";
        System.out.println(manuel);
        System.out.println("Your name is hervé, you are in " + this.mainCharacter.getLocation().getName() +
                ". You can go to : " + (this.mainCharacter.getLocation().getRegionNamesOnDirection()));
        while(sc.hasNextLine()) {
            String cmd = sc.nextLine();

            try {
                parser.parse(this.mainCharacter, cmd).send();
            } catch (InvalidCommandException e) {
                System.err.println(e.getMessage());
            }
        }
    }

}
