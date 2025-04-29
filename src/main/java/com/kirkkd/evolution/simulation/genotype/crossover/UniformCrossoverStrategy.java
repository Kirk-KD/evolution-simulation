package com.kirkkd.evolution.simulation.genotype.crossover;

import com.kirkkd.evolution.simulation.genotype.Allele;
import com.kirkkd.evolution.simulation.genotype.Chromosome;
import com.kirkkd.evolution.simulation.genotype.Locus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UniformCrossoverStrategy implements ICrossoverStrategy {
    private final Random rand = new Random();

    @Override
    public Chromosome crossover(Chromosome a, Chromosome b) {
        if (!a.id().equals(b.id())) {
            throw new IllegalArgumentException("Mismatched chromosome IDs: " + a.id() + " vs " + b.id());
        }
        List<Locus<?>> lociA = a.loci();
        List<Locus<?>> lociB = b.loci();
        List<Locus<?>> childLoci = new ArrayList<>(lociA.size());

        for (int i = 0; i < lociA.size(); i++) {
            @SuppressWarnings("unchecked")
            Allele<Object> alleleA = (Allele<Object>) lociA.get(i).getRandomAllele(rand);
            @SuppressWarnings("unchecked")
            Allele<Object> alleleB = (Allele<Object>) lociB.get(i).getRandomAllele(rand);
            Locus<Object> childLocus = new Locus<>(alleleA, alleleB);
            childLoci.add(childLocus);
        }
        return new Chromosome(a.id(), childLoci);
    }
}
