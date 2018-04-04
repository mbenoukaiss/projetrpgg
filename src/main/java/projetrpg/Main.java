package projetrpg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import projetrpg.entities.EntityType;
import projetrpg.entities.NPC;
import projetrpg.entities.items.Inventory;
import projetrpg.entities.items.Item;
import projetrpg.entities.items.ItemType;
import projetrpg.entities.player.Ability;
import projetrpg.entities.player.AttackType;
import projetrpg.entities.player.Player;
import projetrpg.map.Direction;
import projetrpg.map.MainMap;
import projetrpg.map.Region;
import projetrpg.map.Teleporter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created on 30/03/18.
 *
 * @author mbenoukaiss
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Item hatchet = new Item("Hatchet", 20, ItemType.DMG);
        Item light = new Item("Light", 20, ItemType.UTILS);
        Item apple = new Item("Apple", 0, ItemType.UTILS);

        ArrayList<Region> regions = new ArrayList<>();
        Region centerRegion = new Region(null, "Center", 50);
        regions.add(centerRegion);
        Region northRegion = new Region(null, "North", 50);
        regions.add(northRegion);
        Region southRegion = new Region(null, "South", 50);
        regions.add(southRegion);
        Region estRegion = new Region(null, "Est", 50);
        regions.add(estRegion);
        Region westRegion = new Region(null, "West", 50);
        regions.add(westRegion);

        centerRegion.linkToDirection(northRegion, Direction.NORTH);
        centerRegion.linkToDirection(southRegion, Direction.SOUTH);
        centerRegion.linkToDirection(estRegion, Direction.EST);
        centerRegion.linkToDirection(westRegion, Direction.WEST);

        northRegion.addItemToInventory(hatchet);
        southRegion.addItemToInventory(apple);
        estRegion.addItemToInventory(light);

        NPC zorg = new NPC("Zorg", northRegion, EntityType.VAMPIRE, true, 100, 100,
                true);

        Player player = new Player("Hervé", 0, centerRegion, null, 100,  10,
                EntityType.PLAYER, false, 50);

        MainMap mainMap = new MainMap("FacticeMap",centerRegion, regions, 100);

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
}
