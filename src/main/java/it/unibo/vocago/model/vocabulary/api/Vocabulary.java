package it.unibo.vocago.model.vocabulary.api;

import java.util.List;

public interface Vocabulary {

    void addItem(VocabularyItem item);

    void removeItem(VocabularyItem item);

    List<VocabularyItem> getItems();

    boolean isEmpty();

    Boolean isValid();

    int size();
}
