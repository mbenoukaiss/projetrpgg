package projetrpg.quest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import projetrpg.entities.NPC;
import projetrpg.entities.items.Item;
import projetrpg.utils.AnnotationExclusionStrategy;

import static org.junit.Assert.*;

public class ObjectiveTypeAdapterTest {

    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setExclusionStrategies(new AnnotationExclusionStrategy())
            .registerTypeAdapter(Objective.class, new Objective.TypeAdapter())
            .create();

    private Objective<NPC> o1 = new Objective<>("Premier objectif", ObjectiveType.KILL);
    private String serializedO1 =
            "{\n" +
            "  \"finished\": false,\n" +
            "  \"description\": \"Premier objectif\",\n" +
            "  \"type\": \"KILL\"\n" +
            "}";

    private Objective<NPC> o2 = new Objective<>("Deuxième objectif", ObjectiveType.TALK);
    private String serializedO2 =
            "{\n" +
            "  \"finished\": false,\n" +
            "  \"description\": \"Deuxième objectif\",\n" +
            "  \"type\": \"TALK\"\n" +
            "}";

    private Objective<Item> o3 = new Objective<>("Troisième objectif", ObjectiveType.USE);
    private String serializedO3 =
            "{\n" +
            "  \"finished\": false,\n" +
            "  \"description\": \"Troisième objectif\",\n" +
            "  \"type\": \"USE\"\n" +
            "}";

    private Objective<Item> o4 = new Objective<>("Quatrième objectif", ObjectiveType.PICKUP);
    private String serializedO4 =
            "{\n" +
            "  \"finished\": false,\n" +
            "  \"description\": \"Quatrième objectif\",\n" +
            "  \"type\": \"PICKUP\"\n" +
            "}";

    @Test
    public void serialize() {
        assertEquals(serializedO1, gson.toJson(o1));
        assertEquals(serializedO2, gson.toJson(o2));
        assertEquals(serializedO3, gson.toJson(o3));
        assertEquals(serializedO4, gson.toJson(o4));
    }

    @Test
    public void deserialize() {
        Objective o1deser = gson.fromJson(serializedO1, Objective.class);
        Objective o2deser = gson.fromJson(serializedO2, Objective.class);
        Objective o3deser = gson.fromJson(serializedO3, Objective.class);
        Objective o4deser = gson.fromJson(serializedO4, Objective.class);

        assertEquals(o1, o1deser);
        assertEquals(o2, o2deser);
        assertEquals(o3, o3deser);
        assertEquals(o4, o4deser);
    }
}