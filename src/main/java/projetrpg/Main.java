package projetrpg;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import projetrpg.game.Game;
import projetrpg.game.GameView;
import projetrpg.menu.Home;
import projetrpg.menu.HomeView;
import projetrpg.menu.save.Save;
import projetrpg.menu.save.SaveManager;
import projetrpg.menu.save.SavesServices;
import projetrpg.utils.ParameterView;

import java.io.IOException;

/**
 * Created on 30/03/18.
 *
 * @author mbenoukaiss
 */
public class Main extends Application {

    private SavesServices savesServices;

    private Stage primaryStage;

    private ParameterView params;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.params = new ParameterView();
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

    public void launchParams() {
        Stage stage = new Stage();
        stage.setTitle("Galaxy Explorer Params");
        stage.getIcons().add(new Image("icon.png"));
        stage.setScene(params.getScene());
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    public void launchMainGame(Save save) {
        this.primaryStage.setTitle("Galaxy Explorer");

        Game game = new Game(save);

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

    public ParameterView getParams() {
        return params;
    }

}
