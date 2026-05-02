package it.unibo.vocago.model.learning.api;

import java.util.List;

import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.vocabulary.api.VocabularyItem;
import it.unibo.vocago.model.vocabulary.api.Word;

public interface Question {

    VocabularyItem getItem();

    Direction getDirection();

    List<Word> getPromptWords();

    List<Word> getCorrectAnswer();

}
