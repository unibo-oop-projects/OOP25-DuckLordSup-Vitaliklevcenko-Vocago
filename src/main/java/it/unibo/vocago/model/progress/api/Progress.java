package it.unibo.vocago.model.progress.api;

import it.unibo.vocago.model.types.MasteryLevel;

public interface Progress {
    MasteryLevel getMasteryLevel();
    int getCorrectAnswers();
    int getWrongAnswers();
    void registerCorrectAnswer();
    void registerWrongAnswer();
}
