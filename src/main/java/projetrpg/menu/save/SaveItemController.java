package projetrpg.menu.save;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.Scanner;

public class SaveItemController implements Initializable {

    private File save;
    private Calendar created;
    private Calendar lastSaved;

    @FXML
    private Label name;

    @FXML
    private Label dates;

    @FXML
    private ProgressBar bar;

    public SaveItemController(File save) {
        this.save = save;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Scanner sc;
        try {
            sc = new Scanner(save);
            sc.useDelimiter("\"name\": \"").next();
            sc.useDelimiter("\",");

            this.name.setText(sc.next().replace("\"name\": \"", ""));

            sc.useDelimiter(SavesServices.SEPARATOR);
            sc.next();

            this.created = Calendar.getInstance();
            created.setTimeInMillis(Long.valueOf(sc.next()));
            this.lastSaved = Calendar.getInstance();
            lastSaved.setTimeInMillis(Long.valueOf(sc.next()));

            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy 'at' hh:mm");
            dates.setText(String.format(
                    dates.getText(),
                    sdf.format(created.getTime()),
                    sdf.format(lastSaved.getTime())
            ));

        }catch(FileNotFoundException ignored) {}
    }

    public void rename() {

    }

    public void delete() {

    }

    public void load() {

    }
}
