package projetrpg.commands;

import projetrpg.entities.Attacker;
import projetrpg.entities.Damageable;
import projetrpg.entities.Entity;
import projetrpg.entities.player.Player;
import projetrpg.map.Region;

public class CommandListener {

    private Player player;

    public CommandListener(Player player) {
        this.player = player;
    }

    @Listener({"attack"})
    public void combat(Damageable e) {
        System.out.println(e.getHp());
        this.player.attack(e);
        System.out.println(e.getHp());
    }

    @Listener({"moveto"})
    public void move(Region r) {
        System.out.println(this.player.getLocation().getName());
        if (this.player.getLocation().estAdjacente(r))this.player.move(r);
        System.out.println(this.player.getLocation().getName());
    }
}

