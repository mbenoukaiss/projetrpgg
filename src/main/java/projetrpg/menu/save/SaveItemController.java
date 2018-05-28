package projetrpg.menu.save;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextInputDialog;
import projetrpg.Main;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.ResourceBundle;

public class SaveItemController implements Initializable {

    private final Main main;
    private final SaveManager saveManager;
    private final File saveFile;

    private Save save;

    @FXML
    private Label name;

    @FXML
    private Label dates;

    @FXML
    private ProgressBar bar;

    public SaveItemController(Main main, SaveManager sm, File saveFile) {
        this.main = main;
        this.saveManager = sm;
        this.saveFile = saveFile;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.save = saveManager.saveServices.load(saveFile);
            name.setText(save.getMap().getName());

            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy 'at' HH:mm");
            dates.setText(String.format(
                    dates.getText(),
                    sdf.format(save.getCreated().getTime()),
                    sdf.format(save.getLastSaved().getTime())
            ));
        } catch(SaveException e) {
            e.printStackTrace();
        }
    }

    public void rename() throws SaveException {
        TextInputDialog input = new TextInputDialog();
        input.setTitle("Renaming map");
        input.setContentText("What should be the new name ?");

        Optional<String> result;
        do {
            result = input.showAndWait();

            //Cancel/ALT+F4/anything else
            if(!result.isPresent()) return;
        } while(result.get().isEmpty());

        save.getMap().setName(result.get());
        saveManager.saveServices.save(save);
        saveManager.refresh();
    }

    public void delete() {
        saveManager.saveServices.delete(save);
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        saveManager.refresh();
    }

    public void load() {
        main.launchMainGame(save);
    }
}
