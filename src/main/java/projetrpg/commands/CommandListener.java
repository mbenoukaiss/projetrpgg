package projetrpg.commands;

import projetrpg.entities.Attacker;
import projetrpg.entities.Damageable;
import projetrpg.entities.Entity;
import projetrpg.entities.items.Item;
import projetrpg.entities.player.Player;
import projetrpg.map.Region;

public class CommandListener {

    private Player player;

    public CommandListener(Player player) {
        this.player = player;
    }

    @Listener({"attack"})
    public void combat(Damageable e) {
        if (e != null) {
            System.out.println(e.getHp());
            this.player.attack(e);
            System.out.println(e.getHp());
        } else {
            System.out.println("NULL");
        }
    }

    @Listener({"move"})
    public void move(Region r) {
        if (r != null) {
            System.out.println(this.player.getLocation().getName());
            this.player.move(r);
            System.out.println(this.player.getLocation().getName());
        } else {
            System.out.println("NULL");
        }
    }

    @Listener({"pickup"})
    public void pick(Item i) {
        if (i != null) {
            System.out.println(this.player.getInventory().getAll().size());
            this.player.pickUp(i);
            System.out.println(this.player.getInventory().getAll().size());
        } else {
            System.out.println("NULL");
        }
    }

    @Listener({"equip"})
    public void equip(Item i) {
        if (i != null) {
            System.out.println(((this.player.getItemInHand() == null)? "null" : this.player.getItemInHand().getName()));
            this.player.equip(i);
            System.out.println(this.player.getItemInHand().getName());
        } else {
            System.out.println("NULL");
        }
    }
}

