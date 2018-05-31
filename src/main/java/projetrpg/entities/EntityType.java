package projetrpg.entities;

/**
 * Represents the different types of an entity.
 *
 * @author mhevin
 * @author mbenoukaiss
 */
public enum EntityType {
    PLAYER(0, true),
    VILLAGER(1, false),
    HOSTILE_VILLAGER(10, true),
    UTILS(0, true),
    VAMPIRE(5, true),
    MARTIAN(10, true),
    BOSS_PEACEFUL(10, false),
    BOSS_HOSTILE(10, true),
    RANDOM_THING(1, true);

    /**
     * The amount of experience given to the player
     * when he kills this entity.
     */
    private final int experienceRewarded;

    /**
     * True if the entity can be killed.
     */
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
