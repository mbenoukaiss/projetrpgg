package projetrpg.map;


import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.SwipeEvent;
import javafx.scene.paint.Color;
import projetrpg.game.Game;

import java.util.Set;

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
        g.setLineWidth(3);
        g.setStroke(Color.GRAY);
        for (Direction dir : this.map.getMainCharacter().getLocation().getRegionOnDirection().keySet()) {
            switch (dir) {
                case NORTH:
                    drawRegion(g, Direction.NORTH, 225, 50);
                    break;
                case SOUTH:
                    drawRegion(g, Direction.SOUTH, 225, 400);
                    break;
                case EST:
                    drawRegion(g, Direction.EST, 400, 225);
                    break;
                case WEST:
                    drawRegion(g, Direction.WEST, 50, 225);
                    break;
            }
        }
        int i = 0;
        int x = 150;
        int y = 150;
        Set<Region> goingableRegion = this.map.getMainCharacter().getLocation().getContainedRegions();
        if (this.map.getMainCharacter().getLocation().getParent() != null) {
            goingableRegion.add(this.map.getMainCharacter().getLocation().getParent());
        }
        for (Region region : goingableRegion) {
            if (i % 2 != 0) {
                x=150;
                y+=250;
            }
            g.strokeLine(250, 250, x, y);
            g.setFill(Color.BLUEVIOLET);
            g.fillOval(x-25, y-25, 50, 50);
            g.fillText(region.getName(), x-25, y+50);
            x+=200;
        }
        g.setFill(Color.MEDIUMVIOLETRED);
        g.fillOval(225, 225, 50, 50);
    }

    public Scene getScene() {
        return scene;
    }

    private void drawRegion(GraphicsContext g, Direction dir, int x, int y) {
        g.strokeLine(250, 250, x+25,y+25);
        g.fillOval(x, y, 50, 50);
        g.fillText(this.map.getMainCharacter().getLocation().getRegionOnDirection().get(dir).getName(),
                x+50, y+50);
    }
}
