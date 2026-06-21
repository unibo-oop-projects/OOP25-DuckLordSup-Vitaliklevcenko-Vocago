package it.unibo.vocago.model.statistics;

import java.time.LocalDate;

import it.unibo.vocago.model.statistics.api.Statistics;

/**
 * Implementation of {@link Statistics}.
 */
public final class ProfileStatistics implements Statistics {

    private final int masteredItems;
    private final int correctAnswers;
    private final int wrongAnswers;
    private final int totalWords;
    private final double accuracyPercent;
    private final LocalDate lastStudyDate;
    private final int currentStreak;
    private final long totalStudyTime;

    /**
     * Creates a statistics snapshot.
     *
     * @param masteredItems   the number of mastered items
     * @param correctAnswers  the total number of correct answers
     * @param wrongAnswers    the total number of wrong answers
     * @param totalWords      the total number of words in the vocabulary
     * @param accuracyPercent the overall accuracy, between 0 and 100
     * @param lastStudyDate   the date of the last study session
     * @param currentStreak   the current consecutive-day study streak
     * @param totalStudyTime  the total study time, in milliseconds
     */
    public ProfileStatistics(
            final int masteredItems,
            final int correctAnswers,
            final int wrongAnswers,
            final int totalWords,
            final double accuracyPercent,
            final LocalDate lastStudyDate,
            final int currentStreak,
            final long totalStudyTime) {

        this.masteredItems = masteredItems;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
        this.totalWords = totalWords;
        this.accuracyPercent = accuracyPercent;
        this.currentStreak = currentStreak;
        this.lastStudyDate = lastStudyDate;
        this.totalStudyTime = totalStudyTime;
    }

    @Override
    public int getMasteredItems() {
        return this.masteredItems;
    }

    @Override
    public int getTotalCorrectAnswers() {
        return this.correctAnswers;
    }

    @Override
    public int getTotalWrongAnswers() {
        return this.wrongAnswers;
    }

    @Override
    public int getTotalWords() {
        return this.totalWords;
    }

    @Override
    public double getAccuracyPercent() {
        return this.accuracyPercent;
    }

    @Override
    public LocalDate getLastStudyDate() {
        return this.lastStudyDate;
    }

    @Override
    public int getCurrentStreak() {
        return this.currentStreak;
    }

    @Override
    public long getTotalStudyTime() {
        return this.totalStudyTime;
    }
}