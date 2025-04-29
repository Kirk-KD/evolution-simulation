package com.kirkkd.evolution.simulation.genotype.crossover;

import com.kirkkd.evolution.simulation.genotype.Chromosome;

public interface ICrossoverStrategy {
    Chromosome crossover(Chromosome a, Chromosome b);
}
