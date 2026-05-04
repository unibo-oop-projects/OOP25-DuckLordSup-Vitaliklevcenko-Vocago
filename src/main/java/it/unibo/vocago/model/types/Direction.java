package it.unibo.vocago.model.types;

public enum Direction {

    FIRST_TO_SECOND,
    SECOND_TO_FIRST;

    public Direction opposite() {
        if (this == FIRST_TO_SECOND) {
            return SECOND_TO_FIRST;
        }
        return FIRST_TO_SECOND;
    }

}
