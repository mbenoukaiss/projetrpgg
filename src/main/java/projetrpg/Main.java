package projetrpg;

import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import projetrpg.entities.*;
import projetrpg.entities.items.Item;
import projetrpg.entities.items.ItemType;
import projetrpg.entities.player.Player;
import projetrpg.map.*;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import projetrpg.map.MapSerializer;
import projetrpg.map.RegionSerializer;

/**
 * Created on 30/03/18.
 *
 * @author mbenoukaiss
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Item hatchet = new Item("Hatchet", 40, ItemType.DMG);
        Item light = new Item("Light", 20, ItemType.UTILS);
        Item apple = new Item("Apple", 0, ItemType.FOOD);

        ArrayList<Region> regions = new ArrayList<>();
        Region centerRegion = new Region(0, "Center", null);
        regions.add(centerRegion);
        Region northRegion = new Region(1, "North", null);
        regions.add(northRegion);
        Region southRegion = new Region(2, "South", null);
        regions.add(southRegion);
        Region estRegion = new Region(3, "Est", null);
        regions.add(estRegion);
        Region westRegion = new Region(4, "West", null);
        regions.add(westRegion);

        centerRegion.linkToDirection(northRegion, Direction.NORTH);
        centerRegion.linkToDirection(southRegion, Direction.SOUTH);
        centerRegion.linkToDirection(estRegion, Direction.EST);
        centerRegion.linkToDirection(westRegion, Direction.WEST);

        northRegion.addItemToInventory(hatchet);
        southRegion.addItemToInventory(apple);
        estRegion.addItemToInventory(light);

        NPC zorg = new NPC("Zorg", northRegion, EntityType.VAMPIRE, true, 100,
                20, "Im gonna kill you !");
        NPC jean = new NPC("Jean", southRegion, EntityType.VILLAGER, false, 100,
                0, "Hey, wassup boi !");

        Player player = new Player("Hervé", 0, centerRegion, null, 100,
                10, EntityType.PLAYER, false, 50);

        MainMap mainMap = new MainMap("FacticeMap", centerRegion, regions, 100);

        Partie partie = new Partie(mainMap, player);

        partie.lauchGame();

        ResourceBundle resources = ResourceBundle.getBundle("bundles/game", Locale.ENGLISH);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui.fxml"), resources);

        Scene scene = new Scene(root, 900, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void testSerialization(MainMap map) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(NPC.class, new EntitySerializer())
                .registerTypeAdapter(Region.class, new RegionSerializer())
                .registerTypeAdapter(MainMap.class, new MapSerializer())
                .create();

        System.out.println(gson.toJson(map));
    }
}
