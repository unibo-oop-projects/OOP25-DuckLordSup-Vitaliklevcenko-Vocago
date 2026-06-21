package it.unibo.vocago.model.vocabulary.api;

import java.util.List;

/**
 * A collection of {@link VocabularyItem} belonging to a user, with operations
 * to add, remove and inspect the items it contains.
 */
public interface Vocabulary {

    /**
     * Adds an item to this vocabulary.
     *
     * @param item the item to add
     */
    void addItem(VocabularyItem item);

    /**
     * Removes an item from this vocabulary.
     *
     * @param item the item to remove
     */
    void removeItem(VocabularyItem item);

    /**
     * @return the list of items contained in this vocabulary
     */
    List<VocabularyItem> getItems();

    /**
     * @return {@code true} if this vocabulary contains no items
     */
    boolean isEmpty();

    /**
     * @return {@code true} if this vocabulary is valid, i.e. it contains at
     *         least one complete item suitable for a learning session
     */
    Boolean isValid();

    /**
     * @return the number of items in this vocabulary
     */
    int size();
}
