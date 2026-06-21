package it.unibo.vocago.service.learning;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;

import it.unibo.vocago.model.learning.QuestionImpl;
import it.unibo.vocago.model.learning.api.Question;
import it.unibo.vocago.model.progress.api.Progress;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.types.MasteryLevel;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;
import it.unibo.vocago.model.vocabulary.api.VocabularyItem;
import it.unibo.vocago.model.vocabulary.api.Word;
import it.unibo.vocago.service.learning.api.LearningEngine;

/**
 * Default implementation of {@link LearningEngine}.
 */
public final class LearningEngineImpl implements LearningEngine {

    private static final int MAX_RECENT_ITEMS = 20;
    private static final double MAX_SELECTION_WEIGHT = 0.99;

    private final Queue<VocabularyItem> lastItems;
    private final Random random = new Random();

    /**
     * Creates a learning engine.
     */
    public LearningEngineImpl() {
        this.lastItems = new ArrayDeque<>();
    }

    @Override
    public boolean checkAnswer(final Question question, final String userAnswer) {
        Objects.requireNonNull(question, "question must not be null");

        // check that the user enter a valid answer
        if (userAnswer == null || userAnswer.isBlank()) {
            return false;
        }

        final String userAns = userAnswer.trim();
        for (final Word answer : question.getCorrectAnswer()) {
            if (answer.getWord().trim().equalsIgnoreCase(userAns)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Question getNextQuestion(final Direction direction, final Vocabulary vocabulary) {
        Objects.requireNonNull(direction, "direction must not be null");
        Objects.requireNonNull(vocabulary, "vocabulary must not be null");

        trimSeenItems(vocabulary);
        return selectNextQuestion(validCandidates(vocabulary), direction);
    }

    private void trimSeenItems(final Vocabulary vocabulary) {
        // already asked items can be removed after a while
        final int validItemCount = (int) vocabulary.getItems().stream()
                .filter(VocabularyItem::isValid)
                .count();

        final int maxRemembered = Math.min(MAX_RECENT_ITEMS, validItemCount / 2);

        while (this.lastItems.size() > maxRemembered) {
            this.lastItems.remove();
        }
    }

    private List<VocabularyItem> validCandidates(final Vocabulary vocabulary) {
        return vocabulary.getItems().stream()
                .filter(VocabularyItem::isValid)
                .filter(item -> !this.lastItems.contains(item))
                .toList();
    }

    // can add selectNextQuestionByMastery
    // based on the sm2 algorithm
    private Question selectNextQuestion(final List<VocabularyItem> candidates, final Direction direction) {
        Objects.requireNonNull(direction, "direction must not be null");
        Objects.requireNonNull(candidates, "candidates must not be null");

        if (candidates.isEmpty()) {
            throw new IllegalStateException("No valid vocabulary items available");
        }

        final List<VocabularyItem> newItems = new ArrayList<>();
        for (final VocabularyItem item : candidates) {
            if (item.getProgress(direction).getMasteryLevel() == MasteryLevel.NEW) {
                newItems.add(item);
            }
        }

        if (!newItems.isEmpty()) {
            return createQuestion(newItems.get(this.random.nextInt(newItems.size())), direction);
        }

        double lowestWeight = 1;
        VocabularyItem lowestWeightedItem = candidates.get(0);
        final double cutoff = this.random.nextDouble();
        final List<VocabularyItem> filteredCandidates = new ArrayList<>();

        for (final VocabularyItem item : candidates) {
            final Progress progress = item.getProgress(direction);
            final int correctAnswers = progress.getCorrectAnswers();
            final int wrongAnswers = progress.getWrongAnswers();

            final double weight = Math.min(MAX_SELECTION_WEIGHT,
                    correctAnswers * progress.getMasteryLevel().getMultiplier()
                    / (correctAnswers + wrongAnswers + 1));

            if (weight < lowestWeight) {
                lowestWeight = weight;
                lowestWeightedItem = item;
            }

            if (weight <= cutoff) {
                filteredCandidates.add(item);
            }
        }

        if (filteredCandidates.isEmpty()) {
            return createQuestion(lowestWeightedItem, direction);
        }

        return createQuestion(filteredCandidates.get(this.random.nextInt(filteredCandidates.size())), direction);
    }

    private Question createQuestion(final VocabularyItem item, final Direction direction) {
        this.lastItems.add(item);
        return new QuestionImpl(item, direction);
    }
}
