package projetrpg;

import projetrpg.commands.*;
import projetrpg.entities.Entity;
import projetrpg.entities.NPC;
import projetrpg.entities.items.Item;
import projetrpg.entities.player.Player;
import projetrpg.map.MainMap;
import projetrpg.map.Region;
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
            return null; //TODO: NE PAS RETOURNER NULL C'EST PAS COOL MEC
        });

        parser.registerCommand("move", Region.class, (player, arg)-> {
            for (Region r : player.getLocation().getRegionOnDirection().values()) {
                if (r.getName().toLowerCase().equals(arg.toLowerCase())) {
                    return r;
                }
            }
            return null; //TODO: NE PAS RETOURNER NULL C'EST PAS COOL MEC
        });

        parser.registerCommand("pickup", Item.class, (player, arg)-> {
            for (Item i : player.getLocation().getInventory().getAll()) {
                if (i.getName().toLowerCase().equals(arg.toLowerCase())) {
                    return i;
                }
            }
            return null; //TODO: NE PAS RETOURNER NULL C'EST PAS COOL MEC
        });

        parser.registerCommand("equip", Item.class, (player, arg)-> {
            for (Item i : player.getInventory().getAll()) {
                if (i.getName().toLowerCase().equals(arg.toLowerCase())) {
                    return i;
                }
            }
            return null; //TODO: NE PAS RETOURNER NULL C'EST PAS COOL MEC
        });

        parser.registerCommand("ditch", Item.class, (player, arg)-> {
            for (Item i : player.getInventory().getAll()) {
                if (i.getName().toLowerCase().equals(arg.toLowerCase())) {
                    return i;
                }
            }
            return null; //TODO: NE PAS RETOURNER NULL C'EST PAS COOL MEC
        });

        parser.registerCommand("unequip", Item.class, (player, arg)-> {
            if (player.getItemInHand() != null && player.getItemInHand().getName().equalsIgnoreCase(arg)) {
                return player.getItemInHand();
            }
            return null; //TODO: NE PAS RETOURNER NULL C'EST PAS COOL MEC
        });

        try {
            parser.registerListener(new CommandListener(mainCharacter), CommandListener.class);
        } catch (InvalidAnnotationException e) {
            e.printStackTrace();
        }

        try {
            System.out.println("Vous vous appelez hervé, vous êtes au centre. Vous pouvez aller au nord, sud, est ouest.");
            while(sc.hasNext()) {
                String cmd = sc.nextLine();
                parser.parse(this.mainCharacter, cmd).send();
            }

        } catch (InvalidCommandException e) {
            System.out.println(e.getMessage());
        }
    }
}
