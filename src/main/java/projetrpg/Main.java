package projetrpg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created on 30/03/18.
 *
 * @author mbenoukaiss
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ResourceBundle resources = ResourceBundle.getBundle("bundles/game", Locale.ENGLISH);
        Parent root = FXMLLoader.load(getClass().getResource("ui.fxml"), resources);

        Scene scene = new Scene(root, 900, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
