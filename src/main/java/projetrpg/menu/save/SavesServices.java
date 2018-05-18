package projetrpg.menu.save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import projetrpg.entities.player.Ship;
import projetrpg.entities.player.ShipSerializer;
import projetrpg.map.*;
import projetrpg.quest.Objective;
import projetrpg.quest.ObjectiveSerializer;
import projetrpg.quest.Quest;
import projetrpg.quest.QuestSerializer;
import projetrpg.utils.AnnotationExclusionStrategy;

import java.io.*;
import java.util.*;

public class SavesServices {

    public static final String SEPARATOR = "\n>>>>>>>>>>";
    final File SAVE_FOLDER;
    private final Gson gson;

    public SavesServices(String saveFolder) {
        SAVE_FOLDER = new File(saveFolder);
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .setExclusionStrategies(new AnnotationExclusionStrategy())
                .setExclusionStrategies(new AnnotationExclusionStrategy())
                .registerTypeAdapter(Objective.class, new ObjectiveSerializer())
                .registerTypeAdapter(Quest.class, new QuestSerializer())
                .registerTypeAdapter(Teleporter.class, new TeleporterSerializer())
                .registerTypeAdapter(Region.class, new RegionSerializer())
                .registerTypeHierarchyAdapter(Ship.class, new ShipSerializer())
                .registerTypeAdapter(MainMap.class, new MapSerializer())
                .create();

        if(!SAVE_FOLDER.exists()) SAVE_FOLDER.mkdir();
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

            String fileAsString = scanner.next();
            Calendar created = Calendar.getInstance();
            created.setTimeInMillis(Integer.valueOf(scanner.next()));
            Calendar lastSaved = Calendar.getInstance();
            lastSaved.setTimeInMillis(Integer.valueOf(scanner.next()));

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

    public Save create(String name) {
        File saveFile = new File(SAVE_FOLDER, name + "0.json");

        if(saveFile.exists()) {
            List<String> files = Arrays.asList(SAVE_FOLDER.list());
            int offset = 1;
            while(files.contains(name + offset + ".json")) ++offset;
            saveFile = new File(SAVE_FOLDER, name + offset + ".json");
        }

        Scanner scanner = new Scanner(SavesServices.class.getResourceAsStream("/partie.json"));
        String save = scanner.useDelimiter("\\A").next();

        StringBuilder saveFileContent = new StringBuilder();
        saveFileContent.append(save).append(SEPARATOR);
        saveFileContent.append(String.valueOf(Calendar.getInstance().getTimeInMillis())).append(SEPARATOR);
        saveFileContent.append(String.valueOf(Calendar.getInstance().getTimeInMillis()));

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
            bw.write(saveFileContent.toString());
            bw.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return new Save(
                gson.fromJson(save, MainMap.class),
                Calendar.getInstance(),
                Calendar.getInstance()
        );
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
