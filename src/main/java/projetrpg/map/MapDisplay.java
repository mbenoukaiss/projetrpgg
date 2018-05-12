package projetrpg.map;


import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.SwipeEvent;
import javafx.scene.paint.Color;
import projetrpg.entities.player.Ship;
import projetrpg.game.Game;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
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

    public Canvas drawLocal() {
        Canvas canvas = new Canvas(406.0, 265.0);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawRegion(gc);
        return canvas;
    }

    public void drawPlanets() {
        Group root = new Group();
        Canvas canvas = new Canvas(600, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawPlanets(gc);
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
                    drawRegion(g, Direction.NORTH, 188, 25);
                    break;
                case SOUTH:
                    drawRegion(g, Direction.SOUTH, 188, 210);
                    break;
                case EST:
                    drawRegion(g, Direction.EST, 320, 117);
                    break;
                case WEST:
                    drawRegion(g, Direction.WEST, 25, 117);
                    break;
            }
        }
        int i = 0;
        int x = 110;
        int y = 70;
        Set<Region> goingableRegion = this.map.getMainCharacter().getLocation().getContainedRegions();
        if (this.map.getMainCharacter().getLocation().getParent() != null && !this.map.getRegions().contains(
                this.map.getMainCharacter().getLocation().getParent())) {
            goingableRegion.add(this.map.getMainCharacter().getLocation().getParent());
        }
        for (Region region : goingableRegion) {
            if (i % 2 != 0) {
                x=60;
                y+=135;
            }
            i++;
            g.strokeLine(203, 132, x+15, y+15);
            g.setFill(Color.BLUEVIOLET);
            g.fillOval(x, y, 30, 30);
            g.fillText(region.getName(), x-25, y+40);
            x+=140;
        }
        g.setFill(Color.MEDIUMVIOLETRED);
        g.fillOval(188, 117, 30, 30);
        g.fillText("YOU", 217, 160);
    }

    public void drawPlanets(GraphicsContext g) {
        int x = 50, y = 50;
        int i = 0;
        HashMap<Region, Point> regionCoordinates = new HashMap<>();
        Region playerRegion = new Region();
        for (Region r : this.map.getRegions()) {
            if (this.containsRegion(r, this.map.getMainCharacter().getLocation()) ||
                    (this.map.getMainCharacter().getLocation() == this.map.getMainCharacter().getShip() &&
                    this.containsRegion(r, this.map.getMainCharacter().getShip().getLastRegion()))) {
                playerRegion = r;
            }
            regionCoordinates.put(r, new Point(x, y));
            x+=350;
            if (i % 2 != 0) {
                y += 100;
                x=50;
            }
            i++;
        }

        g.setStroke(Color.GRAY);
        g.setLineWidth(5);
        for (Region region : this.map.getRegions()) {
            if (this.map.getMainCharacter().canTravelTo(region)) {
                g.strokeLine(regionCoordinates.get(playerRegion).x,
                        regionCoordinates.get(playerRegion).y,
                        regionCoordinates.get(region).x, regionCoordinates.get(region).y);
            }
            if (this.containsRegion(region, this.map.getMainCharacter().getLocation()) ||
                    this.containsRegion(region, this.map.getMainCharacter().getShip().getLastRegion())) {
                g.setFill(Color.SANDYBROWN);
                g.fillOval(regionCoordinates.get(playerRegion).x-25,
                        regionCoordinates.get(playerRegion).y-25, 50, 50);
            } else {
                g.setFill(Color.BROWN);
                g.fillOval(regionCoordinates.get(region).x-25, regionCoordinates.get(region).y-25, 50, 50);
            }
            g.fillText(region.getName(), regionCoordinates.get(region).x-25, regionCoordinates.get(region).y+40);
        }
    }

    public Scene getScene() {
        return scene;
    }

    private void drawRegion(GraphicsContext g, Direction dir, int x, int y) {
        g.strokeLine(203, 132, x+12,y+20);
        g.fillOval(x, y, 30, 30);
        g.fillText(this.map.getMainCharacter().getLocation().getRegionOnDirection().get(dir).getName(),
                x+25, y+40);
    }

    private boolean containsRegion(Region r, Region r2) {
        if (r.getContainedRegions().contains(r2) || r == r2) {
            return true;
        }
        for (Region region : r.getContainedRegions()) {
            return containsRegion(region, r2);
        }
        return false;
    }
}
