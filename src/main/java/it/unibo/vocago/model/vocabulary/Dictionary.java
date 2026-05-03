package it.unibo.vocago.model.vocabulary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.unibo.vocago.model.vocabulary.api.Vocabulary;
import it.unibo.vocago.model.vocabulary.api.VocabularyItem;

public class Dictionary implements Vocabulary {

    private final List<VocabularyItem> items;

    // new vocabulary with empty list of words(vocabularyItems)
    public Dictionary() {
        this.items = new ArrayList<>();
    }

    // in case we already have the whole vocabulary and we want to add it with all the words and progresses
    public Dictionary(final List<VocabularyItem> items) {
        if (items == null || items.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("items cannot be null or contain null values");
        }
        this.items = new ArrayList<>(items);
    }

    @Override
    public void addItem(final VocabularyItem item) {
        this.items.add(Objects.requireNonNull(item, "item must not be null"));
    }

    @Override
    public void removeItem(final VocabularyItem item) {
        this.items.remove(Objects.requireNonNull(item, "item must not be null"));
    }

    @Override
    public List<VocabularyItem> getItems() {
        return new ArrayList<>(this.items);
    }

    @Override
    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public int size() {
        return this.items.size();
    }

    @Override
    public Boolean isValid() {
        for (VocabularyItem item : getItems()) {
            if (item.isValid()) {
                return true;
            }
        }
        return false;
    }
}
