package it.unibo.vocago.model.statistics.api;

import java.time.LocalDate;

public interface Statistics {

    int getMasteredItems();

    int getTotalCorrectAnswers();

    int getTotalWrongAnswers();

    int getTotalWords();

    double getAccuracyPercent();
    
    LocalDate getLastStudyDate();

    int getCurrentStreak();

    long getTotalStudyTime();
}
