package it.unibo.vocago.model.vocabulary.api;

/**
 * A single word, as a piece of text used inside a vocabulary item.
 */
@FunctionalInterface
public interface Word {

    /**
     * @return the textual content of this word
     */
    String getWord();
}