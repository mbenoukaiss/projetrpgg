package projetrpg.game;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import projetrpg.Main;
import projetrpg.commands.InvalidCommandException;
import projetrpg.entities.items.Item;
import projetrpg.entities.player.Ability;
import projetrpg.observer.Observable;
import projetrpg.quest.Objective;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The controller of the game.
 */
public class GameController implements Initializable {

    /**
     * Reference to the application.
     */
    private Main main;

    /**
     * The concerned game.
     */
    private Game game;

    /**
     * The game view.
     */
    private GameView gameView;

    /**
     * The text field in which the player
     * enters the commands.
     */
    @FXML
    private TextField commandField;

    /**
     * The textarea showing all the
     * logs during a game.
     */
    @FXML
    private TextArea textLogs;

    /**
     * The list view showing the inventory.
     */
    @FXML
    private ListView<String> inventoryDisplay = new ListView<>();

    /**
     * The text area showing the location of
     * the player and information about it.
     */
    @FXML
    private TextArea locationField = new TextArea("");

    /**
     * The list view showing the objectives
     * the player has to complete.
     */
    @FXML
    private ListView<String> objectivesDisplay = new ListView<>();


    /**
     * The list of the spells.
     */
    @FXML
    private ListView<String> spellsDisplay = new ListView<>();

    @FXML
    private Canvas mapDisplayCanvas = new Canvas();

    @FXML
    private Canvas planetsDisplay = new Canvas();

    GameController(Game game, GameView gameView, Main main) {
        this.main = main;
        this.gameView = gameView;
        this.game = game;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textLogs.setWrapText(true);
        textLogs.appendText(game.manual +
                "\n\nYour name is Hervé ! Talk to jean in order to start your journey towards " +
                "saving the universe!\n\n");
        inventoryDisplay();
        locationDisplay();
        objectivesDisplay();
        spellsDisplay();
        localMapDisplay();
        planetMapDisplay();
    }

    /**
     * Method called when the quit
     * button is clicked.
     */
    public void buttonQuit() {
        Platform.exit();
    }

    /**
     * Method called when the home
     * button is clicked.
     */
    public void buttonHome() throws IOException { main.launchMenu(); }

    /**
     * Method called when the user
     * types anything in the commande field.
     */
    public void enterCommand(KeyEvent e) {
        Calendar cal = Calendar.getInstance();

        //If he's typing a command and not doing Ctrl + A||C||D
        if(e.getCode().isLetterKey() && !e.isControlDown()) {
            Optional<String> suggestion = game.parser.complete(commandField.getText());

            if(suggestion.isPresent()) {
                int begin = commandField.getLength();
                int end = suggestion.get().length();

                commandField.setText(suggestion.get());
                commandField.selectRange(begin, end);
            }
        }

        if (KeyCode.ENTER == e.getCode()) {
            if(!commandField.getSelectedText().isEmpty()) {
                commandField.deselect();
                commandField.appendText(" ");
            } else {

                String logs;
                try {
                    logs = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + "> " +
                            game.parser.parse(game.map.getMainCharacter(), commandField.getText()).send() + "\n\n";
                } catch(InvalidCommandException e1) {
                    logs = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + "> " +
                            e1.getMessage() + "\n\n";
                }

                textLogs.setWrapText(true);
                textLogs.appendText(logs);
                commandField.clear();
                inventoryDisplay();
                inventoryDisplay();
                locationDisplay();
                objectivesDisplay();
                spellsDisplay();
                localMapDisplay();
                planetMapDisplay();
            }
        }
    }

    /**
     * Updates the inventory.
     */
    public void inventoryDisplay() {
        ObservableList<String> items =FXCollections.observableArrayList();
        items.add("Items : ");
        for(Item i : game.map.getMainCharacter().getInventory().getAll()) {
            items.add(i.getName());
        }
        inventoryDisplay.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        inventoryDisplay.setItems(items);
        inventoryDisplay.getSelectionModel().selectFirst();
        if (game.map.getMainCharacter().getItemInHand() != null) {
            inventoryDisplay.getSelectionModel().select(game.map.getMainCharacter().getItemInHand().getName());
        } else {
            inventoryDisplay.getSelectionModel().select("");
        }

    }

    /**
     * Updates the objectives.
     */
    public void  objectivesDisplay() {
        ObservableList<String> objectivs =FXCollections.observableArrayList();
        objectivs.add("Quest : ");
        if (game.map.getMainCharacter().getCurrentQuest() != null) {
            objectivs.add(this.game.map.getMainCharacter().getCurrentQuest().getName() + ":");
            objectivs.add("Objectivs : ");
            for (Objective o : game.map.getMainCharacter().getCurrentQuest().getObjectives()) {
                if (!o.isFinished()) {
                    objectivs.add("-" + o.getDescription());
                }
            }
        }
        objectivesDisplay.setItems(objectivs);
        objectivesDisplay.getSelectionModel().selectFirst();
    }

    /**
     * Updates the location.
     */
    public void locationDisplay() {
        locationField.setWrapText(true);
        locationField.setText("Location : " + game.map.getMainCharacter().getLocation().describe());
    }


    /**
     * Updates the spells.
     */
    public void spellsDisplay() {
        ObservableList<String> spells = FXCollections.observableArrayList();
        spells.add("Spells : ");
        if (game.map.getMainCharacter().getAbilities() != null) {
            for (Ability a : game.map.getMainCharacter().getAbilities()) {
                spells.add(a.getName());
            }
        }
        spellsDisplay.setItems(spells);
        spellsDisplay.getSelectionModel().selectFirst();
    }

    public void localMapDisplay() {
        SnapshotParameters params = new SnapshotParameters();
        WritableImage img = gameView.generateLocalMap().snapshot(params, null);
        this.mapDisplayCanvas.getGraphicsContext2D().drawImage(img, 0, 0);
    }

    public void planetMapDisplay() {
        SnapshotParameters params = new SnapshotParameters();
        WritableImage img = gameView.generatePlanetsMap().snapshot(params, null);
        this.planetsDisplay.getGraphicsContext2D().drawImage(img, 0, 0);
    }

}
