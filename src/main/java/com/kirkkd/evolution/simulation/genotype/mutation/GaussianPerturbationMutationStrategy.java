package com.kirkkd.evolution.simulation.genotype.mutation;

import com.kirkkd.evolution.simulation.genotype.Allele;
import com.kirkkd.evolution.simulation.genotype.Chromosome;
import com.kirkkd.evolution.simulation.genotype.Genome;
import com.kirkkd.evolution.simulation.genotype.Locus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GaussianPerturbationMutationStrategy implements IMutationStrategy {
    private final Random rand = new Random();
    private final double stdDev;
    private final double mutationRate;

    public GaussianPerturbationMutationStrategy(double stdDev, double mutationRate) {
        this.stdDev = stdDev;
        this.mutationRate = mutationRate;
    }

    @Override
    public Genome mutate(Genome genome) {
        List<Chromosome> perturbed = genome.chromosomes().stream()
                .map(chr -> {
                    List<Locus<?>> newLoci = new ArrayList<>();
                    for (Locus<?> locus : chr.loci()) {
                        newLoci.add(maybePerturbLocus(locus));
                    }
                    return new Chromosome(chr.id(), newLoci);
                })
                .toList();
        return new Genome(perturbed);
    }

    private <T> Locus<T> maybePerturbLocus(Locus<T> locus) {
        if (rand.nextDouble() < mutationRate) {
            System.out.println("MUTATE");
            Allele<T> mat = maybePerturbAllele(locus.maternal());
            Allele<T> pat = maybePerturbAllele(locus.paternal());
            return new Locus<>(mat, pat);
        } else return locus;
    }

    @SuppressWarnings("unchecked")
    private <T> Allele<T> maybePerturbAllele(Allele<T> allele) {
        Object value = allele.value();
        Object newVal = value;
        if (value instanceof Double d) {
            newVal = d + rand.nextGaussian() * stdDev;
        } else if (value instanceof Float f) {
            newVal = f + (float) (rand.nextGaussian() * stdDev);
        } else if (value instanceof Integer i) {
            newVal = i + (int) Math.round(rand.nextGaussian() * stdDev);
        }
        return new Allele<>(allele.name(), (T) newVal);
    }
}
