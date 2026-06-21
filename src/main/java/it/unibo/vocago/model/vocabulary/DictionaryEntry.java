package it.unibo.vocago.model.vocabulary;

import java.util.List;
import java.util.Objects;

import it.unibo.vocago.model.progress.WordProgress;
import it.unibo.vocago.model.progress.api.Progress;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.vocabulary.api.VocabularyItem;
import it.unibo.vocago.model.vocabulary.api.Word;

public final class DictionaryEntry implements VocabularyItem {

    private final List<Word> firstLanguageWords;
    private final List<Word> secondLanguageWords;
    private final Progress firstProgress;
    private final Progress secondProgress;

     public DictionaryEntry(final List<Word> firstLanguageWords, final List<Word> secondLanguageWords) {
        this(firstLanguageWords, secondLanguageWords, new WordProgress(), new WordProgress());
    }

    public DictionaryEntry(final List<Word> firstLanguageWords, final List<Word> secondLanguageWords,
            final Progress firstProgress, final Progress secondProgress) {
        if (firstLanguageWords == null || firstLanguageWords.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("First language words cannot be null or contain null values");
        }
        if (secondLanguageWords == null || secondLanguageWords.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Second language words cannot be null or contain null values");
        }

        this.firstLanguageWords = List.copyOf(firstLanguageWords); //copy because of safety
        this.secondLanguageWords = List.copyOf(secondLanguageWords); // copy because of safety
        this.firstProgress = firstProgress != null ? firstProgress : new WordProgress();
        this.secondProgress = secondProgress != null ? secondProgress : new WordProgress();
    }

    @Override
    public List<Word> getFirstLanguageWords() {
        return this.firstLanguageWords; //already stored as copy, no need for extra copy
    }

    @Override
    public List<Word> getSecondLanguageWords() {
        return this.secondLanguageWords; // already stored as copy, no need for extra copy
    }

    @Override
    public Progress getProgress(final Direction direction) {
        return switch (direction) {
            case FIRST_TO_SECOND -> this.firstProgress;
            case SECOND_TO_FIRST -> this.secondProgress;
        };
    }

    @Override
    public Boolean isValid() {
        return hasNonBlankWord(getFirstLanguageWords()) && hasNonBlankWord(getSecondLanguageWords());
    }

    private boolean hasNonBlankWord(final List<Word> words) {
        for (final Word word : words) {
            if (!word.getWord().isBlank()) {
                return true;
            }
        }
        return false;
    }
}
