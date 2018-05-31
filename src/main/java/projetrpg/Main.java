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
        mars.addItemNeeded(Item.HATCHET);
        Planet moon = new Planet(3, "Moon", null, 3);
        regions.add(moon);
        Planet venus = new Planet(4, "Venus", null, 2);
        regions.add(venus);
        Planet saturn = new Planet(5, "Saturn", null, 0);
        saturn.setLandingRegion(new Region(54, "Heaven", "Heaven", saturn, saturn.getShipLevelRequired()));
        regions.add(saturn);
        saturn.addItemNeeded(Item.LEGENDARY_BOOK);

        Region hotrock = new Region(4000, "Hot rock", "wow it's hot", venus, 3);
        venus.setLandingRegion(hotrock);

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
                ", he'll tell you more about this.\n");
        Quest briefing = new Quest(10, "Briefing", "You must be notified about whats going on!", 0);
        Objective<NPC> talkToMaster = new Objective<>("Talk to the space agency's headquarters's master", ObjectiveType.TALK);
        briefing.linkObjective(talkToMaster);
        briefing.addObserver(player);
        talkToMaster.setConcernedObject(master);
        talkToMaster.addObserver(briefing);


        NPC Saul  = new NPC("Saul", London, EntityType.VILLAGER, false, 100, 10, "Oh ! You must be Hervé " +
                "! I'm Saul, you are about to begin your journey towards saving the universe. But first you gotta be ready : in my apartments you'll find" +
                "a laser gun and a practise target. Gear up and show me whats you've got ! Then you'll talk to my brother, Zahir, located in my apartment.\n You may now start this new quest : Gear Up ! ");
        Region saulapp = new Region(21, "Saul's appartments", "The appartment of Saul", London, 0);

        NPC Zahir = new NPC("Zahir", saulapp, EntityType.VILLAGER, false, 100, 10, "I see you've trained a bit, Hervé, i can tell that you are ready now! " +
                "There is a planet located far from here, you need to find a way to get there, and once you do, you'll be able to save humanity. But first, you need some ship improvement : find a toolkit, " +
                "and improve your engine! In this very room you'll find a teleporter that will lead you to Asia, there you'll find the toolkit. But first, you'll need to repair it : find a screwdriver and repair" +
                " the teleporter. (It is in America uhu)");
        ArrayList<Item> tpAsiaRequired = new ArrayList<>();
        Pair<Teleporter, Teleporter> saulsapptp = mainMap.createTeleporters("Saul's appartment", Asia, saulapp);
        saulsapptp.first.addItemToRepair(Item.SCREWDRIVER);
        saulsapptp.second.repair();
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


        NPC Taliyah = new NPC("Taliyah", Asia, EntityType.VILLAGER, false, 100, 10, "You are now ready to improve your engine\nType \"improve engine\" " +
                "in order to improve it.\nTaliyah has a new Quest for you : A bad guy is stealing stuff in bombai, stop him and you'll gain his hatchet! " +
                "You may now start" +
                ": first fight! Once you have finished this quest you may talk to my brother in Bombai, he'll tell you where to go.");
        NPC badGuy = new NPC("Bad Guy", Bombai, EntityType.HOSTILE_VILLAGER, true, 100, 20, "I'm going to kill you!");
        NPC taliyahBrother = new NPC("Taliyah's brother", Bombai, EntityType.VILLAGER, false, 100, 10, "Please save me!" +
                " When you've done so you'll be able to travel to mars in order to continue your adventure!" );
        Quest firstFight = new Quest(20, "first fight", "Stop the bad guy in Bombai", 1);
        Objective<NPC> killBadGuy = new Objective<>("Stop the bad Guy", ObjectiveType.KILL);
        firstFight.linkObjective(killBadGuy);
        firstFight.addObserver(player);
        killBadGuy.addObserver(firstFight);
        killBadGuy.setConcernedObject(badGuy);
        firstFight.linkRewardedItem(Item.HATCHET);
        mainMap.addQuest(firstFight);
        mainMap.addQuest(briefing);
        mainMap.addQuest(gearUp);

        ////////////////////////////////////////////////////////////////
        //MMMMAAAAAAAAAAAAAAAAAAAAAARRRRRRRRRRRRRRRRRRRRRRRRSSSSSSSSSSS
        ////////////////////////////////////////////////////////////////
        int marsHP = 50, marsDamage = 30;
        Region landingPlatform = new Region(1000, "Landing platform", "You are on the landing platform of Mars, it seems really calm, do you see that cliff ?.", mars, 0);
        mars.setLandingRegion(landingPlatform);

        Region terryposFarm = new Region(1010, "Terrypo's farm", "A few weird potatoes are growing here, someone is taking care of this farm.", mars, 0);
        terryposFarm.addItemToInventory(Item.GREEN_POTATO);
        terryposFarm.addItemToInventory(Item.GREEN_POTATO);

        Region daglysvalCliff = new Region(1001, "Daglysval cliff", "This sharp cliff can't be climbed with your current gear, beware the monsters...", mars, 0);
        NPC nfrey = new NPC("Nfrey", daglysvalCliff, EntityType.MARTIAN, true, marsHP+5, marsDamage+5, "ספר ההזמנות");
        NPC nfray = new NPC("Nfray", daglysvalCliff, EntityType.MARTIAN, true, marsHP+5, marsDamage+5, "סהמנופר תהז");
        daglysvalCliff.addEntity(nfrey);
        daglysvalCliff.addEntity(nfray);
        daglysvalCliff.addEntity(new NPC("Nfroy", daglysvalCliff, EntityType.MARTIAN, false, marsHP+5, marsDamage+5, "...my brothers became... crazy... run! You may now start your first quest on Mars : Kill the twins"));
        daglysvalCliff.linkToDirection(landingPlatform, Direction.EST);

        Quest killTheTwins = new Quest(120, "Kill the twins", "Nfroy needs you to kill the two twins right next to him", 2);
        killTheTwins.addObserver(mainMap.getMainCharacter());
        killTheTwins.linkRewardedItem(Item.CLIMBING_SHOES);
        Objective<NPC> killNfrey = new Objective<>("Kill Nfrey", ObjectiveType.KILL);
        killNfrey.setConcernedObject(nfrey);
        killTheTwins.linkObjective(killNfrey);
        Objective<NPC> killNfray = new Objective<>("Kill Nfray", ObjectiveType.KILL);
        killNfray.setConcernedObject(nfray);
        killTheTwins.linkObjective(killNfray);

        Region daglysvalPlateau = new Region(1002, "Daglysval plateau", "It seems like a ship crashed on this planet a while ago, there are fragments everywhere, from here to the cave, around the landing platform...", mars, 0);
        daglysvalPlateau.addEntity(new NPC("Matteo", daglysvalPlateau, EntityType.BOSS_PEACEFUL, false, 0, 0, "Looks like I got there before you WITH MY BRAND NEW CLIMBING BOOTS CHECK MY MERCH, I just need to find some stuff in the cave and I'll finally leave this planet (wow im an antagonist and im saying where u need to go to kill me!!"));
        daglysvalPlateau.addItemToInventory(Item.SHARP_SHIP_FRAGMENT);
        daglysvalPlateau.addItemToInventory(Item.SCREW);
        daglysvalPlateau.addItemToInventory(Item.SCREW);
        daglysvalPlateau.addItemNeeded(Item.CLIMBING_SHOES);
        daglysvalCliff.linkToDirection(daglysvalPlateau, Direction.WEST);

        Pair<Teleporter, Teleporter> dgtps = mainMap.createTeleporters("Daglysval", daglysvalPlateau, earth);
        dgtps.first.addItemToRepair(Item.SCREW);
        dgtps.first.addItemToRepair(Item.SCREW);
        dgtps.first.addItemToRepair(Item.SCREWDRIVER);
        dgtps.first.addItemToRepair(Item.METAL_SCRAP);
        dgtps.second.addItemToRepair(Item.GREEN_POTATO);
        dgtps.second.addItemToRepair(Item.GREEN_CARROT);

        Region caveEntrance = new Region(1003, "Cave entrance", "???", mars, 0);
        caveEntrance.addItemToInventory(Item.SCREW);
        caveEntrance.addItemToInventory(Item.SCREW);
        caveEntrance.addItemToInventory(Item.METAL_SCRAP);
        caveEntrance.addItemToInventory(Item.METAL_SCRAP);
        caveEntrance.addEntity(new NPC("Zahuit", caveEntrance, EntityType.MARTIAN, true, marsHP+25, marsDamage+25, "האקדמריאיה ללקדמין השועבת"));
        caveEntrance.linkToDirection(landingPlatform, Direction.SOUTH);

        Region caveInside = new Region(1004, "Cave inside", "???", caveEntrance, 0);

        Region caveInsideWest = new Region(1005, "Cave inside west", "???", caveEntrance, 0);
        caveInsideWest.linkToDirection(caveInside, Direction.EST);

        Region terryposHole = new Region(1007, "Terrypo's hole", "???", caveInsideWest, 0);
        terryposHole.addEntity(new NPC("Terrypo", terryposHole, EntityType.MARTIAN, false, marsHP+130, 0, "Hey, how did you get there ? Are you that same guy with the weird boots called MATTEO HEVIN?????"));
        terryposHole.addItemToInventory(Item.GREEN_CARROT);
        terryposHole.addItemToInventory(Item.GREEN_CARROT);
        terryposHole.addItemToInventory(Item.GREEN_CARROT);
        terryposHole.addItemToInventory(Item.GREEN_CARROT);
        terryposHole.addItemToInventory(Item.GREEN_CARROT);
        terryposHole.addItemToInventory(Item.GREEN_CARROT);
        terryposHole.addItemToInventory(Item.GREEN_CARROT);
        terryposHole.addItemToInventory(Item.GREEN_POTATO);
        terryposHole.addItemToInventory(Item.GREEN_POTATO);
        terryposHole.addItemToInventory(Item.GREEN_POTATO);
        terryposHole.addItemToInventory(Item.APPLE);
        terryposHole.linkToDirection(caveInside, Direction.SOUTH);

        Region caveInsideEast = new Region(1006, "Cave inside east", "???", caveEntrance, 0);
        caveInsideEast.addEntity(new NPC("Aizoaoi", caveInsideEast, EntityType.MARTIAN, true, marsHP*2, marsDamage+20, "האלקדמר להדמיןקדמריאי השועב"));
        caveInsideEast.linkToDirection(caveInside, Direction.WEST);

        Region frozenLake = new Region(1007, "Frozen lake", "It's cold here, there seems to be some frozen ice ! And.. OH!", caveInsideEast, 0);
        NPC matteo = new NPC("Matteo", frozenLake, EntityType.BOSS_HOSTILE, true, marsHP*4, marsDamage*10, "Get out of here, you don't want to fight me !");
        frozenLake.addEntity(matteo);
        frozenLake.addEntity(new NPC("Evil climbing shoe", frozenLake, EntityType.RANDOM_THING, false, 10, 0, ""));
        frozenLake.addEntity(new NPC("Evil climbing shoe", frozenLake, EntityType.RANDOM_THING, false, 10, 0, ""));

        Quest betterShip = new Quest(150, "Better ship", "You needed a few upgrades in order to go further, matteo gave you the items you need" +
                "ed to improve your radar and your reactors in order to go to the moon", 2);
        betterShip.addObserver(mainMap.getMainCharacter());
        betterShip.linkRewardedItem(Item.MARTIAN_SWORD);
        Objective<Item> somePotatoes = new Objective<>("Get some potatoes", ObjectiveType.PICKUP);
        somePotatoes.addObserver(betterShip);
        somePotatoes.setConcernedObject(Item.GREEN_POTATO);
        betterShip.linkObjective(somePotatoes);
        Objective<NPC> killMatteo = new Objective<>("Get rid of Matteo", ObjectiveType.KILL);
        killMatteo.addObserver(betterShip);
        killMatteo.setConcernedObject(matteo);
        betterShip.linkObjective(killMatteo);
        betterShip.linkRewardedItem(Item.KNIFE);
        betterShip.linkRewardedItem(Item.FLASHLIGHT);
        mainMap.addQuest(betterShip);
        mainMap.addQuest(killTheTwins);


        ////////////////////////////////////////////////////////////////////////////////
        //MMMMMMMMMMMMMMMMMMMMMMMMMMMOOOOOOOOOOOOOOOOOOOOOOOOONNNNNNNNNNNNNNNNNNNNNNNN//
        ////////////////////////////////////////////////////////////////////////////////
        Region moonLandingRegion = new Region(1501, "Moon's landing region", "This is the landing region of the planet moon, Everything" +
                " seems calm, but you hear a roaring machine sound coming from the crater nearby", moon, moon.getShipLevelRequired());
        moon.setLandingRegion(moonLandingRegion);

        Region crater = new Region(1640, "Crater", "A crater on the moon, you see a generator and you think to yourself what could it be ? " +
                "Maybe you should try repairing it.\nYou may now start this quest : inspect.", moon, moon.getShipLevelRequired());
        Region venusLandingRegion = new Region(3565, "Venu's landing region", "You teleported to the region Venus, you dont know where" +
                "to go or what to do, but it seems as an entity wants to communicate with you", venus, venus.getShipLevelRequired());
        Pair<Teleporter, Teleporter> generator = mainMap.createTeleporters("Generator", crater, venusLandingRegion);
        generator.first.addItemToRepair(Item.SCREWDRIVER);
        generator.first.addItemToRepair(Item.TOOLKIT);
        generator.first.addItemToRepair(Item.SCREW);
        generator.second.repair();
        Region moonCave = new Region(54, "Cave", "A cave in the crater, maybe you should see what's inside", crater, crater.getShipLevelRequired());
        moonCave.addItemToInventory(Item.SCREW);
        moonCave.addItemToInventory(Item.TOOLKIT);
        Region otherCave = new Region(454, "Cave connected to the other one", "Another cave, what could be inside", moonCave, moonCave.getShipLevelRequired());
        Quest inspect = new Quest(56, "Inspect", "As you repair the generator, you realize its a teleporter ! What could be on the other side ??", 3);
        Objective<Teleporter> takeGenerator = new Objective<>("Repair the generator", ObjectiveType.REPAIR);
        takeGenerator.addObserver(inspect);
        takeGenerator.setConcernedObject(generator.first);
        inspect.addObserver(player);
        inspect.linkObjective(takeGenerator);
        mainMap.addQuest(inspect);

        /////////////////////////////////////////////////////////////////////////////////
        //VVVVVVVVVVVVVVVVVVVVVVEEEEEEEEEEEEEEEEEEENUUUUUUUUUUUUUUUUSSSSSSSSSSSSSSSSSSS//
        /////////////////////////////////////////////////////////////////////////////////
        venusLandingRegion.addEntity(new NPC("Mysterious being", venusLandingRegion, EntityType.VAMPIRE, true, 150, 20, "......." +
                "I'm mysterious...Goo to the forest....You'll find something very interesting....."));
        Region forest = new Region(455, "Magic Forest", "A magic forest, beautiful, but as you enter it : You see the same entity, and it does not look very happy." +
                "\nYou may now start your last quest : Final Battle", venus, venus.getShipLevelRequired());
        NPC finalEnnemy = new NPC("Evil being", forest, EntityType.BOSS_HOSTILE, true, 200, 30, "You'll never get out of this planet alive!!!!");
        Quest finalBattle = new Quest(5000, "Final Battle", "You fight the evil entity, and are rewarded a book...." +
                "You open your eyes wide open as you read the revelation of the book.... Saturne is a livable Planet, you need to go there as fast as possible", 3);
        Objective<NPC> killBoss = new Objective("Kill the final boss", ObjectiveType.KILL);
        killBoss.setConcernedObject(finalEnnemy);
        killBoss.addObserver(finalBattle);
        finalBattle.linkObjective(killBoss);
        finalBattle.addObserver(player);
        finalBattle.linkRewardedItem(Item.LEGENDARY_BOOK);
        mainMap.addQuest(finalBattle);


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
