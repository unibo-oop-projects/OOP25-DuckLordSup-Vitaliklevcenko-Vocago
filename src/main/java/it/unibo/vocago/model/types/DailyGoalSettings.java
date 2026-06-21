package it.unibo.vocago.model.types;

public final class DailyGoalSettings {

    public static final int MIN = 5;
    public static final int MAX = 40;
    public static final int DEFAULT = 10;

    private DailyGoalSettings() {
    }

    public static int normalize(final int dailyGoal) {
        if (dailyGoal < MIN || dailyGoal > MAX) {
            return DEFAULT;
        }
        return dailyGoal;
    }

}
