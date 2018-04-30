package projetrpg.entities;

/**
 * Represents the different types of an entity.
 *
 * @author mhevin
 * @author mbenoukaiss
 */
public enum EntityType {
    PLAYER(0, true),
    VILLAGER(1, true),
    VAMPIRE(5, true);

    private final int experienceRewarded;

    private final boolean isKillable;

    EntityType(int experienceRewarded, boolean isKillable) {
        this.experienceRewarded = experienceRewarded;
        this.isKillable = isKillable;
    }

    /**
     * Accessor for the experience rewards.
     * @return the experience rewards.
     */
    public int getExperienceRewarded() {
        return experienceRewarded;
    }

    /**
     * Accessor for the status on weither this type can be killed.
     * @return the status.
     */
    public boolean isKillable() {
        return isKillable;
    }

}
