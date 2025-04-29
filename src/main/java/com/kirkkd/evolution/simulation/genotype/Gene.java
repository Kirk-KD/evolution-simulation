package com.kirkkd.evolution.simulation.genotype;

import com.kirkkd.evolution.simulation.phenotype.GeneKey;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Gene<T> {
    private static final Map<String, Gene<?>> VALUES = new HashMap<>();
    private static final Random RANDOM = new Random();

    public static final Gene<Float> BODY_COLOR_R = ofAverage("BODY_COLOR_R", () -> RANDOM.nextFloat(0, 255));
    public static final Gene<Float> BODY_COLOR_G = ofAverage("BODY_COLOR_G", () -> RANDOM.nextFloat(0, 255));
    public static final Gene<Float> BODY_COLOR_B = ofAverage("BODY_COLOR_B", () -> RANDOM.nextFloat(0, 255));

    private final String name;
    private final MapperMode mapperMode;
    private final String domID;
    private final Supplier<T> locusSpecSupplier;

    private static <T> Gene<T> ofMendelian(String name, String domID, Supplier<T> locusSpecSupplier) {
        return new Gene<>(name, MapperMode.MENDELIAN, domID, locusSpecSupplier);
    }

    private static <T> Gene<T> ofAdditive(String name, Supplier<T> locusSpecSupplier) {
        return new Gene<>(name, MapperMode.ADDITIVE, null, locusSpecSupplier);
    }

    private static <T> Gene<T> ofAverage(String name, Supplier<T> locusSpecSupplier) {
        return new Gene<>(name, MapperMode.AVERAGE, null, locusSpecSupplier);
    }

    private Gene(String name, MapperMode mapperMode, String domID, Supplier<T> locusSpecSupplier) {
        this.name = name;
        this.mapperMode = mapperMode;
        this.domID = domID;
        this.locusSpecSupplier = locusSpecSupplier;
        VALUES.put(name, this);
    }

    public MapperMode getMapperMode() {
        return mapperMode;
    }

    public String getDomID() {
        return name + ":" + domID;
    }

    public String getName() {
        return name;
    }

    public Supplier<T> getLocusSpecSupplier() {
        return locusSpecSupplier;
    }

    public LocusSpec<T> getLocusSpec() {
        return new LocusSpec<>(this);
    }

    public GeneKey<T> getGeneKey() {
        return new GeneKey<>(this);
    }

    public static Gene<?> valueOf(String name) {
        return VALUES.get(name);
    }

    public static Gene<?>[] values() {
        return VALUES.values().toArray(new Gene[0]);
    }

    public static List<LocusSpec<?>> locusSpecs() {
        return VALUES.values()
                .stream()
                .map(Gene::getLocusSpec)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static Set<String> dominantIDs() {
        return VALUES.values()
                .stream()
                .map(Gene::getDomID)
                .collect(Collectors.toCollection(HashSet::new));
    }

    public enum MapperMode {
        MENDELIAN, ADDITIVE, AVERAGE
    }
}