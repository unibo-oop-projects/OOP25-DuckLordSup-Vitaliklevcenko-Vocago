package it.unibo.vocago.model.types;

/**
 * Holds the constants and helper logic for the daily study goal, i.e. the
 * number of words the user aims to practise each day.
 */
public final class DailyGoalSettings {

    /** The minimum allowed daily goal. */
    public static final int MIN = 5;

    /** The maximum allowed daily goal. */
    public static final int MAX = 40;

    /** The default daily goal. */
    public static final int DEFAULT = 10;

    private DailyGoalSettings() {
    }

    /**
     * Returns the given daily goal if it lies within the allowed range,
     * otherwise the default value.
     *
     * @param dailyGoal the requested daily goal
     * @return a valid daily goal
     */
    public static int normalize(final int dailyGoal) {
        if (dailyGoal < MIN || dailyGoal > MAX) {
            return DEFAULT;
        }
        return dailyGoal;
    }
}
