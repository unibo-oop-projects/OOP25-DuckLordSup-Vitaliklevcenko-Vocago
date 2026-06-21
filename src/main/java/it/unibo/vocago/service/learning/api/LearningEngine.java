package it.unibo.vocago.service.learning.api;

import it.unibo.vocago.model.learning.api.Question;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;

/**
 * Encapsulates the learning logic, i.e. how the next question is selected and
 * how answers are checked. Acts as the strategy used by a learning session.
 */
public interface LearningEngine {

    /**
     * Checks the user's answer against the given question and the correct answer.
     *
     * @param question   the question being answered
     * @param userAnswer the answer typed by the user
     * @return {@code true} if the answer is accepted as correct
     */
    boolean checkAnswer(Question question, String userAnswer);

    /**
     * Selects the next question from the vocabulary for the given direction.
     *
     * @param direction  the translation direction
     * @param vocabulary the vocabulary to draw from
     * @return the next question to present
     */
    Question getNextQuestion(Direction direction, Vocabulary vocabulary);
}