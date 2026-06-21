package it.unibo.vocago.model.progress.api;

import it.unibo.vocago.model.types.MasteryLevel;

/**
 * Tracks the learning progress of a vocabulary item in a single translation
 * direction, including the mastery level and the number of correct and wrong
 * answers given by the user.
 */
public interface Progress {

    /**
     * @return the current mastery level
     */
    MasteryLevel getMasteryLevel();

    /**
     * @return the number of correct answers recorded so far
     */
    int getCorrectAnswers();

    /**
     * @return the number of wrong answers recorded so far
     */
    int getWrongAnswers();

    /**
     * Records a correct answer, updating the counters and mastery level.
     */
    void registerCorrectAnswer();

    /**
     * Records a wrong answer, updating the counters and mastery level.
     */
    void registerWrongAnswer();
}