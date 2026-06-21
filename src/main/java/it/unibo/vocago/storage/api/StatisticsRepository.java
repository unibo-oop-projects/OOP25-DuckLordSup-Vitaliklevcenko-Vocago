package it.unibo.vocago.storage.api;

import java.time.LocalDate;

/**
 * Defines the persistence contract per user statistics and profile configuration,
 * hiding the underlying storage mechanism from the rest of the application.
 */
public interface StatisticsRepository {

    /**
     * Creates the statistics file for the given user if it does not exist.
     *
     * @param userName the user name
     */
    void createStatisticsFile(String userName);

    /**
     * Saves the statistics of the given user.
     *
     * @param userName       the user name
     * @param lastStudyDate  the date of the last study session
     * @param currentStreak  the current consecutive-day streak
     * @param totalStudyTime the total study time, in milliseconds
     */
    void saveStatistics(String userName, LocalDate lastStudyDate, int currentStreak, long totalStudyTime);

    /**
     * @param userName the user name
     * @return the date of the user's last study session
     */
    LocalDate getLastStudyDate(String userName);

    /**
     * @param userName the user name
     * @return the user's current study streak
     */
    int getCurrentStreak(String userName);

    /**
     * @param userName the user name
     * @return the user's total study time, in milliseconds
     */
    long getTotalStudyTime(String userName);

    /**
     * Deletes the statistics of the given user.
     *
     * @param userName the user name
     * @return {@code true} if the statistics were deleted
     */
    boolean deleteStatistics(String userName);

    /**
     * @param userName the user name
     * @return the user's daily study goal
     */
    int getDailyGoal(String userName);

    /**
     * Saves the configuration of a profile, possibly renaming it.
     *
     * @param profileName    the current profile name
     * @param newProfileName the new profile name
     * @param dailyGoal      the daily study goal
     */
    void saveProfileConfigurations(String profileName, String newProfileName, int dailyGoal);
}
