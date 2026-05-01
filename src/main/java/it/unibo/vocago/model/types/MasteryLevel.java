package it.unibo.vocago.model.types;

public enum MasteryLevel {
    NEW,
    BAD,
    MEDIUM,
    GOOD,
    MASTER;

    public MasteryLevel next() {
        if (this == MASTER) {
            return this;
        }
        return values()[this.ordinal() + 1];
    }

    public MasteryLevel previous() {
        if (this == BAD) {
            return this;
        } else if (this == NEW) {
            return BAD;
        }
        return values()[this.ordinal() - 1];
    }

}