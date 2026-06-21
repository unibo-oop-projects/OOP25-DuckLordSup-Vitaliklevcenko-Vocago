package it.unibo.vocago.model.vocabulary.api;

import java.util.List;

import it.unibo.vocago.model.progress.api.Progress;
import it.unibo.vocago.model.types.Direction;

/**
 * A single vocabulary entry, holding one or more words in the first language
 * and their corresponding words in the second language, together with the
 * learning progress for each translation direction.
 */
public interface VocabularyItem {

    /**
     * @return the words of this item in the first language
     */
    List<Word> getFirstLanguageWords();

    /**
     * @return the words of this item in the second language
     */
    List<Word> getSecondLanguageWords();

    /**
     * Returns the learning progress of this item for the given direction.
     *
     * @param direction the translation direction
     * @return the progress associated with that direction
     */
    Progress getProgress(Direction direction);

    /**
     * @return {@code true} if this item is complete, i.e. it has at least one
     *         word in each language
     */
    Boolean isValid();
}
