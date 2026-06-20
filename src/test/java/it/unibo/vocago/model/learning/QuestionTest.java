package it.unibo.vocago.model.learning;

import static it.unibo.vocago.TestTools.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.vocabulary.DictionaryEntry;

class QuestionTest {

    @Test
    void firstToSecondUsesFirstWordsAsPromptAndSecondWordsAsAnswer() {
        final DictionaryEntry item = entry("house", "casa");
        QuestionImpl Question = new QuestionImpl(item, Direction.FIRST_TO_SECOND);

        assertSame(item, Question.getItem());
        assertEquals(Direction.FIRST_TO_SECOND, Question.getDirection());
        assertEquals("house", (Question.getQuestion().get(0)).getWord());
        assertEquals("casa", (Question.getCorrectAnswer().get(0)).getWord());

        Question = new QuestionImpl(item, Direction.SECOND_TO_FIRST);
        assertEquals("casa", (Question.getQuestion().get(0)).getWord());
        assertEquals("house", (Question.getCorrectAnswer().get(0)).getWord());
    }


    @Test
    void constructorRejectsNullArguments() {
        assertThrows(NullPointerException.class, () -> new QuestionImpl(null, Direction.FIRST_TO_SECOND));
        assertThrows(NullPointerException.class, () -> new QuestionImpl(entry("house", "casa"), null));
    }


}
