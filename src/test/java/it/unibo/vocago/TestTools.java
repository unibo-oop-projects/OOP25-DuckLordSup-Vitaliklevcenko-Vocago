package it.unibo.vocago;

import java.util.List;

import it.unibo.vocago.model.vocabulary.DictionaryEntry;
import it.unibo.vocago.model.vocabulary.WordEntry;
import it.unibo.vocago.model.vocabulary.api.Word;

/**
 * Factory methods for objects shared by the test suite.
 */
public final class TestTools {

    private TestTools() {
    }

    /**
     * Creates a word containing the supplied value.
     *
     * @param value the word value
     * @return the created word
     */
    public static Word word(final String value) {
        return new WordEntry(value);
    }

    /**
     * Creates a dictionary entry containing one word for each language.
     *
     * @param firstLanguageWord the word in the first language
     * @param secondLanguageWord the word in the second language
     * @return the created dictionary entry
     */
    public static DictionaryEntry entry(final String firstLanguageWord, final String secondLanguageWord) {
        return new DictionaryEntry(List.of(word(firstLanguageWord)), List.of(word(secondLanguageWord)));
    }
}
