package it.unibo.vocago.model.vocabulary;

import java.util.Objects;
import it.unibo.vocago.model.vocabulary.api.Word;

/**
 * Default implementation of {@link Word}, wrapping a single trimmed string.
 */
public final class WordEntry implements Word {
    private final String word;

    /**
     * Creates a word from the given text, trimming surrounding whitespace.
     *
     * @param word the textual content of the word; must not be {@code null}
     */
    public WordEntry(final String word) {
        Objects.requireNonNull(word, "word must not be null");
        this.word = word.trim();
    }

    @Override
    public String getWord() {
        return this.word;
    }
}
