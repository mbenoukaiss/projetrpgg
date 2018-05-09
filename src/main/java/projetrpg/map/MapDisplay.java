package projetrpg.map;


import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.SwipeEvent;
import javafx.scene.paint.Color;
import projetrpg.game.Game;

/**
 * Created by mhevin on 09/05/18.
 */
public class MapDisplay {

    private MainMap map;
    private Scene scene;

    public MapDisplay(MainMap map) {
        this.map = map;
    }

    public void draw() {
        Group root = new Group();
        Canvas canvas = new Canvas(500, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawRegion(gc);
        root.getChildren().add(canvas);
        this.scene = new Scene(root);
    }

    private void drawRegion(GraphicsContext g) {
        g.setFill(Color.BLACK);
        g.setLineWidth(4);
        int x = 100, y = 100;
        for (Region r : this.map.getRegions()) {
            g.fillOval(x, y, 50, 50);
            x+=50;
            y+=50;
        }
    }

    public Scene getScene() {
        return scene;
    }
}
