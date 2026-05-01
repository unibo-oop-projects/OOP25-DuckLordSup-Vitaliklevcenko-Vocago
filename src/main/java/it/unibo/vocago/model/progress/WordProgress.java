package it.unibo.vocago.model.progress;

import it.unibo.vocago.model.progress.api.Progress;
import it.unibo.vocago.model.types.MasteryLevel;

public class WordProgress implements Progress{

    private MasteryLevel masteryLevel;
    private int correctAnswers;
    private int wrongAnswers;

    public WordProgress() {
        this(MasteryLevel.NEW, 0, 0);
    }

    public WordProgress(MasteryLevel masteryLevel) {
        this(masteryLevel, 0, 0);
    }

    public WordProgress(final MasteryLevel masteryLevel, final int correctAnswers, final int wrongAnswers) {
        if (masteryLevel == null) {
            throw new IllegalArgumentException("masteryLevel must not be null");
        }
        if (correctAnswers < 0 || wrongAnswers < 0) {
            throw new IllegalArgumentException("Answer counters must not be negative");
        }

        this.masteryLevel = masteryLevel;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
    }

    @Override
    public MasteryLevel getMasteryLevel() {
        return this.masteryLevel;
    }

    @Override
    public int getCorrectAnswers() {
        return this.correctAnswers;
    }

    @Override
    public int getWrongAnswers() {
        return this.wrongAnswers;
    }

    @Override
    public void registerCorrectAnswer() {
        this.correctAnswers++;
        if (this.masteryLevel == MasteryLevel.NEW) {
            this.masteryLevel = MasteryLevel.MEDIUM;
            return;
        }
        if ((this.masteryLevel == MasteryLevel.MEDIUM && correctAnswers < 3)
                || (this.masteryLevel == MasteryLevel.GOOD && correctAnswers < 5)) {
            return;
        }
        if (this.masteryLevel != MasteryLevel.MASTER) {
            this.masteryLevel = this.masteryLevel.next();
        }
    }

    @Override
    public void registerWrongAnswer() {
        this.wrongAnswers++;

        if (this.masteryLevel == MasteryLevel.MASTER) {
            this.masteryLevel = MasteryLevel.MEDIUM;
            return;
        }
        if (this.masteryLevel != MasteryLevel.BAD) {

            this.masteryLevel = this.masteryLevel.previous();
        }
    }
}