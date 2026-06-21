package it.unibo.vocago.service.learning;

import java.util.List;
import java.util.Objects;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.vocago.model.learning.api.Question;
import it.unibo.vocago.model.progress.api.Progress;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;
import it.unibo.vocago.model.vocabulary.api.Word;
import it.unibo.vocago.service.learning.api.LearningEngine;
import it.unibo.vocago.service.learning.api.LearningSession;

public class LearningSessionImpl implements LearningSession {

    private final Vocabulary vocabulary;
    private final LearningEngine learningEngine;
    private final long time;
    private Direction direction;
    private Question question;
    private boolean currentQuestionEvaluated;
    private int correctAnsweredQuestions;

    public LearningSessionImpl(final Vocabulary vocabulary) {
        this(vocabulary, new LearningEngineImpl());
    }

    @SuppressFBWarnings(value = "EI2", justification = "The session intentionally shares the profile vocabulary.")
    public LearningSessionImpl(final Vocabulary vocabulary, final LearningEngine learningEngine) {
        this.vocabulary = vocabulary;
        this.learningEngine = Objects.requireNonNull(learningEngine);
        this.time = System.currentTimeMillis();
        this.direction = Direction.FIRST_TO_SECOND;
        this.currentQuestionEvaluated = false;
        this.correctAnsweredQuestions = 0;
    }

    @Override
    public String getNextQuestion() {
        this.question = this.learningEngine.getNextQuestion(direction, vocabulary);
        this.currentQuestionEvaluated = false;
        return toString(this.question.getQuestion());
    }

    @Override
    public boolean evaluateAnswer(final String answer) {
        final boolean correctAnswer = this.learningEngine.checkAnswer(this.question, answer);
        if (!this.currentQuestionEvaluated) {
            updateProgress(correctAnswer);
            this.currentQuestionEvaluated = true;
            if (correctAnswer) {
                this.correctAnsweredQuestions++;
            }
        }
        return correctAnswer;
    }

    @Override
    public String getCorrectAnswer() {
        return toString(this.question.getCorrectAnswer());
    }

    @Override
    public void switchDirection() {
        this.direction = this.direction.opposite();
    }

    @Override
    public int getCorrectAnsweredQuestions() {
        return this.correctAnsweredQuestions;
    }

    @Override
    public long getStartTime() {
        return this.time;
    }

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public boolean currentQuestionEvaluated() {
        return this.currentQuestionEvaluated;
    }

    private void updateProgress(final boolean correct) {
        final Progress progress = this.question
                .getItem()
                .getProgress(this.question.getDirection());

        if (correct) {
            progress.registerCorrectAnswer();
        } else {
            progress.registerWrongAnswer();
        }
    }

    private String toString(final List<Word> words) {
    final StringBuilder text = new StringBuilder();

        for (final Word word : words) {
            if (text.length() > 0) {
                text.append(", ");
            }
            text.append(word.getWord());
        }

        return text.toString();
    }

}
