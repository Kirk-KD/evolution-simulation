package com.kirkkd.evolution.simulation.genotype;

import java.util.Random;

public record Locus<T>(Allele<T> maternal, Allele<T> paternal) {
    public Allele<T> getRandomAllele(Random rand) {
        return rand.nextBoolean() ? maternal : paternal;
    }

    public Locus<T> copy() {
        return new Locus<>(maternal.copy(), paternal.copy());
    }

    @Override
    public String toString() {
        return String.format("[M=%s\tP=%s]", maternal, paternal);
    }

    public String toIndentedString(String indent) {
        return indent + "  " + this;
    }
}
