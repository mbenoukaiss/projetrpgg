package projetrpg.menu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import projetrpg.map.*;
import projetrpg.quest.Objective;
import projetrpg.quest.ObjectiveSerializer;
import projetrpg.quest.Quest;
import projetrpg.quest.QuestSerializer;
import projetrpg.utils.AnnotationExclusionStrategy;

import java.io.*;
import java.util.*;

public class SavesServices {

    private static final String SEPARATOR = "\n>>>>>>>>>>";
    private final File SAVE_FOLDER;
    private final Gson gson;

    public SavesServices(String saveFolder) {
        SAVE_FOLDER = new File(saveFolder);
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .setExclusionStrategies(new AnnotationExclusionStrategy())
                .registerTypeAdapter(Objective.class, new ObjectiveSerializer())
                .registerTypeAdapter(Quest.class, new QuestSerializer())
                .registerTypeAdapter(Teleporter.class, new TeleporterSerializer())
                .registerTypeAdapter(Region.class, new RegionSerializer())
                .registerTypeAdapter(MainMap.class, new MapSerializer())
                .create();
    }

    public Save load(String name) throws SaveException {
        Save s = null;
        File save = new File(SAVE_FOLDER, name + ".json");

        if(!save.exists()) {
            throw new SaveException("Save " + name + " does not exist.");
        }

        try {
            Scanner scanner = new Scanner(save);
            scanner.useDelimiter(SEPARATOR);

            String fileAsString = scanner.nextLine();
            Calendar created = Calendar.getInstance();
            created.setTimeInMillis(Integer.valueOf(scanner.nextLine()));
            Calendar lastSaved = Calendar.getInstance();
            lastSaved.setTimeInMillis(Integer.valueOf(scanner.nextLine()));

            s = new Save(gson.fromJson(fileAsString, MainMap.class), created, lastSaved);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        return s;
    }

    public void save(Save save) throws SaveException {
        File saveFile = new File(SAVE_FOLDER, save.getMap().getName() + ".json");

        if(saveFile.exists())  {
            if(!saveFile.delete()) {
                throw new SaveException("Could not override previous save.");
            }
        }

        //Si la création du nouveau fichier rate mais qu'il est
        //quand même supprimé alors la sauvegarde vas échouer
        //et tout sera perdu.
        //TODO

        try {
            if(!saveFile.createNewFile()) {
                throw new SaveException("Could not create new save file.");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
            bw.write(gson.toJson(save.getMap()));
            bw.write(SEPARATOR);
            bw.write(String.valueOf(save.getCreated().getTimeInMillis()));
            bw.write(SEPARATOR);
            bw.write(String.valueOf(Calendar.getInstance().getTimeInMillis()));
            bw.close();
        } catch(IOException e) {
            throw new SaveException("Could not open new save file.", e);
        }
    }

    public Save create() {
        //TODO: Charger depuis le jar
        return new Save(gson.fromJson("{\n" +
                "  \"name\": \"FacticeMap\",\n" +
                "  \"spawnpoint\": 1,\n" +
                "  \"humancount\": 100,\n" +
                "  \"maincharacter\": {\n" +
                "    \"experience\": 0,\n" +
                "    \"inventory\": {\n" +
                "      \"maxCapacity\": 50,\n" +
                "      \"items\": []\n" +
                "    },\n" +
                "    \"mana\": 50,\n" +
                "    \"baseMana\": 50,\n" +
                "    \"abilities\": [\n" +
                "      [\n" +
                "        {\n" +
                "          \"name\": \"FireBall\",\n" +
                "          \"minLevelRequired\": 1,\n" +
                "          \"damage\": 30.0,\n" +
                "          \"type\": \"LINE\",\n" +
                "          \"cost\": 10\n" +
                "        },\n" +
                "        false\n" +
                "      ],\n" +
                "      [\n" +
                "        {\n" +
                "          \"name\": \"WaterBeam\",\n" +
                "          \"minLevelRequired\": 2,\n" +
                "          \"damage\": 50.0,\n" +
                "          \"type\": \"AOE\",\n" +
                "          \"cost\": 20\n" +
                "        },\n" +
                "        false\n" +
                "      ]\n" +
                "    ],\n" +
                "    \"baseDamage\": 10,\n" +
                "    \"observers\": [],\n" +
                "    \"ship\": {\n" +
                "      \"TRAVEL_COST\": 10,\n" +
                "      \"baseFuel\": 50,\n" +
                "      \"actualFuel\": 50,\n" +
                "      \"level\": 1,\n" +
                "      \"ameliorations\": [\n" +
                "        \"RADAR_AMELIORATION\",\n" +
                "        \"ENGINE_AMELIORATION\",\n" +
                "        \"REACTORS_AMELIORATION\"\n" +
                "      ],\n" +
                "      \"id\": 11,\n" +
                "      \"name\": \"Hervé\\u0027s Ship\",\n" +
                "      \"regionOnDirection\": {},\n" +
                "      \"containedRegions\": [],\n" +
                "      \"entities\": [],\n" +
                "      \"teleporters\": [],\n" +
                "      \"inventory\": {\n" +
                "        \"maxCapacity\": 10,\n" +
                "        \"items\": []\n" +
                "      },\n" +
                "      \"itemsNeeded\": [],\n" +
                "      \"shipLevelRequired\": 0\n" +
                "    },\n" +
                "    \"name\": \"Hervé\",\n" +
                "    \"type\": \"PLAYER\",\n" +
                "    \"hps\": 100,\n" +
                "    \"baseHps\": 100,\n" +
                "    \"isHostile\": false,\n" +
                "    \"location\": 9\n" +
                "  },\n" +
                "  \"quests\": [\n" +
                "    {\n" +
                "      \"lrequired\": 0,\n" +
                "      \"ereward\": 10,\n" +
                "      \"description\": \"Start your beautiful journey towards saving the universe\",\n" +
                "      \"objectives\": [\n" +
                "        {\n" +
                "          \"finished\": false,\n" +
                "          \"description\": \"Kill Zorg\",\n" +
                "          \"type\": \"KILL\",\n" +
                "          \"concerned\": {\n" +
                "            \"class\": \"projetrpg.entities.NPC\",\n" +
                "            \"object\": 0\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"finished\": false,\n" +
                "          \"description\": \"Use the Apple\",\n" +
                "          \"type\": \"USE\",\n" +
                "          \"concerned\": {\n" +
                "            \"class\": \"projetrpg.entities.items.Item\",\n" +
                "            \"object\": \"APPLE\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"finished\": false,\n" +
                "          \"description\": \"Pickup the Apple\",\n" +
                "          \"type\": \"PICKUP\",\n" +
                "          \"concerned\": {\n" +
                "            \"class\": \"projetrpg.entities.items.Item\",\n" +
                "            \"object\": \"APPLE\"\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"reward\": [\n" +
                "        \"APPLE\",\n" +
                "        \"FLASHLIGHT\"\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"regions\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"Earth\",\n" +
                "      \"directions\": {},\n" +
                "      \"childregions\": [\n" +
                "        {\n" +
                "          \"id\": 9,\n" +
                "          \"name\": \"Flower Hill\",\n" +
                "          \"directions\": {\n" +
                "            \"NORTH\": 6,\n" +
                "            \"EST\": 8\n" +
                "          },\n" +
                "          \"childregions\": [],\n" +
                "          \"entities\": [],\n" +
                "          \"teleporters\": [],\n" +
                "          \"inventory\": {\n" +
                "            \"maxCapacity\": 10,\n" +
                "            \"items\": []\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 6,\n" +
                "          \"name\": \"Forest\",\n" +
                "          \"directions\": {\n" +
                "            \"SOUTH\": 9\n" +
                "          },\n" +
                "          \"childregions\": [\n" +
                "            {\n" +
                "              \"id\": 7,\n" +
                "              \"name\": \"Cave\",\n" +
                "              \"directions\": {},\n" +
                "              \"childregions\": [],\n" +
                "              \"entities\": [\n" +
                "                {\n" +
                "                  \"id\": 1,\n" +
                "                  \"baseDamage\": 1,\n" +
                "                  \"dialogue\": \"I\\u0027m weak\",\n" +
                "                  \"name\": \"Bog\",\n" +
                "                  \"type\": \"VAMPIRE\",\n" +
                "                  \"hps\": 10,\n" +
                "                  \"baseHps\": 10,\n" +
                "                  \"isHostile\": true\n" +
                "                }\n" +
                "              ],\n" +
                "              \"teleporters\": [\n" +
                "                {\n" +
                "                  \"id\": 0,\n" +
                "                  \"name\": \"CtoVtp\",\n" +
                "                  \"repaired\": false,\n" +
                "                  \"link\": 1,\n" +
                "                  \"requirements\": []\n" +
                "                }\n" +
                "              ],\n" +
                "              \"inventory\": {\n" +
                "                \"maxCapacity\": 10,\n" +
                "                \"items\": []\n" +
                "              }\n" +
                "            }\n" +
                "          ],\n" +
                "          \"entities\": [\n" +
                "            {\n" +
                "              \"id\": 2,\n" +
                "              \"baseDamage\": 0,\n" +
                "              \"dialogue\": \"Hey, you are our hero...blablabla....very important... save the universe ... blablabla... You may now start your first Quest : A new Dawn!\",\n" +
                "              \"name\": \"Jean\",\n" +
                "              \"type\": \"VILLAGER\",\n" +
                "              \"hps\": 100,\n" +
                "              \"baseHps\": 100,\n" +
                "              \"isHostile\": false\n" +
                "            }\n" +
                "          ],\n" +
                "          \"teleporters\": [],\n" +
                "          \"inventory\": {\n" +
                "            \"maxCapacity\": 10,\n" +
                "            \"items\": [\n" +
                "              \"TOOLKIT\",\n" +
                "              \"TOOLKIT\"\n" +
                "            ]\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 8,\n" +
                "          \"name\": \"Flower\",\n" +
                "          \"directions\": {\n" +
                "            \"WEST\": 9\n" +
                "          },\n" +
                "          \"childregions\": [],\n" +
                "          \"entities\": [],\n" +
                "          \"teleporters\": [],\n" +
                "          \"inventory\": {\n" +
                "            \"maxCapacity\": 10,\n" +
                "            \"items\": []\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"entities\": [],\n" +
                "      \"teleporters\": [],\n" +
                "      \"inventory\": {\n" +
                "        \"maxCapacity\": 10,\n" +
                "        \"items\": []\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 2,\n" +
                "      \"name\": \"Mars\",\n" +
                "      \"directions\": {},\n" +
                "      \"childregions\": [\n" +
                "        {\n" +
                "          \"id\": 10,\n" +
                "          \"name\": \"Volcano\",\n" +
                "          \"directions\": {},\n" +
                "          \"childregions\": [],\n" +
                "          \"entities\": [\n" +
                "            {\n" +
                "              \"id\": 0,\n" +
                "              \"baseDamage\": 10,\n" +
                "              \"dialogue\": \"Im gonna kill you !\",\n" +
                "              \"name\": \"Zorg\",\n" +
                "              \"type\": \"VAMPIRE\",\n" +
                "              \"hps\": 300,\n" +
                "              \"baseHps\": 300,\n" +
                "              \"isHostile\": true\n" +
                "            }\n" +
                "          ],\n" +
                "          \"teleporters\": [\n" +
                "            {\n" +
                "              \"id\": 1,\n" +
                "              \"name\": \"CtoVtp\",\n" +
                "              \"repaired\": false,\n" +
                "              \"link\": 0,\n" +
                "              \"requirements\": []\n" +
                "            }\n" +
                "          ],\n" +
                "          \"inventory\": {\n" +
                "            \"maxCapacity\": 10,\n" +
                "            \"items\": []\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"entities\": [],\n" +
                "      \"teleporters\": [],\n" +
                "      \"inventory\": {\n" +
                "        \"maxCapacity\": 10,\n" +
                "        \"items\": [\n" +
                "          \"HATCHET\"\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 3,\n" +
                "      \"name\": \"Moon\",\n" +
                "      \"directions\": {},\n" +
                "      \"childregions\": [\n" +
                "        {\n" +
                "          \"id\": 12,\n" +
                "          \"name\": \"Crater\",\n" +
                "          \"directions\": {},\n" +
                "          \"childregions\": [],\n" +
                "          \"entities\": [],\n" +
                "          \"teleporters\": [],\n" +
                "          \"inventory\": {\n" +
                "            \"maxCapacity\": 10,\n" +
                "            \"items\": []\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"entities\": [],\n" +
                "      \"teleporters\": [],\n" +
                "      \"inventory\": {\n" +
                "        \"maxCapacity\": 10,\n" +
                "        \"items\": [\n" +
                "          \"APPLE\"\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 4,\n" +
                "      \"name\": \"Venus\",\n" +
                "      \"directions\": {},\n" +
                "      \"childregions\": [],\n" +
                "      \"entities\": [],\n" +
                "      \"teleporters\": [],\n" +
                "      \"inventory\": {\n" +
                "        \"maxCapacity\": 10,\n" +
                "        \"items\": [\n" +
                "          \"FLASHLIGHT\"\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 5,\n" +
                "      \"name\": \"Saturne\",\n" +
                "      \"directions\": {},\n" +
                "      \"childregions\": [],\n" +
                "      \"entities\": [],\n" +
                "      \"teleporters\": [],\n" +
                "      \"inventory\": {\n" +
                "        \"maxCapacity\": 10,\n" +
                "        \"items\": []\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}", MainMap.class), Calendar.getInstance(), Calendar.getInstance());
    }

    public void delete(String name) {
        File save = new File(SAVE_FOLDER, name);
        if(save.exists()) save.delete();
    }

    public Collection<Save> listSaves() throws SaveException {
        Collection<Save> saves = new HashSet<>();

        for(String s : SAVE_FOLDER.list()) {
            saves.add(load(s.replaceAll(".json", "")));
        }

        return saves;
    }
}
