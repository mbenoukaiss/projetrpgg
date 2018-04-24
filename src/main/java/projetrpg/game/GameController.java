package projetrpg.game;

import javafx.application.Platform;

public class GameController {

    private Game game;

    GameController(Game game) {
        this.game = game;
    }

    public void buttonQuit() {
        Platform.exit();
    }

}
