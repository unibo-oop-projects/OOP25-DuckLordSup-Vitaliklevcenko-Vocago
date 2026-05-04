package it.unibo.vocago.logic.api;

import it.unibo.vocago.model.types.Direction;

public interface LearningSession {

    String nextQuestionPrompt();

    boolean checkAnswer(String answer);

    String showAnswer();

    void switchDirection();

    int getAnsweredQuestions();

    long getElapsedSeconds();

    Direction getDirection();
}
