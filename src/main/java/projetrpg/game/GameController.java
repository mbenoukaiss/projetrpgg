package projetrpg.game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;
import projetrpg.Main;
import projetrpg.commands.InvalidCommandException;
import projetrpg.entities.items.Item;
import projetrpg.entities.player.Ability;
import projetrpg.menu.save.SaveException;
import projetrpg.menu.save.SavesServices;
import projetrpg.observer.Observable;
import projetrpg.quest.Objective;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
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
     * The save services object..
     */
    private SavesServices savesServices;

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

    @FXML
    private Label humanDisplay = new Label("");

    @FXML
    private Label lastSaveTime;

    GameController(Game game, GameView gameView, Main main, SavesServices savesServices) {
        this.main = main;
        this.gameView = gameView;
        this.game = game;
        this.savesServices = savesServices;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textLogs.setWrapText(true);
        final IntegerProperty i = new SimpleIntegerProperty(0);
        Timeline timeline = new Timeline();
        String finalLogs = "You hear an astonishing sound, almost as if an earthquake was ongoing, you start panicking " +
                "but you quickly take over your mind and think : WHATS GOING ON IM GONNA DIE AAAAAAAAAAAH.. Okay what should i do" +
                "? Let's try and talk to my brother robert, he must be in Paris.\nType help in order to see the user's guide.";
        KeyFrame keyFrame = new KeyFrame(
                Duration.seconds(0.02),
                event -> {
                    if (i.get() > finalLogs.length()) {
                        timeline.stop();
                    } else {
                        textLogs.setText(finalLogs.substring(0, i.get()));
                        i.set(i.get() + 1);
                    }
                });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        inventoryDisplay();
        locationDisplay();
        objectivesDisplay();
        spellsDisplay();
        localMapDisplay();
        planetMapDisplay();
        humanDisplay();
        updateSaveTime();   
    }

    private void updateSaveTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy 'at' HH:mm");
        lastSaveTime.setText("Last saved : " + sdf.format(game.save.getLastSaved().getTime()));
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
     * Method called when the save
     * button is clicked.
     */
    public void buttonSave() {
        try {
            savesServices.save(game.save);
            updateSaveTime();
        } catch(SaveException e) {
            e.printStackTrace();
        }
    }

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
                            game.parser.parse(game.save.getMap().getMainCharacter(), commandField.getText()).send() + "\n\n";
                } catch(InvalidCommandException e1) {
                    logs = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + "> " +
                            e1.getMessage() + "\n\n";
                }

                textLogs.setWrapText(true);
                final IntegerProperty i = new SimpleIntegerProperty(0);
                Timeline timeline = new Timeline();
                String finalLogs = logs;
                KeyFrame keyFrame = new KeyFrame(
                        Duration.seconds(0.02),
                        event -> {
                            if (i.get() > finalLogs.length()) {
                                timeline.stop();
                            } else {
                                textLogs.setText(finalLogs.substring(0, i.get()));
                                i.set(i.get() + 1);
                            }
                        });
                timeline.getKeyFrames().add(keyFrame);
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.play();
                commandField.clear();
                inventoryDisplay();
                inventoryDisplay();
                locationDisplay();
                objectivesDisplay();
                spellsDisplay();
                localMapDisplay();
                planetMapDisplay();
                humanDisplay();
            }
        }
    }

    /**
     * Updates the inventory.
     */
    public void inventoryDisplay() {
        ObservableList<String> items =FXCollections.observableArrayList();
        items.add("Items : ");
        for(Item i : game.save.getMap().getMainCharacter().getInventory().getAll()) {
            items.add(i.getName());
        }
        inventoryDisplay.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        inventoryDisplay.setItems(items);
        inventoryDisplay.getSelectionModel().selectFirst();
        if (game.save.getMap().getMainCharacter().getItemInHand() != null) {
            inventoryDisplay.getSelectionModel().select(game.save.getMap().getMainCharacter().getItemInHand().getName());
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
        if (game.save.getMap().getMainCharacter().getCurrentQuest() != null) {
            objectivs.add(this.game.save.getMap().getMainCharacter().getCurrentQuest().getName() + ":");
            objectivs.add("Objectivs : ");
            for (Objective o : game.save.getMap().getMainCharacter().getCurrentQuest().getObjectives()) {
                if (!o.isFinished()) {
                    objectivs.add("-" + o.getDescription());
                }
            }
        }
        objectivesDisplay.setItems(objectivs);
        objectivesDisplay.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> list) {
                final ListCell cell = new ListCell() {
                    private Text text;

                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            text = new Text(item.toString());
                            text.setWrappingWidth(objectivesDisplay.getPrefWidth());
                            setGraphic(text);
                        }
                    }
                };

                return cell;
            }
        });
        objectivesDisplay.getSelectionModel().selectFirst();
    }

    /**
     * Updates the location.
     */
    public void locationDisplay() {
        locationField.setWrapText(true);
        locationField.setText("Location : " + game.save.getMap().getMainCharacter().getLocation().describe());
    }


    /**
     * Updates the spells.
     */
    public void spellsDisplay() {
        ObservableList<String> spells = FXCollections.observableArrayList();
        spells.add("Spells : ");
        if (game.save.getMap().getMainCharacter().getAbilities() != null) {
            for (Ability a : game.save.getMap().getMainCharacter().getAbilities()) {
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

    public void humanDisplay() {
        this.humanDisplay.setText(String.valueOf(this.game.getMap().getHumanCount()));
    }

}
