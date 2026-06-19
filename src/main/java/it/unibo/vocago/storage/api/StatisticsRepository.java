package it.unibo.vocago.storage.api;

import java.time.LocalDate;

public interface StatisticsRepository {

    void createStatisticsFile(String userName);

    void saveStatistics(String userName, LocalDate lastStudyDate, int currentStreak, long totalStudyTime);

    LocalDate getLastStudyDate(String userName);

    int getCurrentStreak(String userName);

    long getTotalStudyTime(String userName);

    boolean deleteStatistics(String userName);

    int getDailyGoal(String userName);

    void saveProfileConfigurations(String profileName, final String newProfileName, final int dailyGoal);
}
