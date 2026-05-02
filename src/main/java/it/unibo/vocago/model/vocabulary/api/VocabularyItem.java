package it.unibo.vocago.model.vocabulary.api;

import java.util.List;

import it.unibo.vocago.model.progress.api.Progress;
import it.unibo.vocago.model.types.Direction;

public interface VocabularyItem {

    public List<Word> getFirstLanguageWords();

    public List<Word> getSecondLanguageWords();

    public Progress getProgress(final Direction direction);
}
