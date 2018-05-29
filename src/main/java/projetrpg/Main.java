package projetrpg;

import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import projetrpg.entities.*;
import projetrpg.entities.items.Item;
import projetrpg.entities.player.*;
import projetrpg.map.Region;
import projetrpg.menu.Home;
import projetrpg.menu.HomeView;
import projetrpg.menu.save.Save;
import projetrpg.menu.save.SaveManager;
import projetrpg.menu.save.SavesServices;
import projetrpg.utils.Pair;
import projetrpg.map.*;

import java.io.IOException;
import java.nio.channels.NonWritableChannelException;
import java.util.*;

import com.google.gson.Gson;
import projetrpg.map.MapSerializer;
import projetrpg.map.RegionSerializer;
import projetrpg.game.Game;
import projetrpg.game.GameView;
import projetrpg.quest.*;
import projetrpg.utils.AnnotationExclusionStrategy;

/**
 * Created on 30/03/18.
 *
 * @author mbenoukaiss
 */
public class Main extends Application {

    private SavesServices savesServices;

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.savesServices = new SavesServices("./saves/");

        launchMenu();
    }

    public static void main(String ... args) {
        launch(args);
    }

    public void launchMenu() {
        this.primaryStage.setTitle("Galaxy Explorer Menu");
        this.primaryStage.getIcons().add(new Image("icon.png"));
        HomeView homeView = new HomeView(this, new Home(savesServices));
        primaryStage.setScene(homeView.getScene());
        primaryStage.show();

        primaryStage.centerOnScreen();
    }

    public void launchSavesMenu(SaveManager sm) {
        this.primaryStage.setTitle("Saves manager");
        primaryStage.setScene(sm.scene());
        primaryStage.show();

        primaryStage.centerOnScreen();
    }

    public void launchMainGame(Save save) {
        this.primaryStage.setTitle("Galaxy Explorer");

        Save testSave = new Save(null, createTestMap(), Calendar.getInstance(), Calendar.getInstance());

        Game game = new Game(testSave);

        GameView vue = null;
        try {
            vue = new GameView(game, this, savesServices);
        } catch(IOException e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Could not load resources required to start the game.");
            a.show();
            Platform.exit();
        }
        primaryStage.setScene(vue.getScene());
        primaryStage.show();
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);

    }

    private MainMap createTestMap() {
        MainMap mainMap = new MainMap("FacticeMap");

        ArrayList<Planet> regions = new ArrayList<>();
        Planet earth = new Planet(1, "Earth", null, 0);
        regions.add(earth);
        Planet mars = new Planet(2, "Mars", null, 1);
        regions.add(mars);
        Planet moon = new Planet(3, "Moon", null, 1);
        regions.add(moon);
        Planet venus = new Planet(4, "Venus", null, 2);
        regions.add(venus);
        Planet saturn = new Planet(5, "Saturn", null, 3);
        regions.add(saturn);
        Planet jupiter = new Planet(6, "Jupiter", null, 2);
        regions.add(jupiter);

        Region America = new Region(7, "America", "This is the America continent.", earth, 0);
        Region Europe = new Region(8, "Europe", "This is the Europe continent.", earth, 0);
        Region Asia = new Region(9, "Asia", "This is the Asia continent.", earth, 0);
        Region Oceania = new Region(10, "Oceania", "This is the Oceania continent.", earth, 0);
        Region Africa = new Region(11, "Africa", "This is the Africa continent.", earth, 0);
        Region Antarctica = new Region(19, "Antarctica", "This is the Antarctica continent", earth, 0);
        earth.setLandingRegion(Europe);
        America.linkToDirection(Europe, Direction.EST);
        Europe.linkToDirection(Asia, Direction.EST);
        Asia.linkToDirection(Oceania, Direction.SOUTH);
        Europe.linkToDirection(Africa, Direction.SOUTH);
        Antarctica.linkToDirection(Asia, Direction.SOUTH);

        Region NewYork = new Region(12, "New-York", "The city of New York", America, 0);
        Region Cleveland = new Region(13, "Cleveland", "The city of Cleveland", America, 0);
        Region SanAntonio = new Region(14, "San Antonio", "The city of San Antonio", America, 0);
        Region Boston = new Region(15, "Boston", "The city of Boston", America, 0);
        NewYork.linkToDirection(Boston, Direction.NORTH);
        NewYork.linkToDirection(Cleveland, Direction.WEST);
        Cleveland.linkToDirection(SanAntonio, Direction.SOUTH);

        Region Paris = new Region(16, "Paris", "The city of Paris", Europe, 0);
        Region London = new Region(17, "London", "The city of London", Europe, 0);
        Region Berlin = new Region(18, "Berlin", "The city of Berlin", Europe, 0);
        Paris.linkToDirection(London, Direction.NORTH);
        Paris.linkToDirection(Berlin, Direction.EST);

        Region Bombai = new Region(19, "Bombai", "The city of Bombai", Asia, 0);

        Region StPetesburg = new Region(20, "St-Petersburg", "The city of St-Petersburg", Africa, 0);


        //Player initialization.
        Ship playerShip = new Ship(11, "Hervé's Ship", null, 50);
        Player player = new Player("Hervé", 0, Paris, null, 100,
                10, EntityType.PLAYER, 50, 50);
        player.setShip(playerShip);
        player.getLocation().addContainedRegion(playerShip);
        ShipAmelioration.ENGINE_AMELIORATION.addItemNeeded(Item.TOOLKIT);
        ShipAmelioration.REACTORS_AMELIORATION.addItemNeeded(Item.KNIFE);
        ShipAmelioration.RADAR_AMELIORATION.addItemNeeded(Item.FLASHLIGHT);

        //MainMap initialization.
        mainMap.setMainCharacter(player);
        mainMap.setSpawnPoint(earth);
        mainMap.setHumanCount(100);
        for (Planet p : regions) {
            mainMap.addRegion(p);
        }
        return mainMap;
    }


    /**
     * Méthode de test pour la sérialisation/déserialisation
     * qui sérialise et désérialise à partir de ce qui vient
     * d'être sérialisé.
     *
     * @param map The original map
     * @return Supposedly the same map
     */
    public void testSerialization(MainMap map) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .setExclusionStrategies(new AnnotationExclusionStrategy())
                .setExclusionStrategies(new AnnotationExclusionStrategy())
                .registerTypeAdapter(Objective.class, new ObjectiveSerializer())
                .registerTypeAdapter(Quest.class, new QuestSerializer())
                .registerTypeAdapter(Teleporter.class, new TeleporterSerializer())
                .registerTypeAdapter(Region.class, new RegionSerializer())
                .registerTypeHierarchyAdapter(Ship.class, new ShipSerializer())
                .registerTypeHierarchyAdapter(Planet.class, new PlanetSerializer())
                .registerTypeAdapter(MainMap.class, new MapSerializer())
                .create();

        System.out.println("--- MAP SERIALIZEE -------------------------------------------");
        System.out.println(gson.toJson(map));
        System.out.println("--- MAP SERIALIZEE DESERIALIZEE RESERIALIZEE -----------------");
        System.out.println(gson.toJson(gson.fromJson(gson.toJson(map), MainMap.class)));
    }

}
