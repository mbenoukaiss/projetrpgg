package projetrpg.menu.save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import projetrpg.entities.player.Ship;
import projetrpg.map.*;
import projetrpg.quest.Objective;
import projetrpg.quest.Quest;
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
                .registerTypeAdapter(Objective.class, new Objective.TypeAdapter())
                .registerTypeAdapter(Quest.class, new Quest.TypeAdapter())
                .registerTypeAdapter(Teleporter.class, new Teleporter.TypeAdapter())
                .registerTypeAdapter(Region.class, new Region.TypeAdapter())
                .registerTypeHierarchyAdapter(Ship.class, new Ship.TypeAdapter())
                .registerTypeHierarchyAdapter(Planet.class, new Planet.TypeAdapter())
                .registerTypeAdapter(MainMap.class, new MapSerializer())
                .create();

        if(!SAVE_FOLDER.exists()) SAVE_FOLDER.mkdir();
    }

    public Save load(String name) throws SaveException {
        File save = new File(SAVE_FOLDER, name + ".json");

        if(!save.exists()) {
            throw new SaveException("Save " + name + " does not exist.");
        }

        return load(save);
    }

    public Save load(File file) throws SaveException {
        Save s = null;
        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter(SEPARATOR);

            String fileAsString = scanner.next();
            Calendar created = Calendar.getInstance();
            created.setTimeInMillis(Long.valueOf(scanner.next()));
            Calendar lastSaved = Calendar.getInstance();
            lastSaved.setTimeInMillis(Long.valueOf(scanner.next()));

            scanner.close();

            s = new Save(file, gson.fromJson(fileAsString, MainMap.class), created, lastSaved);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        return s;
    }

    public void save(Save save) throws SaveException {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(save.getFile()));
            bw.write(gson.toJson(save.getMap()));
            bw.write(SEPARATOR);
            bw.write(String.valueOf(save.getCreated().getTimeInMillis()));
            bw.write(SEPARATOR);
            bw.write(String.valueOf(Calendar.getInstance().getTimeInMillis()));
            bw.close();

            save.updateLastSaved();
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

        try {
            saveFile.createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(SavesServices.class.getResourceAsStream("/partie.json"));
        String save = scanner.useDelimiter("\\A").next();

        MainMap map = gson.fromJson(save, MainMap.class);
        map.setName(name);

        StringBuilder saveFileContent = new StringBuilder();
        saveFileContent.append(gson.toJson(map)).append(SEPARATOR);
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
                saveFile,
                map,
                Calendar.getInstance(),
                Calendar.getInstance()
        );
    }

    public void delete(Save save) {
        if(save.getFile().exists()) save.getFile().delete();
    }

    public Collection<Save> listSaves() throws SaveException {
        Collection<Save> saves = new HashSet<>();

        for(File file : SAVE_FOLDER.listFiles()) {
            saves.add(load(file));
        }

        return saves;
    }
}
