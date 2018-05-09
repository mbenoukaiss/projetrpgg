package projetrpg.map;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RegionTest {

    @Test
    public void getContainedRegions() {
        Region southRegion = new Region(3, "South", null, 0);

        Region flowerPlains = new Region(8, "Flower Plains", southRegion, 0);
        Region flowerMountains = new Region(9, "Flower Mountains", southRegion, 0);

        ArrayList<Region> south = new ArrayList<>();
        south.add(flowerPlains);
        south.add(flowerMountains);

        assertArrayEquals(southRegion.getContainedRegions().toArray(), south.toArray());
    }

    @Test
    public void getParent() {
        Region southRegion = new Region(3, "South", null, 0);

        Region flowerPlains = new Region(8, "Flower Plains", southRegion, 0);

        assertEquals(southRegion, flowerPlains.getParent());
    }

    @Test
    public void linkToDirection() {

        Region centerRegion = new Region(1, "Center", null, 0);
        Region northRegion = new Region(2, "North", null, 0);
        Region southRegion = new Region(3, "South", null, 0);

        centerRegion.linkToDirection(northRegion, Direction.NORTH);
        centerRegion.linkToDirection(southRegion, Direction.SOUTH);

        assertEquals(centerRegion, southRegion.getRegionOnDirection().get(Direction.NORTH));
        assertEquals(centerRegion, northRegion.getRegionOnDirection().get(Direction.SOUTH));
    }
}