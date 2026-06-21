package it.unibo.vocago.model.progress;

import java.util.Objects;

import it.unibo.vocago.model.progress.api.Progress;
import it.unibo.vocago.model.types.MasteryLevel;

public class WordProgress implements Progress {

    private static final int MEDIUM_PROMOTION_THRESHOLD = 3;
    private static final int GOOD_PROMOTION_THRESHOLD = 5;

    private MasteryLevel masteryLevel;
    private int correctAnswers;
    private int wrongAnswers;

    public WordProgress() {
        this(MasteryLevel.NEW, 0, 0);
    }

    public WordProgress(final MasteryLevel masteryLevel) {
        this(masteryLevel, 0, 0);
    }

    public WordProgress(final MasteryLevel masteryLevel, final int correctAnswers, final int wrongAnswers) {
        if (correctAnswers < 0 || wrongAnswers < 0) {
            throw new IllegalArgumentException("Answer counters must not be negative");
        }
        this.masteryLevel = Objects.requireNonNull(masteryLevel, "masteryLevel must not be null");
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
        if (this.masteryLevel == MasteryLevel.MEDIUM && correctAnswers < MEDIUM_PROMOTION_THRESHOLD
                || this.masteryLevel == MasteryLevel.GOOD && correctAnswers < GOOD_PROMOTION_THRESHOLD) {
            return;
        }
        this.masteryLevel = this.masteryLevel.next();

    }

    @Override
    public void registerWrongAnswer() {
        this.wrongAnswers++;
        this.masteryLevel = this.masteryLevel.previous();
    }
}
