package projetrpg.entities;

public enum EntityType {
    PLAYER(0),
    VILLAGER(10),
    BIOBOT(50),
    VAMPIRE(45);

    private final int experienceRewarded;

    EntityType(int experienceRewarded) {
        this.experienceRewarded = experienceRewarded;
    }

    public int getExperienceRewarded() {
        return experienceRewarded;
    }
}
