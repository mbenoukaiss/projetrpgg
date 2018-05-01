package projetrpg.map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import projetrpg.AnnotationExclusionStrategy;
import projetrpg.entities.EntityType;
import projetrpg.entities.player.Player;
import projetrpg.quest.Quest;

import static org.junit.Assert.*;

public class TeleporterSerializerTest {

    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setExclusionStrategies(new AnnotationExclusionStrategy())
            .registerTypeAdapter(Teleporter.class, new TeleporterSerializer())
            .registerTypeAdapter(Region.class, new RegionSerializer())
            .create();

    Teleporter t1 = new Teleporter("t1", null);
    private String serializedT1 =
            "{\n" +
            "  \"id\": 0,\n" +
            "  \"name\": \"t1\",\n" +
            "  \"repaired\": false,\n" +
            "  \"link\": 1,\n" +
            "  \"requirements\": []\n" +
            "}";

    Teleporter t2 = new Teleporter("t2", null);
    private String serializedT2 =
            "{\n" +
            "  \"id\": 1,\n" +
            "  \"name\": \"t2\",\n" +
            "  \"repaired\": false,\n" +
            "  \"link\": 0,\n" +
            "  \"requirements\": []\n" +
            "}";

    Teleporter t3 = new Teleporter("t3", null);
    private String serializedT3 =
            "{\n" +
            "  \"id\": 2,\n" +
            "  \"name\": \"t3\",\n" +
            "  \"repaired\": false,\n" +
            "  \"link\": -1,\n" +
            "  \"requirements\": []\n" +
            "}";

    private void initialization() {
        t1.link(t2);
    }

    @Test
    public void serialize() {
        initialization();

        assertEquals(serializedT1, gson.toJson(t1));
        assertEquals(serializedT2, gson.toJson(t2));
        assertEquals(serializedT3, gson.toJson(t3));
    }

    @Test
    public void deserialize() {
        Teleporter t1deser = gson.fromJson(serializedT1, Teleporter.class);
        Teleporter t2deser = gson.fromJson(serializedT2, Teleporter.class);
        Teleporter t3deser = gson.fromJson(serializedT3, Teleporter.class);
        TeleporterSerializer.linkTeleporters();

        assertEquals(t1, t1deser);
        assertEquals(t2, t2deser);
        assertEquals(t3, t3deser);
    }
}