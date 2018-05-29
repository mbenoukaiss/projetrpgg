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

        Game game = new Game(save);

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
        Planet saturne = new Planet(5, "Saturne", null, 3);
        regions.add(saturne);
        saturne.addItemNeeded(Item.KNIFE);

        Region forest = new Region(6, "Forest", earth, 0);
        Region cave = new Region(7, "Cave", forest, 0);
        Region flowerPlains = new Region(8, "Flower", earth, 0);
        Region flowerHill = new Region(9, "Flower Hill", earth, 0);
        Region volcano = new Region(10, "Volcano", mars, 0);
        Region crater = new Region(12, "Crater",moon, 0);
        Region gas = new Region(13, "Lot of gas where you can't land",saturne, 9999);
        Region hotrock = new Region(13, "Hot rock",venus, 1);
        mars.setLandingRegion(volcano);
        earth.setLandingRegion(forest);
        moon.setLandingRegion(crater);
        saturne.setLandingRegion(gas);
        venus.setLandingRegion(hotrock);

        forest.linkToDirection(flowerHill, Direction.SOUTH);
        flowerHill.linkToDirection(flowerPlains, Direction.EST);

        Pair<Teleporter, Teleporter> ctovtp = mainMap.createTeleporters("CtoVtp", cave, volcano);

        // Items linking to regions.
        mars.addItemToInventory(Item.HATCHET);
        moon.addItemToInventory(Item.APPLE);
        venus.addItemToInventory(Item.FLASHLIGHT);
        forest.addItemToInventory(Item.TOOLKIT);
        forest.addItemToInventory(Item.TOOLKIT);

        //NPC's initialization.
        NPC zorg = new NPC("Zorg", volcano, EntityType.VAMPIRE, true, 300,
                10, "Im gonna kill you !");
        NPC bog = new NPC("Bog", cave, EntityType.VAMPIRE, true, 10, 1,
                "I'm weak");
        NPC jean = new NPC("Jean", forest, EntityType.VILLAGER, false, 100,
                0, "Hey, you are our hero...blablabla....very important... save the universe ... " +
                "blablabla... You may now start your first Quest : A new Dawn!");
        //Player initialization.
        Ship playerShip = new Ship(11, "Hervé's Ship", null, 50);
        Player player = new Player("Hervé", 0, forest, null, 100,
                10, EntityType.PLAYER, 50, 50);
        player.setShip(playerShip);
        player.getLocation().addContainedRegion(playerShip);
        ShipAmelioration.ENGINE_AMELIORATION.addItemNeeded(Item.TOOLKIT);
        ShipAmelioration.REACTORS_AMELIORATION.addItemNeeded(Item.KNIFE);
        ShipAmelioration.RADAR_AMELIORATION.addItemNeeded(Item.FLASHLIGHT);

        Collection<Quest> quests = new HashSet<>();
        Objective<NPC> firstObjective = new Objective<>("Kill Zorg", ObjectiveType.KILL);
        Objective<Item> secondObjective = new Objective<>("Pickup the Apple", ObjectiveType.PICKUP);
        Objective<Item> thirdObjective = new Objective<>("Use the Apple", ObjectiveType.USE);
        Quest firstQuest = new Quest(10, "A new Dawn", "Start your beautiful journey " +
                "towards saving the universe", 0);
        quests.add(firstQuest);

        firstQuest.linkObjective(firstObjective);
        firstQuest.linkObjective(secondObjective);
        firstQuest.linkObjective(thirdObjective);
        firstQuest.addObserver(player);
        firstQuest.linkRewardedItem(Item.APPLE);
        firstQuest.linkRewardedItem(Item.FLASHLIGHT);
        firstObjective.setConcernedObject(zorg);
        firstObjective.addObserver(firstQuest);
        secondObjective.setConcernedObject(Item.APPLE);
        secondObjective.addObserver(firstQuest);
        thirdObjective.addObserver(firstQuest);
        thirdObjective.setConcernedObject(Item.APPLE);

        Ability fireBall = new Ability("FireBall", 1, 30, AttackType.LINE, 10);
        Ability waterBeam = new Ability("WaterBeam", 2, 50, AttackType.AOE, 20);
        player.addAbbility(fireBall);
        player.addAbbility(waterBeam);

        //MainMap initialization.
        mainMap.setMainCharacter(player);
        mainMap.setSpawnPoint(earth);
        mainMap.setHumanCount(100);
        for (Planet p : regions) {
            mainMap.addRegion(p);
        }
        quests.forEach(mainMap::addQuest);

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
