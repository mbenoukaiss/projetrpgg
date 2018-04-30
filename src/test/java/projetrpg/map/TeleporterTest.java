package projetrpg.map;

import org.junit.Test;
import projetrpg.entities.EntityType;
import projetrpg.entities.items.Item;
import projetrpg.entities.player.Player;

import static org.junit.Assert.*;

public class TeleporterTest {

    @Test
    public void link() {
        Teleporter t1 = new Teleporter("t1", null);
        Teleporter t2 = new Teleporter("t2", null);
        Teleporter t3 = new Teleporter("t3", null);
        Teleporter t4 = new Teleporter("t4", null);
        t1.link(t2);

        assertEquals(t1, t2.getLinkedTeleporter());
        assertEquals(t2, t1.getLinkedTeleporter());
        assertEquals(null, t3.getLinkedTeleporter(), null);

        t2.link(t3);
        assertEquals(t2, t3.getLinkedTeleporter());
        assertEquals(t3, t2.getLinkedTeleporter());
        assertEquals(null, t1.getLinkedTeleporter());
    }

}