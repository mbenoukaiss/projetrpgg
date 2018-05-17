package projetrpg.map;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import projetrpg.entities.player.Player;

import java.awt.*;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by mhevin on 09/05/18.
 */
public class MapDisplay {

    private MainMap map;

    public MapDisplay(MainMap map) {
        this.map = map;
    }

    public Canvas drawLocal() {
        Canvas canvas = new Canvas(406.0, 265.0);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawRegion(gc);
        return canvas;
    }

    public Canvas drawPlanets() {
        Canvas canvas = new Canvas(406.0, 220.0);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawPlanets(gc);
        return canvas;
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
        if (goingableRegion.contains(this.map.getMainCharacter().getShip())) {
            goingableRegion.remove(this.map.getMainCharacter().getShip());
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
        g.setFill(Color.BROWN);
        g.fillText("Local map : ", 20, 20);
    }

    public void drawPlanets(GraphicsContext g) {
        int x = 100;
        int y = 50;
        int i = 0;
        Region playerRegion = null;
        HashMap<Region, Point> regionsCoordinates = new HashMap<>();
        for(Region region : this.map.getRegions()) {
            //Si il est sur la même planète ou son vaisseau est sur la même planète
            if(this.map.getMainCharacter().getLocation().getRoot() == region ||
                    (this.map.getMainCharacter().getShip().getParent() != null &&
                            this.map.getMainCharacter().getShip().getParent().getRoot() == region)) {
                playerRegion = region;
            }
            regionsCoordinates.put(region, new Point(x, y));
            x += 200;
            if(i % 2 != 0) {
                y += 50;
                x = 100;
            }
            i++;
        }

        g.setStroke(Color.GRAY);
        g.setLineWidth(4);

        for(Region region : this.map.getRegions()) {
            Player player = this.map.getMainCharacter();
            if(region == playerRegion) {
                g.setFill(Color.SANDYBROWN);
                g.fillOval(regionsCoordinates.get(region).x, regionsCoordinates.get(region).y, 30, 30);
            } else {
                if(player.canTravelTo(region)) {
                    g.strokeLine(regionsCoordinates.get(playerRegion).x + 15, regionsCoordinates.get(playerRegion).y + 15,
                            regionsCoordinates.get(region).x + 15, regionsCoordinates.get(region).y + 15);
                    g.setFill(Color.BROWN);
                    g.fillOval(regionsCoordinates.get(region).x, regionsCoordinates.get(region).y, 30, 30);
                    g.fillText(region.getName(), regionsCoordinates.get(region).x, regionsCoordinates.get(region).y + 45);
                } else {
                    g.setFill(Color.BROWN);
                    g.fillOval(regionsCoordinates.get(region).x, regionsCoordinates.get(region).y, 30, 30);
                    g.fillText(region.getName(), regionsCoordinates.get(region).x, regionsCoordinates.get(region).y + 45);
                }
            }

        }

        for(Region region : this.map.getRegions()) {
            if(region == playerRegion) {
                g.setFill(Color.SANDYBROWN);
                g.fillOval(regionsCoordinates.get(region).x, regionsCoordinates.get(region).y, 30, 30);
            }
        }

        g.fillOval(50, 50, 10, 10);
        g.fillText("YOU", 20, 60);
        g.setFill(Color.BROWN);
        g.fillText("Planetary map : ", 20, 20);
    }

    private void drawRegion(GraphicsContext g, Direction dir, int x, int y) {
        g.strokeLine(203, 132, x+12,y+20);
        g.fillOval(x, y, 30, 30);
        g.fillText(this.map.getMainCharacter().getLocation().getRegionOnDirection().get(dir).getName(),
                x+10, y+45);
    }
}
