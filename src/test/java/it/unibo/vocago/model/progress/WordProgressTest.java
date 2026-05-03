package it.unibo.vocago.model.progress;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import it.unibo.vocago.model.types.MasteryLevel;

class WordProgressTest {

    @Test
    void constructorRejectsInvalidState() {
        assertThrows(IllegalArgumentException.class, () -> new WordProgress(null));
        assertThrows(IllegalArgumentException.class, () -> new WordProgress(MasteryLevel.NEW, -1, 0));
        assertThrows(IllegalArgumentException.class, () -> new WordProgress(MasteryLevel.NEW, 0, -1));
    }

    @Test
    void correctAnswersIncrementAndPromoteAtThresholds() {
        final WordProgress progress = new WordProgress();

        progress.registerCorrectAnswer();
        assertEquals(1, progress.getCorrectAnswers());
        assertEquals(MasteryLevel.MEDIUM, progress.getMasteryLevel());

        progress.registerCorrectAnswer();
        assertEquals(MasteryLevel.MEDIUM, progress.getMasteryLevel());

        progress.registerCorrectAnswer();
        assertEquals(MasteryLevel.GOOD, progress.getMasteryLevel());

        progress.registerCorrectAnswer();
        assertEquals(MasteryLevel.GOOD, progress.getMasteryLevel());

        progress.registerCorrectAnswer();
        assertEquals(5, progress.getCorrectAnswers());
        assertEquals(MasteryLevel.MASTER, progress.getMasteryLevel());
    }

    @Test
    void wrongAnswersIncrementAndDemoteLevels() {
        final WordProgress progress = new WordProgress(MasteryLevel.MASTER);

        progress.registerWrongAnswer();
        assertEquals(1, progress.getWrongAnswers());
        assertEquals(MasteryLevel.MEDIUM, progress.getMasteryLevel());

        progress.registerWrongAnswer();
        assertEquals(MasteryLevel.BAD, progress.getMasteryLevel());

        progress.registerWrongAnswer();
        assertEquals(3, progress.getWrongAnswers());
        assertEquals(MasteryLevel.BAD, progress.getMasteryLevel());
    }
}
