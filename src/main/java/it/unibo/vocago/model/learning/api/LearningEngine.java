package it.unibo.vocago.model.learning.api;

import java.util.List;

import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.vocabulary.api.VocabularyItem;

public interface LearningEngine {

    boolean isCorrectAnswer(final Question question, final String userAnswer);

    Question randomQuestion();

    Question selectNextQuestion(final List<VocabularyItem> validItems, final Direction direction);

    Question getQuestion(final Direction direction);

    void progressUpdate(final Question question, final String userAnswer);
}
