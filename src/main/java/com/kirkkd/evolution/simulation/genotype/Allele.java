package com.kirkkd.evolution.simulation.genotype;

public record Allele<T>(Gene<T> name, T value) {
    public Allele<T> copy() {
        return new Allele<>(name, value);
    }

    @Override
    public String toString() {
        return String.format("%s=%s", name.getName(), value);
    }
}
