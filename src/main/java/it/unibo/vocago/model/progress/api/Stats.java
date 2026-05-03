package it.unibo.vocago.model.progress.api;

import java.time.LocalDate;

public interface Stats {

    int getMasteredItems();

    int getCorrectAnswers();

    int getWrongAnswers();

    int getTotalWords();

    double getAccuracyPercent();
    
    LocalDate getLastStudyDate();

    int getCurrentStreak();

    long getTotalStudyTime();
}
