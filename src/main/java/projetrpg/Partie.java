package projetrpg;

import projetrpg.commands.Command;
import projetrpg.commands.CommandParser;
import projetrpg.commands.InvalidCommandException;
import projetrpg.entities.Entity;
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

    public void lancementJeu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Vous vous appelez hervé, vous êtes sur terre.");
        String cmd = sc.nextLine();
        CommandParser parser = new CommandParser();
        //parser.registerCommand(cmd, Entity.class, (arg) -> );
        try {
            Command c = parser.parse(cmd);

        } catch (InvalidCommandException e) {
            System.out.println(e.getMessage());
        }
    }
}
