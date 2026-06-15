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

    void saveProfileConfigurations(String userName,final String newUserName, final String firstLanguage,
            final String secondLanguage, final int dailyGoal);
}
