package projetrpg.map;


import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.SwipeEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import projetrpg.game.Game;
import projetrpg.utils.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        g.setFill(Color.BROWN);
        g.setStroke(Color.GRAY);
        g.setLineWidth(4);
        int x = 50, y = 50;
        int i =0;
        Map<Region,Point> regionsCoordinates = new HashMap<>();
        for (Region r : this.map.getRegions()) {
            if (r == this.map.getMainCharacter().getShip()) {
                Point p = new Point(250, 250);
                regionsCoordinates.put(r, p);
                g.fillOval(200, 200, 50, 50);
            } else {
                Point p = new Point(x + 25, y + 25);
                g.fillOval(x, y, 50, 50);
                regionsCoordinates.put(r, p);
                x += 350;
                if (i % 2 != 0) {
                    y += 100;
                    x = 50;
                }
            }
            i++;
        }
        for (Region r : regionsCoordinates.keySet()) {
            System.out.println(r.getName());
            for (Region re : r.getGoingableRegions()) {
                g.strokeLine(regionsCoordinates.get(r).x, regionsCoordinates.get(r).y,
                        regionsCoordinates.get(re).x, regionsCoordinates.get(re).y);
            }
        }
    }

    public Scene getScene() {
        return scene;
    }
}
