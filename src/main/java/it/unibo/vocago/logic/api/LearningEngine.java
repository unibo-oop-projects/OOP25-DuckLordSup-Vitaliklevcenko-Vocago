package it.unibo.vocago.logic.api;

import java.util.List;

import it.unibo.vocago.model.learning.api.Question;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;
import it.unibo.vocago.model.vocabulary.api.VocabularyItem;

public interface LearningEngine {

    boolean isCorrectAnswer(final Question question, final String userAnswer);

    Question selectNextQuestion(final List<VocabularyItem> validItems, final Direction direction);

    Question getQuestion(final Direction direction, final Vocabulary vocabulary);

    void progressUpdate(final Question question, final String userAnswer);
}
