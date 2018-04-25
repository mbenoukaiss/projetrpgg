package projetrpg.game;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import projetrpg.commands.CommandParser;
import projetrpg.commands.InvalidCommandException;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController {

    private Game game;
    private CommandParser parser;
    private String logs = "";
    @FXML
    private TextField commandField;
    @FXML
    private TextArea textLogs;

    GameController(Game game) {
        this.game = game;
        parser = game.getParser();
    }


    public void buttonQuit() {
        Platform.exit();
    }
    public void enterCommand(KeyEvent e) {
        if (KeyCode.ENTER == e.getCode()) {
            try {
                logs += parser.parse(game.getMainMap().getMainCharacter(), commandField.getText()).send();
            } catch (InvalidCommandException e1) {
                e1.printStackTrace();
            }

            textLogs.setText(logs);
            commandField.clear();
        }
    }

}
