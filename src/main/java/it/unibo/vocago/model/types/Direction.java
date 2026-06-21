package it.unibo.vocago.model.types;

/**
 * The translation direction of a learning question, i.e. which language is
 * shown as the prompt and which one is expected as the answer.
 */
public enum Direction {

    /** From the first (known) language to the second (studied) language. */
    FIRST_TO_SECOND,

    /** From the second (studied) language to the first (known) language. */
    SECOND_TO_FIRST;

    /**
     * @return the opposite translation direction
     */
    public Direction opposite() {
        if (this == FIRST_TO_SECOND) {
            return SECOND_TO_FIRST;
        }
        return FIRST_TO_SECOND;
    }
}