package it.unibo.vocago.model.types;

/**
 * The mastery level of a vocabulary item in a given direction. Each level
 * carries a weight multiplier used by the question selection logic, so that
 * less mastered items are practised more frequently.
 */
public enum MasteryLevel {

    /** The item has never been answered yet. */
    NEW(0),

    /** The item is poorly known. */
    BAD(0.8),

    /** The item is partially known. */
    MEDIUM(0.9),

    /** The item is well known. */
    GOOD(1.05),

    /** The item is fully mastered. */
    MASTER(1.35);

    private final double multiplier;

    MasteryLevel(final double multiplier) {
        this.multiplier = multiplier;
    }

    /**
     * @return the weight multiplier associated with this level
     */
    public double getMultiplier() {
        return this.multiplier;
    }

    /**
     * Returns the next, higher mastery level, or the same level if already at
     * the maximum.
     *
     * @return the higher mastery level
     */
    public MasteryLevel next() {
        if (this == MASTER) {
            return this;
        } else if (this == NEW) {
            return MEDIUM;
        }
        return values()[this.ordinal() + 1];
    }

    /**
     * Returns the previous, lower mastery level, or the same level if already
     * at the minimum.
     *
     * @return the lower mastery level
     */
    public MasteryLevel previous() {
        if (this == BAD) {
            return this;
        } else if (this == NEW) {
            return BAD;
        } else if (this == MASTER) {
            return MEDIUM;
        }
        return values()[this.ordinal() - 1];
    }
}
