package projetrpg.commands;

import projetrpg.entities.Attacker;
import projetrpg.entities.Damageable;
import projetrpg.entities.Entity;
import projetrpg.entities.NPC;
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
            System.out.println(((Entity) e).getName() + " : " + ((NPC) e).getDialogue());
            System.out.println("Hps de l'ennemi avant l'attaque : " + e.getHp());
            if (this.player.attack(e)) {
                System.out.println("Félicitations, vous avez tuer " + ((Entity) e).getName() + ", vous avez gagner " +
                        ((Entity) e).getType().getExperienceRewarded() + " exp! You are level " + this.player.getLevel());
            } else {
                System.out.println("Hps de l'ennemi après l'attaque : " + e.getHp());
                System.out.println("......\nIl vous attaque!");
                System.out.println("Vos hps avant l'attaque : " + player.getHp());
                ((NPC) e).attack(player);
                System.out.println("Vos hps après l'attaque : " + player.getHp());
                System.out.println("Vous pouvez fuir, ou continuer à attaquer.");
            }
        } else {
            System.out.println("NULL");
        }
    }

    @Listener({"talk"})
    public void talk(NPC e) {
        if (e != null) {
            System.out.println(e.getDialogue());
        } else {
            System.out.println("NULL");
        }
    }

    @Listener({"fleefrom"})
    public void fleeFrom(NPC e) {
        if (e != null && e.isInFight()) {
            e.setHps(e.getBaseHps());
            System.out.println("You fled");
            e.setInFight(false);
        } else {
            System.out.println("NULL");
        }
    }

    @Listener({"move"})
    public void move(Region r) {
        if (r != null) {
            System.out.println("Position initiale du joueur : " + this.player.getLocation().getName());
            this.player.move(r);
            System.out.println("Position actuelle du joueur : " + this.player.getLocation().getName() + ". "
                    + r.describe());
        } else {
            System.out.println("NULL");
        }
    }

    @Listener({"pickup"})
    public void pick(Item i) {
        if (i != null) {
            System.out.println("Taille de l'inventaire du joueur avant : " + this.player.getInventory().getAll().size());
            System.out.println("Taille de l'inventaire de la région avant : " + this.player.getLocation().getInventory().getAll().size());
            this.player.pickUp(i);
            System.out.println("Taille de l'inventaire du joueur après : " + this.player.getInventory().getAll().size());
            System.out.println("Taille de l'inventaire de la région après : " + this.player.getLocation().getInventory().getAll().size());
        } else {
            System.out.println("NULL");
        }
    }

    @Listener({"equip"})
    public void equip(Item i) {
        if (i != null) {
            System.out.println("Item précédemment équipé : " + ((this.player.getItemInHand() == null)? "rien" : this.player.getItemInHand().getName()));
            this.player.equip(i);
            System.out.println("Item actuellement équipé : " + ((this.player.getItemInHand() == null)? "rien" : this.player.getItemInHand().getName()));
        } else {
            System.out.println("NULL");
        }
    }

    @Listener({"ditch"})
    public void ditch(Item i) {
        if (i != null) {
            System.out.println("Taille de l'inventaire du joueur avant : " + this.player.getInventory().getAll().size());
            System.out.println("Taille de l'inventaire de la région avant : " + this.player.getLocation().getInventory().getAll().size());
            this.player.ditch(i);
            System.out.println("Taille de l'inventaire du joueur après : " + this.player.getInventory().getAll().size());
            System.out.println("Taille de l'inventaire de la région après : " + this.player.getLocation().getInventory().getAll().size());
        } else {
            System.out.println("NULL");
        }
    }

    @Listener({"unequip"})
    public void unequip(Item i) {
        if (i != null) {
            System.out.println("Item précédemment équipé : " + ((this.player.getItemInHand() == null)? "rien" : this.player.getItemInHand().getName()));
            this.player.unequip(i);
            System.out.println("Item actuellement équipé : " + ((this.player.getItemInHand() == null)? "rien" : this.player.getItemInHand().getName()));
        } else {
            System.out.println("NULL");
        }
    }
}

