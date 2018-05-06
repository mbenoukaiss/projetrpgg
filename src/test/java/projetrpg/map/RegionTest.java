package projetrpg.map;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RegionTest {

    @Test
    public void getContainedRegions() {
        Region southRegion = new Region(3, "South", null);

        Region flowerPlains = new Region(8, "Flower Plains", southRegion);
        Region flowerMountains = new Region(9, "Flower Mountains", southRegion);

        ArrayList<Region> south = new ArrayList<>();
        south.add(flowerPlains);
        south.add(flowerMountains);

        assertArrayEquals(southRegion.getContainedRegions().toArray(), south.toArray());
    }

    @Test
    public void getParent() {
        Region southRegion = new Region(3, "South", null);

        Region flowerPlains = new Region(8, "Flower Plains", southRegion);

        assertEquals(southRegion, flowerPlains.getParent());
    }

    @Test
    public void linkToDirection() {

        Region centerRegion = new Region(1, "Center", null);
        Region northRegion = new Region(2, "North", null);
        Region southRegion = new Region(3, "South", null);

        centerRegion.linkToDirection(northRegion, Direction.NORTH);
        centerRegion.linkToDirection(southRegion, Direction.SOUTH);

        assertEquals(centerRegion, southRegion.getRegionOnDirection().get(Direction.NORTH));
        assertEquals(centerRegion, northRegion.getRegionOnDirection().get(Direction.SOUTH));
    }
}