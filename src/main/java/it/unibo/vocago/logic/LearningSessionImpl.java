package it.unibo.vocago.logic;

import java.util.List;
import it.unibo.vocago.logic.api.LearningEngine;
import it.unibo.vocago.logic.api.LearningSession;
import it.unibo.vocago.model.learning.api.Question;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;
import it.unibo.vocago.model.vocabulary.api.Word;

public class LearningSessionImpl implements LearningSession {

    private final Vocabulary vocabulary;
    private final LearningEngine learningEngine;
    private final long time;
    private Direction direction;
    private Question question;
    private int countAnsweredQuestions;

    public LearningSessionImpl(final Vocabulary vocabulary){
        this.vocabulary = vocabulary;
        this.learningEngine = new LearningEngineImpl();
        this.time = System.currentTimeMillis();
        direction = Direction.FIRST_TO_SECOND;
        this.countAnsweredQuestions = 0;
    }

    @Override
    public String getNextQuestion() {
        this.question = this.learningEngine.getNextQuestion(direction, vocabulary);
        return toString(this.question.getQuestion());
    }

    @Override
    public boolean evaluateAnswer(String answer) {
        boolean correctAnswer = this.learningEngine.checkAnswer(this.question, answer);
        this.learningEngine.progressUpdate(this.question, correctAnswer);
        this.countAnsweredQuestions++;
        return correctAnswer;
    }

    @Override
    public String getCorrectAnswer() {
        return toString(this.question.getCorrectAnswer());
    }

    @Override
    public void switchDirection() {
        this.direction.opposite();
    }

    @Override
    public int getCountAnsweredQuestions() {
        return this.countAnsweredQuestions;
    }

    @Override
    public long getTime() {
        return this.time;
    }

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    private String toString(final List<Word> words) {
    StringBuilder text = new StringBuilder();

        for (Word word : words) {
            if (text.length() > 0) {
                text.append(", ");
            }
            text.append(word.getWord());
        }

        return text.toString();
    }

}
