package it.unibo.vocago.service.learning.api;

import it.unibo.vocago.model.learning.api.Question;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;

public interface LearningEngine {

    boolean checkAnswer(Question question, String userAnswer);

    Question getNextQuestion(Direction direction, Vocabulary vocabulary);
}
