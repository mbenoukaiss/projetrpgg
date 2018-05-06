package projetrpg.game;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import projetrpg.Main;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class GameView {

    private ResourceBundle languageBundle;
    private Game game;
    private Scene scene;
    private Main main;

    public GameView(Game game, Main main) throws IOException {
        this.game = game;
        this.main = main;
        this.languageBundle = ResourceBundle.getBundle("bundles/game", Locale.ENGLISH);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui.fxml"));
        loader.setController(new GameController(game, this.main));
        loader.setResources(languageBundle);

        this.scene = new Scene(loader.load(), 900, 600);
    }

    public Scene getScene() {
        return scene;
    }
}
