package com.kirkkd.evolution.simulation.phenotype;

import com.kirkkd.evolution.simulation.genotype.Gene;
import com.kirkkd.evolution.simulation.genotype.Genome;

import java.util.HashMap;
import java.util.Map;

public class CompositeMapper implements IPhenotypeMapper {
    @Override
    public Map<GeneKey<Object>, Object> map(Genome genome) {
        Map<GeneKey<Object>, Object> out = new HashMap<>();
        out.putAll(new DominanceMapper(Gene.dominantIDs()).map(genome));
        out.putAll(new AdditiveMapper().map(genome));
        out.putAll(new AverageMapper().map(genome));
        return out;
    }
}
