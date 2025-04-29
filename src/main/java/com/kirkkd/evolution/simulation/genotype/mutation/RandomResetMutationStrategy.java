package com.kirkkd.evolution.simulation.genotype.mutation;

import com.kirkkd.evolution.simulation.genotype.Allele;
import com.kirkkd.evolution.simulation.genotype.Chromosome;
import com.kirkkd.evolution.simulation.genotype.Genome;
import com.kirkkd.evolution.simulation.genotype.Locus;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomResetMutationStrategy implements IMutationStrategy {
    private final Random rand = new Random();
    private final double mutationRate;

    public RandomResetMutationStrategy(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    @Override
    public Genome mutate(Genome genome) {
        List<Chromosome> mutated = genome.chromosomes().stream()
                .map(chr -> {
                    List<Locus<?>> newLoci = chr.loci().stream()
                            .map(this::maybeMutateLocus)
                            .collect(Collectors.toList());
                    return new Chromosome(chr.id(), newLoci);
                })
                .toList();
        return new Genome(mutated);
    }

    <T> Locus<T> maybeMutateLocus(Locus<T> locus) {
        if (rand.nextDouble() < mutationRate) {
            boolean mutateMaternal = rand.nextBoolean();
            Allele<T> original = mutateMaternal ? locus.maternal() : locus.paternal();
            T freshValue = original.name().getLocusSpecSupplier().get();
            Allele<T> newAllele = new Allele<>(original.name(), freshValue);
            return mutateMaternal
                    ? new Locus<>(newAllele, locus.paternal())
                    : new Locus<>(locus.maternal(), newAllele);
        }
        return locus;
    }
}
