package it.unibo.vocago.model.statistics;

import java.time.LocalDate;

import it.unibo.vocago.model.statistics.api.Statistics;

public class ProfileStatistics implements Statistics {
    
    private final int masteredItems;
    private final int correctAnswers;
    private final int wrongAnswers;
    private final int totalWords;
    private final double accuracyPercent;
    private final LocalDate lastStudyDate;
    private final int currentStreak;
    private final long totalStudyTime;
    
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
