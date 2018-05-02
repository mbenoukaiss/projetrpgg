package projetrpg;

import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import projetrpg.entities.*;
import projetrpg.entities.items.Item;
import projetrpg.entities.player.Player;
import projetrpg.map.*;

import java.util.ArrayList;

import com.google.gson.Gson;
import projetrpg.map.MapSerializer;
import projetrpg.map.RegionSerializer;
import projetrpg.game.Game;
import projetrpg.game.GameView;
import projetrpg.quest.*;

/**
 * Created on 30/03/18.
 *
 * @author mbenoukaiss
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Galaxy Explorer");
        primaryStage.getIcons().add(new Image("icon.png"));

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

        Region forest = new Region(6, "Forest", westRegion);
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

        Teleporter caveTeleporter = new Teleporter("caveTeleporter", cave);
        caveTeleporter.addItemToRepair(Item.TOOLKIT);
        cave.addTeleporter(caveTeleporter);

        Teleporter volcanoTeleporter = new Teleporter("volcanoTeleporter", volcano);
        volcanoTeleporter.addItemToRepair(Item.FLASHLIGHT);
        volcano.addTeleporter(volcanoTeleporter);

        caveTeleporter.link(volcanoTeleporter);

        // Items linking to regions.
        northRegion.addItemToInventory(Item.HATCHET);
        southRegion.addItemToInventory(Item.APPLE);
        estRegion.addItemToInventory(Item.FLASHLIGHT);
        forest.addItemToInventory(Item.TOOLKIT);

        //NPC's initialization.
        NPC zorg = new NPC("Zorg", northRegion, EntityType.VAMPIRE, true, 100,
                20, "Im gonna kill you !");
        NPC jean = new NPC("Jean", southRegion, EntityType.VILLAGER, false, 100,
                0, "Hey, wassup boi !");
        //Player initialization.
        Player player = new Player("Hervé", 0, centerRegion, null, 100,
                10, EntityType.PLAYER, false, 50);

        Objective firstObjective = new Objective("Talk to Jean", ObjectiveType.TALK);
        Objective secondObjective = new Objective("Pickup the Apple", ObjectiveType.PICKUP);
        Quest firstQuest = new Quest(10, "A fresh Start");

        firstQuest.linkObjective(firstObjective);
        firstQuest.linkObjective(secondObjective);
        firstQuest.addObserver(player);
        firstQuest.linkRewardedItem(Item.APPLE);
        firstQuest.linkRewardedItem(Item.FLASHLIGHT);
        firstObjective.setConcernedNPC(jean);
        firstObjective.addObserver(firstQuest);
        secondObjective.setConcernedItem(Item.APPLE);
        secondObjective.addObserver(firstQuest);
        player.setCurrentQuest(firstQuest);

        //MainMap initialization.
        MainMap mainMap = new MainMap("FacticeMap");
        mainMap.setMainCharacter(player);
        mainMap.setSpawnPoint(centerRegion);
        mainMap.setHumanCount(100);
        regions.forEach(mainMap::addRegion);

        testSerialization(mainMap);

        //Party initialization.
        Game game = new Game(mainMap);

        GameView vue = new GameView(game, this);
        primaryStage.setScene(vue.getScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
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
