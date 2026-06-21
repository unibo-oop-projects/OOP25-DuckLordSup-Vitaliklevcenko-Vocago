package it.unibo.vocago.model.types;

public enum MasteryLevel {
    NEW(0),
    BAD(0.8),
    MEDIUM(0.9),
    GOOD(1.05),
    MASTER(1.35);

    private final double multiplier;

    MasteryLevel(final double multiplier) {
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return this.multiplier;
    }

    public MasteryLevel next() {
        if (this == MASTER) {
            return this;
        }
        else if (this == NEW) {
            return MEDIUM;
        }
        return values()[this.ordinal() + 1];
    }

    public MasteryLevel previous() {
        if (this == BAD) {
            return this;
        } else if (this == NEW) {
            return BAD;
        }
        else if(this == MASTER){
            return MEDIUM;
        }
        return values()[this.ordinal() - 1];
    }

}
