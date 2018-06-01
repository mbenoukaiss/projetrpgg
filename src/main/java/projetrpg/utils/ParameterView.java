package projetrpg.utils;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ParameterView  {

    private Slider textSpeed = new Slider(0.001, 0.1,0.02);
    private Text title = new Text("Parameters : text speed :");
    private Text slower = new Text("Slower");
    private Text faster = new Text("Faster");
    private Scene scene;

    public ParameterView() {
        this.textSpeed.relocate(200 - (textSpeed.getWidth()/2), 249);
        this.title.relocate(200 - (this.title.getStrokeWidth()/2), 100);
        this.faster.relocate(this.textSpeed.getLayoutX()-this.faster.getStrokeWidth()-50, 249);
        this.slower.relocate(this.textSpeed.getLayoutX()+150, 249);
        Group g = new Group(this.textSpeed, this.title, this.slower, this.faster);
        this.scene = new Scene(g, 500, 500);
    }

    public Scene getScene() {
        return scene;
    }

    public Slider getTextSpeed() {
        return textSpeed;
    }
}
