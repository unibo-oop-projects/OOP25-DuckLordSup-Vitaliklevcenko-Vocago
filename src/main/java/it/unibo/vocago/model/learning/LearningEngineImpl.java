package it.unibo.vocago.model.learning;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;

import it.unibo.vocago.model.learning.api.LearningEngine;
import it.unibo.vocago.model.learning.api.Question;
import it.unibo.vocago.model.progress.api.Progress;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.types.MasteryLevel;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;
import it.unibo.vocago.model.vocabulary.api.VocabularyItem;
import it.unibo.vocago.model.vocabulary.api.Word;

public class LearningEngineImpl implements LearningEngine{

    private final Vocabulary vocabulary;
    private final Random random;
    private final Queue<VocabularyItem> lastItems;

    public LearningEngineImpl(final Vocabulary vocabulary) {
        this.vocabulary = Objects.requireNonNull(vocabulary, "vocabulary must not be null");
        this.random = new Random();
        this.lastItems = new ArrayDeque<>();
    }

    @Override
    public boolean isCorrectAnswer(final Question question, final String userAnswer) {
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
    public Question randomQuestion() {
        final Direction randomDirection;
        if (this.random.nextBoolean()) {
            randomDirection = Direction.FIRST_TO_SECOND;
        } else {
            randomDirection = Direction.SECOND_TO_FIRST;
        }

        return this.nextQuestion(randomDirection);
    }

    //can add selectNextQuestionByMastery
    @Override
    public Question selectNextQuestion(final List<VocabularyItem> validItems, final Direction direction) {

        Objects.requireNonNull(direction, "direction must not be null");
        Objects.requireNonNull(validItems, "validItem must not be null");

        final List<VocabularyItem> newItems = new ArrayList<>();
        for (VocabularyItem item : validItems) {
            if (item.getProgress(direction).getMasteryLevel() == MasteryLevel.NEW) {
                newItems.add(item);
            }
        }
        if (!newItems.isEmpty()) {
            VocabularyItem chosenItem = newItems.get(this.random.nextInt(newItems.size()));
            this.lastItems.add(chosenItem);
            return new FlashCard(chosenItem, direction);
        }

        record WeightedWord(VocabularyItem item, double weight) {}
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
        double cutoff = this.random.nextDouble();
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
        final VocabularyItem chosenItem = candidates.get(this.random.nextInt(candidates.size()));
        this.lastItems.add(chosenItem);
        return new FlashCard(chosenItem, direction);
    }

    @Override
    public Question nextQuestion(final Direction direction) {
        Objects.requireNonNull(direction, "direction must not be null");

        // already asked items can be removed after a while
        int maxRemembered = Math.min(20, this.vocabulary.getItems().size() / 2);
        if (this.lastItems.size() > maxRemembered) {
            this.lastItems.poll();
        }
        
        final List<VocabularyItem> validCandidates = new ArrayList<>();
        for (VocabularyItem item : this.vocabulary.getItems()) {
            if (!item.getFirstLanguageWords().isEmpty() && !item.getSecondLanguageWords().isEmpty()
                    && !this.lastItems.contains(item)) {
                validCandidates.add(item);
            }
        }
        return selectNextQuestion(validCandidates, direction);
    }

    

    @Override
    public void progressUpdate(final Question question, final String userAnswer) {
        Objects.requireNonNull(question, "question must not be null");
        final Progress progress = question.getItem().getProgress(question.getDirection());
        if (isCorrectAnswer(question, userAnswer)) {
            progress.registerCorrectAnswer();
        } else {
            progress.registerWrongAnswer();
        }
    }


}
