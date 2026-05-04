package it.unibo.vocago.model.learning;

import static it.unibo.vocago.TestTools.entry;
import static it.unibo.vocago.TestTools.word;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import it.unibo.vocago.logic.LearningEngineImpl;
import it.unibo.vocago.model.learning.api.Question;
import it.unibo.vocago.model.progress.WordProgress;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.types.MasteryLevel;
import it.unibo.vocago.model.vocabulary.Dictionary;
import it.unibo.vocago.model.vocabulary.DictionaryEntry;

class LearningEngineImplTest {



    @Test
    void isCorrectAnswerMatchesAnyTrimmedCaseInsensitiveAnswer() {
        final DictionaryEntry item = new DictionaryEntry(
                List.of(word("house")),
                List.of(word("casa"), word("abitazione")));
        final Question question = new FlashCard(item, Direction.FIRST_TO_SECOND);
        final LearningEngineImpl engine = new LearningEngineImpl();

        assertTrue(engine.checkAnswer(question, " CASA "));
        assertTrue(engine.checkAnswer(question, "abitazione"));
        assertFalse(engine.checkAnswer(question, "house"));
        assertFalse(engine.checkAnswer(question, ""));
        assertFalse(engine.checkAnswer(question, null));
        assertThrows(NullPointerException.class, () -> engine.checkAnswer(null, "casa"));
    }

    @Test
    void progressUpdateChangesOnlyTheCorrectDirectionProgress() {
        final DictionaryEntry item = entry("house", "casa");
        final LearningEngineImpl engine = new LearningEngineImpl();

        engine.progressUpdate(new FlashCard(item, Direction.FIRST_TO_SECOND), "casa");
        
        assertEquals(1, item.getProgress(Direction.FIRST_TO_SECOND).getCorrectAnswers());
        assertEquals(0, item.getProgress(Direction.SECOND_TO_FIRST).getCorrectAnswers());

        assertEquals(MasteryLevel.MEDIUM, item.getProgress(Direction.FIRST_TO_SECOND).getMasteryLevel());
        assertEquals(MasteryLevel.NEW, item.getProgress(Direction.SECOND_TO_FIRST).getMasteryLevel());

        assertEquals(0, item.getProgress(Direction.FIRST_TO_SECOND).getWrongAnswers());
        

        engine.progressUpdate(new FlashCard(item, Direction.FIRST_TO_SECOND), "wrong");
        assertEquals(1, item.getProgress(Direction.FIRST_TO_SECOND).getWrongAnswers());
        assertEquals(MasteryLevel.BAD, item.getProgress(Direction.FIRST_TO_SECOND).getMasteryLevel());
    }

    @Test
    void getNextQuestionPrioritizesNewItems() {
        final DictionaryEntry learnedItem = new DictionaryEntry(
                List.of(word("dog")),
                List.of(word("cane")),
                new WordProgress(MasteryLevel.GOOD, 5, 0),
                new WordProgress(MasteryLevel.GOOD, 5, 0));

        final DictionaryEntry newItem = entry("cat", "gatto");

        final Dictionary dictionary = new Dictionary(List.of(learnedItem, newItem));
        final LearningEngineImpl engine = new LearningEngineImpl();

        final Question question = engine.getNextQuestion(Direction.FIRST_TO_SECOND, dictionary);

        assertSame(newItem, question.getItem());
    }

    @Test
    void nextQuestionSkipsVocabularyItemsMissingOneLanguage() {
        final DictionaryEntry onlyFirstLanguage = entry("","house");
        final DictionaryEntry onlySecondLanguage = entry("casa", "");
        final DictionaryEntry completeItem = entry("cat", "gatto");
        final Dictionary dictionary = new Dictionary(List.of(onlyFirstLanguage, onlySecondLanguage, completeItem));

        final LearningEngineImpl engine = new LearningEngineImpl();
        assertSame(completeItem, engine.getNextQuestion(Direction.FIRST_TO_SECOND, dictionary).getItem());
    }
}
