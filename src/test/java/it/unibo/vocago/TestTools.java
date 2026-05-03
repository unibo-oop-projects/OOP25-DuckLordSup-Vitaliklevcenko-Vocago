package it.unibo.vocago;

import java.util.List;

import it.unibo.vocago.model.vocabulary.DictionaryEntry;
import it.unibo.vocago.model.vocabulary.WordEntry;
import it.unibo.vocago.model.vocabulary.api.Word;

public final class TestTools {

    private TestTools() {
    }

    public static Word word(final String value) {
        return new WordEntry(value);
    }

    public static DictionaryEntry entry(final String firstLanguageWord, final String secondLanguageWord) {
        return new DictionaryEntry(List.of(word(firstLanguageWord)), List.of(word(secondLanguageWord)));
    }
}
