package com.kirkkd.evolution.simulation.genotype;

public record LocusSpec<T>(Gene<T> name) {
    public T generate() {
        return name.getLocusSpecSupplier().get();
    }
}
