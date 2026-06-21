package it.unibo.vocago.model.learning.api;

import java.util.List;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.vocabulary.api.VocabularyItem;
import it.unibo.vocago.model.vocabulary.api.Word;

/**
 * A single translation question presented during a learning session, has a
 * specific vocabulary item and translation direction.
 */
public interface Question {

    /**
     * @return the vocabulary item this question is based on
     */
    VocabularyItem getItem();

    /**
     * @return the translation direction of this question
     */
    Direction getDirection();

    /**
     * @return the words shown to the user as the prompt
     */
    List<Word> getQuestion();

    /**
     * @return the words accepted as a correct answer
     */
    List<Word> getCorrectAnswer();

}