package projetrpg.menu;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import projetrpg.Main;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class HomeView {
    private Button quitGame;
    private Button loadGame;
    private Button newGame;
    private Scene scene;
    private Main start;

    public HomeView(Main start) {
        this.start = start;
        Label title = new Label("Welcome to Galaxy Explorer!");
        quitGame = new Button("Quit");
        loadGame = new Button("Load");
        newGame = new Button("New");

        Group mainGroup = new Group(title, quitGame, loadGame, newGame);

        title.relocate(100,150);
        loadGame.relocate(75,250);
        newGame.relocate(175,250);
        quitGame.relocate(275,250);

        quitGame.setOnAction(e -> Platform.exit());
        newGame.setOnAction(e -> {
            try {
                start.launchMainGame();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        this.scene = new Scene(mainGroup, 400, 400);
    }

    public Scene getScene() {
        return scene;
    }
}
