package it.unibo.vocago.logic.api;

import it.unibo.vocago.model.types.Direction;

public interface LearningSession {

    String getNextQuestion();

    boolean checkAnswer(String answer);

    String showAnswer();

    void switchDirection();

    int getCountAnsweredQuestions();

    long getElapsedSeconds();

    Direction getDirection();
}
