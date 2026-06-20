package it.unibo.vocago.model.learning;

import java.util.List;
import java.util.Objects;

import it.unibo.vocago.model.learning.api.Question;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.vocabulary.api.VocabularyItem;
import it.unibo.vocago.model.vocabulary.api.Word;

public class QuestionImpl implements Question {

    private final VocabularyItem item;
    private final Direction direction;

    public QuestionImpl(final VocabularyItem item, final Direction direction) {
        this.item = Objects.requireNonNull(item, "item must not be null");
        this.direction = Objects.requireNonNull(direction, "direction must not be null");
    }

    @Override
    public VocabularyItem getItem() {
        return this.item;
    }

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public List<Word> getQuestion() {
        return switch (this.direction) {
            case FIRST_TO_SECOND -> this.item.getFirstLanguageWords();
            case SECOND_TO_FIRST -> this.item.getSecondLanguageWords();
        };
    }

    @Override
    public List<Word> getCorrectAnswer() {
        return switch (this.direction) {
            case FIRST_TO_SECOND -> this.item.getSecondLanguageWords();
            case SECOND_TO_FIRST -> this.item.getFirstLanguageWords();
        };
    }

}
