package it.unibo.vocago.storage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import it.unibo.vocago.model.types.DailyGoalSettings;
import it.unibo.vocago.storage.api.StatisticsRepository;

public class StatisticsFileStorage implements StatisticsRepository {

    private static final Path USERS_DIRECTORY = Path.of("data", "users");
    private static final int LAST_STUDY_DATE_INDEX = 0;
    private static final int CURRENT_STREAK_INDEX = 1;
    private static final int TOTAL_STUDY_TIME_INDEX = 2;
    private static final int DAILY_GOAL_INDEX = 3;

    @Override
    public void createStatisticsFile(final String userName) {
        try {
            Files.createDirectories(USERS_DIRECTORY);
            final Path file = fileFor(userName);
            if (!Files.exists(file)) {
                saveStatistics(userName, LocalDate.now(), 0, 0L, DailyGoalSettings.DEFAULT);
            }
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not create statistics file for user: " + userName, exception);
        }
    }

    @Override
    public void saveStatistics(
            final String userName,
            final LocalDate lastStudyDate,
            final int currentStreak,
            final long totalStudyTime) {
        saveStatistics(userName, lastStudyDate, currentStreak, totalStudyTime, dailyGoalOrDefault(userName));
    }

    private void saveStatistics(
            final String userName,
            final LocalDate lastStudyDate,
            final int currentStreak,
            final long totalStudyTime,
            final int dailyGoal) {

        final int normalizedCurrentStreak = Math.max(currentStreak, 0);
        final long normalizedTotalStudyTime = Math.max(totalStudyTime, 0L);

        try {
            Files.createDirectories(USERS_DIRECTORY);
            Files.write(fileFor(userName), List.of(
                    lastStudyDate.toString(),
                    Integer.toString(normalizedCurrentStreak),
                    Long.toString(normalizedTotalStudyTime),
                    Integer.toString(DailyGoalSettings.normalize(dailyGoal))), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not save statistics for user: " + userName, exception);
        }
    }

    @Override
    public LocalDate getLastStudyDate(final String userName) {
        return LocalDate.parse(readStatisticsLines(userName).get(LAST_STUDY_DATE_INDEX));
    }

    @Override
    public int getCurrentStreak(final String userName) {
        return Integer.parseInt(readStatisticsLines(userName).get(CURRENT_STREAK_INDEX));
    }

    @Override
    public long getTotalStudyTime(final String userName) {
        return Long.parseLong(readStatisticsLines(userName).get(TOTAL_STUDY_TIME_INDEX));
    }

    @Override
    public int getDailyGoal(final String userName) {
        return Integer.parseInt(readStatisticsLines(userName).get(DAILY_GOAL_INDEX));
    }

    @Override
    public boolean deleteStatistics(final String userName) {
        try {
            return Files.deleteIfExists(fileFor(userName));
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not delete statistics for user: " + userName, exception);
        }
    }

    @Override
    public void saveProfileConfigurations(final String profileName, final String newProfileName, final int dailyGoal) {
        final String currentProfileName = profileName.trim();
        final String targetProfileName = newProfileName.trim();
        final LocalDate lastStudyDate = getLastStudyDate(currentProfileName);
        final int currentStreak = getCurrentStreak(currentProfileName);
        final long totalStudyTime = getTotalStudyTime(currentProfileName);

        try {
            Files.createDirectories(USERS_DIRECTORY);
            if (!currentProfileName.equals(targetProfileName)) {
                Files.move(fileFor(currentProfileName), fileFor(targetProfileName));
            }
            saveStatistics(targetProfileName, lastStudyDate, currentStreak, totalStudyTime, dailyGoal);
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not save statistics configuration for profile: " + currentProfileName,
                    exception);
        }
    }

    private List<String> readStatisticsLines(final String userName) {
        createStatisticsFile(userName);
        try {

            final List<String> lines = Files.readAllLines(fileFor(userName), StandardCharsets.UTF_8);
            if (lines.size() != 4) {
                resetStatisticsFile(userName);
                return Files.readAllLines(fileFor(userName), StandardCharsets.UTF_8);
            }

            LocalDate lastStudyDate;
            int currentStreak;
            long totalStudyTime;
            int dailyGoal;

            try {
                lastStudyDate = LocalDate.parse(lines.get(LAST_STUDY_DATE_INDEX));
            } catch (RuntimeException exception) {
                lastStudyDate = LocalDate.now();
            }
            try {
                currentStreak = Integer.parseInt(lines.get(CURRENT_STREAK_INDEX));
            } catch (RuntimeException exception) {
                currentStreak = 0;
            }
            try {
                totalStudyTime = Long.parseLong(lines.get(TOTAL_STUDY_TIME_INDEX));
            } catch (RuntimeException exception) {
                totalStudyTime = 0L;
            }
            try {
                dailyGoal = Integer.parseInt(lines.get(DAILY_GOAL_INDEX));
                dailyGoal = DailyGoalSettings.normalize(dailyGoal);
            } catch (RuntimeException exception) {
                dailyGoal = DailyGoalSettings.DEFAULT;
            }
            saveStatistics(userName, lastStudyDate, currentStreak, totalStudyTime, dailyGoal);
            return Files.readAllLines(fileFor(userName), StandardCharsets.UTF_8);
            
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not load statistics for user: " + userName, exception);
        }
    }

    private void resetStatisticsFile(final String userName) {
        try{
            Files.deleteIfExists(fileFor(userName));
            saveStatistics(userName, LocalDate.now(), 0, 0L, DailyGoalSettings.DEFAULT);
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not reset statistics for user: " + userName, exception);
        }
    }

    private static Path fileFor(final String userName) {
        return USERS_DIRECTORY.resolve(userName.trim() + ".statistics");
    }

    private int dailyGoalOrDefault(final String userName) {
        try {
            return getDailyGoal(userName);
        } catch (RuntimeException exception) {
            return DailyGoalSettings.DEFAULT;
        }
    }

}
