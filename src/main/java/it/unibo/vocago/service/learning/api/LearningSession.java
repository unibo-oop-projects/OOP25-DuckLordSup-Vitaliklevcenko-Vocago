package it.unibo.vocago.service.learning.api;

import it.unibo.vocago.model.types.Direction;

/**
 * Holds the temporary state of a single learning session, it has the needed information such as the current
 * question, the translation direction, the elapsed time and the number of correctly answered questions.
 */
public interface LearningSession {

    /**
     * @return the prompt of the next question
     */
    String getNextQuestion();

    /**
     * Evaluates the given answer against the current question.
     *
     * @param answer the user's answer
     * @return {@code true} if the answer is correct
     */
    boolean evaluateAnswer(String answer);

    /**
     * @return the correct answer to the current question
     */
    String getCorrectAnswer();

    /**
     * @return {@code true} if the current question has already been evaluated
     */
    boolean currentQuestionEvaluated();

    /** Switches the translation direction of the session. */
    void switchDirection();

    /**
     * @return the number of correctly answered questions so far
     */
    int getCorrectAnsweredQuestions();

    /**
     * @return the session start time, in milliseconds
     */
    long getStartTime();

    /**
     * @return the current translation direction
     */
    Direction getDirection();
}