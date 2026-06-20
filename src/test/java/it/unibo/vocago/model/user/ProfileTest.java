package it.unibo.vocago.model.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import it.unibo.vocago.model.vocabulary.Dictionary;

class ProfileTest {

    private static final String ITALIAN = "Italian";
    private static final String ENGLISH = "English";
    private static final String USER_NAME = "Nick";

    @Test
    void defaultConstructorTrimsNameAndCreatesEmptyVocabulary() {
        final Profile profile = new Profile("  Nick  ", ITALIAN, ENGLISH);

        assertEquals(USER_NAME, profile.getUserName());
        assertEquals(ITALIAN, profile.getFirstLanguage());
        assertEquals(ENGLISH, profile.getSecondLanguage());
        assertTrue(profile.getVocabulary().isEmpty());
    }

    @Test
    void constructorStoresProvidedVocabularyAndLanguages() {
        final Dictionary dictionary = new Dictionary();
        final Profile profile = new Profile(USER_NAME, dictionary, ITALIAN, ENGLISH);

        assertSame(dictionary, profile.getVocabulary());
        assertEquals(ITALIAN, profile.getFirstLanguage());
        assertEquals(ENGLISH, profile.getSecondLanguage());
    }

    @Test
    void constructorRejectsInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> new Profile(null, ITALIAN, ENGLISH));
        assertThrows(IllegalArgumentException.class, () -> new Profile(" ", ITALIAN, ENGLISH));
        assertThrows(NullPointerException.class, () -> new Profile(USER_NAME, null, ITALIAN, ENGLISH));
    }
}
