package com.kirkkd.evolution.simulation.phenotype;

import com.kirkkd.evolution.simulation.genotype.Genome;

import java.util.Map;

public interface IPhenotypeMapper {
    Map<GeneKey<Object>, Object> map(Genome genome);
}
