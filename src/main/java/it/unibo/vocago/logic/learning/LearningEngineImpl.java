package it.unibo.vocago.logic.learning;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;

import it.unibo.vocago.logic.learning.api.LearningEngine;
import it.unibo.vocago.model.learning.FlashCard;
import it.unibo.vocago.model.learning.api.Question;
import it.unibo.vocago.model.progress.api.Progress;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.types.MasteryLevel;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;
import it.unibo.vocago.model.vocabulary.api.VocabularyItem;
import it.unibo.vocago.model.vocabulary.api.Word;

public class LearningEngineImpl implements LearningEngine{

    private final Queue<VocabularyItem> lastItems;
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

        String userAns = userAnswer.trim();
        for (Word answer : question.getCorrectAnswer()) {
            if (answer.getWord().trim().equalsIgnoreCase(userAns)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void progressUpdate(final Question question, final boolean correctAnswer) {
    final Progress progress = question.getItem().getProgress(question.getDirection());
    if (correctAnswer) {
        progress.registerCorrectAnswer();
    } else {
        progress.registerWrongAnswer();
    }
}

    @Override
    public Question getNextQuestion(final Direction direction, final Vocabulary vocabulary) {
        Objects.requireNonNull(direction, "direction must not be null");
        Objects.requireNonNull(vocabulary, "vocabulary must not be null");

        trimSeenItems(validCandidates(vocabulary));
        return selectNextQuestion(validCandidates(vocabulary), direction);
    }
    
    private void trimSeenItems(final List<VocabularyItem> vocabulary) {
        // already asked items can be removed after a while
        final int maxRemembered = Math.min(20, vocabulary.size() / 2);
        while (this.lastItems.size() > maxRemembered) {
            this.lastItems.poll();
        }
    }

    private List<VocabularyItem> validCandidates(final Vocabulary vocabulary) {
        final List<VocabularyItem> validCandidates = new ArrayList<>();
        for (VocabularyItem item : vocabulary.getItems()) {
            if (item.isValid() && !this.lastItems.contains(item)) {
                validCandidates.add(item);
            }
        }
        return validCandidates;
    }

    // can add selectNextQuestionByMastery
    private Question selectNextQuestion(final List<VocabularyItem> validItems, final Direction direction) {
        Objects.requireNonNull(direction, "direction must not be null");
        Objects.requireNonNull(validItems, "validItem must not be null");
        if (validItems.isEmpty()) {
            throw new IllegalStateException("No valid vocabulary items available");
        }

        Random random = new Random();
        final List<VocabularyItem> newItems = new ArrayList<>();
        for (VocabularyItem item : validItems) {
            if (item.getProgress(direction).getMasteryLevel() == MasteryLevel.NEW) {
                newItems.add(item);
            }
        }
        if (!newItems.isEmpty()) {
            VocabularyItem chosenItem = newItems.get(random.nextInt(newItems.size()));
            this.lastItems.add(chosenItem);
            return new FlashCard(chosenItem, direction);
        }

        record WeightedWord(VocabularyItem item, double weight) {
        }
        final List<WeightedWord> weightedWords = new ArrayList<>();

        WeightedWord smallestWeight = new WeightedWord(validItems.get(0), 1);

        for (VocabularyItem item : validItems) {
            int correctAnswers = item.getProgress(direction).getCorrectAnswers();
            int wrongAnswers = item.getProgress(direction).getWrongAnswers();

            double baseMultiplier = 0;
            switch (item.getProgress(direction).getMasteryLevel()) {
                case MasteryLevel.BAD:
                    baseMultiplier = 0.8;
                    break;
                case MasteryLevel.MEDIUM:
                    baseMultiplier = 0.9;
                    break;
                case MasteryLevel.GOOD:
                    baseMultiplier = 1.05;
                    break;
                case MasteryLevel.MASTER:
                    baseMultiplier = 1.35;
                    break;
                default:
                    baseMultiplier = 0;
                    break;
            }
            double weightResult = correctAnswers * baseMultiplier / (correctAnswers + wrongAnswers + 1);
            if (weightResult < smallestWeight.weight()) {
                smallestWeight = new WeightedWord(item, weightResult);
            }
            if (weightResult > 1) {
                weightResult = 0.99;
            }
            weightedWords.add(new WeightedWord(item, weightResult));
        }

        double cutoff = random.nextDouble();
        final List<VocabularyItem> candidates = new ArrayList<>();
        for (WeightedWord word : weightedWords) {
            if (word.weight() <= cutoff) {
                candidates.add(word.item());
            }
        }

        if (candidates.isEmpty()) {
            this.lastItems.add(smallestWeight.item());
            return new FlashCard(smallestWeight.item(), direction);
        }

        final VocabularyItem chosenItem = candidates.get(random.nextInt(candidates.size()));
        this.lastItems.add(chosenItem);
        return new FlashCard(chosenItem, direction);
    }
}
