package it.unibo.vocago.model.types;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MasteryLevelTest {

    @Test
    void nextMovesForwardAndStopsAtMaster() {
        assertEquals(MasteryLevel.BAD, MasteryLevel.NEW.next());
        assertEquals(MasteryLevel.MASTER, MasteryLevel.GOOD.next());
        assertEquals(MasteryLevel.MASTER, MasteryLevel.MASTER.next());
    }

    @Test
    void previousMovesBackwardAndStopsAtBad() {
        assertEquals(MasteryLevel.BAD, MasteryLevel.NEW.previous());
        assertEquals(MasteryLevel.BAD, MasteryLevel.BAD.previous());
        assertEquals(MasteryLevel.MEDIUM, MasteryLevel.GOOD.previous());
        assertEquals(MasteryLevel.MEDIUM, MasteryLevel.MASTER.previous());
    }
}
