package com.kirkkd.evolution.simulation.genotype.mutation;

import com.kirkkd.evolution.simulation.genotype.Genome;

public interface IMutationStrategy {
    Genome mutate(Genome genome);
}
