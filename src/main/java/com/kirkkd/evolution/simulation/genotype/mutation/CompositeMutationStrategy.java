package com.kirkkd.evolution.simulation.genotype.mutation;

import com.kirkkd.evolution.simulation.genotype.Chromosome;
import com.kirkkd.evolution.simulation.genotype.Genome;
import com.kirkkd.evolution.simulation.genotype.Locus;

import java.util.List;
import java.util.stream.Collectors;

public class CompositeMutationStrategy implements IMutationStrategy {
    private final GaussianPerturbationMutationStrategy gaussian;
    private final RandomResetMutationStrategy randomReset;

    public CompositeMutationStrategy(
            GaussianPerturbationMutationStrategy gaussian,
            RandomResetMutationStrategy randomReset) {
        this.gaussian = gaussian;
        this.randomReset = randomReset;
    }

    @Override
    public Genome mutate(Genome genome) {
        gaussian.mutate(genome);

        List<Chromosome> mutated = genome.chromosomes().stream()
                .map(chr -> {
                    List<Locus<?>> newLoci = chr.loci().stream()
                            .map(randomReset::maybeMutateLocus)
                            .collect(Collectors.toList());
                    return new Chromosome(chr.id(), newLoci);
                })
                .collect(Collectors.toList());
        return new Genome(mutated);
    }
}
