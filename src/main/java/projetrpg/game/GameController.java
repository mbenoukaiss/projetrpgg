package projetrpg.game;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import projetrpg.Quest.Objectiv;
import projetrpg.commands.CommandParser;
import projetrpg.commands.InvalidCommandException;
import projetrpg.entities.items.Item;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

public class GameController {

    private Game game;
    private CommandParser parser;
    private String logs = "";
    @FXML
    private TextField commandField;
    @FXML
    private TextArea textLogs;
    @FXML
    private ListView<String> inventoryDisplay = new ListView<>();
    @FXML
    private TextArea locationField = new TextArea("");
    @FXML
    private ListView<String> objectivsDisplay = new ListView<>();
    @FXML
    private TextArea questField = new TextArea("");

    GameController(Game game) {
        this.game = game;
        parser = game.getParser();
    }

    public void buttonQuit() {
        Platform.exit();
    }

    public void enterCommand(KeyEvent e) {
        Calendar cal = Calendar.getInstance();
        if (KeyCode.ENTER == e.getCode()) {
            try {
                logs = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ">" +
                        parser.parse(game.getMainMap().getMainCharacter(), commandField.getText()).send() + "\n\n";
            } catch (InvalidCommandException e1) {
                logs = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ">" +
                        e1.getMessage() + "\n\n";
            }
            textLogs.setWrapText(true);
            textLogs.appendText(logs);
            commandField.clear();
            inventoryDisplay();
            locationDisplay();
            questDisplay();
            objectivsDisplay();
        }
    }

    public void inventoryDisplay() {
        ObservableList<String> items =FXCollections.observableArrayList();
        for(Item i : game.getMainMap().getMainCharacter().getInventory().getAll()) {
            items.add(i.getName());
        }
        inventoryDisplay.setItems(items);
    }

    public void  objectivsDisplay() {
        objectivsDisplay.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    {
                        prefWidthProperty().bind(objectivsDisplay.widthProperty().subtract(20)); // 1
                        setMaxWidth(Control.USE_PREF_SIZE); //2
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (item != null && !empty) {
                            this.setWrapText(true); // 3
                            setText(item);
                        } else {
                            setText(null);
                        }
                    }

                };
            }
        });
        ObservableList<String> objectivs =FXCollections.observableArrayList();
        if (this.game.getMainMap().getMainCharacter().getCurrentObjectivs()!= null &&
                !this.game.getMainMap().getMainCharacter().getCurrentObjectivs().isEmpty()) {
            for (Objectiv o : this.game.getMainMap().getMainCharacter().getCurrentObjectivs()) {
                objectivs.add(o.getDescription());
            }
        }
        objectivsDisplay.setItems(objectivs);
    }

    public void locationDisplay() {
        locationField.setWrapText(true);
        locationField.setText(game.getMainMap().getMainCharacter().getLocation().describe());
    }

    public void questDisplay() {
        questField.setWrapText(true);
        if (game.getMainMap().getMainCharacter().getQuest() != null) {
            questField.setText(game.getMainMap().getMainCharacter().getQuest().getDescription());
        } else {
            questField.setText("");
        }
    }

    public void initialize() {
        textLogs.appendText(this.game.getManuel() + "\n");
        objectivsDisplay();
        inventoryDisplay();
        locationDisplay();
        questDisplay();
    }
}
