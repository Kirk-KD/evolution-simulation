package com.kirkkd.evolution.simulation.organism;

import com.kirkkd.evolution.simulation.genotype.Chromosome;
import com.kirkkd.evolution.simulation.genotype.Genome;
import com.kirkkd.evolution.simulation.genotype.crossover.ICrossoverStrategy;
import com.kirkkd.evolution.simulation.genotype.mutation.IMutationStrategy;
import com.kirkkd.evolution.simulation.phenotype.GeneKey;
import com.kirkkd.evolution.simulation.phenotype.IPhenotypeMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Organism {
    private final Genome genome;
    private final IPhenotypeMapper phenotypeMapper;

    private final Map<GeneKey<Object>, Object> phenotype;

    public Organism(Genome genome, IPhenotypeMapper phenotypeMapper) {
        this.genome = genome;
        this.phenotypeMapper = phenotypeMapper;
        this.phenotype = phenotypeMapper.map(genome);
    }

    public Genome getGenome() {
        return genome;
    }

    public IPhenotypeMapper getPhenotypeMapper() {
        return phenotypeMapper;
    }

    @SuppressWarnings("unchecked")
    public <T> T getTrait(GeneKey<T> key) {
        return (T) phenotype.get(key);
    }

    public Organism reproduceWith(Organism other, ICrossoverStrategy cs, IMutationStrategy ms) {
        List<Chromosome> childChromosomes = new ArrayList<>();
        for (Chromosome c1 : this.getGenome().chromosomes()) {
            Chromosome c2 = other.getGenome().chromosomes()
                    .stream()
                    .filter(c -> c.id().equals(c1.id()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Mismatched chromosome sets"));
            childChromosomes.add(cs.crossover(c1, c2));
        }
        Genome childGenome = new Genome(childChromosomes);
        return new Organism(ms.mutate(childGenome), this.phenotypeMapper);
    }

    @Override
    public String toString() {
        return "Organism Genome:\n" + genome.toString();
    }
}
