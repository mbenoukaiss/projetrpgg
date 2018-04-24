package projetrpg;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by mhevin on 24/04/18.
 */
public class VuePartie {

    private ResourceBundle ressources;
    private Parent root;
    private Partie partie;
    private Scene scene;
    private Main main;

    public VuePartie(Partie partie, Main main) throws IOException {
        this.partie = partie;
        this.main = main;
        this.ressources = ResourceBundle.getBundle("bundles/game", Locale.ENGLISH);
        this.root = FXMLLoader.load(getClass().getClassLoader().getResource("ui.fxml"), ressources);
        this.scene = new Scene(root, 900, 600);
    }

    public void buttonQuit() {
        this.main.quit();
    }

    public Scene getScene() {
        return scene;
    }
}
