package it.unibo.vocago.model.progress;

import java.util.Objects;

import it.unibo.vocago.model.progress.api.Progress;
import it.unibo.vocago.model.types.MasteryLevel;

/**
 * Default implementation of {@link Progress}. Keeps the correct and wrong
 * answer counters and updates the mastery level, requiring a number of
 * consecutive correct answers before promoting past the intermediate levels.
 */
public final class WordProgress implements Progress {

    private static final int MEDIUM_PROMOTION_THRESHOLD = 3;
    private static final int GOOD_PROMOTION_THRESHOLD = 5;

    private MasteryLevel masteryLevel;
    private int correctAnswers;
    private int wrongAnswers;

    /**
     * Creates a progress at the {@link MasteryLevel#NEW} level with no answers.
     */
    public WordProgress() {
        this(MasteryLevel.NEW, 0, 0);
    }

    /**
     * Creates a progress at the given mastery level with no answers.
     *
     * @param masteryLevel the initial mastery level; must not be {@code null}
     */
    public WordProgress(final MasteryLevel masteryLevel) {
        this(masteryLevel, 0, 0);
    }

    /**
     * Creates a progress with the given mastery level and answer counters.
     *
     * @param masteryLevel   the initial mastery level; must not be {@code null}
     * @param correctAnswers the initial number of correct answers; must not be negative
     * @param wrongAnswers   the initial number of wrong answers; must not be negative
     * @throws IllegalArgumentException if either counter is negative
     */
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
