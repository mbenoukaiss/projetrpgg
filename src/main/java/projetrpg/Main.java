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

        Item hatchet = new Item("Hatchet", 10, ItemType.DMG);
        Item light = new Item("Light", 0, ItemType.UTILS);
        ArrayList<Item> inventoryItems = new ArrayList<>();
        inventoryItems.add(hatchet);
        inventoryItems.add(light);

        Inventory inventory = new Inventory(10, inventoryItems);

        Region earth = new Region(null, "Earth", inventory);
        Region moon = new Region(null, "Moon", inventory);
        ArrayList<Region> regions = new ArrayList<>();
        regions.add(earth);
        regions.add(moon);

        NPC jean = new NPC("Jean", earth, EntityType.VILLAGER, false, 10, 150, 0, false);
        NPC zorg = new NPC("Zorg", earth, EntityType.VAMPIRE, true, 100, 150, 50, true);

        Region aquitaine = new Region(earth, "Aquitaine", inventory);
        Region cratere = new Region(moon, "Cratère", inventory);

        Teleporter earthT = new Teleporter("EarthTeleporter", aquitaine);
        Teleporter moonT = new Teleporter("MoonTeleporter", cratere);

        Ability fireBall = new Ability("FireBall", 0, 20, AttackType.LINE);
        HashSet<Ability> abilities = new HashSet<>();
        abilities.add(fireBall);
        ArrayList<Item> Inventory = new ArrayList<>();
        Inventory.add(light);
        Inventory playerInventory = new Inventory(10, Inventory);

        Player mainCharacter = new Player("Hervé", 0, earth, playerInventory, light,
                abilities, 100, 10, EntityType.PLAYER, false);

        MainMap mainMap = new MainMap("Systeme solaire", earth, regions, 100000);

        Partie partie = new Partie(mainMap, mainCharacter);

        partie.lancementJeu();

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
