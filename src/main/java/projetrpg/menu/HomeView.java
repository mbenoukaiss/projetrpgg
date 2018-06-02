package projetrpg.menu;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import projetrpg.Main;
import projetrpg.menu.save.SaveManager;

import java.net.URL;
import java.util.Optional;
import java.io.IOException;
import java.util.ResourceBundle;

public class HomeView implements Initializable {

    private Main main;

    private Home home;

    @FXML
    private Button newGame;

    @FXML
    private Button loadGame;

    @FXML
    private Button settings;

    @FXML
    private Button quit;

    private Scene scene;

    public HomeView(Main main, Home home) {
        this.main = main;
        this.home = home;

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("menu.fxml"));
        loader.setController(this);

        try {
            this.scene = new Scene(loader.load(), 400, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadGame.setOnAction(e -> {
            SaveManager sm = new SaveManager(main, home.getSavesServices());
            main.launchSavesMenu(sm);
        });

        newGame.setOnAction(e -> {
            TextInputDialog input = new TextInputDialog();
            input.setTitle("Map name");
            input.setContentText("How would you like to name this new map ?");

            Optional<String> result;
            do {
                result = input.showAndWait();

                //Cancel/ALT+F4/anything else
                if(!result.isPresent()) return;
            } while(result.get().isEmpty());

            main.launchMainGame(home.getSavesServices().create(result.get()));
        });
        quit.setOnAction(e -> Platform.exit());
    }

    public Scene getScene() {
        return scene;
    }


}
