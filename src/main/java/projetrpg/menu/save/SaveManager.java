package projetrpg.menu.save;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

public class SaveManager {

    private final SavesServices saveServices;
    
    @FXML
    private VBox saveItemsList;

    private Collection<Node> saveItems;

    private Scene scene;

    public SaveManager(SavesServices ss) {
        this.saveServices = ss;
        saveItems = new HashSet<>();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("saveManager.fxml"));
            loader.setController(this);
            scene = new Scene(loader.load(), 650, 600);
        } catch(IOException e) {
            e.printStackTrace();
        }

        refresh();
    }

    public void refresh() {
        saveItems.clear();
        saveItemsList.getChildren().clear();

        for(File file : saveServices.SAVE_FOLDER.listFiles()) {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("saveItem.fxml"));
            loader.setController(new SaveItemController(file));

            try {
                Node item = loader.load();
                saveItems.add(item);
                saveItemsList.getChildren().add(item);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void menu() {

    }

    public void quit() {

    }

    public Scene scene() {
        return scene;
    }

}
