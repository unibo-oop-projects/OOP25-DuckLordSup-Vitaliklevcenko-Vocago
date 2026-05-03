package it.unibo.vocago.model.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import it.unibo.vocago.model.vocabulary.Dictionary;

class ProfileTest {

    @Test
    void defaultConstructorTrimsNameAndCreatesEmptyVocabulary() {
        final Profile profile = new Profile("  Nick  ", "Italian", "English");

        assertEquals("Nick", profile.getUserName());
        assertEquals("Italian", profile.getFirstLanguage());
        assertEquals("English", profile.getSecondLanguage());
        assertTrue(profile.getVocabulary().isEmpty());
    }

    @Test
    void constructorStoresProvidedVocabularyAndLanguages() {
        final Dictionary dictionary = new Dictionary();
        final Profile profile = new Profile("Nick", dictionary, "Italian", "English");

        assertSame(dictionary, profile.getVocabulary());
        assertEquals("Italian", profile.getFirstLanguage());
        assertEquals("English", profile.getSecondLanguage());
    }

    @Test
    void constructorRejectsInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> new Profile(null, "Italian", "English"));
        assertThrows(IllegalArgumentException.class, () -> new Profile(" ", "Italian", "English"));
        assertThrows(NullPointerException.class, () -> new Profile("Nick", null, "Italian", "English"));
    }
}
