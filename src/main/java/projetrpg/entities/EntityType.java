package projetrpg.entities;

public enum EntityType {
    PLAYER(0),
    VILLAGER(2),
    BIOBOT(15),
    VAMPIRE(10);

    private final int experienceRewarded;

    EntityType(int experienceRewarded) {
        this.experienceRewarded = experienceRewarded;
    }

    public int getExperienceRewarded() {
        return experienceRewarded;
    }
}
