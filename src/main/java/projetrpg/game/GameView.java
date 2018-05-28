package projetrpg.game;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import projetrpg.Main;
import projetrpg.map.MainMap;
import projetrpg.map.MapDisplay;
import projetrpg.menu.save.SavesServices;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The view of a game.
 */
public class GameView {

    /**
     * Reference to the application.
     */
    private Main main;

    /**
     * The game.
     */
    private Game game;

    /**
     * The resource bundle for the user
     * interface.
     */
    private ResourceBundle languageBundle;

    /**
     * The current scene.
     */
    private Scene scene;

    public GameView(Game game, Main main, SavesServices savesServices) throws IOException {
        this.game = game;
        this.main = main;
        this.languageBundle = ResourceBundle.getBundle("bundles/game", Locale.ENGLISH);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui.fxml"));
        loader.setController(new GameController(game, this, main, savesServices));
        loader.setResources(languageBundle);

        this.scene = new Scene(loader.load(), 1200, 600);
    }

    public Canvas generateLocalMap() {
        MapDisplay display = new MapDisplay(game.save.getMap());
        return display.drawLocal();
    }

    public Canvas generatePlanetsMap() {
        MapDisplay display = new MapDisplay(game.save.getMap());
        return display.drawPlanets();
    }

    /**
     * Getter for the scene.
     *
     * @return The scene of the game.
     */
    public Scene getScene() {
        return scene;
    }
}
