package projetrpg.game;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
        }
    }

    public void inventoryDisplay() {
        ObservableList<String> items =FXCollections.observableArrayList();
        for(Item i : game.getMainMap().getMainCharacter().getInventory().getAll()) {
            items.add(i.getName());
        }
        inventoryDisplay.setItems(items);
    }

    public void locationDisplay() {
        locationField.setWrapText(true);
        locationField.setText(game.getMainMap().getMainCharacter().getLocation().describe());
    }

    public void initialize() {
        inventoryDisplay();
        locationDisplay();
    }
}
