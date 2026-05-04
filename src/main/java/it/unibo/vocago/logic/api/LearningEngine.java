package it.unibo.vocago.logic.api;


import it.unibo.vocago.model.learning.api.Question;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;

public interface LearningEngine {

    boolean isCorrectAnswer(final Question question, final String userAnswer);

    Question getNextQuestion(final Direction direction, final Vocabulary vocabulary);

    void progressUpdate(final Question question, final String userAnswer);
}
