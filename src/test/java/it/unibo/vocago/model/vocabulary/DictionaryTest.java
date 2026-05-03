package it.unibo.vocago.model.vocabulary;

import static it.unibo.vocago.TestTools.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import it.unibo.vocago.model.vocabulary.api.VocabularyItem;
import static it.unibo.vocago.TestTools.word;

class DictionaryTest {


    @Test
    void rejectsNullItems() {
        final List<VocabularyItem> itemsWithNull = new ArrayList<>();
        itemsWithNull.add(null);
        assertThrows(NullPointerException.class, () -> new Dictionary(null));
        assertThrows(IllegalArgumentException.class, () -> new Dictionary(itemsWithNull));
    }
    
    @Test
    void vocabularyIsValidRequiresAtLeastOneCompleteItem() {
        final Dictionary dictionary = new Dictionary();

        assertFalse(dictionary.isValid());

        dictionary.addItem(new DictionaryEntry(List.of(word("house")), List.of()));
        assertFalse(dictionary.isValid());

        dictionary.addItem(entry("cat", "gatto"));
        assertTrue(dictionary.isValid());
    }
}
