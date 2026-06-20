package it.unibo.vocago.model.vocabulary;

import java.util.Objects;
import it.unibo.vocago.model.vocabulary.api.Word;

public class WordEntry implements Word{
    private final String word;

    public WordEntry(final String word) {
        Objects.requireNonNull(word, "word must not be null");
        this.word = word.trim();
    }

    @Override
    public String getWord() {
        return this.word;
    }
}
