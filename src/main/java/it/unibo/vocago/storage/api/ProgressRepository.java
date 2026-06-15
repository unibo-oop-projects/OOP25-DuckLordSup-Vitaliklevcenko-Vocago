package it.unibo.vocago.storage.api;

import java.time.LocalDate;

public interface ProgressRepository {

    void createProgressFile(String userName);

    void saveStats(String userName, LocalDate lastStudyDate, int currentStreak, long totalStudyTime);

    LocalDate getLastStudyDate(String userName);

    int getCurrentStreak(String userName);

    long getTotalStudyTime(String userName);

    boolean deleteProgress(String userName);

    int getDailyGoal(String userName);

    void saveProfileConfigurations(String profileName, final String newProfileName, final int dailyGoal);
}
