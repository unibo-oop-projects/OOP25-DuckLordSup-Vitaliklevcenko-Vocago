package it.unibo.vocago.model.vocabulary;

import it.unibo.vocago.model.vocabulary.api.Word;

public class WordEntry implements Word{
    private final String word;

    public WordEntry(final String word) {
        if (word == null) {
            throw new IllegalArgumentException("word must not be null");
        }
        this.word = word.trim();
    }

    @Override
    public String getWord() {
        return this.word;
    }
}
