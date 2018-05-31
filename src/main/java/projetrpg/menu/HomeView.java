package projetrpg.menu;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import projetrpg.Main;
import projetrpg.menu.save.SaveManager;

import java.util.Optional;
import java.io.IOException;

public class HomeView {

    private Main main;
    private Home home;
    private Button quitGame;
    private Button loadGame;
    private Button newGame;
    private Scene scene;

    public HomeView(Main main, Home home) {
        this.main = main;
        this.home = home;

        Label title = new Label("Welcome to Galaxy Explorer!");
        quitGame = new Button("Quit");
        loadGame = new Button("Load");
        newGame = new Button("New");

        Group mainGroup = new Group(title, quitGame, loadGame, newGame);

        title.relocate(100,150);
        loadGame.relocate(75,250);
        newGame.relocate(175,250);
        quitGame.relocate(275,250);

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
        quitGame.setOnAction(e -> Platform.exit());

        this.scene = new Scene(mainGroup, 400, 400);
    }

    public Scene getScene() {
        return scene;
    }
}
