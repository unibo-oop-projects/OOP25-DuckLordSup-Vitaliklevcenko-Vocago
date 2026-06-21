package it.unibo.vocago.model.vocabulary;

import java.util.List;
import java.util.Objects;

import it.unibo.vocago.model.progress.WordProgress;
import it.unibo.vocago.model.progress.api.Progress;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.vocabulary.api.VocabularyItem;
import it.unibo.vocago.model.vocabulary.api.Word;

/**
 * Default implementation of {@link VocabularyItem}, the word
 * lists are stored as immutable copies.
 */
public final class DictionaryEntry implements VocabularyItem {

    private final List<Word> firstLanguageWords;
    private final List<Word> secondLanguageWords;
    private final Progress firstProgress;
    private final Progress secondProgress;

    /**
     * Creates an entry with fresh, empty progress for both directions.
     *
     * @param firstLanguageWords  the words in the first language
     * @param secondLanguageWords the words in the second language
     */
    public DictionaryEntry(final List<Word> firstLanguageWords, final List<Word> secondLanguageWords) {
        this(firstLanguageWords, secondLanguageWords, new WordProgress(), new WordProgress());
    }

    /**
     * Creates an entry with the given words and progress for each direction.
     *
     * @param firstLanguageWords  the words in the first language; must not be
     *                            {@code null} nor contain {@code null}
     * @param secondLanguageWords the words in the second language; must not be
     *                            {@code null} nor contain {@code null}
     * @param firstProgress       the progress for the first-to-second direction
     * @param secondProgress      the progress for the second-to-first direction
     * @throws IllegalArgumentException if either word list is {@code null} or
     *                                  contains {@code null} values
     */
    public DictionaryEntry(final List<Word> firstLanguageWords, final List<Word> secondLanguageWords,
            final Progress firstProgress, final Progress secondProgress) {
        if (firstLanguageWords == null || firstLanguageWords.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("First language words cannot be null or contain null values");
        }
        if (secondLanguageWords == null || secondLanguageWords.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Second language words cannot be null or contain null values");
        }

        this.firstLanguageWords = List.copyOf(firstLanguageWords);
        this.secondLanguageWords = List.copyOf(secondLanguageWords);
        this.firstProgress = firstProgress != null ? firstProgress : new WordProgress();
        this.secondProgress = secondProgress != null ? secondProgress : new WordProgress();
    }

    @Override
    public List<Word> getFirstLanguageWords() {
        return this.firstLanguageWords;
    }

    @Override
    public List<Word> getSecondLanguageWords() {
        return this.secondLanguageWords;
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
