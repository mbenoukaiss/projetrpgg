package projetrpg;

import projetrpg.entities.player.Player;
import projetrpg.map.MainMap;
import projetrpg.map.Region;

public class Partie {
    private Player mainCharacter;
    private MainMap mainMap;

    public Partie(MainMap m, Player p) {
        this.mainMap = m;
        this.mainCharacter = p;
    }
}
