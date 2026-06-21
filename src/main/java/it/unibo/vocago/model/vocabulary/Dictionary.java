package it.unibo.vocago.model.vocabulary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.unibo.vocago.model.vocabulary.api.Vocabulary;
import it.unibo.vocago.model.vocabulary.api.VocabularyItem;

/**
 * Default implementation of {@link Vocabulary}, copies are used on input and output so the
 * internal list cannot be modified from outside.
 */
public final class Dictionary implements Vocabulary {

    private final List<VocabularyItem> items;

    /**
     * Creates an empty vocabulary.
     */
    public Dictionary() {
        this.items = new ArrayList<>();
    }

    /**
     * Creates a vocabulary initialised with the given items, for example when
     * loading an existing vocabulary together with its words and progress.
     *
     * @param items the initial items; must not be {@code null} nor contain
     *              {@code null} values
     * @throws IllegalArgumentException if the list contains a {@code null} item
     */
    public Dictionary(final List<VocabularyItem> items) {
        Objects.requireNonNull(items, "items cannot be null");
        if (items.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("items cannot contain null values");
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
        for (final VocabularyItem item : getItems()) {
            if (item.isValid()) {
                return true;
            }
        }
        return false;
    }
}