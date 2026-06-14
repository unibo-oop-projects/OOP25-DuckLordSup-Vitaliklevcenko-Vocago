package it.unibo.vocago.logic.learning.api;

import it.unibo.vocago.model.types.Direction;

public interface LearningSession {

    String getNextQuestion();

    boolean evaluateAnswer(String answer);

    String getCorrectAnswer();

    boolean currentQuestionEvaluated();

    void switchDirection();

    int getCorrectAnsweredQuestions();

    long getTime();

    Direction getDirection();
}
