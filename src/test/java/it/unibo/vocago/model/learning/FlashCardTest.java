package it.unibo.vocago.model.learning;

import static it.unibo.vocago.TestTools.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.vocabulary.DictionaryEntry;

class FlashCardTest {

    @Test
    void firstToSecondUsesFirstWordsAsPromptAndSecondWordsAsAnswer() {
        final DictionaryEntry item = entry("house", "casa");
        FlashCard flashCard = new FlashCard(item, Direction.FIRST_TO_SECOND);

        assertSame(item, flashCard.getItem());
        assertEquals(Direction.FIRST_TO_SECOND, flashCard.getDirection());
        assertEquals("house", (flashCard.getPromptWords().get(0)).getWord());
        assertEquals("casa", (flashCard.getCorrectAnswer().get(0)).getWord());

        flashCard = new FlashCard(item, Direction.SECOND_TO_FIRST);
        assertEquals("casa", (flashCard.getPromptWords().get(0)).getWord());
        assertEquals("house", (flashCard.getCorrectAnswer().get(0)).getWord());
    }


    @Test
    void constructorRejectsNullArguments() {
        assertThrows(NullPointerException.class, () -> new FlashCard(null, Direction.FIRST_TO_SECOND));
        assertThrows(NullPointerException.class, () -> new FlashCard(entry("house", "casa"), null));
    }


}
