package it.unibo.vocago.logic.api;

import it.unibo.vocago.model.types.Direction;

public interface LearningSession {

    String getNextQuestion();

    boolean evaluateAnswer(String answer);

    String getCorrectAnswer();

    void switchDirection();

    int getCountAnsweredQuestions();

    long getTime();

    Direction getDirection();
}
