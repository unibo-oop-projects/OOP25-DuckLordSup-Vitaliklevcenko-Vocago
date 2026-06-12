package it.unibo.vocago.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.storage.api.ProgressRepository;

public class ProgressFileStorage implements ProgressRepository {

    private static final Path USERS_DIRECTORY = Path.of("data", "users");
    private static final int LAST_STUDY_DATE_INDEX = 0;
    private static final int CURRENT_STREAK_INDEX = 1;
    private static final int TOTAL_STUDY_TIME_INDEX = 2;

    @Override
    public void createProgressFile(final String userName) {
        try {
            Files.createDirectories(USERS_DIRECTORY);
            final Path file = fileFor(userName);
            if (!Files.exists(file)) {
                saveStats(userName, LocalDate.now(), 0, 0L);
            }
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not create progress file for user: " + userName, exception);
        }
    }

    @Override
    public void saveStats(
            final String userName,
            final LocalDate lastStudyDate,
            final int currentStreak,
            final long totalStudyTime) {

        if (currentStreak < 0 || totalStudyTime < 0) {
            throw new IllegalArgumentException("Progress values must not be negative.");
        }

        try {
            Files.createDirectories(USERS_DIRECTORY);
            Files.write(fileFor(userName), List.of(
                    lastStudyDate.toString(),
                    Integer.toString(currentStreak),
                    Long.toString(totalStudyTime)), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not save progress for user: " + userName, exception);
        }
    }

    @Override
    public LocalDate getLastStudyDate(final String userName) {
        return LocalDate.parse(readProgressLines(userName).get(LAST_STUDY_DATE_INDEX));
    }

    @Override
    public int getCurrentStreak(final String userName) {
        return Integer.parseInt(readProgressLines(userName).get(CURRENT_STREAK_INDEX));
    }

    @Override
    public long getTotalStudyTime(final String userName) {
        return Long.parseLong(readProgressLines(userName).get(TOTAL_STUDY_TIME_INDEX));
    }

    @Override
    public boolean deleteProgress(final String userName) {
        try {
            return Files.deleteIfExists(fileFor(userName));
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not delete progress for user: " + userName, exception);
        }
    }

    private List<String> readProgressLines(final String userName) {
        createProgressFile(userName);

        try {
            final List<String> lines = Files.readAllLines(fileFor(userName), StandardCharsets.UTF_8);
            if (lines.size() < 3) {
                throw new IllegalStateException("Invalid progress file for user: " + userName);
            }
            return lines;
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not load progress for user: " + userName, exception);
        }
    }

    private static Path fileFor(final String userName) {
        return USERS_DIRECTORY.resolve(userName.trim() + ".progress");
    }
    
}
