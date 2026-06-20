package it.unibo.vocago.model.vocabulary.api;

import java.util.List;

import it.unibo.vocago.model.progress.api.Progress;
import it.unibo.vocago.model.types.Direction;

public interface VocabularyItem {

    List<Word> getFirstLanguageWords();

    List<Word> getSecondLanguageWords();

    Progress getProgress(Direction direction);

    Boolean isValid();
}
