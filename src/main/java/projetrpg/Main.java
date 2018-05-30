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
        mainMap.setSpawnPoint(Europe);
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

        Player player = new Player("Hervé", 0, Europe, null, 100, 10, EntityType.PLAYER, 50, 100);

        NPC HervéBrother = new NPC("Robert", Paris, EntityType.VILLAGER, false, 100, 10, "Hey brother, " +
                "Humanity is dying and only you can save us. Go to Berlin and talk to Paul, the space agency's headquarters's master.\n You may now start your first quest : " +
                "Briefing.");
        NPC master = new NPC("Paul", Berlin, EntityType.VILLAGER, false, 100, 10, "Hello Hervé, as you may already know, " +
                "the earth can no longer welcome the human race, we are being too numerous. Nature is taking over our planet, and everyone will die if you do not " +
                "find another planet where we can live peacefully and start over humanity. I'm no expert but i know who is. You must head to London and talk to Saul" +
                ", he'll tell you more about this.");
        Quest briefing = new Quest(10, "Briefing", "You must be notified about whats going on!", 0);
        Objective<NPC> talkToMaster = new Objective<>("Talk to the space agency's headquarters's master", ObjectiveType.TALK);
        briefing.linkObjective(talkToMaster);
        briefing.addObserver(player);
        talkToMaster.setConcernedObject(master);
        talkToMaster.addObserver(briefing);
        mainMap.addQuest(briefing);

        NPC Saul  = new NPC("Saul", London, EntityType.VILLAGER, false, 100, 10, "Oh ! You must be Hervé " +
                "! I'm Saul, you are about to begin your journey towards saving the universe. But first you gotta be ready : in my apartments you'll find" +
                "a laser gun and a practise target. Gear up and show me whats you've got ! Then you'll talk to my brother, Zahir, located in my apartment.\n You may now start this new quest : Gear Up ! ");
        Region saulapp = new Region(21, "Saul's appartments", "The appartment of Saul", London, 0);

        NPC Zahir = new NPC("Zahir", saulapp, EntityType.VILLAGER, false, 100, 10, "I see you've trained a bit, Hervé, i can tell that you are ready now! " +
                "There is a planet located far from here, you need to find a way to get there, and once you do, you'll be able to save humanity. But first, you need some ship improvement : find a toolkit, " +
                "and improve your engine! In this very room you'll find a teleporter that will lead you to Asia, there you'll find the toolkit. But first, you'll need to repair it : find a screwdriver and repair" +
                "the teleporter. (It is in America uhu)");
        ArrayList<Item> tpAsiaRequired = new ArrayList<>();
        tpAsiaRequired.add(Item.SCREWDRIVER);
        Teleporter tpToAsia = new Teleporter(1, "Teleporter To Asia", saulapp, false, tpAsiaRequired);
        Teleporter tpFromAsia = new Teleporter(2, "Teleporter to saul's apartment", Asia, true, new ArrayList<>());
        tpToAsia.link(tpFromAsia);
        saulapp.addItemToInventory(Item.LASERGUN);
        America.addItemToInventory(Item.SCREWDRIVER);
        Asia.addItemToInventory(Item.TOOLKIT);
        NPC target = new NPC("Bob", saulapp, EntityType.UTILS, false, 100, 0, "Im weak.");
        Quest gearUp = new Quest(10, "Gear Up", "Equip yourself and train a bit.", 1);
        Objective<Item> pickupLaser = new Objective<>("Pickup saul's laser", ObjectiveType.PICKUP);
        Objective<Item> equipLaser = new Objective<>("Equip Saul's laser", ObjectiveType.EQUIP);
        Objective<NPC> killTarget = new Objective<>("Kill the practise target", ObjectiveType.KILL);
        gearUp.linkObjective(pickupLaser);
        gearUp.linkObjective(equipLaser);
        gearUp.linkObjective(killTarget);
        gearUp.addObserver(player);
        pickupLaser.addObserver(gearUp);
        pickupLaser.setConcernedObject(Item.LASERGUN);
        equipLaser.addObserver(gearUp);
        equipLaser.setConcernedObject(Item.LASERGUN);
        killTarget.addObserver(gearUp);
        killTarget.setConcernedObject(target);
        mainMap.addQuest(gearUp);


        //Player initialization.
        Ship playerShip = new Ship(22, "Hervé's Ship", null, 50);
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
