package projetrpg.entities;

public enum EntityType {
    PLAYER(0, true),
    VILLAGER(10, true),
    BIOBOT(50, true),
    VAMPIRE(45, true);

    private final int experienceRewarded;
    private final boolean isKillable;

    EntityType(int experienceRewarded, boolean isKillable) {
        this.experienceRewarded = experienceRewarded;
        this.isKillable = isKillable;
    }

    public int getExperienceRewarded() {
        return experienceRewarded;
    }

    public boolean isKillable() {
        return isKillable;
    }

}
