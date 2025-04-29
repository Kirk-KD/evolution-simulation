package com.kirkkd.evolution.simulation.genotype;

import java.util.ArrayList;
import java.util.List;

public class GenomeFactory {
    public static Genome createRandomGenome(List<String> chromosomeIds,
                                            List<LocusSpec<?>> specs) {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (String id : chromosomeIds) {
            List<Locus<?>> loci = new ArrayList<>();
            for (LocusSpec<?> spec : specs) {
                loci.add(createLocus(spec));
            }
            chromosomes.add(new Chromosome(id, loci));
        }
        return new Genome(chromosomes);
    }

    public static <T> Locus<T> createLocus(LocusSpec<T> spec) {
        T v1 = spec.generate();
        T v2 = spec.generate();
        Allele<T> a1 = new Allele<>(spec.name(), v1);
        Allele<T> a2 = new Allele<>(spec.name(), v2);
        return new Locus<>(a1, a2);
    }
}
