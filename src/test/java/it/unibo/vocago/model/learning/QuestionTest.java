package it.unibo.vocago.model.learning;

import static it.unibo.vocago.TestTools.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.vocabulary.DictionaryEntry;

class QuestionTest {

    private static final String HOUSE = "house";
    private static final String CASA = "casa";

    @Test
    void firstToSecondUsesFirstWordsAsPromptAndSecondWordsAsAnswer() {
        final DictionaryEntry item = entry(HOUSE, CASA);
        final QuestionImpl firstToSecondQuestion = new QuestionImpl(item, Direction.FIRST_TO_SECOND);

        assertSame(item, firstToSecondQuestion.getItem());
        assertEquals(Direction.FIRST_TO_SECOND, firstToSecondQuestion.getDirection());
        assertEquals(HOUSE, firstToSecondQuestion.getQuestion().get(0).getWord());
        assertEquals(CASA, firstToSecondQuestion.getCorrectAnswer().get(0).getWord());

        final QuestionImpl secondToFirstQuestion = new QuestionImpl(item, Direction.SECOND_TO_FIRST);
        assertEquals(CASA, secondToFirstQuestion.getQuestion().get(0).getWord());
        assertEquals(HOUSE, secondToFirstQuestion.getCorrectAnswer().get(0).getWord());
    }

    @Test
    void constructorRejectsNullArguments() {
        assertThrows(NullPointerException.class, () -> new QuestionImpl(null, Direction.FIRST_TO_SECOND));
        assertThrows(NullPointerException.class, () -> new QuestionImpl(entry(HOUSE, CASA), null));
    }

}
