package projetrpg.game;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import projetrpg.Main;
import projetrpg.commands.InvalidCommandException;
import projetrpg.entities.items.Item;
import projetrpg.entities.player.Ability;
import projetrpg.quest.Objective;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
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
     * The current quest.
     */
    @FXML
    private TextArea questField = new TextArea("");

    /**
     * The list of the spells.
     */
    @FXML
    private ListView<String> spellsDisplay = new ListView<>();

    GameController(Game game, Main main) {
        this.main = main;
        this.game = game;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textLogs.setWrapText(true);
        textLogs.appendText(game.manual +
                "\n\nYour name is Hervé ! Talk to jean located in south in order to start your journey towards " +
                "saving the universe!\n\n");
        inventoryDisplay();
        locationDisplay();
        objectivesDisplay();
        spellsDisplay();
        questDisplay();
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

        if (KeyCode.ENTER == e.getCode()) {
            String logs;

            try {
                logs = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + "> " +
                        game.parser.parse(game.map.getMainCharacter(), commandField.getText()).send() + "\n\n";
            } catch (InvalidCommandException e1) {
                logs = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + "> " +
                        e1.getMessage() + "\n\n";
            }

            textLogs.setWrapText(true);
            textLogs.appendText(logs);
            commandField.clear();
            inventoryDisplay();
            inventoryDisplay();
            locationDisplay();
            questDisplay();
            objectivesDisplay();
            spellsDisplay();
        }
    }

    /**
     * Updates the inventory.
     */
    public void inventoryDisplay() {
        ObservableList<String> items =FXCollections.observableArrayList();
        for(Item i : game.map.getMainCharacter().getInventory().getAll()) {
            items.add(i.getName());
        }
        inventoryDisplay.setItems(items);
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
        objectivesDisplay.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    {
                        prefWidthProperty().bind(objectivesDisplay.widthProperty().subtract(20));
                        setMaxWidth(Control.USE_PREF_SIZE);
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (item != null && !empty) {
                            this.setWrapText(true);
                            setText(item);
                        } else {
                            setText(null);
                        }
                    }

                };
            }
        });
        ObservableList<String> objectivs =FXCollections.observableArrayList();
        if (game.map.getMainCharacter().getCurrentQuest() != null) {
            for (Objective o : game.map.getMainCharacter().getCurrentQuest().getObjectives()) {
                if (!o.isFinished()) {
                    objectivs.add(o.getDescription());
                }
            }
        }
        objectivesDisplay.setItems(objectivs);
    }

    /**
     * Updates the location.
     */
    public void locationDisplay() {
        locationField.setWrapText(true);
        locationField.setText(game.map.getMainCharacter().getLocation().describe());
    }

    /**
     * Updates the quest.
     */
    public void questDisplay() {
        questField.setWrapText(true);
        if (game.map.getMainCharacter().getCurrentQuest() != null) {
            questField.setText(game.map.getMainCharacter().getCurrentQuest().getName());
        } else {
            questField.setText("");
        }
    }

    /**
     * Updates the spells.
     */
    public void spellsDisplay() {
        ObservableList<String> spells = FXCollections.observableArrayList();
        if (game.map.getMainCharacter().getAbilities() != null) {
            for (Ability a : game.map.getMainCharacter().getAbilities()) {
                spells.add(a.getName());
            }
        }
        spellsDisplay.setItems(spells);
    }

}
