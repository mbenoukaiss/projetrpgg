package projetrpg;

import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import projetrpg.entities.*;
import projetrpg.entities.items.Item;
import projetrpg.entities.player.Ability;
import projetrpg.entities.player.AttackType;
import projetrpg.entities.player.Player;
import projetrpg.game.HomeView;
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

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        launchMenu();
    }

    public static void main(String ... args) {
        launch(args);
    }

    public void launchMenu() throws IOException {
        HomeView homeView = new HomeView(this);
        primaryStage.setScene(homeView.getScene());
        primaryStage.show();

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 3);
    }

    public void launchMainGame() throws IOException {

        this.primaryStage.setTitle("Galaxy Explorer");
        this.primaryStage.getIcons().add(new Image("icon.png"));

        MainMap mainMap = new MainMap("FacticeMap");

        //Regions initialization :
        //          NORTH
        //            |
        // WEST -- CENTER -- EST
        //            |
        //          SOUTH
        // WEST contains a region FOREST
        // FOREST contains a region CAVE
        // SOUTH contains a region FLOWERPLAINS
        // EST contains a region VOLCANO
        ArrayList<Region> regions = new ArrayList<>();
        Region centerRegion = new Region(1, "Center", null);
        regions.add(centerRegion);
        Region northRegion = new Region(2, "North", null);
        regions.add(northRegion);
        Region southRegion = new Region(3, "South", null);
        regions.add(southRegion);
        Region estRegion = new Region(4, "Est", null);
        regions.add(estRegion);
        Region westRegion = new Region(5, "West", null);
        regions.add(westRegion);

        Region forest = new Region(6, "Forest", centerRegion);
        Region cave = new Region(7, "Cave", forest);
        Region flowerPlains = new Region(8, "Flower Plains", southRegion);
        Region flowerMountains = new Region(9, "Flower Mountains", southRegion);
        flowerMountains.linkToDirection(flowerPlains, Direction.NORTH);
        Region volcano = new Region(10, "Volcano", estRegion);

        // Regions linking.
        centerRegion.linkToDirection(northRegion, Direction.NORTH);
        centerRegion.linkToDirection(southRegion, Direction.SOUTH);
        centerRegion.linkToDirection(estRegion, Direction.EST);
        centerRegion.linkToDirection(westRegion, Direction.WEST);
        westRegion.addItemNeeded(Item.FLASHLIGHT);
        westRegion.addItemNeeded(Item.TOOLKIT);

        Pair<Teleporter, Teleporter> ctovtp = mainMap.createTeleporters("CtoVtp", cave, volcano);
        ctovtp.first.addItemToRepair(Item.TOOLKIT);
        ctovtp.first.addItemToRepair(Item.TOOLKIT);
        ctovtp.second.addItemToRepair(Item.FLASHLIGHT);

        // Items linking to regions.
        northRegion.addItemToInventory(Item.HATCHET);
        southRegion.addItemToInventory(Item.APPLE);
        estRegion.addItemToInventory(Item.FLASHLIGHT);
        forest.addItemToInventory(Item.TOOLKIT);
        forest.addItemToInventory(Item.TOOLKIT);

        //NPC's initialization.
        NPC zorg = new NPC("Zorg", northRegion, EntityType.VAMPIRE, true, 300,
                10, "Im gonna kill you !");
        NPC bog = new NPC("Bog", estRegion, EntityType.VAMPIRE, true, 10, 1,
                "I'm weak");
        NPC jean = new NPC("Jean", southRegion, EntityType.VILLAGER, false, 100,
                0, "Hey, you are our hero...blablabla....very important... save the universe ... " +
                "blablabla... You may now start your first Quest : A new Dawn!");
        //Player initialization.
        Player player = new Player("Hervé", 0, centerRegion, null, 100,
                10, EntityType.PLAYER, 50, 50);

        Collection<Quest> quests = new HashSet<>();
        Objective firstObjective = new Objective("Kill Zorg", ObjectiveType.KILL);
        Objective secondObjective = new Objective("Pickup the Apple", ObjectiveType.PICKUP);
        Objective thirdObjective = new Objective("Use the Apple", ObjectiveType.USE);
        Quest firstQuest = new Quest(10, "A new Dawn", "Start your beautiful journey " +
                "towards saving the universe", 0);
        quests.add(firstQuest);

        firstQuest.linkObjective(firstObjective);
        firstQuest.linkObjective(secondObjective);
        firstQuest.linkObjective(thirdObjective);
        firstQuest.addObserver(player);
        firstQuest.linkRewardedItem(Item.APPLE);
        firstQuest.linkRewardedItem(Item.FLASHLIGHT);
        firstObjective.setConcernedNPC(zorg);
        firstObjective.addObserver(firstQuest);
        secondObjective.setConcernedItem(Item.APPLE);
        secondObjective.addObserver(firstQuest);
        thirdObjective.addObserver(firstQuest);
        thirdObjective.setConcernedItem(Item.APPLE);

        Ability fireBall = new Ability("FireBall", 1, 30, AttackType.LINE, 10);
        Ability waterBeam = new Ability("WaterBeam", 2, 50, AttackType.AOE, 20);
        player.addAbbility(fireBall);
        player.addAbbility(waterBeam);

        //MainMap initialization.
        mainMap.setMainCharacter(player);
        mainMap.setSpawnPoint(centerRegion);
        mainMap.setHumanCount(100);
        regions.forEach(mainMap::addRegion);
        quests.forEach(mainMap::addQuest);

        testSerialization(mainMap);

        //Party initialization.
        Game game = new Game(mainMap);

        GameView vue = new GameView(game, this);
        primaryStage.setScene(vue.getScene());
        primaryStage.show();

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 1.5);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 3);
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
                .registerTypeAdapter(Objective.class, new ObjectiveSerializer())
                .registerTypeAdapter(Quest.class, new QuestSerializer())
                .registerTypeAdapter(Teleporter.class, new TeleporterSerializer())
                .registerTypeAdapter(Region.class, new RegionSerializer())
                .registerTypeAdapter(MainMap.class, new MapSerializer())
                .create();

        System.out.println("--- MAP SERIALIZEE -------------------------------------------");
        System.out.println(gson.toJson(map));
        System.out.println("--- MAP SERIALIZEE DESERIALIZEE RESERIALIZEE -----------------");
        System.out.println(gson.toJson(gson.fromJson(gson.toJson(map), MainMap.class)));
    }
}
