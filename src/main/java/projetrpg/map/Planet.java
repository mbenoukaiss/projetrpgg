package projetrpg.map;

public class Planet extends Region {

    private Region landingRegion;

    public Planet(int id, String name, Region parent, int shipLevelRequired) {
        super(id, name, parent, shipLevelRequired);
    }

    public Planet(Region r) {
        super(r);
    }

    public Region getLandingRegion() {
        return landingRegion;
    }

    public void setLandingRegion(Region landingRegion) {
        this.landingRegion = landingRegion;
    }
}
