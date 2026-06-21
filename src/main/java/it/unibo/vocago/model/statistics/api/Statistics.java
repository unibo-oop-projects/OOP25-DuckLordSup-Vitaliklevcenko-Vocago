package it.unibo.vocago.model.statistics.api;

import java.time.LocalDate;

/**
 * A read only summary of a user's overall learning results, seen in the statistics dashboard.
 */
public interface Statistics {

    /**
     * @return the number of items the user has fully mastered
     */
    int getMasteredItems();

    /**
     * @return the total number of correct answers given by the user
     */
    int getTotalCorrectAnswers();

    /**
     * @return the total number of wrong answers given by the user
     */
    int getTotalWrongAnswers();

    /**
     * @return the total number of words in the user's vocabulary
     */
    int getTotalWords();

    /**
     * @return the overall accuracy, as a percentage between 0 and 100
     */
    double getAccuracyPercent();

    /**
     * @return the date of the user's last study session
     */
    LocalDate getLastStudyDate();

    /**
     * @return the current consecutive-day study streak
     */
    int getCurrentStreak();

    /**
     * @return the total study time, in milliseconds
     */
    long getTotalStudyTime();
}
